package br.com.goin.findfriends.model

import br.com.goin.component.session.user.UserModel

class FindFriendsMapper {
    companion object {
        fun mapToModel(users: List<FindFriendsModel>?): List<UserModel> {
            val list = ArrayList<UserModel>()
            users?.run {
                if (this.isNotEmpty()) {
                    for (user in this) {
                        val model = UserModel()
                        model.id = user.id
                        model.followedByMe = user.followedByMe
                        model.name = user.name
                        model.profilePicture = user.profilePicture
                        list.add(model)
                    }
                }
            }
            return list
        }
    }

}
