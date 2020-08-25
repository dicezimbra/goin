package br.com.goin.feature.feed.post

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.session.user.UserHelper
import br.com.goin.component.session.user.UserModel
import br.com.goin.component.session.user.UserSessionInteractor
import br.com.goin.feature.feed.R
import br.com.goin.feature.feed.model.checkIn.CheckIn
import br.com.goin.feature.feed.model.friends.PostUser
import br.com.goin.feature.feed.post.adapter.PostActivityAdapter
import br.com.goin.feature.feed.post.checkIn.CheckInActivity
import br.com.goin.feature.feed.post.feelings.FeelingsActivity
import br.com.goin.feature.feed.post.manager.BackgroundPost
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.jakewharton.rxbinding3.widget.textChanges
import com.linkedin.android.spyglass.mentions.Mentionable
import com.linkedin.android.spyglass.suggestions.interfaces.SuggestionsVisibilityManager
import com.linkedin.android.spyglass.tokenization.QueryToken
import com.linkedin.android.spyglass.tokenization.impl.WordTokenizer
import com.linkedin.android.spyglass.tokenization.impl.WordTokenizerConfig
import com.linkedin.android.spyglass.tokenization.interfaces.QueryTokenReceiver
import com.linkedin.android.spyglass.ui.MentionsEditText
import com.outsmart.outsmartpicker.MediaPicker
import com.outsmart.outsmartpicker.MediaType
import com.theartofdev.edmodo.cropper.CropImage
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_post.*
import java.io.File
import java.util.concurrent.TimeUnit

private const val SHOW = "SHOW"
internal const val POST_TYPE = "POST_TYPE"
private const val REQUEST_NEW_POST = 111
private const val FEELING = "FEELING"
private const val EVENT = "EVENT"
private const val PICK_FEELING = 222
private const val PICK_CHECK_IN = 333
private const val BUCKET = "users-memory"

