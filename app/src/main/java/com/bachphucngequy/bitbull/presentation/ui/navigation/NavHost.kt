package com.bachphucngequy.bitbull.presentation.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bachphucngequy.bitbull.R
import com.bachphucngequy.bitbull.domain.model.Coin
import com.bachphucngequy.bitbull.news.NewsDetailsScreen
import com.bachphucngequy.bitbull.news.NewsViewModel
import com.bachphucngequy.bitbull.news.api.Article
import com.bachphucngequy.bitbull.tweets.TweetsScreen
import com.bachphucngequy.bitbull.tweets.TweetsViewModel
import com.bachphucngequy.bitbull.tweets.account.edit.EditProfileScreen
import com.bachphucngequy.bitbull.tweets.account.edit.EditProfileViewModel
import com.bachphucngequy.bitbull.tweets.account.follows.FollowsScreen
import com.bachphucngequy.bitbull.tweets.account.follows.FollowsViewModel
import com.bachphucngequy.bitbull.tweets.account.profile.ProfileScreen
import com.bachphucngequy.bitbull.tweets.account.profile.ProfileUiAction
import com.bachphucngequy.bitbull.tweets.account.profile.ProfileViewModel
import com.bachphucngequy.bitbull.tweets.common.util.Constants
import com.bachphucngequy.bitbull.tweets.data.Post
import com.bachphucngequy.bitbull.tweets.post.NewPostScreen
import com.bachphucngequy.bitbull.tweets.post.NewPostViewModel
import com.bachphucngequy.bitbull.tweets.post.PostDetailScreen
import com.bachphucngequy.bitbull.tweets.post.PostDetailViewModel
import com.bachphucngequy.bitbull.SignInSignUp.ChangeSuccessScreen
import com.bachphucngequy.bitbull.SignInSignUp.ChooseLanguageScreen
import com.bachphucngequy.bitbull.SignInSignUp.CreateSuccessScreen
import com.bachphucngequy.bitbull.SignInSignUp.EnterCodePasswordScreen
import com.bachphucngequy.bitbull.SignInSignUp.EnterCodeScreen
import com.bachphucngequy.bitbull.SignInSignUp.ForgotPasswordScreen
import com.bachphucngequy.bitbull.SignInSignUp.ResetPasswordScreen
import com.bachphucngequy.bitbull.SignInSignUp.SignInPhoneScreen
import com.bachphucngequy.bitbull.SignInSignUp.SignInScreen
import com.bachphucngequy.bitbull.SignInSignUp.SignUpScreen
import com.bachphucngequy.bitbull.SignInSignUp.StartScreen
import com.bachphucngequy.bitbull.data.entity.Crypto
import com.bachphucngequy.bitbull.firebase.user
import com.bachphucngequy.bitbull.history.HistoryScreen
import com.bachphucngequy.bitbull.history.HistoryViewModel
import com.bachphucngequy.bitbull.presentation.ui.screens.BuySellScreen
import com.bachphucngequy.bitbull.presentation.ui.screens.ChangeNicknameScreen
import com.bachphucngequy.bitbull.presentation.ui.screens.ChangePasswordAccountScreen
import com.bachphucngequy.bitbull.presentation.ui.screens.ChangeScreen
import com.bachphucngequy.bitbull.presentation.ui.screens.CompanyInfoScreen
import com.bachphucngequy.bitbull.presentation.ui.screens.CryptoWalletScreen
import com.bachphucngequy.bitbull.presentation.ui.screens.Deposit
import com.bachphucngequy.bitbull.presentation.ui.screens.FeedbackScreen
import com.bachphucngequy.bitbull.presentation.ui.screens.HomeScreen
import com.bachphucngequy.bitbull.presentation.ui.screens.MarketDetailScreen
import com.bachphucngequy.bitbull.presentation.ui.screens.PaymentScreen
import com.bachphucngequy.bitbull.presentation.ui.screens.PoliciesScreen
import com.bachphucngequy.bitbull.presentation.ui.screens.PrizeScreen
import com.bachphucngequy.bitbull.presentation.ui.screens.QuestionsScreen
import com.bachphucngequy.bitbull.presentation.ui.screens.RankScreen
import com.bachphucngequy.bitbull.presentation.ui.screens.ReferScreen
import com.bachphucngequy.bitbull.presentation.ui.screens.SavedInstanceKeys
import com.bachphucngequy.bitbull.presentation.ui.screens.Screen
import com.bachphucngequy.bitbull.presentation.ui.screens.SearchScreen
import com.bachphucngequy.bitbull.presentation.ui.screens.SupportCenterScreen
import com.bachphucngequy.bitbull.presentation.ui.screens.TradingSection
import com.bachphucngequy.bitbull.presentation.ui.screens.TradingSheetScreen
import com.bachphucngequy.bitbull.presentation.ui.screens.UserAccountScreen
import com.bachphucngequy.bitbull.presentation.ui.screens.ViewMarketScreen
import com.bachphucngequy.bitbull.presentation.ui.screens.Withdraw
import com.bachphucngequy.bitbull.presentation.viewmodel.AuthViewModel
import com.bachphucngequy.bitbull.presentation.viewmodel.MarketDetailViewModel

