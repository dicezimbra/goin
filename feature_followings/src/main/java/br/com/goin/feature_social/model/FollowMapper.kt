package br.com.goin.feature_social.model

import br.com.goin.component.session.user.UserModel
import java.util.ArrayList

class FollowMapper {
    companion object {
        fun mapToModel(users: List<UserCardDetailsResponse>?, type: FollowRepository.ListType? = null): List<UserModel> {
            val list = ArrayList<UserModel>()
            users?.run {
                if (!this.isEmpty())
                    for (user in this) {
                        val model = UserModel()

                        model.id = user.id ?: ""
                        model.followedByMe = user.followedByMe

                        when (type) {
                            FollowRepository.ListType.Followers -> {
                                model.name = user.followerName ?: ""
                                model.profilePicture = user.followerAvatar ?: ""
                            }
                            FollowRepository.ListType.Following -> {
                                model.name = user.userName ?: ""
                                model.profilePicture = user.userAvatar ?: ""
                            }
                            else -> {
                                model.name = user.name ?: ""
                                model.profilePicture = user.profilePicture ?: ""
                            }
                        }

                        list.add(model)
                    }
            }
            return list
        }
    }
}