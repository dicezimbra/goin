package br.com.goin.component.terms

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import iammert.com.expandablelib.ExpandableLayout

object TermsHelper {
    fun settingExpandableLayout(context: Context, expandableLayout: ExpandableLayout, scroll: ScrollView) {
        var currentSelectedParent = -1

        expandableLayout.setRenderer(object : ExpandableLayout.Renderer<ExpandableParent, ExpandableChild> {

            override fun renderParent(view: View?, modelParent: ExpandableParent?, isExpandadable: Boolean, parentPos: Int) {
                (view?.findViewById<View>(R.id.expandablet_text) as TextView).text = modelParent?.text
                (view.findViewById<View>(R.id.expandable_icon) as ImageView).visibility = View.VISIBLE
                (view.findViewById<View>(R.id.expandable_icon) as ImageView).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_document))
                (view.findViewById<View>(R.id.expandable_arrow) as ImageView).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.event_arrow_down))
            }

            override fun renderChild(view: View?, modelChild: ExpandableChild?, parentPos: Int, childPos: Int) {
                (view?.findViewById<View>(R.id.textView4) as TextView).text = modelChild?.text
                view.setBackgroundResource(R.color.grayBackgroundBoard)
                (view.findViewById<View>(R.id.textView4) as TextView).setBackgroundResource(R.color.grayBackgroundBoard)
            }
        })

        expandableLayout.setExpandListener<ExpandableParent> { parentIndex, _, view ->
            collapseItemPreviouslyOpen(currentSelectedParent, expandableLayout)

            scrollToSelectedItem(parentIndex, expandableLayout, scroll)

            currentSelectedParent = parentIndex

            (view.findViewById<View>(R.id.expandable_arrow) as ImageView).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.event_icon_arrow_up))
        }

        expandableLayout.setCollapseListener<ExpandableParent> { _, _, view ->
            currentSelectedParent = -1
            (view.findViewById<View>(R.id.expandable_arrow) as ImageView).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.event_arrow_down))
        }
    }

    private fun collapseItemPreviouslyOpen(currentItemSelected: Int, expandableLayout: ExpandableLayout) {
        if(currentItemSelected != -1) {
            expandableLayout.sections[currentItemSelected].expanded = false
            expandableLayout.notifyParentChanged(currentItemSelected)
            val viewGroup = (expandableLayout.getChildAt(currentItemSelected) as ViewGroup)
            if(viewGroup.childCount - 1 > 0) {
                viewGroup.removeViews(1, viewGroup.childCount - 1)
            }
        }
    }

    private fun scrollToSelectedItem(index: Int, expandableLayout: ExpandableLayout, scroll: ScrollView) {
        val targetView = expandableLayout.getChildAt(index)
        scroll.smoothScrollTo(0, targetView.scrollY)
    }
}