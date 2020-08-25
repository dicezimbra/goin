package br.com.legacy.viewControllers.activitites;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import androidx.databinding.ObservableBoolean;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import br.com.BR;
import br.com.goin.base.utils.Utils;
import br.com.goin.component.navigation.NavigationController;
import br.com.goin.component.ui.kit.dialog.DialogUtils;
import br.com.goin.component.session.user.ActionType;
import br.com.legacy.components.TicketOwnerInfoComponent;
import br.com.legacy.components.ToolbarComponent;
import br.com.legacy.managers.SessionManager;
import br.com.legacy.managers.TicketsManager;
import br.com.legacy.models.PurchaseModel;
import br.com.legacy.navigation.Coordinator;
import br.com.R;
import br.com.legacy.navigation.NavigationManager;
import br.com.legacy.utils.Constants;
import br.com.legacy.utils.TypefaceUtil;
import br.com.legacy.viewControllers.activitites.base.BaseActivity;
import br.com.legacy.viewModels.PurchaseDetailVM;

import br.com.databinding.ActivityPurchaseDetailBinding;
import br.com.databinding.ComponentTicketOwnerDisplayBinding;
import br.com.databinding.ComponentTicketOwnerInfoBinding;
import butterknife.BindView;
import butterknife.ButterKnife;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PurchaseDetailActivity extends BaseActivity implements TicketsManager.PurchaseDetailsHandler, TicketsManager.BuyTicketHandler, TicketOwnerInfoComponent.TransferTicketHandler {

    ActivityPurchaseDetailBinding binding;
    public PurchaseDetailVM purchaseDetailVM;
    public ToolbarComponent toolbar;
    public PurchaseDetailType type;
    public TicketsManager ticketsManager;
    public ObservableBoolean isLoading = new ObservableBoolean(false);
    public ObservableBoolean shouldShowErrorMessage = new ObservableBoolean(false);
    public ObservableBoolean isMyPurchase = new ObservableBoolean(false);
    public ObservableBoolean isTransfer = new ObservableBoolean(false);
    public String toolbarTitle;
    boolean isFromPurchaseActivity;
    ComponentTicketOwnerInfoBinding compBinding;
    ProgressDialog progressDialog;

    @BindView(br.com.R2.id.purchase_detail_toolbar)
    View toolbarThis;
    String originType;

    public enum PurchaseDetailType {
        PurchaseConfirmation, ReadOperation, TransferTicket, ConfirmTransfer
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = (ActivityPurchaseDetailBinding) this.setLayoutId(R.layout.activity_purchase_detail, BR.activity);
        type = (PurchaseDetailType) getIntent().getSerializableExtra(Constants.TYPE);
        isFromPurchaseActivity = getIntent().getBooleanExtra(Constants.IS_FROM_PURCHASE_ACTIVITY, false);
        purchaseDetailVM = (PurchaseDetailVM) linkViewModel(PurchaseDetailVM.class);
        purchaseDetailVM.myId = SessionManager.getInstance().getCurrentUser(this).getId();
        switch (type) {
            case PurchaseConfirmation:
                toolbarTitle = getString(R.string.purchase_confirmation);
                break;
            case ReadOperation:
                toolbarTitle = getString(R.string.purchase_detail);
                break;
            case TransferTicket:
                toolbarTitle = getString(R.string.ticket_transfer);
                break;
            case ConfirmTransfer:
                toolbarTitle = getString(R.string.transfer_confirmation);
                break;
            default:
                toolbarTitle = getString(R.string.purchase_confirmation);
        }
        toolbar = ToolbarComponent.build()
                .addLeftIcon(R.drawable.ic_arrow_back)
                .setTitle(toolbarTitle);

        ButterKnife.bind(this);
        Coordinator.setStatusBarColor(this, getString(R.color.grayBackgroundBoard), toolbarThis);

        originType = (getIntent() != null) ? getIntent().getStringExtra(Constants.ORIGIN_TYPE) : null;

        setupManagers();

        purchaseDetailVM.init(this, getResources(), this, getIntent());
        purchaseDetailVM.getData(getIntent(), ticketsManager, type);

        setupViews();
        setViewsFont();
    }

    public void setViewsFont() {

        TypefaceUtil.initilize(this);
        TypefaceUtil.mediumFont(binding.viewBankSlipButton, binding.paymentBankSlipInfoAdress,
                binding.paymentBankSlipInfoCityAndState, binding.paymentBankSlipInfoEmail,
                binding.paymentBankSlipInfoNeighborhood, binding.paymentBankSlipInfoPhone,
                binding.paymentBankSlipInfoPostalCode, binding.paymentCardInstallments,
                binding.paymentCardNumber, binding.paymentCpf, binding.paymentInfoLabel,
                binding.paymentName, binding.paymentType, binding.purchaseBuyer, binding.purchaseDate,
                binding.purchaseDetailButton, binding.purchaseDetailErrorPlaceholder,
                binding.purchaseDetailEventAddress, binding.purchaseDetailEventDate, binding.purchaseDetailEventName,
                binding.purchaseDetailPendencies, binding.purchaseDetailsLabel, binding.purchaseDetailTicketPrice,
                binding.purchaseDetailTicketName, binding.purchaseDetailVipBoxCapacity, binding.purchaseTotalPrice,
                binding.ticketTransferFromLabel, binding.ticketDataLabel, binding.ticketTransferLabel,
                binding.ticketTransferToLabel, binding.transferDate, binding.transferInfoLabel);
    }

    @Override
    public void onClickToolbarLeftIcon() {
        //  super.onClickToolbarLeftIcon();
        onBackPressed();

    }

    public String getPendenciesText() {
        if (purchaseDetailVM.purchaseModel != null) {
            if (purchaseDetailVM.purchaseModel.paymentStatus != null) {
                switch (purchaseDetailVM.purchaseModel.paymentStatus) {
                    case Pending:
                        return getString(R.string.payment_confirmation_pendencies);
                    case VipBoxFriendsPending:
                        return getString(R.string.friends_payment_pending);
                }
            }
        }
        return "";
    }

    public boolean getPendenciesVisibility() {
        if (purchaseDetailVM.purchaseModel != null) {
            if (purchaseDetailVM.purchaseModel.paymentStatus != null) {
                return purchaseDetailVM.purchaseModel.paymentStatus.equals(PurchaseModel.PaymentStatus.Pending)
                        || purchaseDetailVM.purchaseModel.paymentStatus.equals(PurchaseModel.PaymentStatus.VipBoxFriendsPending);
            }
        }
        return false;
    }

    public String getDateText() {
        if (purchaseDetailVM.purchaseModel != null) {
            Long date = purchaseDetailVM.purchaseModel.eventDate;
            if (date != null) {
                Calendar calendarDate = Calendar.getInstance();
                calendarDate.setTimeInMillis(date * 1000);

                // get short date -----> 16 JUN
                DateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
                String shortDateText = dateFormat.format(calendarDate.getTime()).toUpperCase();

                // get week day -----> sábado
                DateFormat dayWeek = new SimpleDateFormat("EEEE", Locale.getDefault());
                String weekDay = dayWeek.format(calendarDate.getTime());

                // get hour -----> 16h
                DateFormat eventHour = new SimpleDateFormat("HH'h'", Locale.getDefault());
                String hour = eventHour.format(calendarDate.getTime());
                return shortDateText + " (" + weekDay + ") " + getString(R.string.at) + " " + hour;
            }
        }
        return "";
    }

    public String getTicketPriceText() {
        if (purchaseDetailVM.purchaseModel != null) {
            return " - " + Utils.INSTANCE.getPriceText(purchaseDetailVM.purchaseModel.price);
        } else {
            return "";
        }

    }

    public String getPurchaseDateText() {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dateText = "";
        if (purchaseDetailVM.purchaseModel != null && purchaseDetailVM.purchaseModel.purchaseDate != null) {
            Long purchaseDate = purchaseDetailVM.purchaseModel.purchaseDate;
            Calendar calendarDate = Calendar.getInstance();
            calendarDate.setTimeInMillis(purchaseDate * 1000);
            Date date = calendarDate.getTime();
            dateText = formatter.format(date.getTime());
        } else {
            Date now = Calendar.getInstance().getTime();
            dateText = formatter.format(now.getTime());
        }
        return getString(R.string.purchase_date_info) + dateText;
    }

    public String getTransferDateText() {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date now = Calendar.getInstance().getTime();
        String dateText = formatter.format(now.getTime());
        return getString(R.string.transfer_date_info) + dateText;
    }

    public String getTotalValueText() {
        Double priceToPay = 0d;
        if (purchaseDetailVM.purchaseModel != null) {
            if (purchaseDetailVM.purchaseModel.paymentInfo != null && purchaseDetailVM.purchaseModel.paymentInfo.purchaseTotal != null) {
                priceToPay = purchaseDetailVM.purchaseModel.paymentInfo.purchaseTotal;
            } else {
                if (purchaseDetailVM.purchaseModel.isVipBox != null && purchaseDetailVM.purchaseModel.isVipBox) {
                    Double totalValue = purchaseDetailVM.purchaseModel.price;
                    if (purchaseDetailVM.purchaseModel.isSplittedVipBox != null && purchaseDetailVM.purchaseModel.isSplittedVipBox) {
                        int vipCapacity = purchaseDetailVM.purchaseModel.vipCapacity;
                        int vipListSize = purchaseDetailVM.purchaseModel.ticketsInfo.size();
                        Double individualPrice = totalValue / vipCapacity;
                        priceToPay = individualPrice * (vipCapacity - vipListSize + 1);

                    } else {
                        priceToPay = totalValue;
                    }

                } else {
                    priceToPay = purchaseDetailVM.purchaseModel.totalValue;
                }
            }

        }
        return getString(R.string.total_value_info) + Utils.INSTANCE.getPriceText(priceToPay);
    }

    public String getPaymentMethodText() {
        String paymentType = getString(R.string.credit_card);
        if (purchaseDetailVM.purchaseModel != null && purchaseDetailVM.purchaseModel.paymentType != null) {
            switch (purchaseDetailVM.purchaseModel.paymentType) {
                case PayPal:
                    paymentType = getString(R.string.paypal);
                    break;
                case BankSlip:
                    paymentType = getString(R.string.boleto);
                    break;
                case CreditCard:
                    paymentType = getString(R.string.credit_card);
                    break;
            }
        }
        return getString(R.string.payment_method_info) + paymentType;
    }

    public String getInstallmentsText() {
        Integer installments = 0;
        if (purchaseDetailVM.purchaseModel != null && purchaseDetailVM.purchaseModel.isMyTicket) {
            installments = purchaseDetailVM.purchaseModel.paymentInfo.installments;
        }
        return getString(R.string.installments_info) + installments + "x";
    }

    public String getBuyerUserText() {
        String name = "";
        if (purchaseDetailVM.purchaseModel != null) {
            name = purchaseDetailVM.purchaseModel.buyerName;
        }
        return getString(R.string.bought_by_info) + name;

    }

    public String getBuyerNameText() {
        String name = "";
        if (purchaseDetailVM.purchaseModel != null) {
            if (purchaseDetailVM.purchaseModel.buyerName != null) {
                name = purchaseDetailVM.purchaseModel.buyerName;
            } else {
                name = purchaseDetailVM.purchaseModel.paymentInfo.data.firstName
                        + " "
                        + purchaseDetailVM.purchaseModel.paymentInfo.data.lastName;
            }
        }
        return getString(R.string.name_info) + name;
    }

    public String getCpfText() {
        String cpf = "";
        if (purchaseDetailVM.purchaseModel != null && purchaseDetailVM.purchaseModel.isMyTicket) {
            cpf = purchaseDetailVM.purchaseModel.paymentInfo.CPF;
        }
        return getString(R.string.user_cpf_info) + cpf;
    }

    public String getCardNumberText() {
        String cardNumberMask = "";
        if (purchaseDetailVM.purchaseModel != null && purchaseDetailVM.purchaseModel.isMyTicket) {
            if (purchaseDetailVM.purchaseModel.paymentInfo != null && purchaseDetailVM.purchaseModel.paymentInfo.displayNumber != null) {
                cardNumberMask = purchaseDetailVM.purchaseModel.paymentInfo.displayNumber;
            } else if (purchaseDetailVM.displayNumber != null) {
                cardNumberMask = purchaseDetailVM.displayNumber;
            }
        }
        return getString(R.string.card_number_info) + cardNumberMask;
    }

    public String getBuyerEmail() {
        String email = "";
        if (purchaseDetailVM.purchaseModel != null && purchaseDetailVM.purchaseModel.isMyTicket) {
            email = purchaseDetailVM.purchaseModel.paymentInfo.data.buyerEmail;
        }
        return getString(R.string.user_email_info) + email;
    }

    public String getBuyerPhone() {
        String phone = "";
        if (purchaseDetailVM.purchaseModel != null && purchaseDetailVM.purchaseModel.isMyTicket) {
            phone = purchaseDetailVM.purchaseModel.paymentInfo.data.buyerPhone;
        }
        return getString(R.string.user_phone_info) + phone;
    }

    public String getBuyerPostalCode() {
        String postalCode = "";
        if (purchaseDetailVM.purchaseModel != null && purchaseDetailVM.purchaseModel.isMyTicket) {
            postalCode = purchaseDetailVM.purchaseModel.paymentInfo.addressInfo.postalCode;
        }
        return getString(R.string.user_postal_code_info) + postalCode;
    }

    public String getBuyerAdress() {
        String address = "";
        if (purchaseDetailVM.purchaseModel != null && purchaseDetailVM.purchaseModel.isMyTicket
                && purchaseDetailVM.purchaseModel.paymentType.equals(PurchaseModel.PaymentType.BankSlip)) {
            String street = purchaseDetailVM.purchaseModel.paymentInfo.addressInfo.street;
            String streetNumber = purchaseDetailVM.purchaseModel.paymentInfo.addressInfo.streetNumber;
            String complemento = purchaseDetailVM.purchaseModel.paymentInfo.addressInfo.complement;
            address = street + ", " + streetNumber;
            if (complemento != null && !complemento.equals("")) {
                address = address + " - " + complemento;
            }
        }
        return address;
    }

    public String getBuyerNeighborhood() {
        String neighborhood = "";
        if (purchaseDetailVM.purchaseModel != null && purchaseDetailVM.purchaseModel.isMyTicket) {
            neighborhood = purchaseDetailVM.purchaseModel.paymentInfo.addressInfo.neighborhood;
        }
        return neighborhood;
    }

    public String getBuyerCityAndState() {
        String location = "";
        if (purchaseDetailVM.purchaseModel != null && purchaseDetailVM.purchaseModel.isMyTicket) {
            String city = purchaseDetailVM.purchaseModel.paymentInfo.addressInfo.city;
            String state = purchaseDetailVM.purchaseModel.paymentInfo.addressInfo.state;
            location = city + " - " + state;
        }
        return location;
    }

    public String getButtonText() {
        switch (type) {
            case PurchaseConfirmation:
                return getString(R.string.finish_payment);
            case ReadOperation:
                return getString(R.string.buy_more_tickets);
            case TransferTicket:
                return getString(R.string.next);
            case ConfirmTransfer:
                return getString(R.string.transfer);
        }
        return getString(R.string.next);
    }

    public String getVipBoxCapacityInformation() {
        if (purchaseDetailVM.purchaseModel != null && purchaseDetailVM.purchaseModel.vipCapacity != null) {
            return getResources().getQuantityString(R.plurals.vip_box_capacity_label, purchaseDetailVM.purchaseModel.vipCapacity, purchaseDetailVM.purchaseModel.vipCapacity);
        } else {
            return "";
        }
    }

    public void onClickActionButton(View view) {
        switch (type) {
            case PurchaseConfirmation:
                if (progressDialog == null) {
                    progressDialog = DialogUtils.INSTANCE.createProgressDialog(this);
                }
                progressDialog.show();
                if (purchaseDetailVM.purchaseModel != null) {
                    switch (purchaseDetailVM.purchaseModel.paymentType) {
                        //TODO verify if input is correct when endpoint is ready (vipBox)
                        case BankSlip:
                            purchaseDetailVM.buyTicketBankSlip(ticketsManager);
                            break;
                        case CreditCard:
                            purchaseDetailVM.buyTicketCreditCard(ticketsManager);
                            break;
                    }
                }
                break;
            case ReadOperation:
                if (isFromPurchaseActivity) {
                    this.finish();
                } else {
                }
                break;
            case TransferTicket:
                if (compBinding != null && purchaseDetailVM.updateTransferModel(compBinding)) {
                    Coordinator.goToTransferConfirmation(this, purchaseDetailVM.purchaseModel);
                } else {
                    DialogUtils.INSTANCE.show(this, getString(R.string.ticket_owner_info_incomplete));
                }
                break;
            case ConfirmTransfer:
                if (progressDialog == null) {
                    progressDialog = DialogUtils.INSTANCE.createProgressDialog(this);
                }
                progressDialog.show();
                purchaseDetailVM.transferTicket(ticketsManager);
                break;
        }
    }

    public void onClickOpenBankSlipButton(View view) {
        if (purchaseDetailVM.purchaseModel != null) {
            if (purchaseDetailVM.purchaseModel.paymentInfo.data.bankSlipPDF != null) {
                String urlPDF = purchaseDetailVM.purchaseModel.paymentInfo.data.bankSlipPDF;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(urlPDF));
                startActivity(browserIntent);
                return;
            }
        }
        DialogUtils.INSTANCE.show(this, R.string.open_bank_slip_fail);
    }

    @Override
    public void onReceiveTicket(PurchaseModel purchaseModel) {
        purchaseModel.ticketId = purchaseDetailVM.ticketId;
        purchaseModel.originType = originType;
        purchaseModel.paymentInfo.data.cvv = purchaseModel.paymentInfo.data.cvv;
        purchaseDetailVM.setPurchaseModel(purchaseModel);
        if (purchaseModel.isMyTicket == null || !purchaseModel.isMyTicket) {
            isMyPurchase.set(false);
        } else {
            isMyPurchase.set(true);
        }
        binding.setActivity(this);
        binding.notifyChange();
        isLoading.set(false);
    }

    @Override
    public void onError(String message) {
        super.onError(message);
        isLoading.set(false);
        shouldShowErrorMessage.set(true);
    }

    @Override
    public void onSuccessToBuyTicket(PurchaseModel purchaseModel) {
        Toast.makeText(this, R.string.success_purchase_message, Toast.LENGTH_LONG).show();
        purchaseDetailVM.ticketId = purchaseModel.ticketId;
        Coordinator.goToPurchaseDetails(PurchaseDetailActivity.this, purchaseDetailVM.ticketId,
                purchaseDetailVM.isPastEvent, purchaseModel, true);
        progressDialog.hide();
    }

    @Override
    public void onFailureToBuyTicket(String error) {
        progressDialog.hide();
        super.onError(getString(R.string.failure_purchase_message));
    }

    public int getPaymentInfoVisibility() {
        if (isLoading.get() || shouldShowErrorMessage.get()) {
            return View.GONE;
        } else if (isMyPurchase.get()) {
            return View.VISIBLE;
        }
        return View.GONE;
    }

    @Override
    public void onSuccessToTransferTicket() {
        progressDialog.hide();
        DialogUtils.INSTANCE.show(this, R.string.success_transfer_ticket, new DialogUtils.DialogCallback() {
            @Override
            public void onClickOk() {
            }

            @Override
            public void onClickCancel() {
            }
        });
    }

    @Override
    public void onFailureToTransferTicket(String error) {
        progressDialog.hide();
        super.onError(getString(R.string.failure_transfer_ticket));
    }

    @Override
    public void transferTicket(TicketOwnerInfoComponent myComponent) {
        PurchaseModel.TicketsInfo myTicketInfo = new PurchaseModel().new TicketsInfo();
        myTicketInfo.name = myComponent.name.get();
        myTicketInfo.RG = myComponent.rg;
        myTicketInfo.halfPriceInfo = myComponent.halfEntranceId;
        myTicketInfo.userId = myComponent.id;
        purchaseDetailVM.purchaseModel.ticketsInfo.clear();
        purchaseDetailVM.purchaseModel.ticketsInfo.add(myTicketInfo);
        purchaseDetailVM.purchaseModel.isHalfPrice = myTicketInfo.halfPriceInfo != null;

        NavigationController.Companion.getInstance().legacyApp().goToInviteFriends(this, null);
    }

    public int getPurchaseInfoVisibility() {
        if (isLoading.get() || shouldShowErrorMessage.get()) {
            return View.GONE;
        } else {
            switch (type) {
                case PurchaseConfirmation:
                    return View.VISIBLE;
                case ReadOperation:
                    return View.VISIBLE;
                case TransferTicket:
                    return View.GONE;
                case ConfirmTransfer:
                    return View.GONE;
            }
            return View.VISIBLE;
        }
    }

    void setupManagers() {
        ticketsManager = new TicketsManager(this);
        ticketsManager.purchaseDetailsHandler = this;
        ticketsManager.buyTicketHandler = this;
    }

    void setupViews() {
        shouldShowErrorMessage.set(false);
        isMyPurchase.set(false);
        switch (type) {
            case PurchaseConfirmation:
                isMyPurchase.set(true);
                break;
            case ReadOperation:
                if (purchaseDetailVM.purchaseModel != null) {
                    binding.setActivity(this);
                    isMyPurchase.set(true);
                    binding.notifyChange();
                    isLoading.set(false);
                } else {
                    isLoading.set(true);
                }
                break;
            case TransferTicket:
                isTransfer.set(true);
                compBinding = ComponentTicketOwnerInfoBinding.inflate(getLayoutInflater(), binding.componentTicketOwnerContainer, true);
                compBinding.setData(getDestinationComponent());
                break;
            case ConfirmTransfer:
                isTransfer.set(true);
                ComponentTicketOwnerDisplayBinding compFromBinding = ComponentTicketOwnerDisplayBinding
                        .inflate(getLayoutInflater(), binding.componentTicketFromContainer, true);
                compFromBinding.setData(getFromComponent());
                ComponentTicketOwnerDisplayBinding compToBinding = ComponentTicketOwnerDisplayBinding
                        .inflate(getLayoutInflater(), binding.componentTicketToContainer, true);
                compToBinding.setData(getDestinationComponent());
                binding.setActivity(this);
                break;
            default:
                isLoading.set(true);
        }

    }

    public int getVipBoxInfoVisibility() {
        if (purchaseDetailVM.purchaseModel != null && purchaseDetailVM.purchaseModel.isVipBox != null && purchaseDetailVM.purchaseModel.isVipBox) {
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
    }

    public TicketOwnerInfoComponent getDestinationComponent() {
        if (purchaseDetailVM.ticketOwnerInfoComponents.size() > 1) {
            return purchaseDetailVM.ticketOwnerInfoComponents.get(1);
        } else {
            return null;
        }
    }

    public TicketOwnerInfoComponent getFromComponent() {
        if (purchaseDetailVM.ticketOwnerInfoComponents.size() > 0) {
            return purchaseDetailVM.ticketOwnerInfoComponents.get(0);
        } else {
            return null;
        }
    }

    public boolean goneSlipBankbutton() {

        return type == PurchaseDetailType.ReadOperation && purchaseDetailVM.purchaseModel.paymentType.equals(PurchaseModel.PaymentType.BankSlip)
                && !"INGRESSE".equals(purchaseDetailVM.purchaseModel.originType);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
