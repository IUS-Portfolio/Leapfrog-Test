package com.lftechnology.leapfrogtest.domain.repository;

import com.lftechnology.leapfrogtest.domain.entity.User;

import io.reactivex.Observable;

public interface AuthenticationRepository {

    Observable<User> login(String email, String password);

}
