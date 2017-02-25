package com.lftechnology.leapfrogtest.login;

import android.support.annotation.NonNull;

import com.lftechnology.leapfrogtest.R;
import com.lftechnology.leapfrogtest.UseCaseExecutor;
import com.lftechnology.leapfrogtest.domain.usecase.LoginWithEmail;

import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

import static com.google.common.base.Preconditions.checkNotNull;

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

    public boolean validateEmail(@NonNull String email) {
        checkNotNull(email, "Email cannot be null");

        boolean valid = true;

        if (email.isEmpty()) {
            valid = false;
            loginView.setEmailError(loginView.getContext().getString(R.string.error_invalid_email));
        } else {
            loginView.setEmailError(null);
        }

        return valid;
    }

    public boolean validatePassword(@NonNull String password) {
        checkNotNull(password, "Password cannot be null");

        boolean valid = true;

        if (password.isEmpty()) {
            valid = false;
            loginView.setPasswordError("Please enter your password");
        } else {
            loginView.setPasswordError(null);
        }

        return valid;
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
            Timber.e(exception);
            loginView.showLoginError(exception.getMessage());
        }

        @Override
        public void onComplete() {

        }
    }
}
