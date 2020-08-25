package br.com.legacy.managers.dtos;

import br.com.goin.component.model.event.Event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by appsimples on 04/10/17.
 */

public class EventDetails implements Serializable {
    public String description;
    public String id;
    public String clubId;
    public String image;
    public String name;
    public String club;
    public String placeName;
    public String placeAddress;
    public Long startDate;
    public Long endDate;
    public String categoryName;
    public Integer interestedCount;
    public Integer checkInCount;
    public Integer allConfirmedCount;
    public Information information;
    public ArrayList<TicketDetails> tickets;
    public Float latitude;
    public Float longitude;
    public Integer highestPrice;
    public String categoryType;
    public Boolean isConfirmed;
    public Event.EventConfirmationType confirmType;
    public Integer lowestPrice;
    public String originId;
    public String originType;
    public String originAction;
    public String originURL;
    public String[] subcategories;
    public Boolean originHasDiscount;
    public String imageWidth;
    public String imageHeight;
    public String imageAspect;
    public List<CategoriesInfo>  categoriesInfos;
    public OriginInfos originInfos;

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
