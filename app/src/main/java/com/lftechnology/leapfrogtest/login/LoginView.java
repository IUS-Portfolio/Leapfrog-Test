package com.lftechnology.leapfrogtest.login;

/**
 * Created by ayush on 1/19/17.
 */

public interface LoginView {

    void showEmailError(String errorMessage);

    void showPasswordError(String errorMessage);

    void showLoginError(String errorMessage);

    void navigateToLandingPage();

}
