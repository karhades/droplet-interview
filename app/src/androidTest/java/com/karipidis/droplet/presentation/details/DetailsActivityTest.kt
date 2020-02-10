package com.karipidis.droplet.presentation.details

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.karipidis.droplet.R
import com.karipidis.droplet.data.FakeUserRepository
import com.karipidis.droplet.di.detailsModule
import com.karipidis.droplet.di.userRepositoryModule
import com.karipidis.droplet.domain.entities.User
import com.karipidis.droplet.domain.repositories.UserRepository
import com.karipidis.droplet.presentation.welcome.WelcomeActivity
import org.hamcrest.Matchers.not
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
import java.io.BufferedReader

@RunWith(AndroidJUnit4::class)
@MediumTest
class DetailsActivityTest : KoinTest {

    @get:Rule
    val intentsTestRule = IntentsTestRule(DetailsActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun before() {
        stopKoin() // Stop koin since application starts it beforehand
        startKoin {
            androidContext(context)
            modules(detailsModule, userRepositoryModule, module(override = true) {
                single<UserRepository> { FakeUserRepository() }
            })
        }
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun showsError_whenGetUserFails() {
        val fakeUserRepository = get<UserRepository>() as FakeUserRepository
        fakeUserRepository.throwGetUserError = true

        val intent = DetailsActivity.newIntent(context, "user_id")
        intentsTestRule.launchActivity(intent)

        onView(withText(R.string.error_get_user)).check(matches(isDisplayed()))
    }

    @Test
    fun showsEmpty_DetailsWhenUserDoesNotExist() {
        val intent = DetailsActivity.newIntent(context, "user_id")
        intentsTestRule.launchActivity(intent)

        onView(withId(R.id.first_name_edit_text)).check(matches(withText("")))
        onView(withId(R.id.last_name_edit_text)).check(matches(withText("")))
        onView(withId(R.id.email_edit_text)).check(matches(withText("")))
    }

    @Test
    fun showsUser_DetailsWhenUserExists() {
        val avatar = getBase64StringFromAssets()
        val userId = "user_id"
        val firstName = "Firstname"
        val lastName = "Lastname"
        val email = "firstname.lastname@gmail.com"
        val user = User(
            id = userId,
            avatar = avatar,
            firstName = firstName,
            lastName = lastName,
            email = email
        )
        val fakeUserRepository = get<UserRepository>() as FakeUserRepository
        fakeUserRepository.user = user

        val intent = DetailsActivity.newIntent(context, userId)
        intentsTestRule.launchActivity(intent)

        onView(withText(firstName)).check(matches(isDisplayed()))
        onView(withText(lastName)).check(matches(isDisplayed()))
        onView(withText(email)).check(matches(isDisplayed()))
    }

    @Test
    fun showsError_WhenUpdateUserFails() {
        val fakeUserRepository = get<UserRepository>() as FakeUserRepository
        fakeUserRepository.throwUpdateUserError = true

        val intent = DetailsActivity.newIntent(context, "user_id")
        intentsTestRule.launchActivity(intent)

        onView(withId(R.id.first_name_edit_text))
            .perform(typeText("firstName"), closeSoftKeyboard())
        onView(withId(R.id.last_name_edit_text))
            .perform(typeText("lastName"), closeSoftKeyboard())
        onView(withId(R.id.email_edit_text))
            .perform(typeText("email@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.save_button)).perform(click())

        onView(withText(R.string.error_update_user)).check(matches(isDisplayed()))
    }

    @Test
    fun disablesSaveButton_whenUpdateUserIsInProgress() {
        val fakeUserRepository = get<UserRepository>() as FakeUserRepository
        fakeUserRepository.delayUpdateUser = true

        val intent = DetailsActivity.newIntent(context, "user_id")
        intentsTestRule.launchActivity(intent)

        onView(withId(R.id.first_name_edit_text))
            .perform(typeText("firstName"), closeSoftKeyboard())
        onView(withId(R.id.last_name_edit_text))
            .perform(typeText("lastName"), closeSoftKeyboard())
        onView(withId(R.id.email_edit_text))
            .perform(typeText("email@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.save_button)).perform(click())

        onView(withId(R.id.save_button)).check(matches(not(isEnabled())))
    }

    @Test
    fun startsWelcomeScreen_whenLogoutIsPressed() {
        val intent = DetailsActivity.newIntent(context, "user_id")
        intentsTestRule.launchActivity(intent)

        // Stub intent
        val intentResult = Instrumentation.ActivityResult(Activity.RESULT_OK, intent)
        Intents.intending(anyIntent()).respondWith(intentResult)
        onView(withId(R.id.logout_button)).perform(click())

        intended(IntentMatchers.hasComponent(WelcomeActivity::class.java.name))
    }

    private fun getBase64StringFromAssets(): String {
        val assetManager = context.assets
        val inputStream = assetManager.open("base64.txt")
        return inputStream.bufferedReader()
            .use(BufferedReader::readText)
            .replace("\r", "")
    }
}