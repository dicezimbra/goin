package br.com.goin.component.ui.kit.views

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridSpaceItemDecoration constructor(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildLayoutPosition(view)
        if (position != 0 && parent.layoutManager is GridLayoutManager) {
            if(parent.getChildLayoutPosition(view) % 2 == 0) {
                outRect.set(space, space, 0, space)
            }else{
                outRect.set(0, space, space, space)
            }
        }
    }
}