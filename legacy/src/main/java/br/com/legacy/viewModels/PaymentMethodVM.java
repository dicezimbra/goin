package br.com.legacy.viewModels;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;

import com.google.gson.Gson;
import br.com.legacy.models.PurchaseModel;
import br.com.legacy.utils.Constants;
import br.com.legacy.viewModels.base.BaseViewModel;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.math.BigDecimal;

public class PaymentMethodVM extends BaseViewModel {

    private PurchaseModel purchaseModel;

    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
            .clientId(Constants.PAYPAL_APP_ID)
            .acceptCreditCards(true)
            .merchantPrivacyPolicyUri(Uri.EMPTY) //TODO: Policitica de Privadade
            .merchantUserAgreementUri(Uri.EMPTY) //TODO: Termos de Uso
            .merchantName(Constants.COMPANY_NAME);

    public PaymentMethodVM(Application application) {
        super(application);
    }

    public void init(HandlerError handlerError, Intent intent) {
        super.init(handlerError);
        setPurchaseModel((new Gson()).fromJson(intent.getStringExtra(Constants.PURCHASE_MODEL), PurchaseModel.class));
    }

    public PurchaseModel getPurchaseModel() {
        return purchaseModel;
    }

    public void setPurchaseModel(PurchaseModel purchaseModel) {
        this.purchaseModel = purchaseModel;
    }

    public void startPayPal(Activity activity){
        // Start PayPal Service

        Intent intent = new Intent(activity, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        activity.startService(intent);

        // PayPal Interface

        // PAYMENT_INTENT_SALE will cause the payment to complete immediately.
        // Change PAYMENT_INTENT_SALE to
        //   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
        //   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
        //     later via calls from your server.

        PayPalPayment payment = new PayPalPayment(new BigDecimal(this.purchaseModel.price), "BRL", this.purchaseModel.ticketName,
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent paymentIntent = new Intent(activity, PaymentActivity.class);

        // send the same configuration for restart resiliency
        paymentIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        paymentIntent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        activity.startActivityForResult(paymentIntent, 0);
    }
}
