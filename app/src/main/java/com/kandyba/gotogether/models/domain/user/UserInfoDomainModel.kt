package com.kandyba.gotogether.models.domain.user

import com.kandyba.gotogether.models.domain.events.EventInfoDomainModel

class UserInfoDomainModel (
    val id: String,
    val email: String,
    val firstName: String,
    val birthDate: String,
    val sex: String,
    val age: String,
    val longitude: String,
    val latitude: String,
    val createdAt: String,
    val updatedAt: String,
    val events: Map<String, EventInfoDomainModel>
)