package com.bachphucngequy.bitbull.presentation.ui.screens

sealed class Screen(val route: String) {
    object Home : Screen("HomeScreen")
    object tradingsection : Screen("tradingsection")
    object Withdraw : Screen("withdraw")
    object Deposit : Screen("deposit") // New route for Deposit
    object Trade : Screen("trade")
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
    object History: Screen("HistoryScreen")
    object ChangeNickname : Screen("ChangeNicknameScreen")
    object ChangePasswordAccount : Screen("ChangePasswordAccountScreen")
    object CompanyInfo: Screen("CompanyInfoScreen")
    object Feedback: Screen("FeedbackScreen")
    object SupportCenter: Screen("SupportCenterScreen")
    object Policies: Screen("PoliciesScreen")
    object Questions: Screen("QuestionsScreen")
    object Prize: Screen("PrizeScreen")
    object Payment: Screen("PaymentScreen")
    object Change: Screen("ChangeScreen")
    object Refer: Screen("ReferScreen")
    object Rank: Screen("RankScreen")
}

object SavedInstanceKeys {
    const val ARTICLE_URL = "articleUrl"
    const val POST_ID = "postId"
    const val USER_ID = "userId"
}