package br.com.legacy.models;

import android.app.Activity;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import br.com.BR;
import br.com.goin.component.session.user.UserModel;

import java.io.Serializable;

/**
 * Created by ruderos on 10/17/17.
 */

public class ItemAvatarTextModel extends BaseObservable implements Serializable{

    public interface ItemAvatarTextHandler {
        void onClickItem(ItemAvatarTextModel item);
        void onClickAvatar(ItemAvatarTextModel item);
        void onClickText(ItemAvatarTextModel item);
    }

    @Bindable
    public String avatarUrl;
    @Bindable
    public String text;
    @Bindable
    public Boolean isSelected;

    public UserModel userModel;

    public Object id;

    public Activity activity;
    ItemAvatarTextHandler handler;

    public ItemAvatarTextModel(Activity activity, String avatarUrl, String text, Object id) {
        this.activity = activity;
        this.avatarUrl = avatarUrl;
        this.text = text;
        this.id = id;
        this.isSelected = false;
        notifyChange();
    }

    public ItemAvatarTextModel(Activity activity, String avatarUrl, String text, Object id, Boolean isSelected) {
        this.activity = activity;
        this.avatarUrl = avatarUrl;
        this.text = text;
        this.id = id;
        this.isSelected = isSelected;
        notifyChange();
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        notifyPropertyChanged(BR.avatarUrl);
    }

    public void setText(String text) {
        this.text = text;
        notifyPropertyChanged(BR.text);
    }

    public void setSelected(Boolean selected) {
        this.isSelected = selected;
        notifyPropertyChanged(BR.isSelected);
    }

    public void setHandler(ItemAvatarTextHandler handler) {
        this.handler = handler;
    }

    public void onClickItem() {
        if (this.handler != null) {
            handler.onClickItem(this);
        }
    }

    public void onClickAvatar() {
        if (this.handler != null) {
            handler.onClickAvatar(this);
        }
    }

    public void onClickText() {
        if (this.handler != null) {
            handler.onClickText(this);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ItemAvatarTextModel){
            return this.id.equals(((ItemAvatarTextModel) obj).id);
        }
        return false;
    }
}
