package br.com.legacy.managers.dtos;

import java.util.List;

import br.com.goin.component.model.event.api.response.ApiEvent;

/**
 * Created by appsimples on 10/16/17.
 */

public class Club {

    public String id;
    public String name;
    public Integer followersCount;
    public String logoImage;
    public String coverImage;
    public String description;
    public String address;
    public float rating;
    public Integer ratingCount;
    public Boolean followedByMe;
    public List<PhotoGalleryItem> photoGallery;
    public List<Post> posts;
    public List<ApiEvent> events;
    public String website;
    public List<UserCardDetails> followers;
    public Float latitude;
    public Float longitude;
    // TODO Lista de eventos

    public class Rating {
        public Float average;
        public Integer count;
    }

    public class PhotoGalleryItem {
        public String url;
    }

    public Club club;
    public List<Club> searchClubs;
    public List<Club> followedClubs; // TODO passar para a classe que a Luciana criou
    public String followClub; // TODO MESMA COISA
    public String unfollowClub; // TODO MESMA COISA
}
