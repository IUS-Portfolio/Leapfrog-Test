package com.lftechnology.leapfrogtest.domain.usecase;

import com.lftechnology.leapfrogtest.domain.entity.User;
import com.lftechnology.leapfrogtest.domain.repository.AuthenticationRepository;
import com.lftechnology.leapfrogtest.domain.repository.PreferencesRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Predicate;
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
        TestObserver<LoginWithEmail.ResponseModel> testObserver = TestObserver.create();
        when(authenticationRepository.login(anyString(), anyString())).thenReturn(Observable.just(user));
        when(preferencesRepository.saveUserDetails(any(User.class))).thenReturn(Observable.just(user));
        loginWithEmail.setRequestValues(new LoginWithEmail.RequestModel(EMAIL, PASSWORD));

        loginWithEmail.run().subscribe(testObserver);

        verify(authenticationRepository).login(EMAIL, PASSWORD);
        verify(preferencesRepository).saveUserDetails(user);
        verifyNoMoreInteractions(authenticationRepository);
        verifyNoMoreInteractions(preferencesRepository);
        testObserver.assertValue(new Predicate<LoginWithEmail.ResponseModel>() {
            @Override
            public boolean test(LoginWithEmail.ResponseModel responseModel) throws Exception {
                return responseModel.getUser().equals(user);
            }
        });
        testObserver.assertComplete();
    }

    @Test
    public void loginWithEmail_exceptionThrownDuringAuthentication() {
        TestObserver<LoginWithEmail.ResponseModel> testObserver = TestObserver.create();
        when(authenticationRepository.login(EMAIL, PASSWORD)).thenReturn(Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> e) throws Exception {
                throw new Exception();
            }
        }));
        loginWithEmail.setRequestValues(new LoginWithEmail.RequestModel(EMAIL, PASSWORD));

        loginWithEmail.run().subscribe(testObserver);

        verify(authenticationRepository).login(EMAIL, PASSWORD);
        verifyNoMoreInteractions(authenticationRepository);
        verifyNoMoreInteractions(preferencesRepository);
        testObserver.assertError(Exception.class);
    }

    @Test
    public void loginWithEmail_exceptionThrownDuringPreferenceUpdate() {
        TestObserver<LoginWithEmail.ResponseModel> testObserver = TestObserver.create();
        when(authenticationRepository.login(EMAIL, PASSWORD)).thenReturn(Observable.just(user));
        when(preferencesRepository.saveUserDetails(user)).thenReturn(Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> e) throws Exception {
                throw new Exception();
            }
        }));
        loginWithEmail.setRequestValues(new LoginWithEmail.RequestModel(EMAIL, PASSWORD));

        loginWithEmail.run().subscribe(testObserver);

        verify(authenticationRepository).login(EMAIL, PASSWORD);
        verify(preferencesRepository).saveUserDetails(user);
        verifyNoMoreInteractions(authenticationRepository);
        verifyNoMoreInteractions(preferencesRepository);
        testObserver.assertError(Exception.class);
    }
}
