package br.com.legacy.managers.dtos;

import br.com.legacy.models.PurchaseModel;

import java.util.List;

public class BuyTicketCreditCardInput {

    public String ticketId;
    public List<TicketInfoInput> ticketsInfo;
    public CreditCardInput creditCardInfo;
    public Boolean isSplittedBox;
    public String vipBoxId;
    public PurchaseModel.TicketType ticketType;
    public String eventId;
    public String originType;
    public PurchaseModel.AddressInfo addressInfo;

    public class TicketInfoInput {
        public String userId;
        public String name;
        public String RG;
        public String halfPriceId;
    }

    public class CreditCardInput {
        public String CPF;
        public String expiracyMonth;
        public String expiracyYear;
        public String token;
        public String displayNumber;
        public String cardHolder;
        public String cvv;
        public String birthday;

    }

}
