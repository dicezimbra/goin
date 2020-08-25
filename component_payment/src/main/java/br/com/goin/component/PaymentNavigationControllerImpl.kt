package br.com.goin.component

import android.app.Activity
import android.content.Context
import br.com.goin.base.model.PaymentModel
import br.com.goin.component.confirmation.ConfirmationActivity
import br.com.goin.component.listadresses.ListAdressesActivity
import br.com.goin.component.listcreditcards.ListCreditCardActivity
import br.com.goin.component.navigation.PaymentNavigationController
import br.com.goin.component.newaddress.NewAddressActivity
import br.com.goin.component.newcreditcard.NewCreditCardActivity

class PaymentNavigationControllerImpl : PaymentNavigationController {

    override fun goToSelectAddressScreen(context: Context, model : PaymentModel) {
        ListAdressesActivity.starter(context, model)
    }

    override fun goToNewAddressScreen(context: Activity, model: PaymentModel?) {
        NewAddressActivity.starter(context, model)
    }

    override fun goToCreditCardList(context: Context, paymentInfo: PaymentModel) {
        ListCreditCardActivity.starter(context, paymentInfo)
    }

    override fun goToNewCreditCradScreen(context: Activity, paymentInfo: PaymentModel) {
        NewCreditCardActivity.starter(context,paymentInfo)
    }

    override fun goToConfirmationScreen(context: Context, paymentModel: PaymentModel) {
        ConfirmationActivity.starter(context, paymentModel)
    }
}