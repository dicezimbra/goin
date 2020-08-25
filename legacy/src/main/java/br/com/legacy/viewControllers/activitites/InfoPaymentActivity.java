package br.com.legacy.viewControllers.activitites;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import androidx.databinding.BindingAdapter;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.InputType;
import android.view.View;

import br.com.BR;
import br.com.goin.component.ui.kit.dialog.DialogUtils;
import br.com.legacy.components.ToolbarComponent;
import br.com.legacy.customViews.CustomEditText;
import br.com.legacy.managers.dtos.ErrorResponse;
import br.com.legacy.managers.Iugu.IuguManager;
import br.com.legacy.managers.Iugu.IuguPaymentTokenResponse;
import br.com.legacy.navigation.Coordinator;
import br.com.R;
import br.com.legacy.utils.Constants;
import br.com.legacy.utils.TypefaceUtil;
import br.com.legacy.viewControllers.activitites.base.BaseActivity;
import br.com.legacy.viewModels.InfoPaymentVM;

import br.com.databinding.ActivityInfoPaymentBinding;
import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoPaymentActivity extends BaseActivity implements IuguManager.IuguServiceHandler {

    ActivityInfoPaymentBinding binding;
    public InfoPaymentVM infoPaymentVM;
    public ToolbarComponent toolbar;
    ProgressDialog progressDialog;
    String originType;

    @BindView(br.com.R2.id.info_payment_toolbar)
    View toolbarThis;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = (ActivityInfoPaymentBinding) this.setLayoutId(R.layout.activity_info_payment, BR.activity);
        infoPaymentVM = (InfoPaymentVM) this.linkViewModel(InfoPaymentVM.class);

        ButterKnife.bind(this);
        Coordinator.setStatusBarColor(this, getString(R.color.grayBackgroundBoard), toolbarThis);

        toolbar = ToolbarComponent.build()
                .addLeftIcon(R.drawable.ic_arrow_back)
                .setTitle(getString(R.string.ticket_purchase));

        originType = (getIntent() != null) ? getIntent().getStringExtra(Constants.ORIGIN_TYPE) : null;

        infoPaymentVM.init(this, getIntent(), this);

        setViewsFont();
    }

    void setViewsFont() {

        TypefaceUtil.initilize(this);
        TypefaceUtil.mediumFont(binding.infoPaymentBuyerCity, binding.infoPaymentBuyerComplement,
                binding.infoPaymentBuyerMail, binding.infoPaymentBuyerNeighborhood, binding.infoPaymentBuyerPhone,
                binding.infoPaymentBuyerPostalCode, binding.infoPaymentBuyerState, binding.infoPaymentBuyerStreet,
                binding.infoPaymentBuyerStreetNumber, binding.infoPaymentCardNumber,
                binding.infoPaymentCpfField, binding.infoPaymentCvv, binding.infoPaymentExpirationMonth,
                binding.infoPaymentExpirationYear, binding.infoPaymentFirstNameField,
                binding.infoPaymentLastNameField, binding.infoPaymentLabel, binding.infoPaymentNextButton,
                binding.infoPaymentParcelsNumber);
    }

    @BindingAdapter("android:setCustomStyle")
    public static void setPaddingLeft(View view, boolean customStyle) {
        ((CustomEditText) view).setStyle(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, R.color.gray, R.color.lightGray, R.color.lightGray, 12);
    }

    @BindingAdapter("android:email")
    public static void setEmail(View view, String email) {
        if (view instanceof CustomEditText && email != null && !email.isEmpty()) {
            ((CustomEditText) view).setText(email);
        }
    }

    @BindingAdapter("android:phone")
    public static void setPhone(View view, String phone) {
        if (view instanceof CustomEditText && phone != null && !phone.isEmpty()) {
            ((CustomEditText) view).setText(phone);
        }
    }

    @BindingAdapter("android:name")
    public static void setName(View view, String name) {
        if (view instanceof CustomEditText && name != null && !name.isEmpty()) {
            ((CustomEditText) view).setText(name);
        }
    }

    @Override
    public void onClickToolbarLeftIcon() {
        super.onClickToolbarLeftIcon();
        onBackPressed();
    }

    public void onClickNextButton(View view) {
        if (infoPaymentVM.verifyUserInputs(binding, this)) {
            if (progressDialog == null) {
                progressDialog = DialogUtils.INSTANCE.createProgressDialog(this);
            }
            progressDialog.show();
            switch (infoPaymentVM.paymentMethod) {
                case Card:

                    if ("INGRESSE".equals(originType)) {

                        infoPaymentVM.setCreditCardMask("");
                        infoPaymentVM.updatePurchaseModel();
                        progressDialog.dismiss();
                        Coordinator.goToPurchaseConfirmation(this, infoPaymentVM.purchaseModel,
                                "", infoPaymentVM.purchaseModel.paymentInfo.displayNumber, originType);
                    } else {

                        infoPaymentVM.getIuguPaymentToken(this, this);
                    }

                    break;
                case Boleto:
                    infoPaymentVM.updatePurchaseModel();
                    progressDialog.hide();
                    Coordinator.goToPurchaseConfirmation(this, infoPaymentVM.purchaseModel);
                    break;
            }
        } else {
            DialogUtils.INSTANCE.show(this, getString(R.string.ticket_owner_info_invalid));
        }
    }

    @Override
    public void onSuccessToGetToken(IuguPaymentTokenResponse response) {
        progressDialog.hide();
        String paymentToken = response.id;
        String creditCardMask = response.extra_info.display_number;
        infoPaymentVM.setCreditCardMask(creditCardMask);
        infoPaymentVM.updatePurchaseModel();

        infoPaymentVM.purchaseModel.originType = originType;
        Coordinator.goToPurchaseConfirmation(this, infoPaymentVM.purchaseModel, paymentToken,
                creditCardMask, originType);
    }

    @Override
    public void onError(ErrorResponse error) {
        progressDialog.hide();
        DialogUtils.INSTANCE.show(this, R.string.error_with_payment_infos);
    }
}