@Composable
fun MyAppNavHost(innerPadding: PaddingValues,
                 newsViewModel: NewsViewModel,
                 authViewModel: AuthViewModel
) {
    val navController = rememberNavController()
    var crypto by remember { mutableStateOf(Crypto.BITCOIN) }
    NavHost(navController = navController, startDestination = com.bachphucngequy.bitbull.Navigation.Screen.Start.route) {
        composable(com.bachphucngequy.bitbull.Navigation.Screen.Start.route) {
            StartScreen(
                onNavigateToSignIn={navController.navigate(com.bachphucngequy.bitbull.Navigation.Screen.SignIn.route)},
                onNavigateToSignUp={navController.navigate(com.bachphucngequy.bitbull.Navigation.Screen.SignUp.route)},
                authViewModel = authViewModel
            )
        }

        composable(com.bachphucngequy.bitbull.Navigation.Screen.SignIn.route){
            SignInScreen(
                onNavigateToForgotPassword = { navController.navigate(com.bachphucngequy.bitbull.Navigation.Screen.ForgotPassword.route) },
                onNavigateToSignInPhone = { navController.navigate(com.bachphucngequy.bitbull.Navigation.Screen.SignInPhone.route) },
                onNavigateToSignUp = { navController.navigate(com.bachphucngequy.bitbull.Navigation.Screen.SignUp.route) },
                onNavigateToViewMarket = { navController.navigate(Screen.Home.route) },
                authViewModel = authViewModel
            )
        }

        composable(com.bachphucngequy.bitbull.Navigation.Screen.SignInPhone.route){
            SignInPhoneScreen(
                onNavigateToSignIn = { navController.navigate(com.bachphucngequy.bitbull.Navigation.Screen.SignIn.route) },
                onNavigateToEnterCode = { otp, email ->
                    navController.navigate("${com.bachphucngequy.bitbull.Navigation.Screen.EnterCode.route}/$otp/$email") },
                authViewModel = authViewModel
            )
        }

        composable("${com.bachphucngequy.bitbull.Navigation.Screen.EnterCode.route}/{otp}/{email}") {
                backStackEntry ->
            val otp = backStackEntry.arguments?.getString("otp") ?: ""
            val email = backStackEntry.arguments?.getString("email") ?: ""

            EnterCodeScreen(
                onNavigateToSignInPhone = { navController.navigate(com.bachphucngequy.bitbull.Navigation.Screen.SignInPhone.route) },
                onNavigateToViewMarket = { navController.navigate(Screen.Home.route) },
                authViewModel = authViewModel,
                otp=otp,
                email = email
            )
        }

        composable(com.bachphucngequy.bitbull.Navigation.Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onNavigateToSignIn = { navController.navigate(com.bachphucngequy.bitbull.Navigation.Screen.SignIn.route) },
                onNavigateToEnterCodePassword = { otp, email ->
                    navController.navigate("${com.bachphucngequy.bitbull.Navigation.Screen.EnterCodePassword.route}/$otp/$email") }
            )
        }

        composable("${com.bachphucngequy.bitbull.Navigation.Screen.EnterCodePassword.route}/{otp}/{email}") {
                backStackEntry ->
            val otp = backStackEntry.arguments?.getString("otp") ?: ""
            val email = backStackEntry.arguments?.getString("email") ?: ""

            EnterCodePasswordScreen(
                onNavigateToForgotPassword = { navController.navigate(com.bachphucngequy.bitbull.Navigation.Screen.ForgotPassword.route) },
                onNavigateToResetPassword = {email2-> navController.navigate("${com.bachphucngequy.bitbull.Navigation.Screen.ResetPassword.route}/$email2") },
                otp=otp,
                email = email
            )
        }

        composable("${com.bachphucngequy.bitbull.Navigation.Screen.ResetPassword.route}/{email2}") {
                backStackEntry ->
            val email2 = backStackEntry.arguments?.getString("email2") ?: ""

            ResetPasswordScreen(
                onNavigateToSignIn = { navController.navigate(com.bachphucngequy.bitbull.Navigation.Screen.SignIn.route) },
                onNavigateToForgotPassword = { navController.navigate(com.bachphucngequy.bitbull.Navigation.Screen.ForgotPassword.route) },
                onNavigateToChangeSuccess = { navController.navigate(com.bachphucngequy.bitbull.Navigation.Screen.ChangeSuccess.route) },
                email = email2
            )
        }

        composable(com.bachphucngequy.bitbull.Navigation.Screen.ChangeSuccess.route) {
            ChangeSuccessScreen(
                onNavigateToSignIn = { navController.navigate(com.bachphucngequy.bitbull.Navigation.Screen.SignIn.route) }
            )
        }

        composable(com.bachphucngequy.bitbull.Navigation.Screen.ChooseLanguage.route) {
            ChooseLanguageScreen(
                onNavigateToUserAccount = { navController.navigate(Screen.UserAccount.route) })
        }

        composable(com.bachphucngequy.bitbull.Navigation.Screen.SignUp.route){
            SignUpScreen(
                onNavigateToSignIn = { navController.navigate(com.bachphucngequy.bitbull.Navigation.Screen.SignIn.route) },
                onNavigateToCreateSuccess = { navController.navigate(com.bachphucngequy.bitbull.Navigation.Screen.CreateSuccess.route) },
                authViewModel = authViewModel
            )
        }

        composable(com.bachphucngequy.bitbull.Navigation.Screen.CreateSuccess.route) {
            CreateSuccessScreen(
                onNavigateToSignIn = { navController.navigate(com.bachphucngequy.bitbull.Navigation.Screen.SignIn.route) },
                authViewModel = authViewModel
            )
        }

        composable(Screen.UserAccount.route) {
            UserAccountScreen(
                onNavigateToHome = { navController.navigate(Screen.Home.route) },
                onNavigateToCryptoWallet = { navController.navigate(Screen.CryptoWallet.route) },
                onNavigateToRank = { navController.navigate(Screen.Rank.route) },
                onNavigateToRefer = { navController.navigate(Screen.Refer.route) },
                onNavigateToDeposit = { navController.navigate(Screen.Deposit.route) },
                onNavigateToChange = { navController.navigate(Screen.Change.route) },
                onNavigateToPayment = { navController.navigate(Screen.Payment.route) },
                onNavigateToPrize = { navController.navigate(Screen.Prize.route) },
                onNavigateToQuestions = { navController.navigate(Screen.Questions.route) },
                onNavigateToPolicies = { navController.navigate(Screen.Policies.route) },
                onNavigateToSupportCenter = { navController.navigate(Screen.SupportCenter.route) },
                onNavigateToFeedback = { navController.navigate(Screen.Feedback.route) },
                onNavigateToCompanyInfo = { navController.navigate(Screen.CompanyInfo.route) },
                onNavigateToChangePasswordAccount = { navController.navigate(Screen.ChangePasswordAccount.route) },
                onNavigateToChangeNickname = { navController.navigate(Screen.ChangeNickname.route) },
                onNavigateToChooseLanguage = { navController.navigate(com.bachphucngequy.bitbull.Navigation.Screen.ChooseLanguage.route) },
                onNavigateToSignIn = { navController.navigate(com.bachphucngequy.bitbull.Navigation.Screen.SignIn.route) },
                onNavigateToHistory = { navController.navigate(Screen.History.route) },
                authViewModel = authViewModel
            )
        }

        composable(Screen.CryptoWallet.route) {
            CryptoWalletScreen(
                onNavigateToUserAccount = { navController.navigate(Screen.UserAccount.route) },
                onNavigateToDeposit = { navController.navigate(Screen.Deposit.route) },
                onNavigateToWithdraw = { navController.navigate(Screen.Withdraw.route) },
                onNavigateToHistory = { navController.navigate(Screen.History.route) },
                onNavigateToChange = { navController.navigate(Screen.Change.route) }
            )
        }

        composable(Screen.Rank.route) {
            RankScreen(
                onNavigateToUserAccount = { navController.navigate(Screen.UserAccount.route) },
                authViewModel = authViewModel
            )
        }

        composable(Screen.Refer.route) {
            ReferScreen(
                onNavigateToUserAccount = { navController.navigate(Screen.UserAccount.route) }
            )
        }

        composable(Screen.Change.route) {
            ChangeScreen(
                onNavigateToUserAccount = { navController.navigate(Screen.UserAccount.route) },
                onNavigateToHome = { navController.navigate(Screen.Home.route) }
            )
        }

        composable(Screen.Payment.route) {
            PaymentScreen(
                onNavigateToUserAccount = { navController.navigate(Screen.UserAccount.route) },
                onNavigateToPrize = { navController.navigate(Screen.Prize.route) }
            )
        }

        composable(Screen.Prize.route) {
            PrizeScreen(
                onNavigateToUserAccount = { navController.navigate(Screen.UserAccount.route) }
            )
        }

        composable(Screen.Questions.route) {
            QuestionsScreen(
                onNavigateToUserAccount = { navController.navigate(Screen.UserAccount.route) }
            )
        }

        composable(Screen.Policies.route) {
            PoliciesScreen(
                onNavigateToUserAccount = { navController.navigate(Screen.UserAccount.route) }
            )
        }

        composable(Screen.SupportCenter.route) {
            SupportCenterScreen(
                onNavigateToUserAccount = { navController.navigate(Screen.UserAccount.route) }
            )
        }

        composable(Screen.Feedback.route) {
            FeedbackScreen(
                onNavigateToUserAccount = { navController.navigate(Screen.UserAccount.route) }
            )
        }

        composable(Screen.CompanyInfo.route) {
            CompanyInfoScreen(
                onNavigateToUserAccount = { navController.navigate(Screen.UserAccount.route) }
            )
        }

        composable(Screen.ChangeNickname.route) {
            ChangeNicknameScreen(
                onNavigateToUserAccount = { navController.navigate(Screen.UserAccount.route) }
            )
        }

        composable(Screen.ChangePasswordAccount.route) {
            ChangePasswordAccountScreen(
                onNavigateToUserAccount = { navController.navigate(Screen.UserAccount.route) }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onSearchClick = { navController.navigate(Screen.Search.route) },
                onNavigateToFeeds = { navController.navigate(Screen.Tweets.route) },
                onNavigateToDeposit = { navController.navigate(Screen.Deposit.route) },
                onNavigateToWithdraw = { navController.navigate(Screen.Withdraw.route) },
                onNavigateToUserAccount = { navController.navigate(Screen.UserAccount.route) },
                onNavigateToDetail = { coin ->
                    navController.navigate(Screen.MarketDetail.route)
                    crypto = coin
                },
                onNavigateToTrade = {navController.navigate(Screen.Trade.route)},
                onNavigateToMarket = {navController.navigate(Screen.Search.route)}
            )
        }
        composable(Screen.Trade.route) {
            CryptoWalletScreen(
                onNavigateToUserAccount = {navController.popBackStack() },
                onNavigateToDeposit = { navController.navigate(Screen.Deposit.route) },
                onNavigateToWithdraw = { navController.navigate(Screen.Withdraw.route) },
                onNavigateToHistory = { navController.navigate(Screen.History.route) },
                onNavigateToChange = { navController.navigate(Screen.Change.route) }
            )
        }
        composable(Screen.ViewMarket.route) {
            SearchScreen(
                onCancelClick = { navController.popBackStack() },
                onSearchItemClick = {
                    navController.navigate(Screen.MarketDetail.route)
                    crypto = it
                }
            )
        }
        composable(Screen.MarketDetail.route) {
            val marketDetailViewModel = viewModel<MarketDetailViewModel>()
            MarketDetailScreen(
                onNavigateToBuySell = {
                    navController.navigate(Screen.BuySell.route)
                },
                onBackClick = { navController.popBackStack() },
                newsViewModel = newsViewModel,
                onNavigateToNewsDetails = {article ->
                    navigateToNewsDetails(
                        navController = navController,
                        article = article
                    )
                },
                onFavouriteClick = {updatedCrypto ->
                    crypto = updatedCrypto
                    if(crypto.isFavourite) {
                        marketDetailViewModel.insertFavouriteToFirebase(crypto.symbol)
                    } else {
                        marketDetailViewModel.deleteFavouriteToFirebase(crypto.symbol)
                    }
                },
                crypto = crypto
            )
        }
        composable(Screen.TradingSheet.route) {
            TradingSheetScreen( onNavigateToBuySellScreen = { navController.navigate(Screen.BuySell.route) })
        }
        composable(Screen.BuySell.route) {
            BuySellScreen( onNavigateToTradingSheet = { navController.navigate(Screen.TradingSheet.route) })
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onCancelClick = { navController.popBackStack() },
                onSearchItemClick = {
                    navController.navigate(Screen.MarketDetail.route)
                    crypto = it
                }
            )
        }

        composable(route = Screen.NewsDetails.route) {
            navController.previousBackStackEntry?.savedStateHandle?.get<String?>(SavedInstanceKeys.ARTICLE_URL)
                ?.let { articleUrl ->
                    NewsDetailsScreen(
                        articleUrl = articleUrl,
                        navigateUp = { navController.navigateUp() }
                    )
                }
        }

        composable(Screen.Tweets.route) {
            val tweetsViewModel = viewModel<TweetsViewModel>()
            TweetsScreen(
                postsUIState = tweetsViewModel.postsUiState,
                onUiAction = {tweetsViewModel.onUiAction(it)},
                onProfileNavigation = { userId ->
                    navigateToProfile(
                        navController = navController,
                        userId = userId
                    )
                },
                onPostDetailNavigation = { post ->
                    navigateToPostDetail(
                        navController = navController,
                        post = post
                    )
                },
                onNewPostNavigation = {
                    navController.navigate(Screen.NewPost.route)
                },
                navigateUp = {navController.navigateUp()}
            )
        }

        composable(route = Screen.PostDetail.route) {
            val postDetailViewModel = viewModel<PostDetailViewModel>()
            navController.previousBackStackEntry?.savedStateHandle?.get<String?>(SavedInstanceKeys.POST_ID)
                ?.let { postId ->
                    PostDetailScreen(
                        postUiState = postDetailViewModel.postUiState,
                        commentsUiState = postDetailViewModel.commentsUiState,
                        postId = postId,
                        onProfileNavigation = { userId ->
                            navigateToProfile(
                                navController = navController,
                                userId = userId
                            )
                        },
                        onUiAction = {postDetailViewModel.onUiAction(it)},
                        navigateUp = { navController.navigateUp() }
                    )
                }
        }

        // There are 2 directions to go to Profile screen:
        // 1. Forward from TweetsScreen (Home)
        // 2. Back from EditProfileScreen
        composable(route = Screen.Profile.route) {
            val profileViewModel = viewModel<ProfileViewModel>()
            navController.previousBackStackEntry?.savedStateHandle?.get<String?>(SavedInstanceKeys.USER_ID)
                ?.let {userId ->
                    ProfileScreen(
                        userInfoUiState = profileViewModel.userInfoUiState,
                        profilePostsUiState = profileViewModel.profilePostsUiState,
                        profileId = userId,
                        onUiAction = { profileViewModel.onUiAction(it)},
                        onButtonClick = {
                            profileViewModel.userInfoUiState.profile?.let { profile ->
                                if (profile.isOwnProfile) {
                                    navigateToEditProfile(navController, profile.id)
                                } else {
                                    profileViewModel.onUiAction(ProfileUiAction.FollowUserAction(profile = profile))
                                }
                            }
                        },
                        onFollowersScreenNavigation = {
                            navigateToFollows(navController = navController, userId = userId, followsType = Constants.FOLLOWERS_CODE)
                        },
                        onFollowingScreenNavigation = {
                            navigateToFollows(navController = navController, userId = userId, followsType = Constants.FOLLOWING_CODE)
                        },
                        onPostDetailNavigation = {post ->
                            navigateToPostDetail(navController = navController, post = post)
                        },
                        navigateUp = {navController.navigateUp()}
                    )
                }
        }

        composable(route = Screen.EditProfile.route) {
            val editProfileViewModel = viewModel<EditProfileViewModel>()
            navController.previousBackStackEntry?.savedStateHandle?.get<String?>(SavedInstanceKeys.USER_ID)
                ?.let {userId ->
                    EditProfileScreen(
                        editProfileUiState = editProfileViewModel.uiState,
                        onNameChange = editProfileViewModel::onNameChange,
                        onBioChange = editProfileViewModel::onBioChange,
                        onImageUrlChange = editProfileViewModel::onImageUrlChange,
                        onUploadButtonClick = { editProfileViewModel.uploadProfile() },
                        fetchProfile = { editProfileViewModel.fetchProfile(userId) },
                        navigateUp = {navController.navigateUp()}
                    )
                }
        }

        composable(route = Screen.Following.route) {
            val followsViewModel = viewModel<FollowsViewModel>()
            navController.previousBackStackEntry?.savedStateHandle?.get<String?>(SavedInstanceKeys.USER_ID)
                ?.let {userId ->
                    FollowsScreen(
                        uiState = followsViewModel.uiState,
                        fetchFollows = { followsViewModel.fetchFollows(
                            currentUserId = userId,
                            followsType = Constants.FOLLOWING_CODE
                        ) },
                        onItemClick = {},
                        navigateUp = {navController.navigateUp()}
                    )
                }
        }

        composable(route = Screen.Followers.route) {
            val followsViewModel = viewModel<FollowsViewModel>()
            navController.previousBackStackEntry?.savedStateHandle?.get<String?>(SavedInstanceKeys.USER_ID)
                ?.let {userId ->
                    FollowsScreen(
                        uiState = followsViewModel.uiState,
                        fetchFollows = { followsViewModel.fetchFollows(
                            userId,
                            followsType = Constants.FOLLOWERS_CODE
                        ) },
                        onItemClick = {followsUserId ->
                            navigateToProfile(
                                navController = navController,
                                userId = followsUserId
                            )
                        },
                        navigateUp = {navController.navigateUp()}
                    )
                }
        }

        composable(route = Screen.NewPost.route) {
            val newPostViewModel = viewModel<NewPostViewModel>()
            NewPostScreen(
                onUiAction = {newPostViewModel.onUiAction(it)},
                navigateUp = {navController.navigateUp()}
            )
        }
        composable(Screen.Deposit.route) {
            Deposit(number = user.usid, navController = navController)
        }
        composable(Screen.Withdraw.route) {
            Withdraw(onNavigateToHelp = { /*TODO*/ }, onNavigateToHistory = { /*TODO*/ }, navController = navController)
        }
        composable(Screen.History.route) {
            val historyViewModel = viewModel<HistoryViewModel>()
            HistoryScreen(
                uiState = historyViewModel.uiState,
                onUiAction = { historyViewModel.onUiAction(it) },
                navigateUp = {navController.navigateUp()}
            )
        }
    }
}

