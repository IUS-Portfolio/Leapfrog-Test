package com.lftechnology.leapfrogtest.login;

import com.lftechnology.leapfrogtest.UseCaseExecutor;
import com.lftechnology.leapfrogtest.domain.usecase.LoginWithEmail;

import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

public class LoginPresenter {

    private LoginWithEmail loginWithEmail;
    private UseCaseExecutor useCaseExecutor;
    private LoginView loginView;

    public LoginPresenter(UseCaseExecutor useCaseExecutor, LoginWithEmail loginWithEmail) {
        this.useCaseExecutor = useCaseExecutor;
        this.loginWithEmail = loginWithEmail;
    }

    public void attachView(LoginView loginView) {
        this.loginView = loginView;
    }

    public void detachView() {
        this.loginView = null;
    }

    public void performLogin(String email, String password) {
        loginWithEmail.setRequestValues(new LoginWithEmail.RequestModel(email, password));
        useCaseExecutor.execute(loginWithEmail, new LoginObserver());
    }

    private class LoginObserver extends DisposableObserver<LoginWithEmail.ResponseModel> {
        @Override
        public void onNext(LoginWithEmail.ResponseModel responseModel) {
            loginView.navigateToLandingPage();
        }

        @Override
        public void onError(Throwable exception) {
            loginView.showLoginError(exception.getMessage());
            Timber.e(exception);
        }

        @Override
        public void onComplete() {

        }
    }
}
