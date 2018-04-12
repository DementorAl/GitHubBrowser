package ru.danilov.githubbrowse.model

import com.google.gson.annotations.SerializedName

class PeopleSearchItemModel{
    val login : String =""
    @SerializedName("avatar_url")
    val avatar  : String = ""
    val id : Long = 0
    @SerializedName("html_url")
    val url : String = ""
}