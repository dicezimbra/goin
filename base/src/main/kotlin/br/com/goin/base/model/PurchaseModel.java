package br.com.goin.base.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class PurchaseModel implements Serializable {

    public String ticketName;
    public String ticketId;
    public PaymentStatus paymentStatus;
    public Boolean isHalfPrice;
    public Boolean isVipBox;
    public Boolean isSplittedVipBox;
    public Integer vipCapacity;
    public String eventName;
    public String eventAddress;
    public Long eventDate;
    public Double price;
    public PaymentType paymentType;
    public ArrayList<TicketsInfo> ticketsInfo;
    public Double totalValue;
    public PaymentInfo paymentInfo;
    public String buyerName;
    public Long purchaseDate;
    public String eventId;
    public Boolean isMyTicket;
    public String vipBoxId;
    public String originType;

    public enum PaymentType {
        CreditCard, BankSlip, PayPal
    }

    public enum PaymentStatus {
        Confirmed, Pending, VipBoxFriendsPending
    }

    public enum TicketType {
        VIP, REGULAR, TICKET, QUEUE, BOOKING, VOUCHER
    }

    public final class TicketsInfo implements Serializable{
        public String userId;
        public String name;
        public String RG;
        public String halfPriceInfo;

        // response
        public String halfPriceId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TicketsInfo that = (TicketsInfo) o;
            return Objects.equals(userId, that.userId) &&
                    Objects.equals(name, that.name) &&
                    Objects.equals(RG, that.RG) &&
                    Objects.equals(halfPriceInfo, that.halfPriceInfo) &&
                    Objects.equals(halfPriceId, that.halfPriceId);
        }

        @Override
        public int hashCode() {

            return Objects.hash(userId, name, RG, halfPriceInfo, halfPriceId);
        }
    }

    public class PaymentInfo {
        public String CPF;
        public String birthday;
        public Integer installments;
        public PaymentData data;
        public AddressInfo addressInfo;

        // response

        public String type;
        public Integer months;
        public String cardHolder;
        public String displayNumber;
        public Double purchaseTotal;

    }

    public class PaymentData {
        // Bank Slip
        public String buyerEmail;
        public String buyerPhone;
        public String bankSlipPDF;

        // Credit Card
        public String number;
        public String verificationValue;
        public String firstName;
        public String lastName;
        public String expiracyMonth;
        public String expiracyYear;
        public String cvv;
    }

    public class AddressInfo {

        public String postalCode;
        public String street;
        public String streetNumber;
        public String complement;
        public String neighborhood;
        public String city;
        public String state;
    }
}
