package ru.danilov.githubbrowse.view.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKScope
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKError
import kotlinx.android.synthetic.main.activity_login.*
import ru.danilov.githubbrowse.view.main.MainActivity
import ru.danilov.githubbrowse.R
import ru.danilov.githubbrowse.utils.onClick
import ru.danilov.githubbrowse.presenter.LoginPresenter
import ru.danilov.githubbrowse.utils.startActivity
import ru.danilov.githubbrowse.utils.SocialNetworksEnum


class LoginActivity : AppCompatActivity(), LoginView {

    companion object {
        private const val GOOGLE_REQUEST_CODE = 1098
    }
    val presenter: LoginPresenter by lazy { LoginPresenter(this) }
    lateinit var callbackManager: CallbackManager
    private val facebookLoginButton: LoginButton by lazy { LoginButton(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.app_name)
        setContentView(R.layout.activity_login)
        presenter.initState()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val isFacebook = callbackManager.onActivityResult(requestCode, resultCode, data);
        if (!isFacebook) {
            if (requestCode == GOOGLE_REQUEST_CODE) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                    presenter.setToken(googleSignInAccount = account
                            , socialNetworkType = SocialNetworksEnum.GOOGLE)
                } catch (e: ApiException) {
                    Log.d("googleSignIn", "signInResult:failed code=" + e.statusCode)
                }
                return
            } else {
                if (!VKSdk.onActivityResult(requestCode, resultCode, data, object : VKCallback<VKAccessToken> {
                            override fun onResult(res: VKAccessToken?) {
                                presenter.setToken(vkAccessToken = res, socialNetworkType = SocialNetworksEnum.VK)
                            }

                            override fun onError(error: VKError?) {
                                showError("")
                            }
                        })) {
                    super.onActivityResult(requestCode, resultCode, data)
                }
            }
        }
    }

    override fun initFBState() {
        facebookLoginButton.setReadPermissions("email")
        callbackManager = com.facebook.CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        presenter.setToken(fbAccessToken = loginResult.accessToken,
                                socialNetworkType = SocialNetworksEnum.FB)
                    }

                    override fun onCancel() {}
                    override fun onError(exception: FacebookException) {
                        val message = exception.message
                        if (message != null) {
                            showError(message)
                        }
                    }
                })
        fbSignInButton.onClick { facebookLoginButton.callOnClick() }
    }

    override fun initVkState() {
        vkSignInButton.onClick { VKSdk.login(this, VKScope.EMAIL, VKScope.OFFLINE) }
    }

    override fun initGoogleState() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInButton.onClick {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, GOOGLE_REQUEST_CODE)
        }
    }


    override fun successAuthAction() {
        startActivity<MainActivity>()
        finish()
    }

    override fun showError(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }


}