package br.com.legacy.models;

import android.content.Context;

import br.com.R;
import br.com.goin.component.session.user.UserModel;

import static br.com.legacy.models.FriendModel.FriendStatus.NotInvited;


/**
 * Created by teruya on 04/09/2017.
 */

public class FriendModel extends UserModel {

    public FriendModel(String photoUrl,String name, FriendStatus friendStatus){
        this.setName(name);
        this.setProfilePicture(photoUrl);
        this.friendStatus = friendStatus;
    }

    public FriendModel(String id, String name, String photoUrl, FriendStatus friendStatus){
        this.setId(id);
        this.setName(name);
        this.setProfilePicture(photoUrl);
        this.friendStatus = friendStatus;
    }
    public enum FriendStatus{
        Invited,NotInvited
    }

    public FriendStatus friendStatus;

    @Override
    public String getTextButton(Context context){
        switch (getActionType()){
            case Invite:
                if (this.friendStatus == NotInvited){
                    return context.getString(R.string.invite_button);
                } else{
                    return context.getString(R.string.invited_button);
                }
            case Follow:
                return context.getString(R.string.follow);
            default:
                return context.getString(R.string.invite_button);
        }
    }

    @Override
    public boolean setBackgroundPrimaryColor() {
        switch (getActionType()){
            case Invite:
                return this.friendStatus != NotInvited;
            default:
                return true;
        }
    }

    @Override
    public boolean setTextPrimaryColor() {
        switch (getActionType()){
            case Invite:
                return this.friendStatus == NotInvited;
            default:
                return false;
        }
    }


}
