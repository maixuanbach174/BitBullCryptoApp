package com.bachphucngequy.bitbull.Navigation

sealed class Screen(val route: String) {
    object ViewMarket : Screen("ViewMarketsScreen")
    object MarketDetail : Screen("MarketDetailScreen")
    object BuySell : Screen("BuySellScreen")
    object TradingSheet : Screen("TradingSheetScreen")
    object Start : Screen("StartScreen")
    object SignIn : Screen("SignInScreen")
    object SignInPhone : Screen("SignInPhoneScreen")
    object EnterCode : Screen("EnterCodeScreen")
    object ForgotPassword : Screen("ForgotPasswordScreen")
    object EnterCodePassword : Screen("EnterCodePasswordScreen")
    object ResetPassword : Screen("ResetPasswordScreen")
    object ChangeSuccess : Screen("ChangeSuccessScreen")
    object SignUp : Screen("SignUpScreen")
    object CreateSuccess : Screen("CreateSuccessScreen")
}