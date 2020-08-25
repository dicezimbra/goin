package br.com.goin.component.terms

import android.content.Context
import br.com.goin.component.navigation.TermsNavigationController

class TermsNavigationControllerImpl: TermsNavigationController{

    override fun goToTermOfUse(context: Context) {
        TermsActivity.starter(context, TermsType.TERMS_OF_USE)
    }

    override fun goToHalfPricePolicy(context: Context) {
        TermsActivity.starter(context, TermsType.HALF_PRICING_POLICY)
    }

    override fun goToPrivacyPolicy(context: Context) {
        TermsActivity.starter(context, TermsType.PRIVACY_POLICY)
    }
}