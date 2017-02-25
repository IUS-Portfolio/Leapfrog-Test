package com.lftechnology.leapfrogtest.login;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.lftechnology.leapfrogtest.R;
import com.lftechnology.leapfrogtest.UseCaseExecutor;
import com.lftechnology.leapfrogtest.data.MockAuthenticationRepository;
import com.lftechnology.leapfrogtest.data.MockPreferencesRepository;
import com.lftechnology.leapfrogtest.domain.repository.AuthenticationRepository;
import com.lftechnology.leapfrogtest.domain.repository.PreferencesRepository;
import com.lftechnology.leapfrogtest.domain.usecase.LoginWithEmail;
import com.lftechnology.leapfrogtest.utils.RxUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import timber.log.Timber;

import static com.lftechnology.leapfrogtest.utils.RxUtils.TEXT_CHANGE_DEBOUNCE_INTERVAL_MILLIS;
import static com.lftechnology.leapfrogtest.utils.UiUtils.extractString;
import static timber.log.Timber.d;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoginView {

    @BindView(R.id.layout_email)
    TextInputLayout emailLayout;

    @BindView(R.id.email)
    AutoCompleteTextView emailEditText;

    @BindView(R.id.layout_password)
    TextInputLayout passwordLayout;

    @BindView(R.id.password)
    EditText passwordEditText;

    @BindView(R.id.login_progress)
    View progressView;

    LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initializeComponents();
        initializeObservables();
    }

    private void initializeComponents() {
        AuthenticationRepository authenticationRepository = new MockAuthenticationRepository();
        PreferencesRepository preferencesRepository = new MockPreferencesRepository();
        LoginWithEmail loginWithEmail = new LoginWithEmail(authenticationRepository, preferencesRepository);
        loginPresenter = new LoginPresenter(new UseCaseExecutor(AndroidSchedulers.mainThread()), loginWithEmail);
        loginPresenter.attachView(this);
    }

    private void initializeObservables() {
        Observable<Boolean> emailObservable = RxUtils.getTextChangeObservable(emailEditText)
                .debounce(TEXT_CHANGE_DEBOUNCE_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(String email) throws Exception {
                        return loginPresenter.validateEmail(email);
                    }
                });

        Observable<Boolean> passwordObservable = RxUtils.getTextChangeObservable(passwordEditText)
                .debounce(TEXT_CHANGE_DEBOUNCE_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(String password) throws Exception {
                        return loginPresenter.validatePassword(password);
                    }
                });

        Observable.combineLatest(emailObservable, passwordObservable, new BiFunction<Boolean, Boolean, Boolean>() {
            @Override
            public Boolean apply(Boolean validEmail, Boolean validPassword) throws Exception {
                return validEmail && validPassword;
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                Timber.d("Submit button enabled: %b", aBoolean);
            }
        });
    }

    @OnClick(R.id.email_sign_in_button)
    public void loginWithEmail() {
        if (loginPresenter.validateEmail(extractString(emailEditText)) &&
                loginPresenter.validatePassword(extractString(passwordEditText))) {
            loginPresenter.performLogin(extractString(emailEditText), extractString(passwordEditText));
        }
    }

    @Override
    public void setEmailError(String errorMessage) {
        emailLayout.setError(errorMessage);
    }

    @Override
    public void setPasswordError(String errorMessage) {
        passwordLayout.setError(errorMessage);
    }

    @Override
    public void showLoginError(String errorMessage) {

    }

    @Override
    public void navigateToLandingPage() {
        d("Login Successful");
    }

    @Override
    public Context getContext() {
        return this;
    }
}
