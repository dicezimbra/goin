package br.com.goin.base.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.goin.base.model.EventModel;

/**
 * Created by appsimples on 04/10/17.
 */

public class EventDetails implements Serializable {
    @SerializedName("description")
    public String description;
    @SerializedName("id")
    public String id;
    @SerializedName("clubId")
    public String clubId;
    @SerializedName("image")
    public String image;
    @SerializedName("name")
    public String name;
    @SerializedName("club")
    public String club;
    @SerializedName("placeName")
    public String placeName;
    @SerializedName("placeAddress")
    public String placeAddress;
    @SerializedName("startDate")
    public Long startDate;
    @SerializedName("endDate")
    public Long endDate;
    @SerializedName("categoryName")
    public String categoryName;
    @SerializedName("interestedCount")
    public Integer interestedCount;
    @SerializedName("checkInCount")
    public Integer checkInCount;
    @SerializedName("allConfirmedCount")
    public Integer allConfirmedCount;
    @SerializedName("information")
    public Information information;
    @SerializedName("tickets")
    public ArrayList<TicketDetails> tickets;
    @SerializedName("latitude")
    public Float latitude;
    @SerializedName("longitude")
    public Float longitude;
    @SerializedName("highestPrice")
    public Integer highestPrice;
    @SerializedName("categoryType")
    public String categoryType;
    @SerializedName("isConfirmed")
    public Boolean isConfirmed;
    @SerializedName("confirmType")
    public EventModel.EventConfirmationType confirmType;
    @SerializedName("lowestPrice")
    public Integer lowestPrice;
    @SerializedName("originType")
    public String originType;
    @SerializedName("originURL")
    public String originURL;
    @SerializedName("subcategories")
    public String[] subcategories;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventDetails that = (EventDetails) o;
        return Objects.equals(description, that.description) &&
                Objects.equals(id, that.id) &&
                Objects.equals(clubId, that.clubId) &&
                Objects.equals(image, that.image) &&
                Objects.equals(name, that.name) &&
                Objects.equals(club, that.club) &&
                Objects.equals(placeName, that.placeName) &&
                Objects.equals(placeAddress, that.placeAddress) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(interestedCount, that.interestedCount) &&
                Objects.equals(checkInCount, that.checkInCount) &&
                Objects.equals(allConfirmedCount, that.allConfirmedCount) &&
                Objects.equals(information, that.information) &&
                Objects.equals(tickets, that.tickets) &&
                Objects.equals(latitude, that.latitude) &&
                Objects.equals(longitude, that.longitude) &&
                Objects.equals(isConfirmed, that.isConfirmed) &&
                confirmType == that.confirmType &&
                Objects.equals(lowestPrice, that.lowestPrice);
    }

    @Override
    public int hashCode() {

        return Objects.hash(description, id, clubId, image, name, club, placeName, placeAddress, startDate, endDate, interestedCount, checkInCount, allConfirmedCount, information, tickets, latitude, longitude, isConfirmed, confirmType, lowestPrice);
    }


}
