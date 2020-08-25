package br.com.legacy.managers;

import android.app.Activity;

import br.com.goin.component.navigation.NavigationController;
import br.com.legacy.managers.dtos.ErrorResponse;
import br.com.legacy.managers.dtos.PostLiker;
import br.com.legacy.models.ItemAvatarTextModel;
import br.com.legacy.navigation.Coordinator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by appsimples on 10/17/17.
 */

public class PostLikersManager extends BaseManager {

    public interface PostLikerHandler {
        void onReceivePostLikers(List<ItemAvatarTextModel> items);
        void onFailure(String message);
    }

    public PostLikerHandler handler;

    public PostLikersManager(Activity activity) {
        super(activity);
    }

    public void requestPostLiekers(final String postId) {
        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                serviceManager.getPostsLikers(PostLikersManager.this, new RequestResponseHandler<HashMap<String,List<PostLiker>>>() {
                    @Override
                    public void onResponse(HashMap<String, List<PostLiker>> response) {
                        if (handler != null) {
                            handler.onReceivePostLikers(mapDtoToModelList(response.get("postLikers")));
                        }
                    }

                    @Override
                    public void onFailure(ErrorResponse error) {
                        if (handler != null) {
                            handler.onFailure(error.message);
                        }
                    }
                }, postId);
            }
        });
    }

    public ItemAvatarTextModel mapDtoToModel(PostLiker postLiker) {
        ItemAvatarTextModel model = new ItemAvatarTextModel(
                activity,
                postLiker.avatar,
                postLiker.name,
                postLiker.userId
        );
        model.setHandler(new ItemAvatarTextModel.ItemAvatarTextHandler() {
            @Override
            public void onClickItem(ItemAvatarTextModel item) {
                goToUserProfile(item);
            }

            @Override
            public void onClickAvatar(ItemAvatarTextModel item) {
                goToUserProfile(item);
            }

            @Override
            public void onClickText(ItemAvatarTextModel item) {
                goToUserProfile(item);
            }
        });
        return model;
    }

    public List<ItemAvatarTextModel> mapDtoToModelList(List<PostLiker> postLikers) {
        List<ItemAvatarTextModel> models = new ArrayList<>();
        for (PostLiker postLiker: postLikers) {
            models.add(mapDtoToModel(postLiker));
        }
        return models;
    }

    public void goToUserProfile(ItemAvatarTextModel item) {
        NavigationController.Companion.getInstance().profileModule().goToProfile(activity, item.id.toString());
    }
}
