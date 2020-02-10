package com.karipidis.droplet.data.local

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class UserDaoTest {

    private lateinit var userDatabase: UserDatabase

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        userDatabase = Room.inMemoryDatabaseBuilder(context, UserDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun after() {
        userDatabase.close()
    }

    @Test
    fun getUser_returnsNull_whenUserIdDoesNotExist() = runBlocking {
        val userId = "user_id"

        val localUser = userDatabase.userDao()
            .getUser(userId)

        assertThat(localUser).isNull()
    }

    @Test
    fun getUser_returnsSame_whenUserIsInserted() = runBlocking {
        val userId = "user_id"
        val localUser = LocalUser(
            id = userId,
            avatar = "avatar",
            firstName = "firstName",
            lastName = "lastName",
            email = "email"
        )

        userDatabase.userDao()
            .addUser(localUser)

        val result = userDatabase.userDao()
            .getUser(userId)

        assertThat(result).isEqualTo(localUser)
    }
}