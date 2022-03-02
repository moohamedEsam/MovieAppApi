package com.example.movieappapi.composables

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.movieappapi.domain.utils.SemanticContentDescription
import com.example.movieappapi.ui.theme.MovieAppApiTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test


class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun loginWithBlankCredentials() {
        composeTestRule.setContent {
            MovieAppApiTheme {
                MainScreen()
            }
        }
        val usernameTextField =
            composeTestRule.onNodeWithContentDescription(SemanticContentDescription.LOGIN_SCREEN_USERNAME_TEXT_FIELD)

        val passwordTextField =
            composeTestRule.onNodeWithContentDescription(SemanticContentDescription.LOGIN_SCREEN_PASSWORD_TEXT_FIELD)

        usernameTextField.performTextInput("          ")
        passwordTextField.performTextInput("          ")

        composeTestRule.onNodeWithText("login").performClick()
        composeTestRule.onNodeWithContentDescription(SemanticContentDescription.ERROR_RESOURCE_TEXT)
            .assertExists()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loginWithValidButWrongCredentials() = runBlockingTest {
        composeTestRule.setContent {
            MovieAppApiTheme {
                MainScreen()
            }
        }
        val usernameTextField =
            composeTestRule.onNodeWithContentDescription(SemanticContentDescription.LOGIN_SCREEN_USERNAME_TEXT_FIELD)

        val passwordTextField =
            composeTestRule.onNodeWithContentDescription(SemanticContentDescription.LOGIN_SCREEN_PASSWORD_TEXT_FIELD)

        usernameTextField.performTextInput("mohamedEsam")
        passwordTextField.performTextInput("3264607123")

        composeTestRule.onNodeWithText("login").performClick()

        composeTestRule.onNodeWithContentDescription(SemanticContentDescription.ERROR_RESOURCE_TEXT)
            .assertExists()
    }

    @Test
    fun loginWithValidCredentials() {
        composeTestRule.setContent {
            MovieAppApiTheme {
                MainScreen()
            }
        }
        val usernameTextField =
            composeTestRule.onNodeWithContentDescription(SemanticContentDescription.LOGIN_SCREEN_USERNAME_TEXT_FIELD)

        val passwordTextField =
            composeTestRule.onNodeWithContentDescription(SemanticContentDescription.LOGIN_SCREEN_PASSWORD_TEXT_FIELD)

        usernameTextField.performTextInput("mohamedEsam")
        passwordTextField.performTextInput("0Epn4HAjP176")

        composeTestRule.onNodeWithText("login").performClick()

        composeTestRule.onNodeWithContentDescription(SemanticContentDescription.ERROR_RESOURCE_TEXT)
            .assertDoesNotExist()
    }
}