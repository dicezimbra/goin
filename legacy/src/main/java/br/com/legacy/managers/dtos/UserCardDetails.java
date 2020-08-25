package br.com.legacy.managers.dtos;

/**
 * Created by appsimples on 8/9/17.
 */

public class UserCardDetails {
    public String id;
    public String followerName;
    public String followerAvatar;
    public String userName;
    public String userAvatar;
    public Boolean followedByMe;

    // get the User Card EventSessionDetail of a friends list (different attribute names)

    public String userId;
    public String name;
    public String avatar;
    public boolean invitedByMe;

    // get the User Card EventSessionDetail from a club

    public String profilePicture;
    public String chatId;

}
