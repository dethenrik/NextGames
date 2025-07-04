package dk.nextgames.app.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import dk.nextgames.app.ui.games.GamesPage
import dk.nextgames.app.ui.gameinterface.GameInterfacePage
import dk.nextgames.app.ui.leaderboard.LeaderboardPage
import dk.nextgames.app.ui.login.LoginScreen
import dk.nextgames.app.ui.navigation.NAVIGATIONPAGE
import dk.nextgames.app.ui.settings.SettingsPage
import dk.nextgames.app.ui.userpage.UserPage

object Routes {
    const val LOGIN           = "login"
    const val NAVIGATIONPAGE  = "NavigationPage"
    const val USER            = "user"
    const val SETTINGS        = "settings"
    const val GAMES           = "games"
    const val GAME            = "game"       // kalder vi stadig "game"
    const val LEADERBOARD     = "leaderboard"
}

@Composable
fun NextGamesNav() {
    val nav = rememberNavController()

    NavHost(
        navController = nav,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoggedIn = {
                    nav.navigate(Routes.NAVIGATIONPAGE) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.NAVIGATIONPAGE) {
            NAVIGATIONPAGE(
                onNavigate = nav::navigate,
                onLogout   = {
                    nav.navigate(Routes.LOGIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.USER) {
            UserPage(onBack = { nav.popBackStack() })
        }
        composable(Routes.SETTINGS) {
            SettingsPage(onBack = { nav.popBackStack() })
        }

        // —— Her kalder vi nu med den fulde netlifyUrl, ikke blot id ——
        composable(Routes.GAMES) {
            GamesPage(
                onBack        = { nav.popBackStack() },
                onGameClicked = { url ->
                    // URL kan indeholde "/" så vi URL-encoder den
                    val encoded = Uri.encode(url)
                    nav.navigate("${Routes.GAME}/$encoded")
                }
            )
        }

        composable(
            route = "${Routes.GAME}/{gameUrl}",
            arguments = listOf(navArgument("gameUrl") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val encodedUrl = backStackEntry.arguments?.getString("gameUrl") ?: ""
            GameInterfacePage(
                encodedUrl = encodedUrl,
                onBack     = { nav.popBackStack() }
            )
        }

        composable(Routes.LEADERBOARD) {
            LeaderboardPage(onBack = { nav.popBackStack() })
        }
    }
}
