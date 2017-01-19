package com.lftechnology.leapfrogtest.domain.repository;

import com.lftechnology.leapfrogtest.domain.entity.User;

import io.reactivex.Observable;

public interface PreferencesRepository {

    Observable<Boolean> saveUserDetails(User user);
}
