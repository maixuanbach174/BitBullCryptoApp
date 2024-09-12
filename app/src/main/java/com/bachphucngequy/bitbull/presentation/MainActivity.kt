package com.bachphucngequy.bitbull.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Scaffold
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.bachphucngequy.bitbull.news.NewsViewModel
import com.bachphucngequy.bitbull.presentation.ui.navigation.MyAppNavHost
import com.bachphucngequy.bitbull.presentation.ui.theme.BitBullTheme
import com.bachphucngequy.bitbull.presentation.viewmodel.AuthViewModel
import com.bachphucngequy.bitbull.presentation.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !viewModel.isReady.value
            }
        }
        val newsViewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        val authViewModel : AuthViewModel by viewModels()
        authViewModel.configureGoogleSignIn(this)

        setContent {
            BitBullTheme{
                Scaffold(
                    content = {
                        MyAppNavHost(
                            it,
                            newsViewModel = newsViewModel,
                            authViewModel = authViewModel
                        )
                    }
                )
            }
        }
    }
}
