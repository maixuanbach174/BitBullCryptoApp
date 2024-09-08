package com.bachphucngequy.bitbull.presentation.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.bachphucngequy.bitbull.SignInSignUp.CreateSuccessScreen
import com.bachphucngequy.bitbull.SignInSignUp.EnterCodePasswordScreen
import com.bachphucngequy.bitbull.SignInSignUp.EnterCodeScreen
import com.bachphucngequy.bitbull.SignInSignUp.ForgotPasswordScreen
import com.bachphucngequy.bitbull.SignInSignUp.ResetPasswordScreen
import com.bachphucngequy.bitbull.SignInSignUp.SignInPhoneScreen
import com.bachphucngequy.bitbull.SignInSignUp.SignInScreen
import com.bachphucngequy.bitbull.SignInSignUp.SignUpScreen
import com.bachphucngequy.bitbull.SignInSignUp.StartScreen
import com.bachphucngequy.bitbull.firebase.user
import com.bachphucngequy.bitbull.presentation.ui.components.home.Crypto
import com.bachphucngequy.bitbull.presentation.ui.components.home.sampleData
import com.bachphucngequy.bitbull.presentation.ui.screens.BuySellScreen
import com.bachphucngequy.bitbull.presentation.ui.screens.Deposit
import com.bachphucngequy.bitbull.presentation.ui.screens.HomeScreen
import com.bachphucngequy.bitbull.presentation.ui.screens.MarketDetailScreen
import com.bachphucngequy.bitbull.presentation.ui.screens.SavedInstanceKeys
import com.bachphucngequy.bitbull.presentation.ui.screens.Screen
import com.bachphucngequy.bitbull.presentation.ui.screens.SearchScreen
import com.bachphucngequy.bitbull.presentation.ui.screens.TradingSheetScreen
import com.bachphucngequy.bitbull.presentation.ui.screens.UserAccountScreen
import com.bachphucngequy.bitbull.presentation.ui.screens.ViewMarketScreen
import com.bachphucngequy.bitbull.presentation.ui.screens.Withdraw
import com.bachphucngequy.bitbull.presentation.viewmodel.AuthViewModel

@Composable
fun MyAppNavHost(innerPadding: PaddingValues,
                 coins: List<Coin>,
                 newsViewModel: NewsViewModel,
                 tweetsViewModel: TweetsViewModel,
                 postDetailViewModel: PostDetailViewModel,
                 profileViewModel: ProfileViewModel,
                 editProfileViewModel: EditProfileViewModel,
                 followsViewModel: FollowsViewModel,
                 newPostViewModel: NewPostViewModel,
                 authViewModel: AuthViewModel
) {
    val navController = rememberNavController()
    var symbol by remember { mutableStateOf("") }
    var coin by remember { mutableStateOf(Coin()) }
    var pairCoin by remember { mutableStateOf("") }
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
                onNavigateToSignIn = { navController.navigate(com.bachphucngequy.bitbull.Navigation.Screen.SignIn.route) },
                authViewModel = authViewModel
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onSearchClick = { navController.navigate(Screen.Search.route) },
                onNavigateToFeeds = { navController.navigate(Screen.Tweets.route) },
                onNavigateToDeposit = { navController.navigate(Screen.Deposit.route) },
                onNavigateToWithdraw = { navController.navigate(Screen.Withdraw.route) },
                onNavigateToUserAccount = { navController.navigate(Screen.UserAccount.route) }
            )
        }
        composable(Screen.ViewMarket.route) {
            ViewMarketScreen(onNavigateToMarketDetail = { navController.navigate(Screen.MarketDetail.route) })
        }
        composable(Screen.MarketDetail.route) {
            MarketDetailScreen(
                onNavigateToBuySell = {
                    navController.navigate(Screen.BuySell.route)
                },
                onBackClick = { navController.popBackStack() },
                marketName = symbol,
                coin = coin,
                pairCoin = pairCoin,
                newsViewModel = newsViewModel,
                onNavigateToNewsDetails = {article ->
                    navigateToNewsDetails(
                        navController = navController,
                        article = article
                    )
                },
                onFavouriteClick = {
                    val newIsFavourite = !coin.isFavourite
                    coin = coin.copy(
                        isFavourite = newIsFavourite
                    )
                    //TODO: Add to sampleData, need to change later
                    coins.find { it.id == coin.id }?.let {
                        it.isFavourite = newIsFavourite
                    }
                    if(newIsFavourite) {
                        sampleData.add(
                            Crypto(
                                icon = R.drawable.btc,
                                name = coin.name,
                                volume = "$464,70M",
                                price = "$59.749",
                                change = "-0,42%"
                            )
                        )
                    } else {
                       sampleData.find { it.name == coin.name }?.let {crypto ->
                           sampleData.remove(crypto)
                       }
                    }
                }
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
                    pairCoin = it
                    if (it.length == 8) {
                        symbol = it.substring(0, 4) + "/" + it.substring(4)
                        coin = coins.find { coin ->
                            it.substring(
                                0,
                                4
                            ) == if (coin.symbol.length >= 4) coin.symbol.substring(0, 4) else ""
                        } ?: Coin()
                    } else {
                        symbol = it.substring(0, 3) + "/" + it.substring(3)
                        coin = coins.find { coin ->
                            it.substring(0, 3) == coin.symbol.substring(0, 3)
                        } ?: Coin()
                    }
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
            navController.previousBackStackEntry?.savedStateHandle?.get<String?>(SavedInstanceKeys.USER_ID)
                ?.let {userId ->
                    EditProfileScreen(
                        editProfileUiState = editProfileViewModel.uiState,
                        onNameChange = editProfileViewModel::onNameChange,
                        bioTextFieldValue = editProfileViewModel.bioTextFieldValue,
                        onBioChange = editProfileViewModel::onBioChange,
                        onUploadButtonClick = { editProfileViewModel.uploadProfile() },
                        onUploadSucceed = {navController.navigateUp()},
                        fetchProfile = { editProfileViewModel.fetchProfile(userId) },
                        navigateUp = {navController.navigateUp()}
                    )
                }
        }

        composable(route = Screen.Following.route) {
            navController.previousBackStackEntry?.savedStateHandle?.get<String?>(SavedInstanceKeys.USER_ID)
                ?.let {userId ->
                    FollowsScreen(
                        uiState = followsViewModel.uiState,
                        fetchFollows = { followsViewModel.fetchFollows(
                            userId,
                            followsType = Constants.FOLLOWING_CODE
                        ) },
                        onItemClick = {},
                        navigateUp = {navController.navigateUp()}
                    )
                }
        }

        composable(route = Screen.Followers.route) {
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
            NewPostScreen(
                onUiAction = {newPostViewModel.onUiAction(it)},
                navigateUp = {navController.navigateUp()}
            )
        }
        composable(Screen.Deposit.route) {
            Deposit(number = user.usid, navController = navController)
        }
        composable(Screen.Withdraw.route) {
            Withdraw(onNavigateToWithdraw = { /*TODO*/ }, onNavigateToHistory = { /*TODO*/ }) {

            }
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





