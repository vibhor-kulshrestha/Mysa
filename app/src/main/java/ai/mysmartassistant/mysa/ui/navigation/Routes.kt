package ai.mysmartassistant.mysa.ui.navigation

import ai.mysmartassistant.mysa.domain.auth.LoginMedium

sealed class Route(val route: String) {

    object Auth : Route("auth")
    object Home : Route("home")
    object OnBoard : Route("onBoard")

    object Login : Route("auth/login")
    object Otp : Route("auth/otp/{phone}/{medium}") {
        fun createRoute(phone: String,medium: LoginMedium): String =
            "auth/otp/$phone/${medium.name}"
    }

    object ProfileDetails : Route("onBoard/profile")

    object Chat : Route("home/chat")
    object Camera : Route("home/camera")
    object History : Route("home/profile")
    object TodayTask : Route("home/todayTask")
    object Reminder : Route("home/reminder")
    object Meeting : Route("home/meeting")
    object Document : Route("home/document")
    object Notes : Route("home/notes")
    object Calender : Route("home/calender")

}