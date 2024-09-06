package com.bachphucngequy.bitbull.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bachphucngequy.bitbull.presentation.viewmodel.AuthViewModel
import com.bachphucngequy.bitbull.SignInSignUp.ChangeSuccessScreen
import com.bachphucngequy.bitbull.SignInSignUp.CreateSuccessScreen
import com.bachphucngequy.bitbull.SignInSignUp.EnterCodePasswordScreen
import com.bachphucngequy.bitbull.SignInSignUp.EnterCodeScreen
import com.bachphucngequy.bitbull.SignInSignUp.ForgotPasswordScreen
import com.bachphucngequy.bitbull.SignInSignUp.ResetPasswordScreen
import com.bachphucngequy.bitbull.SignInSignUp.SignInPhoneScreen
import com.bachphucngequy.bitbull.SignInSignUp.SignInScreen
import com.bachphucngequy.bitbull.SignInSignUp.SignUpScreen
import com.bachphucngequy.bitbull.SignInSignUp.StartScreen

@Composable
fun MyAppNavHost(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Start.route) {
        composable(Screen.Start.route) {
            StartScreen(
                onNavigateToSignIn={navController.navigate(Screen.SignIn.route)},
                onNavigateToSignUp={navController.navigate(Screen.SignUp.route)},
                authViewModel = authViewModel
            )
        }

        composable(Screen.SignIn.route){
            SignInScreen(
                onNavigateToForgotPassword = { navController.navigate(Screen.ForgotPassword.route) },
                onNavigateToSignInPhone = { navController.navigate(Screen.SignInPhone.route) },
                onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) },
                onNavigateToViewMarket = { navController.navigate(Screen.ViewMarket.route) },
                authViewModel = authViewModel
                )
        }

        composable(Screen.SignInPhone.route){
            SignInPhoneScreen(
                onNavigateToSignIn = { navController.navigate(Screen.SignIn.route) },
                onNavigateToEnterCode = { otp, email ->
                    navController.navigate("${Screen.EnterCode.route}/$otp/$email") },
                authViewModel = authViewModel
            )
        }

        composable("${Screen.EnterCode.route}/{otp}/{email}") {
                backStackEntry ->
            val otp = backStackEntry.arguments?.getString("otp") ?: ""
            val email = backStackEntry.arguments?.getString("email") ?: ""

            EnterCodeScreen(
                onNavigateToSignInPhone = { navController.navigate(Screen.SignInPhone.route) },
                onNavigateToViewMarket = { navController.navigate(Screen.ViewMarket.route) },
                authViewModel = authViewModel,
                otp=otp,
                email = email
            )
        }

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onNavigateToSignIn = { navController.navigate(Screen.SignIn.route) },
                onNavigateToEnterCodePassword = { otp, email ->
                    navController.navigate("${Screen.EnterCodePassword.route}/$otp/$email") }
            )
        }

        composable("${Screen.EnterCodePassword.route}/{otp}/{email}") {
                backStackEntry ->
            val otp = backStackEntry.arguments?.getString("otp") ?: ""
            val email = backStackEntry.arguments?.getString("email") ?: ""

            EnterCodePasswordScreen(
                onNavigateToForgotPassword = { navController.navigate(Screen.ForgotPassword.route) },
                onNavigateToResetPassword = {email2-> navController.navigate("${Screen.ResetPassword.route}/$email2") },
                otp=otp,
                email = email
            )
        }

        composable("${Screen.ResetPassword.route}/{email2}") {
                backStackEntry ->
            val email2 = backStackEntry.arguments?.getString("email2") ?: ""

            ResetPasswordScreen(
                onNavigateToSignIn = { navController.navigate(Screen.SignIn.route) },
                onNavigateToForgotPassword = { navController.navigate(Screen.ForgotPassword.route) },
                onNavigateToChangeSuccess = { navController.navigate(Screen.ChangeSuccess.route) },
                email = email2
            )
        }

        composable(Screen.ChangeSuccess.route) {
            ChangeSuccessScreen(
                onNavigateToSignIn = { navController.navigate(Screen.SignIn.route) }
            )
        }

        composable(Screen.SignUp.route){
            SignUpScreen(
                onNavigateToSignIn = { navController.navigate(Screen.SignIn.route) },
                onNavigateToCreateSuccess = { navController.navigate(Screen.CreateSuccess.route) },
                authViewModel = authViewModel
            )
        }

        composable(Screen.CreateSuccess.route) {
            CreateSuccessScreen(
                onNavigateToSignIn = { navController.navigate(Screen.SignIn.route) },
                authViewModel = authViewModel
            )
        }
    }
}





