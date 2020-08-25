package br.com.goin.base.model;

import android.content.Context;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import br.com.goin.base.dtos.CategoriesInfo;
import br.com.goin.base.dtos.CategoryDetails;
import br.com.goin.base.dtos.TicketDetails;

/**
 * Created by appsimples on 22/09/17.
 */

public class EventModel implements Serializable {


    public static final String TYPE_INGRESSE = "INGRESSE";

    public enum EventConfirmationType{Interested, CheckedIn}

    public static final String EVENT = "event";

    public String id;
    public String clubId;
    public String name;
    public String description;
    public String club;
    public String categoryName;
    public String image;
    public EventInformation information;
    public String placeName;
    public String placeAddress;
    public Calendar startDate;
    public Boolean originHasDiscount;
    public CategoryDetails.CategoryType categoryType;
    public Calendar endDate;
    public Integer intentionCount;
    public Integer checkInCount;
    public List<TicketDetails> tickets;
    public Boolean isConfirmed;
    public String photoUrl;
    public Float distance;
    public String[] subcategories;
    public Double lat;
    public Double lng;
    public EventConfirmationType confirmType;
    public String originType;
    public String originAction;
    public String originURL;
    public Integer lowestPrice;
    public Double biggerPrice;
    public Integer highestPrice;
    public String originId;
    public String imageWidth;
    public String imageHeight;
    public String imageAspect;
    public String buttonColor;
    public String bigIcon;
    public String smallIconColored;
    public String smallIconWhite;
    public OriginInfosModel originInfos = new OriginInfosModel();

    public String categoryId;


    public EventModel(String[] subcategories, Integer highsestPrice, String originId, String originURL,
                      String id, String categoryType, String clubId, String name, String image,
                      String description, String club, String placeName, String placeAddress, Calendar startDate, Calendar endDate,
                      Integer intentionCount, Integer checkInCount, List<TicketDetails> tickets, Boolean isConfirmed, String photoUrl,
                      Float distance, Double lat, Double lng, EventConfirmationType confirmType,
                      Integer lowestPrice, Double biggerPrice, String originType, boolean originHasDiscount, String originAction,
                      CategoriesInfo categoriesInfo) {
        this.id = id;
        this.subcategories = subcategories;
        this.highestPrice = highsestPrice;
        this.categoryType = CategoryDetails.CategoryType.valueOf(categoryType);
        this.image = image;
        this.clubId = clubId;
        this.name = name;
        this.description = description;
        this.club = club;
        this.placeName = placeName;
        this.placeAddress = placeAddress;
        this.startDate = startDate;
        this.endDate = endDate;
        this.intentionCount = intentionCount;
        this.checkInCount = checkInCount;
        this.tickets = tickets;
        this.isConfirmed = isConfirmed;
        this.photoUrl = photoUrl;
        this.distance = distance;
        this.lat = lat;
        this.lng = lng;
        this.confirmType = confirmType;
        this.lowestPrice = lowestPrice;
        this.biggerPrice = biggerPrice;
        this.originType = originType;
        this.originAction = originAction == null ? "SITE": originAction;
        this.originURL = originURL;
        this.originId = originId;
        this.originHasDiscount = originHasDiscount;

    }



    public EventModel() {}

    public EventModel(String image, String eventId) {
        this.id = eventId;
        this.image = image; }



    public String getEventDate() {
        if (this.startDate != null) {
            return getShortDate() + " (" + getShortWeekDay() + ")";
        } else {
            return "";
        }
    }

    public String getShortDate(){
        DateFormat date = new SimpleDateFormat("dd MMM", Locale.getDefault());
        if (this.startDate != null) {
            return date.format(this.startDate.getTime()).toUpperCase();
        } else {
            return "";
        }
    }

  /*  public String getShortDateText(Context context){
        DateFormat date = new SimpleDateFormat("dd MMM", Locale.getDefault());
        String startDateText = "";
        String endDateText = "";
        if(this.startDate != null){
            startDateText = date.format(this.startDate.getTime()).toUpperCase();
        }
        if(this.endDate != null){
            endDateText = date.format(this.endDate.getTime()).toUpperCase();
        }

        if(startDateText.equals(endDateText)){
            return startDateText;
        } else {
            return startDateText + " " + context.getString(R.string.to) + "\n" + endDateText;
        }
    }*/

