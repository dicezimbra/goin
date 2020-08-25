package br.com.legacy.managers.dtos;

import br.com.goin.component.session.user.UserModel;

/**
 * Created by appsimples on 4/16/18.
 */

public class FacebookSigninResponse {
    public UserModel user;
    public Boolean isSignup = false;
}
