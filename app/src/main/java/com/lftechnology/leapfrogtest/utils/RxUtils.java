package com.lftechnology.leapfrogtest.utils;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static com.google.common.base.Preconditions.checkNotNull;

public class RxUtils {

    public static final int TEXT_CHANGE_DEBOUNCE_INTERVAL_MILLIS = 500;

    public static Observable<String> getTextChangeObservable(@NonNull final TextView textView) {
        checkNotNull(textView);

        final PublishSubject<String> subject = PublishSubject.create();

        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                subject.onNext(s.toString());
            }
        });

        return subject;
    }

}
