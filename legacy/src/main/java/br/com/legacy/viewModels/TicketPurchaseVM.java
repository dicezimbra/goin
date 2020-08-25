package br.com.legacy.viewModels;

import android.app.Application;
import androidx.arch.core.util.Function;
import android.content.Intent;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.com.legacy.components.TicketOwnerInfoComponent;
import br.com.legacy.models.PurchaseModel;
import br.com.legacy.models.TicketModel;
import br.com.goin.component.session.user.UserModel;
import br.com.legacy.utils.Constants;
import br.com.legacy.viewModels.base.BaseViewModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TicketPurchaseVM extends BaseViewModel implements TicketOwnerInfoComponent.CloseComponentHandler {

    public ObservableList<TicketOwnerInfoComponent> ticketOwnerInfoComponents = new ObservableArrayList<>();

    public TicketOwnerInfoComponent myTicketComponent;

    public Integer remaining;

    public PurchaseModel purchaseModel;

    public TicketPurchaseVM(Application application) {
        super(application);
    }

    public void init(HandlerError handlerError, UserModel myUser, Intent intent) {
        super.init(handlerError);

        TicketModel ticketModel = (new Gson()).fromJson(intent.getStringExtra(Constants.TICKET), TicketModel.class);
        setPurchaseModel(ticketModel);

        myTicketComponent = mapTicketOwnerInfoComponent(0).apply(myUser);
        myTicketComponent.isMyTicketComponent = true;
        addMyTicketToList();

    }

    private Function<UserModel, TicketOwnerInfoComponent> mapTicketOwnerInfoComponent(final int position) {
        return new Function<UserModel, TicketOwnerInfoComponent>() {
            @Override
            public TicketOwnerInfoComponent apply(UserModel input) {
                TicketOwnerInfoComponent component = new TicketOwnerInfoComponent(TicketPurchaseVM.this);

                if (input != null) {

                    component.setIndex(position + 1);
                    component.avatar.set(input.getProfilePicture());
                    component.name.set(input.getName());
                    component.halfEntranceId = input.getHalfEntranceId();
                    component.rg = input.getRg();
                    component.id = input.getId();
                    component.hasHalfEntrance = (purchaseModel == null || purchaseModel.isHalfPrice == null) ? false :
                            purchaseModel.isHalfPrice;
                }
                return component;
            }
        };
    }

    private Function<List<UserModel>, List<TicketOwnerInfoComponent>> mapTicketOwnersListComponent() {
        return new Function<List<UserModel>, List<TicketOwnerInfoComponent>>() {
            @Override
            public List<TicketOwnerInfoComponent> apply(List<UserModel> userModels) {
                List<TicketOwnerInfoComponent> list = new ArrayList<>();
                int i = 0;
                for (UserModel userModel : userModels) {
                    list.add(mapTicketOwnerInfoComponent(i).apply(userModel));
                    i++;
                }
                return list;
            }
        };
    }

    private void updateListIndexes() {
        for (int i = 0; i < this.ticketOwnerInfoComponents.size(); i++) {
            ticketOwnerInfoComponents.get(i).setIndex(i + 1);
        }
    }

    public void addMyTicketToList() {
        if (!ticketOwnerInfoComponents.isEmpty() && ticketOwnerInfoComponents.get(0).equals(myTicketComponent)) {
            return;
        }
        updateFieldsList();
        ticketOwnerInfoComponents.add(0, myTicketComponent);
        updateListIndexes();
    }

    public void removeMyTicketFromList() {
        if (!ticketOwnerInfoComponents.isEmpty() && ticketOwnerInfoComponents.get(0).equals(myTicketComponent)) {
            updateFieldsList();
            ticketOwnerInfoComponents.remove(0);
            updateListIndexes();
        }
    }

    @Override
    public void removeComponent(int index) {
        if (index < ticketOwnerInfoComponents.size()) {
            updateFieldsList();
            ticketOwnerInfoComponents.remove(index);
            updateListIndexes();
        }
    }

    public void updateList(Intent intent) {
        Type listType = new TypeToken<List<UserModel>>() {
        }.getType();
        List<UserModel> items = (new Gson()).fromJson(intent.getStringExtra(Constants.SELECTED_FRIENDS), listType);
        updateFieldsList();
        ticketOwnerInfoComponents.addAll(getAdditionalComponentsList(items));
        updateListIndexes();

    }

    List<TicketOwnerInfoComponent> getAdditionalComponentsList(List<UserModel> items) {
        List<TicketOwnerInfoComponent> components = new ArrayList<>();
        for (UserModel item : items) {
            if (!verifyListContainsUser(item.getId())) {
                components.add(mapTicketOwnerInfoComponent(0).apply(item));
            }
        }
        return components;
    }

    boolean verifyListContainsUser(String id) {
        for (TicketOwnerInfoComponent component : ticketOwnerInfoComponents) {
            if (component.id.equals(id)) return true;
        }
        return false;
    }

    public void updateFieldsList() {
        for (TicketOwnerInfoComponent component : ticketOwnerInfoComponents) {
            component.rg = component.getRgEditText().getText().toString();
            component.halfEntranceId = component.getHalfEntranceEditText().getText().toString();
        }
    }

    void setPurchaseModel(TicketModel ticketModel) {
        remaining = ticketModel.remaining;
        purchaseModel = new PurchaseModel();
        purchaseModel.ticketId = ticketModel.id;
        purchaseModel.eventId = ticketModel.event.id;
        purchaseModel.ticketName = ticketModel.name;
        purchaseModel.eventName = ticketModel.eventName;
        purchaseModel.eventAddress = ticketModel.eventAddress;
        purchaseModel.isHalfPrice = ticketModel.isHalfPrice == null ? false : ticketModel.isHalfPrice;
        purchaseModel.eventDate = ticketModel.date != null ? ticketModel.date.getTimeInMillis() : 0;
        purchaseModel.price = ticketModel.price;
        purchaseModel.vipCapacity = ticketModel.vipCapacity;
        purchaseModel.isVipBox = ticketModel.isVipBox;
        purchaseModel.eventId = ticketModel.event.id;
    }

    public void setFinalTicketOwners() {
        updateFieldsList();
        int ticketOwnersNumber = ticketOwnerInfoComponents.size();
        purchaseModel.totalValue = purchaseModel.price * ticketOwnersNumber;
        purchaseModel.ticketsInfo = getTicketsInfoModelList();
    }

    ArrayList<PurchaseModel.TicketsInfo> getTicketsInfoModelList() {
        ArrayList<PurchaseModel.TicketsInfo> ticketsInfoList = new ArrayList<>();
        for (TicketOwnerInfoComponent component : ticketOwnerInfoComponents) {
            PurchaseModel.TicketsInfo item = new PurchaseModel().new TicketsInfo();
            item.name = component.name.get();
            item.RG = component.rg;
            item.halfPriceInfo = component.halfEntranceId;
            item.userId = component.id;
            ticketsInfoList.add(item);
        }
        return ticketsInfoList;
    }
}
