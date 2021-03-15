package com.kandyba.gotogether.models.domain.events


data class EventInfoDomainModel(
    val title: String,
    val photoLinks: List<String>,
    val likedByUser: Boolean,
    val dates: List<Date>,
    val price: String,
    val isFree: Boolean,
    val categories: List<String>
)




