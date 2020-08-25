package br.com.legacy.managers.dtos;

import br.com.legacy.models.PurchaseModel;

import java.util.List;

public class BuyTicketBankSlipInput {

    public String ticketId;
    public List<TicketInfoInput> ticketsInfo;
    public BankSlipInput bankSlipInfo;
    public Boolean isSplittedBox;
    public String vipBoxId;
    public PurchaseModel.TicketType ticketType;
    public PurchaseModel.AddressInfo addressInfo;


    public class TicketInfoInput{
        public String userId;
        public String name;
        public String RG;
        public String halfPriceId;
    }

    public class BankSlipInput{
        public String CPF;
        public String buyerEmail;
        public String buyerPhone;
        public String buyerName;
    }
}
