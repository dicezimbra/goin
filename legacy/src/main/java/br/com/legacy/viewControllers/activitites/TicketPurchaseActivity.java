package br.com.legacy.viewControllers.activitites;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableBoolean;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;

import br.com.BR;
import br.com.goin.base.utils.Utils;
import br.com.goin.component.navigation.NavigationController;
import br.com.goin.component.session.user.UserModel;
import br.com.goin.component.ui.kit.dialog.DialogUtils;
import br.com.legacy.components.TicketOwnerInfoComponent;
import br.com.legacy.components.ToolbarComponent;
import br.com.legacy.managers.SessionManager;
import br.com.legacy.models.TicketModel;
import br.com.legacy.navigation.Coordinator;
import br.com.R;
import br.com.legacy.utils.Constants;
import br.com.legacy.utils.TypefaceUtil;
import br.com.legacy.viewControllers.activitites.base.BaseActivity;
import br.com.legacy.viewModels.TicketPurchaseVM;
import br.com.databinding.ActivityTicketPurchaseBinding;
import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.ArrayList;
import java.util.List;

public class TicketPurchaseActivity extends BaseActivity {

    ActivityTicketPurchaseBinding binding;
    public TicketPurchaseVM ticketPurchaseVM;
    public TicketModel ticketModel;
    public List<UserModel> ticketOwners = new ArrayList<>();
    public ObservableBoolean myTicketEnabled = new ObservableBoolean(true);
    @BindView(br.com.R2.id.ticket_purchase_toolbar)
    View toolbarThis;
    String originType;

    @BindView(br.com.R2.id.text_terms)
    TextView policy;

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = (ActivityTicketPurchaseBinding) setLayoutId(R.layout.activity_ticket_purchase, BR.activity);
        ticketPurchaseVM = (TicketPurchaseVM) linkViewModel(TicketPurchaseVM.class);

        ButterKnife.bind(this);
        Coordinator.setStatusBarColor(this, getString(R.color.grayBackgroundBoard), toolbarThis);

        toolbar = ToolbarComponent.build()
                .addLeftIcon(R.drawable.ic_arrow_back)
                .setTitle(getString(R.string.ticket_purchase));

        ticketPurchaseVM.init(this, SessionManager.getInstance().getCurrentUser(this), getIntent());
        originType = (getIntent() != null) ? getIntent().getStringExtra(Constants.ORIGIN_TYPE) : null;

        ticketModel = new Gson().fromJson(getIntent().getStringExtra(Constants.TICKET), TicketModel.class);
        binding.selectedFriendsRv.setNestedScrollingEnabled(false);

        TypefaceUtil.initilize(this);
        TypefaceUtil.mediumFont(binding.eventName, binding.eventAddress, binding.eventPrice,
                binding.eventVipBoxCapacity, binding.labelTicketInfo, binding.nextButton, binding.addFriendsButton);

