package br.com.goin.feature.feed.commentary

import br.com.goin.feature.feed.commentary.model.CreateComment
import br.com.goin.feature.feed.commentary.model.CreateCommentResponse
import java.io.Serializable

interface CommentPresenter {

    fun onCreate(postId: String?, post: Serializable?)
    fun sendComment(comment: CreateComment)
    fun onDeleteComment(postId: String?, commentId: String?)
}