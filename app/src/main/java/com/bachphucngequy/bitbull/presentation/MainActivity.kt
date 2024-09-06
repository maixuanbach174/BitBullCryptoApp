package com.bachphucngequy.bitbull.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bachphucngequy.bitbull.data.repository.CoinRepositoryImpl
import com.bachphucngequy.bitbull.news.NewsViewModel
import com.bachphucngequy.bitbull.presentation.ui.theme.BitBullTheme
import com.bachphucngequy.bitbull.presentation.viewmodel.AuthViewModel
import com.bachphucngequy.bitbull.presentation.viewmodel.CoinViewModel
import com.bachphucngequy.bitbull.presentation.viewmodel.SplashViewModel
import com.bachphucngequy.bitbull.retrofit.RetrofitInstance
import com.bachphucngequy.bitbull.tweets.TweetsViewModel
import com.bachphucngequy.bitbull.tweets.account.edit.EditProfileViewModel
import com.bachphucngequy.bitbull.tweets.account.follows.FollowsViewModel
import com.bachphucngequy.bitbull.tweets.account.profile.ProfileViewModel
import com.bachphucngequy.bitbull.tweets.post.NewPostViewModel
import com.bachphucngequy.bitbull.tweets.post.PostDetailViewModel
import com.bachphucngequy.bitbull.ui.navigation.MyAppNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    private val viewModel by viewModels<SplashViewModel>()

    private val coinViewModel by viewModels<CoinViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CoinViewModel(CoinRepositoryImpl(RetrofitInstance.coinPaprikaApi)) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        installSplashScreen().apply {
//            setKeepOnScreenCondition {
//                !viewModel.isReady.value
//            }
//        }
        val newsViewModel = ViewModelProvider(this)[NewsViewModel::class.java]
        val tweetsViewModel = ViewModelProvider(this)[TweetsViewModel::class.java]
        val postDetailViewModel = ViewModelProvider(this)[PostDetailViewModel::class.java]
        val profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        val editProfileViewModel = ViewModelProvider(this)[EditProfileViewModel::class.java]
        val followsViewModel = ViewModelProvider(this)[FollowsViewModel::class.java]
        val newPostViewModel = ViewModelProvider(this)[NewPostViewModel::class.java]

//        val authViewModel : AuthViewModel by viewModels()
//        authViewModel.configureGoogleSignIn(this)

        setContent {
            BitBullTheme{
                val coins by coinViewModel.coin.collectAsState()
                Scaffold(
                    content = {
                        MyAppNavHost(
                            it,
                            coins = coins,
                            newsViewModel = newsViewModel,
                            tweetsViewModel = tweetsViewModel,
                            postDetailViewModel = postDetailViewModel,
                            profileViewModel = profileViewModel,
                            editProfileViewModel = editProfileViewModel,
                            followsViewModel = followsViewModel,
                            newPostViewModel = newPostViewModel,
//                            authViewModel = authViewModel
                        )
                    }
                )
//                Surface(onClick = { /*TODO*/ }) {
//                    CoinDetailScreen(coinId = "eth-ethereum")
//                }
            }
        }
    }
}
