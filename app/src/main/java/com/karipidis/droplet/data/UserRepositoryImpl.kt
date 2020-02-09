package com.karipidis.droplet.data

import com.google.firebase.auth.FirebaseAuth
import com.karipidis.droplet.domain.repositories.UserRepository

class UserRepositoryImpl(private val firebaseAuth: FirebaseAuth) : UserRepository {

    override val userId: String?
        get() = firebaseAuth.currentUser?.uid
}