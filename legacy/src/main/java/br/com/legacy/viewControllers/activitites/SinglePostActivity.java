package br.com.legacy.viewControllers.activitites;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;

import com.ethanhua.skeleton.RecyclerViewSkeletonScreen;
import com.ethanhua.skeleton.Skeleton;
import br.com.legacy.adapters.FeedAdapter;
import br.com.legacy.managers.FeedManager;
import br.com.legacy.models.FeedCardModel;
import br.com.legacy.navigation.Coordinator;
import br.com.R;
import br.com.legacy.utils.Constants;
import br.com.databinding.ActivitySinglePostBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class SinglePostActivity extends AppCompatActivity implements FeedManager.PostRequestHandler, FeedAdapter.PostHandler {

    private FeedManager feedManager;
    private ActivitySinglePostBinding binding;
    private FeedAdapter adapter;
    private RecyclerViewSkeletonScreen skeletonScreen;
    private List<FeedCardModel> posts = new ArrayList<>();

    Disposable dComments;
    Disposable dLikes;
    Disposable dCreatePost;
    Disposable dDeletePost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_single_post);

        Coordinator.setStatusBarColor(this,null);

        binding.listFeedCards.setVisibility(View.VISIBLE);
        binding.listFeedCards.setLayoutManager(new LinearLayoutManager(this));

//        binding.settingsToolbar.toolbarLeftIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

   //     ToolbarConfigurator.configToolbar(binding.settingsToolbar, getString(R.string.post_title), R.drawable.ic_arrow_back,0);

        feedManager = new FeedManager(this);
        feedManager.postRequestHandler = this;

        String postId = getIntent().getStringExtra(Constants.POST_ID);
        if (postId == null) finish();
        feedManager.getPost(postId);

        adapter = new FeedAdapter(posts, this, this, feedManager, FeedAdapter.PostsType.Others, null);

        adapter.onClickDelete = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        
        adapter.limitPostDescription = false;

        skeletonScreen = Skeleton.bind(binding.listFeedCards)
                .adapter(adapter)
                .color(R.color.white)
                .count(1)
                .load(R.layout.item_feedcard_skeleton)
                .show();
    }

    @Override
    public void onReceivePosts(List<FeedCardModel> posts, String lastPostId, String lastMasterPostId, Boolean canGetMorePosts) {
        this.posts = posts;
        skeletonScreen.hide();
        binding.listFeedCards.setAdapter(adapter);
        adapter.setPosts(posts);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String error) {
        skeletonScreen.hide();
    }

    @Override
    public void onLikePost(FeedCardModel post, int position) {
        if (post.likedByMe) {
            feedManager.dislikePost(post.idPost);
        } else {
            feedManager.likePost(post.idPost);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        dDeletePost = FeedManager.postDeletedSubject
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        FeedCardModel deletedPost = null;
                        int index = 0;
                        for (FeedCardModel post : posts) {
                            if (post.idPost.equals(s)) {
                                deletedPost = post;
                                break;
                            }
                            index++;
                        }
                        if (deletedPost != null) {
                            posts.remove(deletedPost);
                            adapter.notifyItemRemoved(index);
                        }
                    }
                });

        dLikes = FeedManager.likeSubject
                .subscribe(new Consumer<FeedCardModel>() {
                    @Override
                    public void accept(FeedCardModel model) {
                        for (FeedCardModel post : posts) {
                            if (post.idPost.equals(model.idPost)) {
                                post.likedByMe = model.likedByMe;
                                post.likesCounter = model.likesCounter;
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

        dComments = FeedManager.commentSubject
                .subscribe(new Consumer<Bundle>() {
                    @Override
                    public void accept(Bundle bundle) {
                        if (bundle != null) {
                            String postId = bundle.getString(Constants.POST_ID);
                            for (FeedCardModel p : posts) {
                                if (p.idPost.equals(postId)) {
                                    p.commentsCounter = bundle.getInt(Constants.COMMENT_COUNT);
                                    p.commentText = bundle.getString(Constants.LAST_COMMENT_CONTENT);
                                    p.commentUserName = bundle.getString(Constants.LAST_COMMENT_AUTHOR);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });

        dCreatePost = FeedManager.createPostSubject
                .subscribe(new Consumer<FeedCardModel>() {
                    @Override
                    public void accept(FeedCardModel feedCardModel) {
                        posts.add(0, feedCardModel);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (dComments != null)
            dComments.dispose();
        if (dLikes != null)
            dLikes.dispose();
        if (dCreatePost != null)
            dCreatePost.dispose();
        if (dDeletePost != null)
            dDeletePost.dispose();
    }
}
