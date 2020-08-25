package br.com.goin.component.terms

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import br.com.goin.base.helpers.AlertDialogHelper
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.terms.model.TermsModel
import iammert.com.expandablelib.Section
import kotlinx.android.synthetic.main.activity_user_terms.*
import kotlinx.android.synthetic.main.item_custom_toolbar_term.*

class TermsActivity : AppCompatActivity(), TermsView {
    private val presenter: TermsPresenter = TermsPresenterImpl(this)
    private lateinit var termType: TermsType

    companion object {
        const val termTypeKey = "type"

        fun starter(context: Context, type: TermsType) {
            val intent = Intent(context, TermsActivity::class.java).apply {
                putExtra(termTypeKey, type.name)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_terms)

        intent.extras?.let {
            termType = TermsType.valueOf(it.getString(termTypeKey) ?: "")
        }

        presenter.onCreate()
    }

    override fun onResume() {
        super.onResume()
        when(termType){
            TermsType.TERMS_OF_USE -> {
                Analytics.instance.screenView(this, getString(R.string.term_use_screen_name))
            }
            TermsType.HALF_PRICING_POLICY -> {
                Analytics.instance.screenView(this, getString(R.string.half_ticket_screen_name))
            }
            TermsType.PRIVACY_POLICY -> {
                Analytics.instance.screenView(this, getString(R.string.privicy_screen_name))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.fetchTerms(termType)
    }

    override fun setupToolbar() {
        val toolbarTitleId = when(termType) {
            TermsType.HALF_PRICING_POLICY -> R.string.entry_policy
            TermsType.PRIVACY_POLICY -> R.string.privacy_policy
            TermsType.TERMS_OF_USE -> R.string.terms_of_use
        }

        toolbar_title.text = getString(toolbarTitleId)
    }

    override fun setupBackButton() {
        toolbar_left_icon.setOnClickListener { this.onBackPressed() }
    }

    override fun configureExpandableLayout(termsModel: List<TermsModel>) {
        TermsHelper.settingExpandableLayout(this, expandable_layout, user_terms_scroll)

        try {
            if (expandable_layout.sections.size <= 0) {
                for (section in termsModel) setupSection(section)
            }
        } catch (e: Exception) {
            Log.e("UserTermsActivity", e.message, e)
        }
    }

    private fun setupSection(model: TermsModel) {
        val section = Section<ExpandableParent, ExpandableChild>()

        section.apply {
            parent = ExpandableParent(model.title, 0)
            children.add(ExpandableChild(model.description))
        }

        expandable_layout.addSection(section)
    }

    override fun showLoading() {
        user_terms_progress_bar.visibility = View.VISIBLE
        expandable_layout.visibility = View.GONE
    }

    override fun hideLoading() {
        user_terms_progress_bar.visibility = View.GONE
        expandable_layout.visibility = View.VISIBLE
    }

    override fun displayDialogOnError() {
        AlertDialogHelper.show(this, getString(R.string.unkown_error))
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
}