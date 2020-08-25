package br.com.legacy.managers.dtos;

import java.util.List;

/**
 * Created by appsimples on 1/8/18.
 */

public class Feed {
    public List<Post> post;
    public String lastPostId;
    public String lastMasterPostId;
    public Boolean morePostsToGet;
}
