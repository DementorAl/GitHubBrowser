package ru.danilov.githubbrowse.presenter

import android.os.Bundle
import android.util.Log
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.GraphRequest.TAG
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.vk.sdk.VKAccessToken
import ru.danilov.githubbrowse.repository.NetworkRepository
import ru.danilov.githubbrowse.repository.PreferenceRepository
import ru.danilov.githubbrowse.utils.SocialNetworksEnum
import ru.danilov.githubbrowse.utils.async
import ru.danilov.githubbrowse.view.login.LoginView


class LoginPresenter(val view: LoginView) {

    fun initState() {
        if (PreferenceRepository.token.isNotEmpty()) {
            view.successAuthAction()
        }
        view.initFBState()
        view.initGoogleState()
        view.initVkState()
    }

    fun setToken(vkAccessToken: VKAccessToken? = null,
                 fbAccessToken: AccessToken? = null,
                 googleSignInAccount: GoogleSignInAccount? = null,
                 socialNetworkType: SocialNetworksEnum) {
        when (socialNetworkType.value) {
            SocialNetworksEnum.VK.value -> {
                getInfoFromVk(vkAccessToken)
            }
            SocialNetworksEnum.FB.value -> {
                getInfoFromFb(fbAccessToken)
            }
            SocialNetworksEnum.GOOGLE.value -> {
                getInfoFromGoogle(googleSignInAccount)
            }

        }
    }

    private fun getInfoFromVk(token: VKAccessToken?) {
        PreferenceRepository.login = token?.email ?: ""
        PreferenceRepository.token = token?.accessToken ?: ""
        NetworkRepository.service.getVKUserInfo().async()
                .subscribe({
                    PreferenceRepository.name = "${it.response[0].firstName} ${it.response[0].lastName}"
                    PreferenceRepository.avatar = it.response[0].photo
                    view.successAuthAction()
                }, {
                    it.message
                })

    }

    private fun getInfoFromFb(token: AccessToken?) {
        val request: GraphRequest = GraphRequest.newMeRequest(token) { `object`, response ->
            Log.d(TAG, "object = " + `object`.toString())
            Log.d(TAG, "response = " + response.toString())
            if (response != null) {
                try {
                    val data = response.jsonObject
                    if (data.has("picture")) {
                        PreferenceRepository.avatar = data.getJSONObject("picture").getJSONObject("data").getString("url")
                    }
                    if (data.has("name")) {
                        PreferenceRepository.name = data.getString("name")
                    }
                    if (data.has("email")) {
                        PreferenceRepository.login = data.getString("email")
                    }
                    PreferenceRepository.token = token?.token?:""
                    view.successAuthAction()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

        }
        val parameters = Bundle()
        parameters.putString("fields", "id,name,link,cover,picture.type(large),email")
        request.parameters = parameters
        request.executeAsync()
    }

    private fun getInfoFromGoogle(googleSignInAccount: GoogleSignInAccount?) {
        PreferenceRepository.login = googleSignInAccount?.email ?: ""
        PreferenceRepository.name = googleSignInAccount?.displayName ?: ""
        PreferenceRepository.avatar = googleSignInAccount?.photoUrl?.toString() ?: ""
        PreferenceRepository.token = googleSignInAccount?.id ?: ""
        view.successAuthAction()
    }


}