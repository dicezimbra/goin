package br.com.goin.findfriends.adapter

interface FindFriendsClickListener {

    fun onClickFollow(userId: String)
    fun onClickUnfollow(userId: String)
    fun onClickUser(userId: String, currentUserId: String?)
}
