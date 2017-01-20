package com.lftechnology.leapfrogtest.data;

import com.lftechnology.leapfrogtest.domain.entity.User;
import com.lftechnology.leapfrogtest.domain.repository.AuthenticationRepository;

import io.reactivex.Observable;

public class MockAuthenticationRepository implements AuthenticationRepository {

    @Override
    public Observable<User> login(String email, String password) {
        if (!email.equals("ius.maharjan@gmail.com") || !password.equals("Test123")) {
            return Observable.error(new Exception("Invalid credentials"));
        }
        User user = new User();
        user.setName("Ayush");
        return Observable.just(user);
    }
}
