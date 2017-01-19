package com.lftechnology.leapfrogtest.domain.usecase;

import com.lftechnology.leapfrogtest.domain.entity.User;
import com.lftechnology.leapfrogtest.domain.repository.AuthenticationRepository;
import com.lftechnology.leapfrogtest.domain.repository.PreferencesRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.observers.TestObserver;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginWithEmailTest {

    private static final String EMAIL = "ius.maharjan@gmail.com";
    private static final String PASSWORD = "test123";

    @Mock
    AuthenticationRepository authenticationRepository;

    @Mock
    PreferencesRepository preferencesRepository;

    private User user = new User();
    private LoginWithEmail loginWithEmail;

    @Before
    public void setup() {
        user = new User();
        loginWithEmail = new LoginWithEmail(authenticationRepository, preferencesRepository);
    }

    @Test
    public void loginWithEmail_requiredFunctionsShouldBeCalled() {
        TestObserver<Boolean> testObserver = TestObserver.create();
        when(authenticationRepository.login(anyString(), anyString())).thenReturn(Observable.just(user));
        when(preferencesRepository.saveUserDetails(any(User.class))).thenReturn(Observable.just(true));

        loginWithEmail.execute(EMAIL, PASSWORD).subscribe(testObserver);

        verify(authenticationRepository).login(EMAIL, PASSWORD);
        verify(preferencesRepository).saveUserDetails(user);
        verifyNoMoreInteractions(authenticationRepository);
        verifyNoMoreInteractions(preferencesRepository);
        testObserver.assertNoErrors();
    }

    @Test
    public void loginWithEmail_exceptionThrownDuringAuthentication() {
        TestObserver<Boolean> testObserver = TestObserver.create();
        when(authenticationRepository.login(EMAIL, PASSWORD)).thenReturn(Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> e) throws Exception {
                throw new Exception();
            }
        }));
        when(preferencesRepository.saveUserDetails(user)).thenReturn(Observable.just(true));

        loginWithEmail.execute(EMAIL, PASSWORD).subscribe(testObserver);

        verify(authenticationRepository).login(EMAIL, PASSWORD);
        verifyNoMoreInteractions(authenticationRepository);
        verifyNoMoreInteractions(preferencesRepository);
        testObserver.assertError(Exception.class);
    }

    @Test
    public void loginWithEmail_exceptionThrownDuringPreferenceUpdate() {
        TestObserver<Boolean> testObserver = TestObserver.create();
        when(authenticationRepository.login(EMAIL, PASSWORD)).thenReturn(Observable.just(user));
        when(preferencesRepository.saveUserDetails(user)).thenReturn(Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                throw new IOException();
            }
        }));

        loginWithEmail.execute(EMAIL, PASSWORD).subscribe(testObserver);

        verify(authenticationRepository).login(EMAIL, PASSWORD);
        verify(preferencesRepository).saveUserDetails(user);
        verifyNoMoreInteractions(authenticationRepository);
        verifyNoMoreInteractions(preferencesRepository);
        testObserver.assertError(IOException.class);
    }
}
