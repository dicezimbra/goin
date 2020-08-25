package br.com.legacy.models;

import android.app.Activity;
import android.content.Context;

import br.com.R;
import br.com.goin.component.session.user.UserModel;
import br.com.legacy.utils.DateFormatter;

/**
 * Created by teruy on 11/09/2017.
 */

public class CommentModel {

    public Activity activity;

    public interface ItemCommentHandler {
        void onClickText(CommentModel item);
    }

    ItemCommentHandler handler;

    public CommentModel() {}

    public CommentModel(Activity activity) {
        this.activity = activity;
    }

    public CommentModel(UserModel userModel, String commentString, Long time){
        this.user = userModel;
        this.commentString = commentString;
        this.time = time;
    }
    public UserModel user;

    public String authorName;
    public String commentString;
    public Long time;
    public String postId;
    public String userId;
    public String commentId;

    public String getCommentTime(Context context) {
        if (this.time != null) {
            return DateFormatter.getTimeDifText(this.time, context);
        }
        return context.getString(R.string.long_time_ago);
    }

    public void setHandler(ItemCommentHandler handler) {
        this.handler = handler;
    }

    public void onClickText() {
        if (this.handler != null) {
            handler.onClickText(this);
        }
    }
}
