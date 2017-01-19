package com.lftechnology.leapfrogtest.data;

import com.lftechnology.leapfrogtest.domain.entity.User;
import com.lftechnology.leapfrogtest.domain.repository.AuthenticationRepository;

import io.reactivex.Observable;

public class MockAuthenticationRepository implements AuthenticationRepository {

    @Override
    public Observable<User> login(String email, String password) {
        User user = new User();
        user.setName("Ayush");
        return Observable.just(user);
    }
}
