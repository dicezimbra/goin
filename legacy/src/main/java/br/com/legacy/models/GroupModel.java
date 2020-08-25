package br.com.legacy.models;

import java.util.List;

import br.com.goin.component.session.user.UserModel;

public class GroupModel {

    private String id;
    private String name;
    private List<UserModel> members;
    private String photo;

    public GroupModel(String id, String name, List<UserModel> members, String photo) {
        this.id = id;
        this.name = name;
        this.members = members;
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public List<UserModel> getMembers() {
        return members;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMembers(List<UserModel> members) {
        this.members = members;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
