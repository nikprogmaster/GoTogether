package com.kandyba.gotogether.models.domain.events


data class EventInfoDomainModel(
    val title: String,
    val photoLinks: List<String>,
    val likedByUser: Boolean,
    val dates: List<DateDomainModel>,
    val price: String,
    val isFree: Boolean,
    val categories: List<String>
)