    public String getWeekDay(){
        DateFormat dayWeek = new SimpleDateFormat("EEEE", Locale.getDefault());
        if (this.startDate != null) {
            return dayWeek.format(this.startDate.getTime());
        } else {
            return "";
        }
    }

    public String getShortWeekDay(){
        DateFormat dayWeek = new SimpleDateFormat("EEEE", Locale.getDefault());
        if (this.startDate != null) {
            return dayWeek.format(this.startDate.getTime()).split("-")[0];
        } else {
            return "";
        }
    }

 /*   public String getShortWeekDayText(Context context){
        DateFormat dayWeek = new SimpleDateFormat("EEEE", Locale.getDefault());
        String startWeekDayText = "";
        String endWeekDayText = "";

        if(this.startDate != null){
            startWeekDayText = dayWeek.format(this.startDate.getTime()).substring(0,3);
        }
        if(this.endDate != null){
            endWeekDayText = dayWeek.format(this.endDate.getTime()).substring(0,3);
        }

        if(startWeekDayText.equals(endWeekDayText)){
            return startWeekDayText;
        } else {
            return startWeekDayText + " " + context.getString(R.string.to) + " " + endWeekDayText;
        }
    }*/

    public String getEventHour() {
        DateFormat eventHourMinutes = new SimpleDateFormat("HH'h'mm", Locale.getDefault());
        DateFormat eventHour = new SimpleDateFormat("HH'h'", Locale.getDefault());

        if (startDate != null) {
            if (startDate.get(Calendar.MINUTE) != 0)
                return eventHourMinutes.format(this.startDate.getTime());
            return eventHour.format(this.startDate.getTime());
        } else {
            return "";
        }
    }

    public String getEventMinimumPrice() {
            if (lowestPrice == null){
                return "";
            }else {
                return "R$" + new DecimalFormat("##,###,###,##0", new DecimalFormatSymbols(new Locale ("pt", "BR"))).format(lowestPrice);
            }
    }

    public String getEventDistance() {
        if (distance != null) {
            if (distance < 1000) {
                return String.format("%dm", distance.intValue());
            } else {
                Float dkm = distance/1000;
                if (distance/100 < 100) {
                    return String.format("%dkm", dkm.intValue());
                } else {
                    return String.format("%.1fkm", dkm);
                }
            }
        } else {
            return "";
        }
    }

   /* public String getEventDetailDistance(Context context) {
        if (distance != null) {
            return String.format(context.getResources().getString(R.string.distance_from_you), getEventDistance());
        } else {
            return "";
        }
    }*/

    public void incrementIntentionCount(){
        this.intentionCount ++;
    //    notifyPropertyChanged(this.intentionCount);
    }

    public void decrementIntentionCount(){
        this.intentionCount --;
  //      notifyPropertyChanged(this.intentionCount);
    }


    public Integer getIntentionCount(){
        return this.intentionCount;
    }

    public boolean hasEventStarted(){
        Calendar calendar = Calendar.getInstance();
        return startDate.before(calendar) && endDate.after(calendar);
    }

    public boolean isCheckedIn(){
        return "CheckedIn".equals(this.confirmType.name());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventModel that = (EventModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(clubId, that.clubId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(club, that.club) &&
                Objects.equals(placeName, that.placeName) &&
                Objects.equals(placeAddress, that.placeAddress) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(intentionCount, that.intentionCount) &&
                Objects.equals(checkInCount, that.checkInCount) &&
                Objects.equals(tickets, that.tickets) &&
                Objects.equals(isConfirmed, that.isConfirmed) &&
                Objects.equals(photoUrl, that.photoUrl) &&
                Objects.equals(distance, that.distance) &&
                Objects.equals(lat, that.lat) &&
                Objects.equals(lng, that.lng) &&
                confirmType == that.confirmType &&
                Objects.equals(lowestPrice, that.lowestPrice) &&
                Objects.equals(biggerPrice, that.biggerPrice);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, clubId, name, description, club, placeName, placeAddress, startDate, endDate, intentionCount, checkInCount, tickets, isConfirmed, photoUrl, distance, lat, lng, confirmType, lowestPrice, biggerPrice);
    }

    public boolean isCorporative(){
        return "corporation".equals(originType);
    }

    public boolean isFree(){
        return lowestPrice == 0;
    }
}