private fun navigateToNewsDetails(navController: NavController, article: Article){
    navController.currentBackStackEntry?.savedStateHandle?.set(SavedInstanceKeys.ARTICLE_URL, article.url)
    navController.navigate(
        route = Screen.NewsDetails.route
    )
}

private fun navigateToPostDetail(navController: NavController, post: Post){
    navController.currentBackStackEntry?.savedStateHandle?.set(SavedInstanceKeys.POST_ID, post.id)
    navController.navigate(
        route = Screen.PostDetail.route
    )
}

private fun navigateToProfile(navController: NavController, userId: String) {
    navController.currentBackStackEntry?.savedStateHandle?.set(SavedInstanceKeys.USER_ID, userId)
    navController.navigate(
        route = Screen.Profile.route
    )
}

private fun navigateToEditProfile(navController: NavController, userId: String) {
    navController.currentBackStackEntry?.savedStateHandle?.set(SavedInstanceKeys.USER_ID, userId)
    navController.navigate(
        route = Screen.EditProfile.route
    )
}

private fun navigateToFollows(navController: NavController, userId: String, followsType: Int) {
    navController.currentBackStackEntry?.savedStateHandle?.set(SavedInstanceKeys.USER_ID, userId)
    if(followsType == Constants.FOLLOWING_CODE) {
        navController.navigate(
            route = Screen.Following.route
        )
    }
    else if(followsType == Constants.FOLLOWERS_CODE) {
        navController.navigate(
            route = Screen.Followers.route
        )
    }
}





