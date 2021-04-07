package com.kandyba.gotogether.models.data.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class UserInfoDataModel(
    @SerializedName("tokens")
    @Expose
    val tokens: List<String>,

    @SerializedName("id")
    @Expose
    val id: String,

    @SerializedName("password_digest")
    @Expose
    val passwordDigest: String,

    @SerializedName("email")
    @Expose
    val email: String,

    @SerializedName("first_name")
    @Expose
    val firstName: String? = null,

    @SerializedName("phone")
    @Expose
    val phone: String? = null,

    @SerializedName("birth_date")
    @Expose
    val birthDate: String? = null,

    @SerializedName("sex")
    @Expose
    val sex: String? = null,

    @SerializedName("longitude")
    @Expose
    val longitude: String? = null,

    @SerializedName("latitude")
    @Expose
    val latitude: String? = null,

    @SerializedName("created_at")
    @Expose
    val createdAt: String,

    @SerializedName("updated_at")
    @Expose
    val updatedAt: String,

    @SerializedName("info")
    @Expose
    val info: String? = null,

    @SerializedName("is_loyal")
    @Expose
    val isLoyal: Boolean
) {

}