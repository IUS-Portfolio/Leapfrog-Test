package com.lftechnology.leapfrogtest.login;

import android.content.Context;
import android.support.annotation.Nullable;

public interface LoginView {

    Context getContext();

    void setEmailError(@Nullable String errorMessage);

    void setPasswordError(@Nullable String errorMessage);

    void showLoginError(String errorMessage);

    void navigateToLandingPage();

}
