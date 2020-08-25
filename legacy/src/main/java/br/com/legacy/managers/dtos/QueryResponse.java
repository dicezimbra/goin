package br.com.legacy.managers.dtos;

import com.google.gson.annotations.SerializedName;

import br.com.goin.component.model.event.Banner;
import br.com.goin.component.session.user.UserModel;
import br.com.legacy.models.ValidateCouponModel;

import java.util.List;

/**
 * Created by appsimples on 04/10/17.
 */

public class QueryResponse {
    @SerializedName("events") public List<EventDetails> events;
    @SerializedName("allEvents") public List<EventDetails> allEvents;
    @SerializedName("eventSuggestions") public List<EventDetails> eventSuggestions;
    @SerializedName("searchEvents") public List<EventDetails> searchEvents;
    @SerializedName("myEvents") public List<EventDetails> myEvents;
    @SerializedName("searchMapEvents") public List<EventDetails> searchMapEvents;
    @SerializedName("searchMapSuggestions") public List<EventDetails> searchMapSuggestions;
    @SerializedName("confirmPresence") public String confirmPresence;
    @SerializedName("unconfirmPresence") public String unconfirmPresence;
    @SerializedName("buyTicket") public String buyTicket;
    @SerializedName("cancelBuyTicket") public String cancelBuyTicket;
    @SerializedName("confirmBuyTicket") public String confirmBuyTicket;
    @SerializedName("getEvent") public EventDetails getEvent;
    @SerializedName("createPost") public Post createPost;
    @SerializedName("searchUsers") public List<UserModel> searchUsers;
    @SerializedName("searchFacebookFriends") public List<UserModel> searchFacebookFriends;
    @SerializedName("checkInAvailable") public List<EventDetails> checkInAvailable;

    @SerializedName("getPost") public Post getPost;
    @SerializedName("confirmedPeople") public List<UserCardDetails> confirmedPeople;
    @SerializedName("facebookSignIn") public FacebookSigninResponse facebookSignIn;
    @SerializedName("sendMessage") public SendMessageOutput sendMessage;
    @SerializedName("sendGroupMessage") public String sendGroupMessage;
    @SerializedName("createGroup") public CreateGroupOutputView createGroup;
    @SerializedName("getMyTicketDetail") public TicketDetails getMyTicketDetail;
    @SerializedName("buyTicketCreditCard") public TicketDetails buyTicketCreditCard;
    @SerializedName("buyTicketBankSlip") public TicketDetails buyTicketBankSlip;
    @SerializedName("confirmTokenIsValid") public TokenValid confirmTokenIsValid;
    @SerializedName("banners") public List<Banner> banners;
    @SerializedName("validateCoupon") public ValidateCouponModel validateCoupon;
}
