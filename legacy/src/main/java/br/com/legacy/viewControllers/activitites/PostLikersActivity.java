package br.com.legacy.viewControllers.activitites;

import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import br.com.legacy.adapters.ItemAvatarTextAdapter;
import br.com.legacy.managers.PostLikersManager;
import br.com.legacy.models.ItemAvatarTextModel;
import br.com.legacy.navigation.Coordinator;
import br.com.R;
import br.com.legacy.utils.Constants;
import br.com.databinding.ActivityPostLikersBinding;

import java.util.List;

public class PostLikersActivity extends AppCompatActivity implements PostLikersManager.PostLikerHandler {

    RecyclerView postLikersRecycleView;
    ItemAvatarTextAdapter adapter;
    ActivityPostLikersBinding binding;

    PostLikersManager manager;
    String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_likers);

        Coordinator.setStatusBarColor(this,null);

   //     ToolbarConfigurator.configToolbar(binding.postLikersToolbar,getResources().getString(R.string.post_likes),R.drawable.ic_arrow_back,0);
        postLikersRecycleView = binding.postLikersRecycleView;

        postId = getIntent().getStringExtra(Constants.POST_ID);

        manager = new PostLikersManager(this);
        manager.handler = this;
        manager.requestPostLiekers(postId);

        binding.refreshLayout.setRefreshing(true);
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                manager.requestPostLiekers(postId);
            }
        });

//        binding.postLikersToolbar.toolbarLeftIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }

    @Override
    public void onReceivePostLikers(List<ItemAvatarTextModel> items) {

        adapter = new ItemAvatarTextAdapter(items);
        postLikersRecycleView.setLayoutManager(new LinearLayoutManager(this));
        postLikersRecycleView.setAdapter(adapter);
        if (items == null || items.isEmpty()) {
            binding.noLikesText.setVisibility(View.VISIBLE);
            binding.postLikersRecycleView.setVisibility(View.GONE);
        } else {
            binding.noLikesText.setVisibility(View.GONE);
            binding.postLikersRecycleView.setVisibility(View.VISIBLE);
        }

        binding.refreshLayout.setRefreshing(false);
    }

    @Override
    public void onFailure(String message) {

        binding.refreshLayout.setRefreshing(false);
      //  DialogUtils.INSTANCE.show(this, message);
    }

}