        configurePolicyText();
    }

    public TicketPurchaseActivity() {
        super();
    }

    public String getPriceInformation() {
        return ticketModel.name + " - " + Utils.INSTANCE.getPriceText(ticketModel.price);
    }

    public String getVipBoxCapacityInformation() {
        if (ticketModel != null && ticketModel.vipCapacity != null) {
            return getResources().getQuantityString(R.plurals.vip_box_capacity_label, ticketModel.vipCapacity, ticketModel.vipCapacity);
        }
        return "";
    }

    public void onClickAddFriendsButton(View view) {
        Coordinator.goToSelectTicketOwners(this, ticketModel);
    }

    public void onClickNextButton(View view) {
        ticketOwners.clear();
        ticketPurchaseVM.updateFieldsList();

        if (ticketPurchaseVM.ticketOwnerInfoComponents.isEmpty()) {
            DialogUtils.INSTANCE.show(TicketPurchaseActivity.this, R.string.empty_ticket_owners_list);
            return;
        }

        for (TicketOwnerInfoComponent component : ticketPurchaseVM.ticketOwnerInfoComponents) {
            if (!verifyUserInputs(component)) {
                DialogUtils.INSTANCE.show(this, getString(R.string.ticket_owner_info_incomplete));
                return;
            }

            if (ticketModel.isVipBox != null && ticketModel.isVipBox) {
                if (ticketPurchaseVM.ticketOwnerInfoComponents.size() > ticketModel.vipCapacity) {
                    DialogUtils.INSTANCE.show(TicketPurchaseActivity.this, R.string.selected_friends_over_capacity);
                    return;
                }
            } else {
                if (ticketPurchaseVM.ticketOwnerInfoComponents.size() > ticketPurchaseVM.remaining) {
                    DialogUtils.INSTANCE.show(TicketPurchaseActivity.this, R.string.invalid_selected_quantity);
                    return;
                }
            }

        }

        ticketPurchaseVM.setFinalTicketOwners();
        if (ticketModel.isVipBox != null && ticketModel.isVipBox) {
            ticketPurchaseVM.purchaseModel.isSplittedVipBox = binding.splitCheckbox.isChecked();
        }
        Coordinator.goToChoosePaymentMethods(this, ticketPurchaseVM.purchaseModel, originType);
    }

    boolean verifyUserInputs(TicketOwnerInfoComponent component) {
        if (component.getRgEditText() != null) {
            if (component.getRgEditText().getText().toString().isEmpty()) {
                return false;
            }
        } else {
            return false;
        }

        if (component.hasHalfEntrance) {
            if (component.getHalfEntranceEditText() != null) {
                return !component.getHalfEntranceEditText().getText().toString().isEmpty();
            } else {
                return false;
            }
        }

        return true;
    }

    public void setMyTicketEnabled(boolean myTicketEnabled) {
        this.myTicketEnabled.set(myTicketEnabled);
        if (myTicketEnabled) {
            ticketPurchaseVM.addMyTicketToList();
        } else {
            ticketPurchaseVM.removeMyTicketFromList();
        }
    }

    @BindingAdapter("android:switchListener")
    public static void setSwitchListener(View view, final TicketPurchaseActivity ticketPurchaseActivity) {
        if (view instanceof Switch) {
            ((Switch) view).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ticketPurchaseActivity.setMyTicketEnabled(isChecked);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_SELECTED_FRIENDS && resultCode == RESULT_OK) {
            ticketPurchaseVM.updateList(data);
        }
    }

    @Override
    public void onClickToolbarLeftIcon() {
        //super.onClickToolbarLeftIcon();
        onBackPressed();
    }

    public boolean shouldShowIncludeSwitch() {

        boolean showSwitch = false;

        showSwitch = ticketModel == null
                || ticketModel.isVipBox == null
                || !ticketModel.isVipBox;

        showSwitch = (!"INGRESSE".equals(originType));

        return showSwitch;
    }

    public boolean isIngresse() {

        return "INGRESSE".equals(originType);
    }

    private void configurePolicyText(){
        ClickableSpan halfPricePolicySpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                NavigationController.Companion.getInstance().termsModule().goToHalfPricePolicy(TicketPurchaseActivity.this);
            }
        };

        String terms = getString(R.string.privacy_terms_buy);
        SpannableString stringEstilizada = new SpannableString(terms);

        if(terms.contains("Ao efetivar a compra")){
            int halfPriceIndex = terms.indexOf("Política de Meia Entrada");
            int halfPriceLenght = "Política de Meia Entrada".length();

            stringEstilizada.setSpan(halfPricePolicySpan, halfPriceIndex, halfPriceIndex + halfPriceLenght, Spanned.SPAN_INCLUSIVE_INCLUSIVE );
        }else{
            int halfPriceIndex = terms.indexOf("Half Price Policy");
            int halfPriceLenght = "Half Price Policy".length();
            stringEstilizada.setSpan(halfPricePolicySpan, halfPriceIndex, halfPriceIndex + halfPriceLenght, Spanned.SPAN_INCLUSIVE_INCLUSIVE );
        }

        policy.setMovementMethod( LinkMovementMethod.getInstance() );
        policy.setText(stringEstilizada);
    }

    public boolean shouldShowSplitValueCheckbox() {
        return !shouldShowIncludeSwitch();
    }
}
