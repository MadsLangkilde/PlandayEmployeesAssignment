package com.planday.employeesassignment.apiauthentication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.planday.employeesassignment.data.apis.plandayauthenticationapi.AuthenticationcationApi
import com.planday.employeesassignment.data.apis.plandayauthenticationapi.models.OpenApiAuthentication
import com.planday.employeesassignment.data.repositories.ApiAuthenticationRepository
import com.planday.employeesassignment.data.repositories.ApiAuthenticationRepository.AuthenticationProgressStates.*
import com.planday.employeesassignment.rules.RxImmediateSchedulerRule
import io.reactivex.Maybe
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class AuthenticationRepositoryTests {
    @Rule
    @JvmField
    var rule = InstantTaskExecutorRule()

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    @Test
    fun testAuthenticateStateNotStarted() {
        val authenticationSuccess:              AuthenticationcationApi         = MockedAuthenticationApiImpl(Single.just(Response.success(OpenApiAuthentication())))
        val apiAuthenticationRepoSuccess:       ApiAuthenticationRepository     = ApiAuthenticationRepository()
        apiAuthenticationRepoSuccess.init(authenticationSuccess, "", "")

        val currentAutenticationState = apiAuthenticationRepoSuccess.authenticationProgressState.get()
        Assert.assertEquals(NotStarted, currentAutenticationState)
    }

    @Test
    fun testAuthenticateStateSuccess() {
        val authenticationSuccess           = MockedAuthenticationApiImpl(Single.just(Response.success(OpenApiAuthentication())))
        val apiAuthenticationRepoSuccess    = ApiAuthenticationRepository()
        apiAuthenticationRepoSuccess.init(authenticationSuccess, "", "")

        apiAuthenticationRepoSuccess.authenticate()
        val currentAutenticationState       = apiAuthenticationRepoSuccess.authenticationProgressState.get()
        Assert.assertEquals(Success, currentAutenticationState)
    }

    @Test
    fun testAuthenticateStateFailed() {
        val failedResponse                                                      = Response.error<OpenApiAuthentication>(501, ResponseBody.create(MediaType.parse("application/json"), "{}"))
        val authenticationNotSuccesful:         AuthenticationcationApi         = MockedAuthenticationApiImpl(Single.just(failedResponse))
        val apiAuthenticationRepoNotSuccesful:  ApiAuthenticationRepository     = ApiAuthenticationRepository()
        apiAuthenticationRepoNotSuccesful.init(authenticationNotSuccesful, "", "")

        apiAuthenticationRepoNotSuccesful.authenticate()
        val currentAutenticationState = apiAuthenticationRepoNotSuccesful.authenticationProgressState.get()
        Assert.assertEquals(Failed, currentAutenticationState)
    }

    class MockedAuthenticationApiImpl(val response: Single<Response<OpenApiAuthentication>>) : AuthenticationcationApi("") {
        override fun authenticate(clientId: String, refreshToken: String): Single<Response<OpenApiAuthentication>> {
            return response
        }
    }
}

