package dk.nextgames.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import org.json.JSONObject

class GameInterfaceViewModel : ViewModel() {

    /** ---------- 1) hvem er logget ind? ---------- */
    private val user = Firebase.auth.currentUser

    // JSON sendes til spillet – kan udvides efter behov
    val userJson: String = JSONObject().apply {
        put("uid",   user?.uid ?: "")
        put("name",  user?.displayName ?: "")
        put("email", user?.email ?: "")
    }.toString()

    /** ---------- 2) high-score upload ---------- */
    fun submitHighScore(
        score:     Int,
        gameId:    String,
        gameTitle: String
    ) = viewModelScope.launch {
        val u = user ?: return@launch          // ingen bruger → intet upload
        val ref = Firebase.database.reference
            .child("highscores")
            .push()                            // ny node

        val data = mapOf(
            "uid"         to u.uid,
            "displayName" to (u.displayName ?: ""),
            "email"       to (u.email ?: ""),
            "games_Id"    to gameId,
            "gameTitle"   to gameTitle,
            "score"       to score,
            "timestamp"   to ServerValue.TIMESTAMP
        )
        ref.setValue(data)
    }
}
