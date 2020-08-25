package br.com.goin.features.wallet.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import androidx.core.content.ContextCompat;
import br.com.goin.base.dtos.EventDetails;
import br.com.goin.feature.wallet.R;

/**
 * Created by appsimples on 05/10/17.
 */

public class TicketModel implements Serializable {


    public interface TicketItemHandler {
        void onClickButton(TicketModel ticket);
        void onClickContainer(TicketModel ticket);
    }

    public transient Activity activity;
    public String id;
    public String name;
    public Double price;
    //public String event;
    public String club;
    public String clubId;
    public Calendar date;
    public String description;
    public Boolean isHalfPrice;
    public Boolean isVipBox;
    public Integer vipCapacity;
    public String userName;
    public String userId;
    public Integer bought;
    public Calendar purchaseDate;
    public Integer total;
    public TicketStatus buyStatus;
    public String eventName;
    public Long eventDate;
    public String eventAddress;
    public CardType ticketCardType;
    public Integer remaining;
    public boolean isPastEvent = true;
    public EventDetails event;
    public String origin;
    public String qrCode;
    public String originType;
    public Boolean discount;
    public Integer discountPercent;
    public Double discountValue;
    public Double finalValue;
    public String originUrl;
    public String ticketType;
    public boolean isValid;
    public String paymentId;

    public TicketItemHandler handler;

    public enum TicketStatus {
        FOR_SELL, SOLD_OUT, CONFIRMED, PENDING, VIP_BOX_FRIENDS_PENDING, VIP_BOX_PENDING, VIP_BOX_CONFIRMED
    }

    public enum CardType {
        TICKET_FOR_SELL, MY_TICKET, MY_TICKET_VIP_BOX
    }

    public TicketModel() {
    }

