package com.lftechnology.leapfrogtest.domain;

import io.reactivex.Observable;

public abstract class UseCase<Q extends UseCase.RequestModel, P extends UseCase.ResponseModel> {

    private Q requestValues;

    public void setRequestValues(Q requestValues) {
        this.requestValues = requestValues;
    }

    public Observable<P> run() {
        return executeUseCase(requestValues);
    }

    protected abstract Observable<P> executeUseCase(Q requestValues);

    public interface RequestModel {

    }

    public interface ResponseModel {

    }
}
