package ru.danilov.githubbrowse.model

import com.google.gson.annotations.SerializedName

class VkCustomResponse(val response: List<VKResponseBody>)

class VKResponseBody {
    @SerializedName("first_name")
    var firstName: String = ""
    @SerializedName("last_name")
    var lastName: String = ""
    @SerializedName("photo_200")
    var photo: String = ""
}