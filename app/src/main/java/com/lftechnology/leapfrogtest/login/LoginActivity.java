package com.lftechnology.leapfrogtest.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.lftechnology.leapfrogtest.R;
import com.lftechnology.leapfrogtest.data.MockAuthenticationRepository;
import com.lftechnology.leapfrogtest.data.MockPreferencesRepository;
import com.lftechnology.leapfrogtest.domain.repository.AuthenticationRepository;
import com.lftechnology.leapfrogtest.domain.repository.PreferencesRepository;
import com.lftechnology.leapfrogtest.domain.usecase.LoginWithEmail;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.lftechnology.leapfrogtest.utils.UiUtils.extractString;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoginView {

    @BindView(R.id.email)
    AutoCompleteTextView mEmailView;

    @BindView(R.id.password)
    EditText mPasswordView;

    @BindView(R.id.login_progress)
    View mProgressView;

    @BindView(R.id.email_login_form)
    View mLoginFormView;

    LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        AuthenticationRepository authenticationRepository = new MockAuthenticationRepository();
        PreferencesRepository preferencesRepository = new MockPreferencesRepository();
        LoginWithEmail loginWithEmail = new LoginWithEmail(authenticationRepository, preferencesRepository);
        loginPresenter = new LoginPresenter(loginWithEmail, AndroidSchedulers.mainThread());
        loginPresenter.attachView(this);

    }

    @OnClick(R.id.email_sign_in_button)
    public void loginWithEmail() {
        loginPresenter.performLogin(extractString(mEmailView), extractString(mPasswordView));
    }

    @Override
    public void showEmailError(String errorMessage) {

    }

    @Override
    public void showPasswordError(String errorMessage) {

    }

    @Override
    public void showLoginError(String errorMessage) {

    }

    @Override
    public void navigateToLandingPage() {

    }
}
