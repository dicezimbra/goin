package br.com.legacy.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import br.com.goin.base.utils.Utils;
import br.com.goin.component.navigation.NavigationController;
import br.com.goin.component.session.user.UserModel;
import br.com.legacy.features.fullImage.FullImageActivity;
import br.com.legacy.managers.FeedManager;
import br.com.legacy.managers.RafflesManager;
import br.com.legacy.managers.SessionManager;
import br.com.legacy.models.FeedCardModel;
import br.com.legacy.models.RaffleCardModel;
import br.com.legacy.navigation.Coordinator;
import br.com.R;
import br.com.legacy.utils.Constants;

import br.com.databinding.ItemCardRaffleBinding;
import br.com.databinding.ItemFeedCardBinding;
import br.com.legacy.utils.TypefaceUtil;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by ivo on 13/09/2017.
 */

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface PostHandler {
        void onLikePost(FeedCardModel post, int position);
    }

    public interface CommentHandler {
        void invoke(String postId);
    }

    public PostHandler postHandler;
    public CommentHandler commentHandler;
    public List<FeedCardModel> posts;
    public FeedManager feedManager;
    public RafflesManager rafflesManager;
    public PostsType type;
    final Activity activity;
    public String currentUserId;
    int mediaWidth = 0;
    int mediaHeight = 0;
    public View.OnClickListener onClickDelete;
    public Boolean limitPostDescription = true;
    String categoryName;

    public FeedAdapter(List<FeedCardModel> list, Activity activity, PostHandler handler, FeedManager feedManager, PostsType postsType, String categoryName) {
        this(list, activity, handler, feedManager, postsType, categoryName, null);
    }

    public FeedAdapter(List<FeedCardModel> list, Activity activity, PostHandler handler, FeedManager feedManager, PostsType postsType, String categoryName, CommentHandler commentHandler) {
        this.posts = list;
        this.activity = activity;
        this.postHandler = handler;
        this.feedManager = feedManager;
        this.type = postsType;
        this.categoryName = categoryName;
        this.commentHandler = commentHandler;
        getUser(activity);
    }

    private String getUser(Activity activity) {
        UserModel currentUser = SessionManager.getInstance().getCurrentUser(activity);
        if (currentUser != null) {
            currentUserId = currentUser.getId();
            return currentUserId;
        }

        return currentUserId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case Constants.POST:
                ItemFeedCardBinding feedBinding = ItemFeedCardBinding.inflate(inflater, parent, false);
                return new FeedViewHolder(feedBinding, activity, this.postHandler, type, this, categoryName, commentHandler);
            case Constants.RAFFLE:
                ItemCardRaffleBinding raffleBinding = ItemCardRaffleBinding.inflate(inflater, parent, false);
                return new RaffleViewHolder(raffleBinding, activity, rafflesManager);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        switch (viewHolder.getItemViewType()) {
            case Constants.POST:
                final FeedCardModel feedCardModel = posts.get(position);



                final FeedViewHolder holder = (FeedViewHolder) viewHolder;
                getUser(activity);
                if (currentUserId != null && currentUserId.equals(feedCardModel.sharerId) ||
                        (feedCardModel.sharerId == null && currentUserId.equals(feedCardModel.posterId))) {
                    feedCardModel.actionOnClickOverflow = activity.getResources().getString(R.string.delete);
                } else {
                    feedCardModel.actionOnClickOverflow = activity.getResources().getString(R.string.report);
                }
                View.OnClickListener onClickOverflow = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        if (feedCardModel.actionOnClickOverflow == null || !feedCardModel.actionOnClickOverflow.isEmpty()) {
                            builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            if (currentUserId.equals(feedCardModel.sharerId) ||
                                    (feedCardModel.sharerId == null && currentUserId.equals(feedCardModel.posterId))) {
                                showDeleteDialog(builder, feedCardModel.idPost, position, viewHolder.itemView);
                            } else {
                                showReportDialog(builder, feedCardModel.idPost);
                            }
                        } else {

                            builder.setPositiveButton(feedCardModel.actionOnClickOverflow, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    if (currentUserId.equals(feedCardModel.sharerId) ||
                                            (feedCardModel.sharerId == null && currentUserId.equals(feedCardModel.posterId))) {
                                        showDeleteDialog(builder, feedCardModel.idPost, position, viewHolder.itemView);
                                    } else {
                                        showReportDialog(builder, feedCardModel.idPost);
                                    }
                                }
                            });
                        }
                    }
                };
                holder.binding.feedCardOverflowButton.setOnClickListener(onClickOverflow);
                holder.binding.feedCardSharedOverflowButton.setOnClickListener(onClickOverflow);

                int lightBlue = ContextCompat.getColor(activity, R.color.colorPrimaryDark);
                int purple = ContextCompat.getColor(activity, R.color.colorPrimary);
                feedCardModel.setHeaderCard(purple, lightBlue, activity.getResources(), holder.binding.profileNameTxt);
                feedCardModel.setCommentCard(holder.binding.commentTextLayout);
                feedCardModel.setSharedCard(holder.binding.sharedProfileNameTxt);


                //Applying custom font
                TypefaceUtil.initilize(activity);

                TypefaceUtil.regularFont(holder.binding.profileNameTxt,
                        holder.binding.txtHourFeed,
                        holder.binding.txtCommentFeed,
                        holder.binding.txtFeedSubject,
                        holder.binding.txtIconFeed,
                        holder.binding.txtLocationFeed,
                        holder.binding.commentTextLayout,
                        holder.binding.sharedProfileNameTxt);


                holder.binding.txtFeedSubject.post(new Runnable() {
                    @Override
                    public void run() {
                        Layout postSubjectLayout = holder.binding.txtFeedSubject.getLayout();
                        if (postSubjectLayout != null && limitPostDescription && feedCardModel.postDescriptionText != null) {
                            int lines = postSubjectLayout.getLineCount();
                            if (lines > Constants.DESCRIPTION_MAX_LINES) {
                                holder.binding.txtFeedSubject.setMaxLines(Constants.DESCRIPTION_MAX_LINES);
                                holder.binding.txtFeedSubject.setEllipsize(TextUtils.TruncateAt.END);
                                holder.binding.readMore.setVisibility(View.VISIBLE);
                                holder.binding.readMore.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        holder.binding.readMore.startAnimation(Utils.INSTANCE.setClickEffect());
                                        Coordinator.goToSinglePost(activity, posts.get(position).idPost);
                                    }
                                });
                            } else {
                                holder.binding.txtFeedSubject.setMaxLines(lines);
                                holder.binding.txtFeedSubject.setEllipsize(null);
                                holder.binding.readMore.setVisibility(View.GONE);
                            }
                        }
                    }
                });


                if (feedCardModel.isShareable()) {
                    holder.binding.shareContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            v.startAnimation(Utils.INSTANCE.setClickEffect());
                            confirmBeforeShare(feedCardModel.userName, new br.com.goin.component.ui.kit.dialog.DialogUtils.DialogCallback() {
                                @Override
                                public void onClickOk() {
                                    feedManager.sharePost(feedCardModel.idPost, activity.getWindow().getDecorView());
                                }

                                @Override
                                public void onClickCancel() {

                                }
                            });
                        }
                    });

                } else {
                    holder.binding.shareContainer.setVisibility(View.GONE);
                }

                holder.bind(feedCardModel, position);
                break;
            case Constants.RAFFLE:
                final RaffleCardModel raffleCardModel = (RaffleCardModel) posts.get(position);
                RaffleViewHolder raffleHolder = (RaffleViewHolder) viewHolder;
                raffleHolder.bind(raffleCardModel, position);
        }
    }

    private void showDeleteDialog(AlertDialog.Builder builder, final String idPost, final int position, final View viewHolderItemView) {
        builder.setMessage(R.string.confirm_delete_message)
                .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        feedManager.deletePost(idPost);
                        removeAt(position);
                        FeedManager.postDeletedSubject.onNext(idPost);
                        if (onClickDelete != null) {
                            onClickDelete.onClick(viewHolderItemView);
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showReportDialog(final AlertDialog.Builder builder, final String idPost) {
        builder.setMessage(R.string.confirm_report_message)
                .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        builder.setMessage(R.string.post_reported_message)
                                .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                        feedManager.reportPost(idPost);
                        AlertDialog alert = builder.create();
                        alert.show();
                        alert.getButton(AlertDialog.BUTTON_NEUTRAL).setEnabled(false);
                        alert.getButton(AlertDialog.BUTTON_NEUTRAL).setVisibility(View.GONE);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public int getItemCount() {

        return this.posts.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (posts.get(position) instanceof RaffleCardModel)
            return Constants.RAFFLE;
        else
            return Constants.POST;
    }

    public int getMediaWidth() {
        if (mediaWidth == 0) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            mediaWidth = displayMetrics.widthPixels - (2 * activity.getResources().getDimensionPixelSize(R.dimen._3sdp));
        }
        return mediaWidth;
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder {

        public final ItemFeedCardBinding binding;

        Activity activity;
        PostHandler postHandler;
        CommentHandler commentHandler;

        FeedAdapter adapter;
        String categoryName;

        public FeedViewHolder(ItemFeedCardBinding binding, Activity activity, PostHandler handler, PostsType type, FeedAdapter adapter, String categoryName, CommentHandler commentHandler) {
            super(binding.getRoot());
            this.activity = activity;
//            this.itemContainer = (RelativeLayout) binding.getRoot().findViewById(R.id.card_feed_layout);
            this.binding = binding;
            this.postHandler = handler;
            this.adapter = adapter;
            this.categoryName = categoryName;
            this.commentHandler = commentHandler;
        }

        public void bind(final FeedCardModel item, final int position) {
            if (item.mediaPath != null && item.imagePost != null) {
                Glide.with(activity).clear(binding.photoFeed);
                binding.photoFeed.setImageDrawable(item.imagePost);
            } else {
                CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(activity);
                circularProgressDrawable.setStrokeWidth(5);
                circularProgressDrawable.setCenterRadius(30);
                circularProgressDrawable.start();
                Glide.with(activity)
                        .load(item.postPhotoUrl)
                        .transition(withCrossFade())
                        .apply(RequestOptions.placeholderOf(circularProgressDrawable))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                if (item.hasVideoPost) {
                                    binding.iconPlay.setVisibility(View.VISIBLE);
                                }
                                return false;
                            }
                        })
                        .into(binding.photoFeed);
            }

            binding.photoFeed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FullImageActivity.Companion.starter(activity, item.postPhotoUrl);
                }
            });


            Glide.with(activity)
                    .load(item.profilePhotoUrl)

                    .apply(RequestOptions.circleCropTransform())
                    .transition(withCrossFade())
                    .apply(RequestOptions.circleCropTransform())
                    .apply(RequestOptions.placeholderOf(R.drawable.user_icon))
                    .apply(new RequestOptions().signature(SessionManager.getInstance().getSignature(activity, SessionManager.ImageType.MyProfile)))
                    .into(binding.profileImage);


            if (item.hasImagePost || item.hasVideoPost) {
                int width = adapter.getMediaWidth();
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.BELOW, R.id.feed_subject_layout);
                binding.feedMediaContainter.setLayoutParams(params);

            } else {
                binding.feedMediaContainter.setVisibility(View.GONE);
            }

            if (item.hasVideoPost) {

                binding.photoFeed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.mediaPath != null)
                            Coordinator.goToPlayer(activity, item.mediaPath);
                        else
                            Coordinator.goToPlayer(activity, item.postVideoUrl);
                    }
                });
            }

            if (item.isShared()) {
                Glide.with(activity)
                        .load(item.sharerProfilePicture)
                        .transition(withCrossFade())
                        .apply(RequestOptions.circleCropTransform())
                        .apply(RequestOptions.placeholderOf(R.drawable.user_icon)) // TODO colocar um placeholder
                        .into(binding.sharedProfileImage);

                binding.sharedProfileImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.startAnimation(Utils.INSTANCE.setClickEffect());
                        String currentUserId = SessionManager.getInstance().getCurrentUser(activity).getId();
                        if (currentUserId != null) {
                            NavigationController.Companion.getInstance().profileModule().goToProfile(activity, item.sharerId);
                        }
                    }
                });

                setPostShared(item);
            }

            //TODO verify if the poster is a club or an user (if posterType == club -> Coordinator.goToClub , else -> goToUserProfile(below))
            binding.profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(Utils.INSTANCE.setClickEffect());
                    String currentUserId = SessionManager.getInstance().getCurrentUser(activity).getId();
                    if (currentUserId != null) {
                        if (item.posterId.startsWith("us-east")) {
                            // temporary (poster doesn't have a type yet)

                            // poster is an user
                            NavigationController.Companion.getInstance().profileModule().goToProfile(activity, item.posterId);

                        } else {
                            // poster is a club
                            NavigationController.Companion.getInstance().placeModule().goToPlace(activity, item.posterId, categoryName);
                        }
                    }
                }
            });

            setPostHeader(item);
            if (item.commentUserName != null) {
                setPostComment(item);
            }

            setPostDescription(item);

            binding.commentContainer.setOnClickListener(v -> {
                binding.iconCommentFeed.startAnimation(Utils.INSTANCE.setClickEffect());
                String postId = item.idPost;
                if (commentHandler != null) commentHandler.invoke(postId);
            });

            binding.likeContainer.setOnClickListener(v -> {
                binding.iconLikeFeed.startAnimation(Utils.INSTANCE.setClickEffect());
                postHandler.onLikePost(item, position);
                item.onClickLikedByMe();
                binding.txtIconFeed.setText(item.getPostLikes());
            });

            binding.setFeedCard(item);
            binding.executePendingBindings();
        }

        public void setPostHeader(final FeedCardModel item) {
            SpannableStringBuilder builder = new SpannableStringBuilder();

            String name = item.userName == null ? "Usuário padrão" : item.userName;

            Spannable spanUser = new SpannableString(name);

            ClickableSpan clickUser = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    view.startAnimation(Utils.INSTANCE.setClickEffect());
                    String currentUserId = SessionManager.getInstance().getCurrentUser(activity).getId();
                    if (currentUserId != null) {
                        if (item.posterId.startsWith("us-east")) {
                            // temporary (poster doesn't have a type yet)

                            // poster is an user
                            NavigationController.Companion.getInstance().profileModule().goToProfile(activity, item.posterId);
                        } else {
                            // poster is a club
                            NavigationController.Companion.getInstance().placeModule().goToPlace(activity, item.posterId, categoryName);
                        }
                    }
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setColor(ContextCompat.getColor(activity, R.color.gray));
                    ds.setUnderlineText(false);
                }
            };

            spanUser.setSpan(clickUser, 0, spanUser.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            spanUser.setSpan(new StyleSpan(Typeface.BOLD), 0, name.length(), 0);

            builder.append(spanUser);

            SpannableString span;

            if (item.feeling != null) {
                span = new SpannableString(" " + activity.getResources().getString(R.string.is_feeling) + " ");
                builder.append(span);

                Spannable spanFeeling = new SpannableString(item.feeling.displayName);
                spanFeeling.setSpan(new ForegroundColorSpan(ContextCompat.getColor(activity, R.color.colorPrimaryDark)), 0, item.feeling.displayName.length(), 0);
                spanFeeling.setSpan(new StyleSpan(Typeface.BOLD), 0, item.feeling.displayName.length(), 0);
                builder.append(spanFeeling);

                if (item.eventName != null) {
                    span = new SpannableString(" " + activity.getResources().getString(R.string.in) + " ");
                    builder.append(span);

                    Spannable spanEvent = new SpannableString(item.eventName);

                    ClickableSpan clickEvent = new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            view.startAnimation(Utils.INSTANCE.setClickEffect());
                            String currentUserId = SessionManager.getInstance().getCurrentUser(activity).getId();
                            if (currentUserId != null) {
                                //TODO VER O QUE VAI FAZER
//                                Coordinator.goToEventDetail(activity, item_success_dialog.checkInEventId, null);
                            }
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            ds.setColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
                            ds.setUnderlineText(false);
                        }
                    };

                    spanEvent.setSpan(clickEvent, 0, spanEvent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spanEvent.setSpan(new StyleSpan(Typeface.BOLD), 0, item.eventName.length(), 0);

                    builder.append(spanEvent);

                }

                if (item.feeling.emoji != null) {
                    item.feeling.emoji.setBounds(0, 0, activity.getResources().getDimensionPixelSize(R.dimen._11sdp), activity.getResources().getDimensionPixelSize(R.dimen._11sdp));
                    span.setSpan(new ImageSpan(item.feeling.emoji), span.length() - 1, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append(span);
                }

            } else {
                if (item.eventName != null) {
                    span = new SpannableString(" " + activity.getResources().getString(R.string.did_checkin) + " ");
                    builder.append(span);

                    Spannable spanEvent = new SpannableString(item.eventName);
                    ClickableSpan clickEvent = new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            view.startAnimation(Utils.INSTANCE.setClickEffect());
                            String currentUserId = SessionManager.getInstance().getCurrentUser(activity).getId();
                            if (currentUserId != null) {
                                //TODO VER O QUE VAI FAZER
                                // Coordinator.goToEventDetail(activity, item_success_dialog.checkInEventId, null);


                            }
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            ds.setColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
                            ds.setUnderlineText(false);
                        }
                    };

                    spanEvent.setSpan(clickEvent, 0, spanEvent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spanEvent.setSpan(new StyleSpan(Typeface.BOLD), 0, item.eventName.length(), 0);
                    builder.append(spanEvent);
                }
            }

            binding.profileNameTxt.setText(builder, TextView.BufferType.SPANNABLE);
            binding.profileNameTxt.setMovementMethod(LinkMovementMethod.getInstance());

        }

        private void setPostComment(final FeedCardModel item) {
            SpannableStringBuilder builder = new SpannableStringBuilder();

            Spannable spanUser = new SpannableString(item.commentUserName);

            ClickableSpan clickUser = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    view.startAnimation(Utils.INSTANCE.setClickEffect());
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setColor(ContextCompat.getColor(activity, R.color.gray));
                    ds.setUnderlineText(false);
                }
            };

            spanUser.setSpan(clickUser, 0, spanUser.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanUser.setSpan(new StyleSpan(Typeface.BOLD), 0, item.commentUserName.length(), 0);

            builder.append(spanUser);

            if (item.commentText != null) {
                SpannableStringBuilder spanComment = Utils.INSTANCE.getPostWithTags(" " + item.commentText);

                ClickableSpan clickComment = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        view.startAnimation(Utils.INSTANCE.setClickEffect());
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        ds.setUnderlineText(false);
                    }
                };

                spanComment.setSpan(clickComment, 0, spanComment.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(spanComment);
            }

            binding.commentTextLayout.setText(builder, TextView.BufferType.SPANNABLE);
            binding.commentTextLayout.setMovementMethod(LinkMovementMethod.getInstance());

        }

        private void setPostDescription(final FeedCardModel item) {
            SpannableStringBuilder builder = Utils.INSTANCE.getPostWithTags(item.postDescriptionText);
            binding.txtFeedSubject.setMovementMethod(LinkMovementMethod.getInstance());
            binding.txtFeedSubject.setText(builder, TextView.BufferType.SPANNABLE);
        }

        private void setPostShared(final FeedCardModel item) {
            SpannableStringBuilder builder = new SpannableStringBuilder();

            Spannable spanUser = new SpannableString(item.sharerName);

            ClickableSpan clickUser = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    view.startAnimation(Utils.INSTANCE.setClickEffect());
                    String currentUserId = SessionManager.getInstance().getCurrentUser(activity).getId();
                    if (currentUserId != null) {

                        NavigationController.Companion.getInstance().profileModule().goToProfile(activity, item.sharerId);
                    }
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setColor(ContextCompat.getColor(activity, R.color.gray));
                    ds.setUnderlineText(false);
                }
            };

            spanUser.setSpan(clickUser, 0, spanUser.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanUser.setSpan(new StyleSpan(Typeface.BOLD), 0, item.sharerName.length(), 0);

            builder.append(spanUser);

            Spannable spanShared = new SpannableString(" " + activity.getResources().getString(R.string.shared_a_post));

            spanShared.setSpan(new StyleSpan(Typeface.NORMAL), 0, activity.getResources().getString(R.string.shared_a_post).length(), 0);
            builder.append(spanShared);

            binding.sharedProfileNameTxt.setText(builder, TextView.BufferType.SPANNABLE);
            binding.sharedProfileNameTxt.setMovementMethod(LinkMovementMethod.getInstance());

        }

    }


    public void removeAt(int position) {
        posts.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, posts.size());
    }

    public void setPosts(List<FeedCardModel> posts) {
        if (currentUserId == null) {
            UserModel currentUser = SessionManager.getInstance().getCurrentUser(activity);
            if (currentUser != null) {
                currentUserId = currentUser.getId();
            } else {
                Coordinator.goToStart(activity);
                activity.finish();
            }
        }


        this.posts = posts;
        notifyDataSetChanged();
    }

    public enum PostsType {
        CurrentUser, Others
    }

    public static class RaffleViewHolder extends RecyclerView.ViewHolder implements RaffleCardModel.RaffleItemHandler {

        public final ItemCardRaffleBinding binding;
        public CardView itemContainer;

        Activity activity;
        RafflesManager rafflesManager;

        public RaffleViewHolder(ItemCardRaffleBinding binding, Activity activity, RafflesManager rafflesManager) {
            super(binding.getRoot());
            this.activity = activity;
            this.rafflesManager = rafflesManager;
            this.itemContainer = binding.getRoot().findViewById(R.id.card_view_raffle);
            this.binding = binding;
        }

        public void bind(final RaffleCardModel item, final int position) {

            Glide.with(activity)
                    .load(item.imageUrl)
                    .transition(withCrossFade())
                    .apply(RequestOptions.placeholderOf(R.drawable.violao)) // TODO colocar um placeholder
                    .into(binding.raffleImage);

            binding.raffleName.setAllCaps(true);

            item.handler = this;

            binding.setRaffleCard(item);
            binding.executePendingBindings();
        }

        @Override
        public void onClickParticipateButton(String id, Boolean state, RaffleCardModel.RaffleItemParticipateFeedback feedback) {
            if (state)
                rafflesManager.joinRaffle(id, feedback);
            else
                rafflesManager.leaveRaffle(id, feedback);

        }
    }

    private void confirmBeforeShare(String userName, final br.com.goin.component.ui.kit.dialog.DialogUtils.DialogCallback callback) {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        String shareAlert = String.format(activity.getResources().getString(R.string.share_alert), userName);
        builder.setMessage(shareAlert);
        builder.setPositiveButton(R.string.share, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.onClickOk();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create();
        builder.show();
    }
}
