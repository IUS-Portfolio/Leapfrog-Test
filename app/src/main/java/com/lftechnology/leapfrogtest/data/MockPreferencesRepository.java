package com.lftechnology.leapfrogtest.data;

import com.lftechnology.leapfrogtest.domain.entity.User;
import com.lftechnology.leapfrogtest.domain.repository.PreferencesRepository;

import io.reactivex.Observable;

public class MockPreferencesRepository implements PreferencesRepository {

    @Override
    public Observable<User> saveUserDetails(User user) {
        return Observable.just(user);
    }
}
