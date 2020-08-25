package br.com.legacy.helper

import android.graphics.Rect
import android.view.View

import androidx.recyclerview.widget.RecyclerView

class SpacingItemDecoration(private val horizontalSpacing: Int, private val verticalSpacing: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.left = this.horizontalSpacing / 2
        outRect.right = this.horizontalSpacing / 2
        outRect.top = this.verticalSpacing / 2
        outRect.bottom = this.verticalSpacing / 2
    }
}
