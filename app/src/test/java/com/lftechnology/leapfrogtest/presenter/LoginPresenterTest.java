package com.lftechnology.leapfrogtest.presenter;

import com.lftechnology.leapfrogtest.UseCaseExecutor;
import com.lftechnology.leapfrogtest.data.MockAuthenticationRepository;
import com.lftechnology.leapfrogtest.data.MockPreferencesRepository;
import com.lftechnology.leapfrogtest.domain.usecase.LoginWithEmail;
import com.lftechnology.leapfrogtest.login.LoginPresenter;
import com.lftechnology.leapfrogtest.login.LoginView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest {

    private LoginPresenter loginPresenter;

    @Mock
    private LoginView loginView;

    @Before
    public void setup() {
        Scheduler scheduler = Schedulers.single();
        LoginWithEmail loginWithEmail = new LoginWithEmail(new MockAuthenticationRepository(), new MockPreferencesRepository());
        loginPresenter = new LoginPresenter(new UseCaseExecutor(scheduler), loginWithEmail);
        loginPresenter.attachView(loginView);
    }

    @Test
    public void performLogin_invalidCredentials_shouldThrowException() {
        String email = "123";
        String password = "Test";

        loginPresenter.performLogin(email, password);

        verify(loginView).showLoginError(anyString());
        verifyNoMoreInteractions(loginView);
    }

    @Test
    public void performLogin_successfulCase_shouldOpenLandingPage() {
        String email = "ius.maharjan@gmail.com";
        String password = "Test123";

        loginPresenter.performLogin(email, password);

        verify(loginView).navigateToLandingPage();
        verifyNoMoreInteractions(loginView);
    }

    @After
    public void tearDown() {
        loginPresenter.detachView();
    }

}
