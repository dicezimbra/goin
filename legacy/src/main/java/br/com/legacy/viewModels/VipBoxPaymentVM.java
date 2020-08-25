package br.com.legacy.viewModels;

import android.app.Application;
import android.content.Intent;

import br.com.legacy.models.PurchaseModel;
import br.com.legacy.models.TicketModel;
import br.com.legacy.viewModels.base.BaseViewModel;

public class VipBoxPaymentVM extends BaseViewModel {

    public PurchaseModel purchaseModel;


    public VipBoxPaymentVM(Application application) {
        super(application);
    }

    public void init(HandlerError handlerError, Intent intent) {
        super.init(handlerError);
    }

    void setPurchaseModel(TicketModel ticketModel){
        purchaseModel = new PurchaseModel();
        purchaseModel.ticketId = ticketModel.id;
        purchaseModel.ticketName = ticketModel.name;
        purchaseModel.eventName = ticketModel.eventName;
        purchaseModel.eventAddress = ticketModel.eventAddress;
        purchaseModel.isHalfPrice = ticketModel.isHalfPrice == null ? false : ticketModel.isHalfPrice;
        purchaseModel.eventDate = ticketModel.date != null ? ticketModel.date.getTimeInMillis() : 0;
        purchaseModel.price = ticketModel.price;
        purchaseModel.vipCapacity = ticketModel.vipCapacity;
        purchaseModel.isVipBox = ticketModel.isVipBox;
    }
}
