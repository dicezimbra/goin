package br.com.legacy.viewControllers.activitites;

import androidx.databinding.ObservableBoolean;
import android.os.Bundle;
import androidx.annotation.Nullable;

import br.com.BR;
import br.com.goin.base.utils.Utils;
import br.com.legacy.components.ToolbarComponent;
import br.com.legacy.managers.TicketsManager;
import br.com.legacy.models.PurchaseModel;
import br.com.legacy.navigation.Coordinator;
import br.com.R;
import br.com.legacy.utils.Constants;
import br.com.legacy.viewControllers.activitites.base.BaseActivity;
import br.com.legacy.viewModels.VipBoxPaymentVM;
import br.com.databinding.ActivityVipBoxPaymentBinding;

public class VipBoxPaymentActivity extends BaseActivity implements TicketsManager.PurchaseDetailsHandler{

    ActivityVipBoxPaymentBinding binding;
    VipBoxPaymentVM vipBoxPaymentVM;
    public ObservableBoolean shouldShowErrorMessage = new ObservableBoolean(false);
    public ObservableBoolean isLoading = new ObservableBoolean(false);
    public TicketsManager ticketsManager;
    public String ticketId;
    String originType;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = (ActivityVipBoxPaymentBinding) this.setLayoutId(R.layout.activity_vip_box_payment, BR.activity);
        vipBoxPaymentVM = (VipBoxPaymentVM) this.linkViewModel(VipBoxPaymentVM.class);
        toolbar = ToolbarComponent.build()
                .addLeftIcon(R.drawable.ic_arrow_back)
                .setTitle(getString(R.string.vip_box_payment));

        vipBoxPaymentVM.init(this,getIntent());

        if (getIntent() != null){

            ticketId = getIntent().getStringExtra(Constants.TICKET_ID);
            originType = getIntent().getStringExtra(Constants.ORIGIN_TYPE);
        }

        setupManagers();

        getData();

    }

    public String getPriceInformation(){
        if(vipBoxPaymentVM.purchaseModel != null && vipBoxPaymentVM.purchaseModel.ticketName != null){
            return vipBoxPaymentVM.purchaseModel.ticketName + " - " + Utils.INSTANCE.getPriceText(vipBoxPaymentVM.purchaseModel.price);
        }
        return "";
    }

    public String getVipBoxCapacityInformation(){
        if(vipBoxPaymentVM.purchaseModel != null && vipBoxPaymentVM.purchaseModel.vipCapacity != null){
            return getResources().getQuantityString(R.plurals.vip_box_capacity_label,vipBoxPaymentVM.purchaseModel.vipCapacity,vipBoxPaymentVM.purchaseModel.vipCapacity);
        } else{
            return "";
        }
    }

    public String getPriceToPayInformation(){
        if(vipBoxPaymentVM.purchaseModel != null && vipBoxPaymentVM.purchaseModel.price != null && vipBoxPaymentVM.purchaseModel.vipCapacity != null){
            Double priceToPay = vipBoxPaymentVM.purchaseModel.price/vipBoxPaymentVM.purchaseModel.vipCapacity;
            return getString(R.string.price_to_pay) + Utils.INSTANCE.getPriceText(priceToPay);
        } else {
            return "";
        }
    }

    public String getEventName(){
        if(vipBoxPaymentVM.purchaseModel != null && vipBoxPaymentVM.purchaseModel.eventName != null){
            return vipBoxPaymentVM.purchaseModel.eventName;
        } else {
            return "";
        }
    }

    public String getEventAddress(){
        if(vipBoxPaymentVM.purchaseModel != null && vipBoxPaymentVM.purchaseModel.eventAddress != null){
            return vipBoxPaymentVM.purchaseModel.eventAddress;
        } else {
            return "";
        }
    }

    public void onClickPayPalButton(){
//        paymentMethodVM.startPayPal(this);
    }

    public void onClickCardButton(){
        Coordinator.goToInfoPayment(this,vipBoxPaymentVM.purchaseModel,
                PaymentMethodActivity.PaymentMethod.Card, originType);
    }

    public void onClickBoletoButton(){
        Coordinator.goToInfoPayment(this,vipBoxPaymentVM.purchaseModel,
                PaymentMethodActivity.PaymentMethod.Boleto, originType);
    }

    @Override
    public void onClickToolbarLeftIcon() {
        onBackPressed();
    }

    @Override
    public void onReceiveTicket(PurchaseModel purchaseModel) {
        isLoading.set(false);
        shouldShowErrorMessage.set(false);
        purchaseModel.ticketId = ticketId;
        purchaseModel.isMyTicket = true;
        purchaseModel.isSplittedVipBox = true;
        purchaseModel.paymentInfo.purchaseTotal = purchaseModel.price / purchaseModel.vipCapacity;
        vipBoxPaymentVM.purchaseModel = purchaseModel;


        binding.setActivity(this);
    }

    @Override
    public void onError(String message) {
        super.onError(message);
        shouldShowErrorMessage.set(true);
        isLoading.set(false);
    }

    void setupManagers(){
        ticketsManager = new TicketsManager(this);
        ticketsManager.purchaseDetailsHandler = this;
    }

    void getData(){
        ticketsManager.requestTicketDetail(ticketId);
        isLoading.set(true);
    }
}
