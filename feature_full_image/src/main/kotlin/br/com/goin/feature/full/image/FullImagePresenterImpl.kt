package br.com.goin.feature.full.image

class FullImagePresenterImpl(val view: FullImageView) : FullImagePresenter {

    private var extras: String? = null

    override fun onReceiveExtras(stringExtra: String) {
        extras = stringExtra
        extras?.let {
            onLoadExtras(it)
        }
    }

    override fun onLoadExtras(it: String) {
        view.setImage(it)
    }

}
