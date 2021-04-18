package com.kandyba.gotogether.domain.user

import com.kandyba.gotogether.models.general.UserInfoRequestBody
import com.kandyba.gotogether.models.general.UserInterestsRequestBody
import com.kandyba.gotogether.models.general.UserMainRequestBody
import com.kandyba.gotogether.models.presentation.UserInfoModel
import io.reactivex.Single


interface UserInteractor {

    fun updateMainUserInfo(token: String, bodyMain: UserMainRequestBody): Single<UserInfoModel>

    fun updateUserInfo(token: String, body: UserInfoRequestBody): Single<UserInfoModel>

    fun updateUserInterests(token: String, body: UserInterestsRequestBody): Single<UserInfoModel>

    fun getUserInfo(token: String, uid: String, updateCache: Boolean): Single<UserInfoModel>
}