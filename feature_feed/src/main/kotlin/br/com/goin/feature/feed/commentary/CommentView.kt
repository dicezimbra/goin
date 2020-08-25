package br.com.goin.feature.feed.commentary

import br.com.goin.component.session.user.UserModel
import br.com.goin.feature.feed.commentary.model.Comments
import br.com.goin.feature.feed.model.Post

interface CommentView {
    fun emptyComments(message: String?)
    fun showLoading()
    fun hideLoading()
    fun configureToolbar()
    fun setUserInfo(it: UserModel)
    fun onLoadComments(comments: MutableList<Comments>)
    fun intentPost(intent: Post)
    fun onClickListeners(postId: String?, userId: String?)
    fun showMessageNoComments()
}