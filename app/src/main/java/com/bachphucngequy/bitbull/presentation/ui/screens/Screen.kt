package com.bachphucngequy.bitbull.presentation.ui.screens

sealed class Screen(val route: String) {
    object Home : Screen("HomeScreen")
    object tradingsection : Screen("tradingsection")
    object Withdraw : Screen("withdraw")
    object Deposit : Screen("deposit") // New route for Deposit
    object ViewMarket : Screen("ViewMarketsScreen")
    object MarketDetail : Screen("MarketDetailScreen")
    object BuySell : Screen("BuySellScreen")
    object TradingSheet : Screen("TradingSheetScreen")
    object Search : Screen("SearchScreen")
    object NewsDetails : Screen("NewsDetailsScreen")
    object PostDetail : Screen("PostDetailScreen")
    object Tweets : Screen("TweetsScreen")
    object Profile : Screen("ProfileScreen")
    object EditProfile : Screen("EditProfileScreen")
    object Following : Screen("FollowingScreen")
    object Followers : Screen("FollowersScreen")
    object NewPost : Screen("NewPostScreen")
    object UserAccount : Screen("UserAccountScreen")
    object CryptoWallet : Screen("CryptoWalletScreen")
}

object SavedInstanceKeys {
    const val ARTICLE_URL = "articleUrl"
    const val POST_ID = "postId"
    const val USER_ID = "userId"
}