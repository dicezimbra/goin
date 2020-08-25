package br.com.goin.component.ui.kit.toolbar

import androidx.appcompat.widget.Toolbar


class GoinToolbarPresenterImpl(val view: GoinToolbarView): GoinToolbarPresenter{

    override fun onCreate(){
        view.createView()
    }

}