    public TicketModel(String id, String name, Double price, String club, String clubId,
                       Calendar date, String description, Boolean isHalfPrice, Boolean isVipBox,
                       Integer vipCapacity, String userName, String userId, Integer bought,
                       Calendar purchaseDate, Integer total, TicketStatus buyStatus,
                       String eventName, String eventAddress, CardType ticketCardType,
                       Integer remaining, boolean isPastEvent, EventDetails event) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.club = club;
        this.clubId = clubId;
        this.date = date;
        this.description = description;
        this.isHalfPrice = isHalfPrice;
        this.isVipBox = isVipBox;
        this.vipCapacity = vipCapacity;
        this.userName = userName;
        this.userId = userId;
        this.bought = bought;
        this.purchaseDate = purchaseDate;
        this.total = total;
        this.buyStatus = buyStatus;
        this.eventName = eventName;
        this.eventAddress = eventAddress;
        this.ticketCardType = ticketCardType;
        this.remaining = remaining;
        this.isPastEvent = isPastEvent;
        this.event = event;
        this.discount = discount;
        this.discountPercent = discountPercent;
        this.discountValue = discountValue;
        this.finalValue = finalValue;
        this.originUrl = originUrl;
    }

    public int setCardColor(Context context) {
        Calendar currentDate = Calendar.getInstance();
        if (ticketCardType != null) {
            if (ticketCardType == CardType.TICKET_FOR_SELL) {
                if (date != null && date.before(currentDate)) {
                    return ContextCompat.getColor(context, R.color.colorAccent);
                } else {
                    return ContextCompat.getColor(context, R.color.white);
                }
            } else {
                if (date != null && date.before(currentDate)) {
                    this.isPastEvent = true;
                    return ContextCompat.getColor(context, R.color.colorPrimaryDarkAccent);
                } else {
                    this.isPastEvent = false;
                    return ContextCompat.getColor(context, R.color.colorDarkRed);
                }
            }
        }
        return ContextCompat.getColor(context, R.color.colorPrimary);
    }

    public int setButtonColor(Context context) {
        if (buyStatus != null) {
            if (remaining != null) {
                if (remaining < 1) {
                    buyStatus = TicketStatus.SOLD_OUT;
                }
            }
            switch (buyStatus) {
                case FOR_SELL:
                    return ContextCompat.getColor(context, R.color.colorAccent);
                case SOLD_OUT:
                    return ContextCompat.getColor(context, R.color.colorDarkRed);
                case CONFIRMED:
                    return ContextCompat.getColor(context, R.color.colorPrimaryDarkRedAccent);
                case PENDING:
                    return ContextCompat.getColor(context, R.color.colorPrimaryDarkRedAccent);
                case VIP_BOX_PENDING:
                    return ContextCompat.getColor(context, R.color.colorPrimaryDarkRedAccent);
                case VIP_BOX_FRIENDS_PENDING:
                    return ContextCompat.getColor(context, R.color.colorPrimaryDarkRedAccent);
                case VIP_BOX_CONFIRMED:
                    return ContextCompat.getColor(context, R.color.colorPrimaryDarkRedAccent);

            }
        }
        return ContextCompat.getColor(context, R.color.colorAccent);
    }

    public Drawable getButtonDrawable(Context context) {
        if(buyStatus != null){
            if (remaining != null){
                if (remaining < 1){
                    buyStatus = TicketStatus.SOLD_OUT;
                }
            }
            switch (buyStatus) {
                case FOR_SELL:
                    return ContextCompat.getDrawable(context,R.drawable.button_orange_style);
                case SOLD_OUT:
                   // return ContextCompat.getColor(context, R.color.colorDarkRed);
                case CONFIRMED:
                 //   return ContextCompat.getColor(context, R.color.colorPrimaryDarkRedAccent);
                case PENDING:
                //    return ContextCompat.getColor(context, R.color.colorPrimaryDarkRedAccent);
                case VIP_BOX_PENDING:
                //    return ContextCompat.getColor(context,R.color.colorPrimaryDarkRedAccent);
                case VIP_BOX_FRIENDS_PENDING:
                //    return ContextCompat.getColor(context, R.color.colorPrimaryDarkRedAccent);
                case VIP_BOX_CONFIRMED:
                //    return ContextCompat.getColor(context, R.color.colorPrimaryDarkRedAccent);

            }
        }
        return ContextCompat.getDrawable(context,R.drawable.button_orange_style);
    }


    public String getTicketStatus() {
        if(buyStatus != null){
            switch (buyStatus) {
                case FOR_SELL:
                    return activity.getResources().getString(R.string.buy);
                case SOLD_OUT:
                    return activity.getResources().getString(R.string.sold_out);
                case CONFIRMED:
                    return "";
                case PENDING:
                    return activity.getResources().getString(R.string.pending_payment);
                case VIP_BOX_CONFIRMED:
                    return "";
                case VIP_BOX_FRIENDS_PENDING:
                    return activity.getResources().getString(R.string.friends_pending_payment);
                case VIP_BOX_PENDING:
                    return activity.getResources().getString(R.string.pending_payment);
            }
        }
        return "";
    }

    public boolean isTicketStatusButtonVisible(){
        if(buyStatus != null){
            Calendar currentDate = Calendar.getInstance();
            return buyStatus != TicketStatus.CONFIRMED && buyStatus != TicketStatus.VIP_BOX_CONFIRMED && (date == null || !date.before(currentDate) || ticketCardType != CardType.TICKET_FOR_SELL);
        }
        return true;
    }

    public String getPrice() {
        return String.format(Locale.getDefault(), "R$ %.2f", price);
    }

    public String getDiscountPrice() {
        return String.format(Locale.getDefault(), "R$ %.2f", finalValue);
    }

    public Boolean hasTicketDiscount(){
        return discount != null && discount;
    }

    public String getEventDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
        if (this.date != null) {
            if (date.get(Calendar.MINUTE) != 0)
                return dateFormat.format(this.date.getTime()).toUpperCase()+"\n"+date.get(Calendar.HOUR_OF_DAY) + "h" + date.get(Calendar.MINUTE) +"m";
            return dateFormat.format(this.date.getTime()).toUpperCase()+"\n"+date.get(Calendar.HOUR_OF_DAY) + "h";
        } else {
            return "";
        }
    }

    public String getPurchaseDate(Context context) {
        if (this.purchaseDate != null) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String purchaseDate = dateFormat.format(this.purchaseDate.getTime()).toUpperCase();
            return String.format(context.getResources().getString(R.string.purchase_date), purchaseDate);
        } else {
            return "";
        }
    }

    public String getTextPurchasedTickets(Context context) {
        if (userName != null) {
            return userName;
        } else {
            switch (ticketCardType) {
                case MY_TICKET:
                    switch (buyStatus) {
                        case PENDING:
                            return context.getResources().getQuantityString(R.plurals.tickets_pending, bought, bought);
                        case CONFIRMED:
                            return context.getResources().getQuantityString(R.plurals.tickets_bought, bought, bought);
                    }
                    break;
                case MY_TICKET_VIP_BOX:
                    return context.getResources().getQuantityString(R.plurals.tickets_vip_box_bought, vipCapacity, vipCapacity);
            }
        }
        return "";
    }

    public void onClickTicketButton(){
        if (handler != null)
            handler.onClickButton(this);
    }

    public void onClickTicketContainer(){
        if(handler != null)
            handler.onClickContainer(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketModel that = (TicketModel) o;
        return isPastEvent == that.isPastEvent &&
                Objects.equals(activity, that.activity) &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(price, that.price) &&
                Objects.equals(club, that.club) &&
                Objects.equals(clubId, that.clubId) &&
                Objects.equals(date, that.date) &&
                Objects.equals(description, that.description) &&
                Objects.equals(isHalfPrice, that.isHalfPrice) &&
                Objects.equals(isVipBox, that.isVipBox) &&
                Objects.equals(vipCapacity, that.vipCapacity) &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(bought, that.bought) &&
                Objects.equals(purchaseDate, that.purchaseDate) &&
                Objects.equals(total, that.total) &&
                buyStatus == that.buyStatus &&
                Objects.equals(eventName, that.eventName) &&
                Objects.equals(eventDate, that.eventDate) &&
                Objects.equals(eventAddress, that.eventAddress) &&
                ticketCardType == that.ticketCardType &&
                Objects.equals(remaining, that.remaining) &&
                Objects.equals(event, that.event) &&
                Objects.equals(handler, that.handler);
    }

    public boolean isCorporative(){
        return "corporation".equals(originType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(activity, id, name, price, club, clubId, date, description, isHalfPrice, isVipBox, vipCapacity, userName, userId, bought, purchaseDate, total, buyStatus, eventName, eventDate, eventAddress, ticketCardType, remaining, isPastEvent, event, handler);
    }
}
