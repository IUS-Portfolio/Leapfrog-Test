package com.lftechnology.leapfrogtest.domain.usecase;

import com.lftechnology.leapfrogtest.domain.entity.User;
import com.lftechnology.leapfrogtest.domain.repository.AuthenticationRepository;
import com.lftechnology.leapfrogtest.domain.repository.PreferencesRepository;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Use-case for user to login with their email address
 */
public class LoginWithEmail {

    AuthenticationRepository authenticationRepository;
    PreferencesRepository preferencesRepository;

    LoginWithEmail(AuthenticationRepository authenticationRepository, PreferencesRepository preferencesRepository) {
        this.authenticationRepository = authenticationRepository;
        this.preferencesRepository = preferencesRepository;
    }

    Observable<Boolean> loginWithEmail(String email, String password) {
        return authenticationRepository.login(email, password)
                .flatMap(new Function<User, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(User user) throws Exception {
                        return preferencesRepository.saveUserDetails(user);
                    }
                });
    }
}
