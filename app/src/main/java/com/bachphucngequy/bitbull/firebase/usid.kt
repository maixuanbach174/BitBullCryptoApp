package com.bachphucngequy.bitbull.firebase

import com.google.firebase.auth.FirebaseAuth

object user {
    var usid: String = "7TMUTzl854XqDhaNfKcBl8mmwHo2"
    fun fetchUserId() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            usid = currentUser.uid
        } else {
            // Handle user not being logged in, if necessary
            usid = "User not logged in"
        }
    }
}