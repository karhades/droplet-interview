package com.karipidis.droplet.presentation.welcome

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.FirebaseUiException
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.auth.util.ExtraConstants
import com.karipidis.droplet.R
import com.karipidis.droplet.data.FakeUserRepository
import com.karipidis.droplet.di.welcomeModule
import com.karipidis.droplet.domain.repositories.UserRepository
import com.karipidis.droplet.presentation.details.DetailsActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get

@RunWith(AndroidJUnit4::class)
@MediumTest
class WelcomeActivityTest : KoinTest {

    @get:Rule
    val intentsTestRule = IntentsTestRule(WelcomeActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun before() {
        stopKoin() // Stop koin since application starts it beforehand
        startKoin {
            androidContext(context)
            modules(welcomeModule, module(override = true) {
                single<UserRepository> { FakeUserRepository() }
            })
        }
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun showsErrorWhenLoginFails() {
        intentsTestRule.launchActivity(null)

        // Stub intent
        val errorCode = ErrorCodes.UNKNOWN_ERROR
        val idpResponse = IdpResponse.from(FirebaseUiException(errorCode))
        val intent = Intent().apply { putExtra(ExtraConstants.IDP_RESPONSE, idpResponse) }
        val intentResult = Instrumentation.ActivityResult(Activity.RESULT_CANCELED, intent)
        Intents.intending(anyIntent()).respondWith(intentResult)

        onView(withId(R.id.continue_button)).perform(click())

        onView(withText(R.string.login_failed)).check(matches(isDisplayed()))
    }

    @Test
    fun startsDetailsWhenUserIdIsNotNull() {
        val fakeUserRepository = get<UserRepository>() as FakeUserRepository
        val userId = "user_id"
        fakeUserRepository._userId = userId

        intentsTestRule.launchActivity(null)

        // Stub intent
        val intentResult = Instrumentation.ActivityResult(Activity.RESULT_OK, Intent())
        Intents.intending(anyIntent()).respondWith(intentResult)

        onView(withId(R.id.continue_button)).perform(click())

        intended(hasExtra(DetailsActivity.EXTRA_USER_ID, userId))
    }
}