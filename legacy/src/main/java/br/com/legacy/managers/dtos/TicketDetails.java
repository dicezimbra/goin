package br.com.legacy.managers.dtos;

import br.com.legacy.models.PurchaseModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by appsimples on 10/10/17.
 */

public class TicketDetails implements Serializable {
    public String id;
    public String ticketId;
    public String name;
    public Double price;
    public String description;
    public Integer quantity;
    public Boolean isHalfPrice;
    public Boolean isVipBox;
    public Integer capacity;
    public Integer remaining;
    public Long eventDate;
    public String eventName;
    public String clubName;
    public Boolean available;
    public String buyerName;
    public Integer numTickets;
    public PaymentStatus paymentStatus;
    public String eventAddress;
    public Long paymentDate;
    public ArrayList<PurchaseModel.TicketsInfo> ticketsInfo;
    public PaymentInfo paymentInfo;
    public PurchaseModel.TicketType enumTicketType;
    public String vipBoxId;
    public EventDetails event;
    public String eventId;
    public String qrCode;
    public String originType;
    public String ticketType;
    public boolean isValid;
    public String paymentId;
    public Boolean discount;
    public    Integer discountPercent;
    public    Double discountValue;
    public    Double finalValue;
   public String originUrl;



    public class PaymentInfo implements Serializable{
        public String CPF;
        public String type;
        // Bank Slip
        public String postalCode;
        public String street;
        public String streetNumber;
        public String complement;
        public String neighborhood;
        public String city;
        public String state;
        public String buyerEmail;
        public String buyerPhone;
        public String pdf;
        // Credit Card
        public String number;
        public String displayNumber;
        public String cardHolder;
        public Integer months;
        public String buyerName;
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
