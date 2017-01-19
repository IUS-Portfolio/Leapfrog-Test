package com.lftechnology.leapfrogtest.presenter;

import com.lftechnology.leapfrogtest.data.MockAuthenticationRepository;
import com.lftechnology.leapfrogtest.data.MockPreferencesRepository;
import com.lftechnology.leapfrogtest.domain.usecase.LoginWithEmail;
import com.lftechnology.leapfrogtest.login.LoginPresenter;
import com.lftechnology.leapfrogtest.login.LoginView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest {

    private LoginPresenter loginPresenter;

    private LoginWithEmail loginWithEmail;

    private Scheduler scheduler;

    @Mock
    private LoginView loginView;

    @Before
    public void setup() {
        scheduler = Schedulers.single();
        loginWithEmail = new LoginWithEmail(new MockAuthenticationRepository(), new MockPreferencesRepository());
        loginPresenter = new LoginPresenter(loginWithEmail, scheduler);
    }

    @Test
    public void performLogin_successfulCase_shouldOpenLandingPage() {
        String email = "ius.maharjan@gmail.com";
        String password = "test";
        loginPresenter.initialize(loginView);

        loginPresenter.performLogin(email, password);

        verify(loginView).navigateToLandingPage();
    }

}