class PostActivity : AppCompatActivity(), PostActivityView,
        SuggestionsVisibilityManager, QueryTokenReceiver {

    private val presenter: PostActivityPresenter = PostActivityPresenterImpl(this)
    private var user: UserModel? = null
    private var event: CheckIn? = null
    private var description: String? = null
    private var mediaPicker: MediaPicker? = null
    private var pickerChoose: BroadcastReceiver? = null
    private var mediaFilePath: String? = null
    private var feeling: String? = null
    private var hasVideo: Boolean = false
    private var hasPhoto: Boolean = false
    private var mediaImage: Bitmap? = null
    private var disposables = CompositeDisposable()
    private var friendsAdapter: PostActivityAdapter? = null
    private var mediaImageTemp: String? = null

    companion object {
        fun starter(activity: Activity, showInMainFeed: Boolean, postType: String? = null,
                    eventName: String? = null, eventId: String? = null,
                    clubName: String? = null, clubId: String? = null) {
            val intent = Intent(activity, PostActivity::class.java)
            intent.putExtra(SHOW, showInMainFeed)
            intent.putExtra(POST_TYPE, postType)
            if((eventId != null && eventName != null) || (clubId != null && clubName != null)) {
                intent.putExtra(EVENT, CheckIn(name = eventName, id = eventId, club = clubName, clubId = clubId))
            }
            activity.startActivityForResult(intent, REQUEST_NEW_POST)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        user = UserSessionInteractor.instance.fetchUser()
        mediaPicker = MediaPicker()

        txt_mentions_post.typeface = Typeface.createFromAsset(this.assets, "fonts/Quicksand-Medium.ttf")
        feelings_info_post.typeface = Typeface.createFromAsset(this.assets, "fonts/Quicksand-Medium.ttf")
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        presenter.onCreate()
        presenter.onReceiveUser(user)
        presenter.onReceiveEvent(intent.extras[EVENT])
    }

    override fun onResume() {
        super.onResume()
        Analytics.instance.screenView(this, getString(R.string.create_post_screen_name))
    }

    override fun onFriendsLoaded(friends: MutableList<PostUser>) {
        friendsAdapter = PostActivityAdapter(friends)
        friendsAdapter?.onFriendClick = {
            txt_mentions_post.insertMention(UserSuggestion(it.name, it.id))
            persons_list_post.visibility = View.GONE
            btn_publish_post.isEnabled = true
            btn_publish_post.alpha = 1.0f
        }
        persons_list_post.adapter = friendsAdapter
        persons_list_post.layoutManager = LinearLayoutManager(this)
        persons_list_post.visibility = View.VISIBLE
    }

    override fun configurePostQuery() {
        val tokenizerConfig = WordTokenizerConfig.Builder().setMaxNumKeywords(3).build()
        val tokenizer = WordTokenizer(tokenizerConfig)
        txt_mentions_post.tokenizer = tokenizer
        txt_mentions_post.setSuggestionsVisibilityManager(this)
        txt_mentions_post.setQueryTokenReceiver(this)
        txt_mentions_post.setAvoidPrefixOnTap(true)

        txt_mentions_post.addMentionWatcher(object : MentionsEditText.MentionWatcher {
            override fun onMentionDeleted(mention: Mentionable, text: String, start: Int, end: Int) {
                btn_publish_post.isEnabled = false
                btn_publish_post.alpha = 0.4f
            }

            override fun onMentionPartiallyDeleted(mention: Mentionable, text: String, start: Int, end: Int) {
            }

            override fun onMentionAdded(mention: Mentionable, text: String, start: Int, end: Int) {
                persons_list_post.visibility = View.GONE
                btn_publish_post.isEnabled = true
                btn_publish_post.alpha = 1.0f
            }
        })

        txt_mentions_post.textChanges()
                .skipInitialValue()
                .subscribe {
                    if (it.isNotEmpty()) {
                        btn_publish_post.alpha = 1.0f
                        btn_publish_post.isEnabled = true
                    } else {
                        btn_publish_post.alpha = 0.4f
                        persons_list_post.visibility = View.GONE
                        btn_publish_post.isEnabled = false
                    }
                }.addTo(disposables)
    }

    override fun displaySuggestions(display: Boolean) {
        if (display && friendsAdapter?.itemCount != 0) {
            persons_list_post.visibility = View.VISIBLE
        }
    }

    override fun isDisplayingSuggestions(): Boolean {
        return persons_list_post.visibility == View.VISIBLE
    }

    override fun onQueryReceived(queryToken: QueryToken): MutableList<String> {
        val buckets = listOf(BUCKET)
        val textFilter = queryToken.tokenString.replace("@", "")

        textFilter.let {
            if (it.length in 2..5) {
                presenter.searchFriends(it)
            }
        }
        return buckets as MutableList<String>
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()

        unregisterReceiver(pickerChoose)
    }

    override fun registerReceiver() {
        registerReceiver(pickerChoose, IntentFilter(MediaPicker.PICKER_RESPONSE_FILTER))
    }

    override fun setupIntents() {
        val postType = intent.getStringExtra(POST_TYPE)
        when (postType) {
            PostType.CheckIn.toString() -> openCheckIn()
            PostType.Feeling.toString() -> openFeelings()
            PostType.Video.toString() -> videoSelected()
            PostType.Picture.toString() -> photoSelected()
        }
    }

    override fun setupBroadcastReceiver() {
        pickerChoose = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {

                val filePath = intent.getStringExtra(MediaPicker.PICKER_INTENT_FILE)
                filePath?.let { it ->
                    val file = File(it)
                    file.also {
                        val thumb = intent.getParcelableExtra<Bitmap>(MediaPicker.PICKER_INTENT_FILE_THUMB)
                        mediaFilePath = it.absolutePath

                        if (hasVideo) {
                            mediaImage = thumb
                            photo_video_post.clearColorFilter()
                            photo_video_post.setImageBitmap(mediaImage)
                            photo_video_post.visibility = View.VISIBLE
                            photo_video_post.isEnabled = true
                            btn_publish_post.isEnabled = true
                            btn_publish_post.alpha = 1.0f
                            hasVideo = true
                            hasPhoto = false
                        } else if (hasPhoto) {
                            hasPhoto = true
                            hasVideo = false
                            PostUtils.getImageContentUri(this@PostActivity, file)?.let { it1 -> PostUtils.cropImage(this@PostActivity, it1) }
                        }
                    }
                }
            }
        }
    }

    override fun setupTransaction() {
        mediaPicker?.let {
            val transaction  = supportFragmentManager.beginTransaction()
            transaction.add(it, "mediaPicker")
            transaction.commitAllowingStateLoss()
            supportFragmentManager.executePendingTransactions()
        }

    }

    override fun configureOnClickListeners() {
        toolbar.title = getString(R.string.create_post)
        toolbar.setOnBackButtonClicked { onBackPressed() }
        btn_camera_post.setOnClickListener { photoSelected() }
        btn_video_post.setOnClickListener { videoSelected() }
        btn_feelings_post.setOnClickListener { openFeelings() }
        btn_check_in_post.setOnClickListener { openCheckIn() }
        btn_publish_post.setOnClickListener { sendPost() }
    }

    override fun photoSelected() {
        hasPhoto = true
        hasVideo = false
        mediaPicker?.pickMediaWithPermissions(MediaType.IMAGE)
    }

    override fun videoSelected() {
        hasVideo = true
        hasPhoto = false
        mediaPicker?.pickMediaWithPermissions(MediaType.VIDEO)
    }

    override fun openCheckIn() {
        CheckInActivity.starter(this)
    }

    override fun configHideKeyboard() {
    }

    override fun openFeelings() {
        FeelingsActivity.starter(this)
    }

    override fun loadUserName(it: UserModel) {
        Glide.with(this)
                .load(it.profilePicture)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_user))
                .apply(RequestOptions.errorOf(R.drawable.ic_user))
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions().signature(UserHelper.getSignature()))
                .into(picture_post)
    }

    override fun loadUserPicture(it: UserModel) {
        persons_name_post.text = it.name
    }

    override fun sendPost() {
        if (btn_publish_post.isEnabled) {
            description = PostUtils.getPostDescriptionWithTags(txt_mentions_post)

            if (description.equals("")) {
                description = null
            }

            val post = CreatePost(
                    description = description,
                    checkInEventId = event?.id,
                    feeling = feeling,
                    video = hasVideo,
                    image = hasPhoto)

            val myConstraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

            val task = OneTimeWorkRequest.Builder(BackgroundPost::class.java)
                    .setInputData(PostActivityHelper.createData(post, mediaImageTemp, mediaFilePath, event?.id, event?.clubId))
                    .setConstraints(myConstraints)
                    .setInitialDelay(0, TimeUnit.SECONDS)
                    .addTag("createPost")
                    .build()

            val workManager = WorkManager.getInstance()
            workManager.enqueue(task)
            finish()
        }
    }

    override fun onSuccessPost(createPost: CreatePost) {
        createPost.let {
            val post = Intent()
            val gson = Gson().toJson(it)
            post.putExtra("REQUEST_NEW_POST", gson)
            setResult(Activity.RESULT_OK, post)
            finish()
        }
    }

    override fun currentTime(): Long {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
    }

    override fun setFeelings(feelings: String?, eventCheckedIn: CheckIn?) {
        event = eventCheckedIn
        feeling = feelings

        val builder = SpannableStringBuilder()
        val orange = ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        val quicksandMedium = Typeface.createFromAsset(assets, "fonts/Quicksand-Medium.ttf")
        val bold = StyleSpan(quicksandMedium.style)

        user?.let {
            val spanUser = SpannableString(it.name)
            spanUser.setSpan(orange, 0, it.name.length, 0)
            spanUser.setSpan(bold, 0, it.name.length, 0)
            builder.append(spanUser)
        }

        var span: SpannableString

        if (feelings != null) {
            span = SpannableString(" " + resources.getString(R.string.is_feeling) + " ")
            builder.append(span)

            val spanFeelings = SpannableString(feelings)
            spanFeelings.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimaryDark)), 0, feelings.length, 0)
            spanFeelings.setSpan(StyleSpan(quicksandMedium.style), 0, feelings.length, 0)
            builder.append(spanFeelings)

            if (eventCheckedIn != null) {
                span = SpannableString(" " + resources.getString(R.string.`in`) + " ")
                builder.append(span)

                val name = if(eventCheckedIn.id != null) eventCheckedIn.name else eventCheckedIn.club
                val spanEvent = SpannableString(name)
                spanEvent.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimaryDark)), 0, name!!.length, 0)
                spanEvent.setSpan(StyleSpan(quicksandMedium.style), 0, name!!.length, 0)
                builder.append(spanEvent)
            }
        } else {
            span = SpannableString(" " + resources.getString(R.string.did_checkin) + " ")
            builder.append(span)

            val name = if(eventCheckedIn?.id != null) eventCheckedIn.name else eventCheckedIn?.club
            val spanEvent = SpannableString(name)
            spanEvent.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimaryDark)), 0, name!!.length, 0)
            spanEvent.setSpan(StyleSpan(quicksandMedium.style), 0, name!!.length, 0)
            builder.append(spanEvent)
        }

        feelings_info_post.visibility = View.VISIBLE
        feelings_info_post.setText(builder, TextView.BufferType.SPANNABLE)
        btn_publish_post.alpha = 1.0f
        btn_publish_post.isEnabled = true

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    val resultUri = result.uri
                    mediaImage = PostUtils.getMediumImage(this@PostActivity, resultUri)

                    photo_video_post.clearColorFilter()
                    photo_video_post.setImageBitmap(mediaImage)
                    photo_video_post.visibility = View.VISIBLE
                    photo_video_post.isEnabled = true
                    btn_publish_post.isEnabled = true
                    btn_publish_post.alpha = 1.0f
                    hasPhoto = true
                    hasVideo = false
                }
            }
            PICK_FEELING -> {
                val feeling = data?.getStringExtra(FEELING)
                presenter.onReceiveFeelings(feeling)
            }

            PICK_CHECK_IN -> {
                val event = Gson().fromJson(data?.getStringExtra(EVENT), CheckIn::class.java)
                presenter.onReceiveEventCheckedIn(event)
            }

        }
    }


}
