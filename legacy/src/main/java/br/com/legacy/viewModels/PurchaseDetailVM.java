package br.com.legacy.viewModels;

import android.app.Application;
import androidx.arch.core.util.Function;
import android.content.Intent;
import android.content.res.Resources;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.google.gson.Gson;

import br.com.databinding.ComponentTicketOwnerInfoBinding;
import br.com.legacy.components.TicketOwnerInfoComponent;
import br.com.legacy.managers.TicketsManager;
import br.com.legacy.models.PurchaseModel;
import br.com.legacy.utils.Constants;
import br.com.legacy.viewControllers.activitites.PurchaseDetailActivity;
import br.com.legacy.viewModels.base.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

public class PurchaseDetailVM extends BaseViewModel {

    public PurchaseModel purchaseModel;
    String token;
    String avatarUrl;
    public String ticketId;
    public String displayNumber;
    public String myId;
    public PurchaseDetailActivity.PurchaseDetailType type;
    public ObservableList<TicketOwnerInfoComponent> ticketOwnerInfoComponents = new ObservableArrayList<>();
    public boolean isPastEvent;
    TicketOwnerInfoComponent.TransferTicketHandler transferTicketHandler;
    Resources res;
    String originType;

    public PurchaseDetailVM(Application application) {
        super(application);
    }

    public void init(HandlerError handlerError, Resources res, TicketOwnerInfoComponent.TransferTicketHandler transferTicketHandler, Intent intent) {
        super.init(handlerError);
        this.res = res;
        this.transferTicketHandler = transferTicketHandler;
        this.avatarUrl = intent.getStringExtra(Constants.URL);
        this.isPastEvent = intent.getBooleanExtra(Constants.IS_PAST_EVENT,false);
    }

    private Function<PurchaseModel.TicketsInfo, TicketOwnerInfoComponent> mapTicketOwnerInfoComponent(final int position){
        return new Function<PurchaseModel.TicketsInfo, TicketOwnerInfoComponent>() {
            @Override
            public TicketOwnerInfoComponent apply(PurchaseModel.TicketsInfo input) {
                TicketOwnerInfoComponent component = new TicketOwnerInfoComponent(res, transferTicketHandler);

                component.name.set(input.name);
                component.rg = input.RG;
                component.id = input.userId;

                if(input.halfPriceInfo == null || input.halfPriceInfo.isEmpty()){
                    component.hasHalfEntrance = false;
                } else {
                    component.halfEntranceId = input.halfPriceInfo;
                    component.hasHalfEntrance = true;
                }

                switch (type){
                    case ConfirmTransfer:
                        component.setIndex(-1);
                        component.isMyTicketComponent = component.id != null && component.id.equals(myId);
                        break;
                    case ReadOperation:
                        component.setIndex(position + 1);
                        component.isMyTicketComponent = component.id != null && component.id.equals(myId);
                        component.shouldShowTransferButton = !isPastEvent && purchaseModel.paymentStatus.equals(PurchaseModel.PaymentStatus.Confirmed);
                        break;
                    case TransferTicket:
                        component.setIndex(-1);
                        // set isMyTicketComponent -> hide closeButton
                        component.isMyTicketComponent = true;
                        component.avatar.set(avatarUrl);
                        component.hasHalfEntrance = purchaseModel.isHalfPrice == null ? false : purchaseModel.isHalfPrice;
                        break;
                    case PurchaseConfirmation:
                        component.setIndex(position + 1);
                        component.isMyTicketComponent = component.id != null && component.id.equals(myId);
                        break;
                }

                return component;
            }
        };
    }

    private Function<List<PurchaseModel.TicketsInfo>, List<TicketOwnerInfoComponent>> mapTicketOwnersListComponent() {
        return new Function<List<PurchaseModel.TicketsInfo>, List<TicketOwnerInfoComponent>>() {
            @Override
            public List<TicketOwnerInfoComponent> apply(List<PurchaseModel.TicketsInfo> input) {
                List<TicketOwnerInfoComponent> list = new ArrayList<>();
                int i = 0;
                for (PurchaseModel.TicketsInfo info : input) {
                    list.add(mapTicketOwnerInfoComponent(i).apply(info));
                    i++;
                }
                return list;
            }
        };
    }

    public void getData(Intent intent, TicketsManager ticketsManager,PurchaseDetailActivity.PurchaseDetailType type){
        this.type = type;
        this.ticketId = intent.getStringExtra(Constants.TICKET_ID);

        switch (type){
            case PurchaseConfirmation:
                setPurchaseModel((new Gson()).fromJson(intent.getStringExtra(Constants.PURCHASE_MODEL), PurchaseModel.class));
                token = intent.getStringExtra(Constants.TOKEN);
                displayNumber = intent.getStringExtra(Constants.DISPLAY_NUMBER);
                break;
            case ReadOperation:
                String str = intent.getStringExtra(Constants.PURCHASE_MODEL);
                if(str != null && !str.equals("null")){
                    purchaseModel = (new Gson()).fromJson(str, PurchaseModel.class);
                    purchaseModel.ticketId = ticketId;
                    setPurchaseModel(purchaseModel);
                } else {
                    ticketsManager.requestTicketDetail(ticketId);
                }
                break;
            case TransferTicket:
                setPurchaseModel((new Gson()).fromJson(intent.getStringExtra(Constants.PURCHASE_MODEL), PurchaseModel.class));
                break;
            case ConfirmTransfer:
                setPurchaseModel((new Gson()).fromJson(intent.getStringExtra(Constants.PURCHASE_MODEL), PurchaseModel.class));
                break;
        }
    }

    public void setPurchaseModel(PurchaseModel purchaseModel) {
        this.purchaseModel = purchaseModel;
        if(purchaseModel.isMyTicket == null){
            purchaseModel.isMyTicket = true;
        }
        ticketOwnerInfoComponents.addAll(mapTicketOwnersListComponent().apply(purchaseModel.ticketsInfo));
    }

    public boolean updateTransferModel(ComponentTicketOwnerInfoBinding compBinding){
        if(purchaseModel.ticketsInfo.size() >= 2){
            if(!compBinding.ticketOwnerRgField.getText().toString().isEmpty()){
                purchaseModel.ticketsInfo.get(1).RG = compBinding.ticketOwnerRgField.getText().toString();
            } else {
                return false;
            }

            if(purchaseModel.isHalfPrice != null && purchaseModel.isHalfPrice){
                if(!compBinding.ticketOwnerHalfField.getText().toString().isEmpty()){
                    purchaseModel.ticketsInfo.get(1).halfPriceInfo = compBinding.ticketOwnerHalfField.getText().toString();
                } else {
                    return false;
                }
            }

        } else {
            return false;
        }

        return true;

    }

    public void transferTicket(TicketsManager manager) {
        if (purchaseModel.ticketsInfo.size() > 1) {
            purchaseModel.ticketsInfo.get(1).halfPriceId = purchaseModel.ticketsInfo.get(1).halfPriceInfo;
            manager.transferTicket(purchaseModel.ticketId, purchaseModel.ticketsInfo.get(1));
        }
    }
        
    public void buyTicketCreditCard(TicketsManager manager){
        manager.buyTicketCreditCard(purchaseModel,token,displayNumber);
    }
    
    public void buyTicketBankSlip(TicketsManager manager){
        manager.buyTicketBankSlip(purchaseModel);
    }
}
