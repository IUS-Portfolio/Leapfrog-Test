package com.lftechnology.leapfrogtest.login;

import com.lftechnology.leapfrogtest.domain.usecase.LoginWithEmail;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

public class LoginPresenter {

    private LoginWithEmail loginWithEmail;
    private CompositeDisposable compositeDisposable;
    private Scheduler observerThreadScheduler;
    private LoginView loginView;

    public LoginPresenter(LoginWithEmail loginWithEmail, Scheduler observerThreadScheduler) {
        this.loginWithEmail = loginWithEmail;
        this.compositeDisposable = new CompositeDisposable();
        this.observerThreadScheduler = observerThreadScheduler;
    }

    public void initialize(LoginView loginView) {
        this.loginView = loginView;
    }

    public void performLogin(String email, String password) {
        loginWithEmail.execute(email, password)
                .observeOn(observerThreadScheduler)
                .subscribeWith(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean success) {
                        Timber.d("execute:OnNext:%s", success);
                        loginView.navigateToLandingPage();
                    }

                    @Override
                    public void onError(Throwable exception) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
