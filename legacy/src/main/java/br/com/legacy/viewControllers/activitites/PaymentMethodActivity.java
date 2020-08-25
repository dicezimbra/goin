package br.com.legacy.viewControllers.activitites;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;

import br.com.BR;
import br.com.legacy.components.ToolbarComponent;
import br.com.legacy.navigation.Coordinator;
import br.com.R;
import br.com.legacy.utils.Constants;
import br.com.legacy.utils.TypefaceUtil;
import br.com.legacy.viewControllers.activitites.base.BaseActivity;
import br.com.legacy.viewModels.PaymentMethodVM;
import br.com.databinding.ActivityPaymentMethodBinding;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

public class PaymentMethodActivity extends BaseActivity{

    ActivityPaymentMethodBinding binding;
    PaymentMethodVM paymentMethodVM;
    @BindView(br.com.R2.id.payment_method_toolbar)
    View toolbarThis;
    String originType;

    public enum PaymentMethod{
        PayPal,Card,Boleto
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = (ActivityPaymentMethodBinding) this.setLayoutId(R.layout.activity_payment_method, BR.activity);
        paymentMethodVM = (PaymentMethodVM) this.linkViewModel(PaymentMethodVM.class);

        ButterKnife.bind(this);
        Coordinator.setStatusBarColor(this, getString(R.color.grayBackgroundBoard), toolbarThis);

        toolbar = ToolbarComponent.build()
                .addLeftIcon(R.drawable.ic_arrow_back)
                .setTitle(getString(R.string.ticket_purchase));

        paymentMethodVM.init(this, getIntent());
        originType = (getIntent() != null) ? getIntent().getStringExtra(Constants.ORIGIN_TYPE) : null;

        TypefaceUtil.initilize(this);
        TypefaceUtil.mediumFont(binding.cardButton, binding.ticketButton, binding.paypalButton);
    }

    public void onClickPayPalButton(){
        paymentMethodVM.startPayPal(this);
    }

    public void onClickCardButton(){
        Coordinator.goToInfoPayment(this,paymentMethodVM.getPurchaseModel(), PaymentMethod.Card, originType);
    }

    public void onClickBoletoButton(){
        Coordinator.goToInfoPayment(this, paymentMethodVM.getPurchaseModel(), PaymentMethod.Boleto, originType);
    }

    @Override
    public void onClickToolbarLeftIcon() {
        onBackPressed();
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));
                    // TODO integration confirmTicket
//                    ticketsManager.confirmTicket(ticketIdWithDate, confirm.toJSONObject().toString());
                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            // TODO cancelTicket
//            ticketsManager.cancelTicket(ticketIdWithDate, eventId);
            Log.i("paymentExample", "The user canceled.");
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            //TODO cancelTicket
//            ticketsManager.cancelTicket(ticketIdWithDate, eventId);
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }

    public boolean isIngresse(){

        return "INGRESSE".equals(originType);
    }
}
