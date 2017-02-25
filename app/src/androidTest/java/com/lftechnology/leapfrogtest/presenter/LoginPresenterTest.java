package com.lftechnology.leapfrogtest.presenter;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.lftechnology.leapfrogtest.R;
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
import org.mockito.MockitoAnnotations;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class LoginPresenterTest {

    private LoginPresenter loginPresenter;
    private Context context;

    @Mock
    private LoginView loginView;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        context = InstrumentationRegistry.getTargetContext();
        when(loginView.getContext()).thenReturn(context);

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

        verify(loginView, timeout(2000)).showLoginError(anyString());
        verifyNoMoreInteractions(loginView);
    }

    @Test
    public void performLogin_successfulCase_shouldOpenLandingPage() {
        loginPresenter.attachView(loginView);
        String email = "ius.maharjan@gmail.com";
        String password = "Test123";

        loginPresenter.performLogin(email, password);

        verify(loginView, timeout(2000)).navigateToLandingPage();
        verifyNoMoreInteractions(loginView);
    }

    @Test(expected = NullPointerException.class)
    public void validateEmail_nullEmail_shouldThrowNullPointerException() {
        loginPresenter.validateEmail(null);
    }

    @Test
    public void validateEmail_emptyEmail_shouldDisplayValidationError() {
        String email = "";

        boolean result = loginPresenter.validateEmail(email);

        verify(loginView).setEmailError(context.getString(R.string.error_invalid_email));
        assertThat(result).isFalse();
    }

    @After
    public void tearDown() {
        loginPresenter.detachView();
    }

}
