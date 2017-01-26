package com.lftechnology.leapfrogtest.login;

import android.support.annotation.Nullable;

public interface LoginView {

    void setEmailError(@Nullable String errorMessage);

    void setPasswordError(@Nullable String errorMessage);

    void showLoginError(String errorMessage);

    void navigateToLandingPage();

}
