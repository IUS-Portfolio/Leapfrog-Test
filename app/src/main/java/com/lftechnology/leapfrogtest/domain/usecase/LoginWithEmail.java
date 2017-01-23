package com.lftechnology.leapfrogtest.domain.usecase;

import com.lftechnology.leapfrogtest.domain.UseCase;
import com.lftechnology.leapfrogtest.domain.entity.User;
import com.lftechnology.leapfrogtest.domain.repository.AuthenticationRepository;
import com.lftechnology.leapfrogtest.domain.repository.PreferencesRepository;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Use-case for user to login with their email address
 */
public class LoginWithEmail extends UseCase<LoginWithEmail.RequestModel, LoginWithEmail.ResponseModel> {

    private AuthenticationRepository authenticationRepository;
    private PreferencesRepository preferencesRepository;

    public LoginWithEmail(AuthenticationRepository authenticationRepository, PreferencesRepository preferencesRepository) {
        this.authenticationRepository = authenticationRepository;
        this.preferencesRepository = preferencesRepository;
    }

    @Override
    protected Observable<ResponseModel> executeUseCase(RequestModel requestModel) {
        return authenticationRepository.login(requestModel.getEmail(), requestModel.getPassword())
                .flatMap(new Function<User, ObservableSource<User>>() {
                    @Override
                    public ObservableSource<User> apply(User user) throws Exception {
                        return preferencesRepository.saveUserDetails(user);
                    }
                })
                .flatMap(new Function<User, ObservableSource<ResponseModel>>() {
                    @Override
                    public ObservableSource<ResponseModel> apply(User user) throws Exception {
                        return Observable.just(new ResponseModel(user));
                    }
                });
    }

    public static final class RequestModel implements UseCase.RequestModel {

        private final String email;
        private final String password;

        public RequestModel(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }

    public static final class ResponseModel implements UseCase.ResponseModel {

        private final User user;

        public ResponseModel(User user) {
            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }
}
