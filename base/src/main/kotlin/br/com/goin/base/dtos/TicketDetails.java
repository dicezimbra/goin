package br.com.goin.base.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

import br.com.goin.base.model.PurchaseModel;

/**
 * Created by appsimples on 10/10/17.
 */

public class TicketDetails implements Serializable {
    @SerializedName("id") public String id;
    @SerializedName("ticketId")    public String ticketId;
    @SerializedName("name")public String name;
    @SerializedName("price")public Double price;
    @SerializedName("description")public String description;
    @SerializedName("quantity")public Integer quantity;
    @SerializedName("isHalfPrice")public Boolean isHalfPrice;
    @SerializedName("isVipBox")public Boolean isVipBox;
    @SerializedName("capacity")public Integer capacity;
    @SerializedName("remaining")public Integer remaining;
    @SerializedName("eventDate")public Long eventDate;
    @SerializedName("eventName")public String eventName;
    @SerializedName("clubName")public String clubName;
    @SerializedName("available")public Boolean available;
    @SerializedName("buyerName")public String buyerName;
    @SerializedName("numTickets")public Integer numTickets;
    @SerializedName("paymentStatus")public PaymentStatus paymentStatus;
    @SerializedName("eventAddress")public String eventAddress;
    @SerializedName("paymentDate")public Long paymentDate;
    @SerializedName("ticketsInfo")public ArrayList<PurchaseModel.TicketsInfo> ticketsInfo;
    @SerializedName("paymentInfo")public PaymentInfo paymentInfo;
    @SerializedName("enumTicketType")public PurchaseModel.TicketType enumTicketType;
    @SerializedName("vipBoxId")public String vipBoxId;
    @SerializedName("event")public EventDetails event;
    @SerializedName("eventId")public String eventId;
    @SerializedName("qrCode")public String qrCode;
    @SerializedName("originType")public String originType;
    @SerializedName("ticketType")public String ticketType;
    @SerializedName("isValid")public boolean isValid;
    @SerializedName("paymentId")public String paymentId;
    @SerializedName("discount")public Boolean discount;

    public class PaymentInfo implements Serializable{

        @SerializedName("CPF")
        public String CPF;
        @SerializedName("type")
        public String type;
        // Bank Slip
        @SerializedName("postalCode")
        public String postalCode;
        @SerializedName("street")
        public String street;
        @SerializedName("streetNumber")
        public String streetNumber;
        @SerializedName("complement")
        public String complement;
        @SerializedName("neighborhood")
        public String neighborhood;
        @SerializedName("city")
        public String city;
        @SerializedName("state")
        public String state;
        @SerializedName("buyerEmail")
        public String buyerEmail;
        @SerializedName("buyerPhone")
        public String buyerPhone;
        @SerializedName("pdf")
        public String pdf;
        // Credit Card
        @SerializedName("number")
        public String number;
        @SerializedName("displayNumber")
        public String displayNumber;
        @SerializedName("cardHolder")
        public String cardHolder;
        @SerializedName("months")
        public Integer months;
        @SerializedName("buyerName")
        public String buyerName;
        @SerializedName("purchaseTotal")
        public Integer purchaseTotal;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PaymentInfo that = (PaymentInfo) o;
            return Objects.equals(CPF, that.CPF) &&
                    Objects.equals(type, that.type) &&
                    Objects.equals(postalCode, that.postalCode) &&
                    Objects.equals(street, that.street) &&
                    Objects.equals(streetNumber, that.streetNumber) &&
                    Objects.equals(complement, that.complement) &&
                    Objects.equals(neighborhood, that.neighborhood) &&
                    Objects.equals(city, that.city) &&
                    Objects.equals(state, that.state) &&
                    Objects.equals(buyerEmail, that.buyerEmail) &&
                    Objects.equals(buyerPhone, that.buyerPhone) &&
                    Objects.equals(pdf, that.pdf) &&
                    Objects.equals(number, that.number) &&
                    Objects.equals(displayNumber, that.displayNumber) &&
                    Objects.equals(cardHolder, that.cardHolder) &&
                    Objects.equals(months, that.months) &&
                    Objects.equals(buyerName, that.buyerName) &&
                    Objects.equals(purchaseTotal, that.purchaseTotal);
        }

        @Override
        public int hashCode() {

            return Objects.hash(CPF, type, postalCode, street, streetNumber, complement, neighborhood, city, state, buyerEmail, buyerPhone, pdf, number, displayNumber, cardHolder, months, buyerName, purchaseTotal);
        }
    }

    public enum PaymentStatus {
        Confirmed, Pending, Canceled, Unpaid, AwaitingFriends
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketDetails that = (TicketDetails) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(price, that.price) &&
                Objects.equals(description, that.description) &&
                Objects.equals(quantity, that.quantity) &&
                Objects.equals(isHalfPrice, that.isHalfPrice) &&
                Objects.equals(isVipBox, that.isVipBox) &&
                Objects.equals(capacity, that.capacity) &&
                Objects.equals(remaining, that.remaining) &&
                Objects.equals(eventDate, that.eventDate) &&
                Objects.equals(eventName, that.eventName) &&
                Objects.equals(clubName, that.clubName) &&
                Objects.equals(available, that.available) &&
                Objects.equals(buyerName, that.buyerName) &&
                Objects.equals(numTickets, that.numTickets) &&
                paymentStatus == that.paymentStatus &&
                Objects.equals(eventAddress, that.eventAddress) &&
                Objects.equals(paymentDate, that.paymentDate) &&
                Objects.equals(ticketsInfo, that.ticketsInfo) &&
                Objects.equals(paymentInfo, that.paymentInfo) &&
                enumTicketType == that.enumTicketType &&
                Objects.equals(vipBoxId, that.vipBoxId) &&
                Objects.equals(event, that.event) &&
                Objects.equals(eventId, that.eventId);
    }

    public boolean isCorporative(){
        return "corporation".equals(originType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, price, description, quantity, isHalfPrice, isVipBox, capacity, remaining, eventDate, eventName, clubName, available, buyerName, numTickets, paymentStatus, eventAddress, paymentDate, ticketsInfo, paymentInfo, enumTicketType, vipBoxId, event, eventId);
    }
}
