package com.lftechnology.leapfrogtest;

import com.lftechnology.leapfrogtest.domain.UseCase;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

public class UseCaseExecutor {

    private CompositeDisposable compositeDisposable;

    private Scheduler scheduler;

    public UseCaseExecutor(Scheduler scheduler) {
        compositeDisposable = new CompositeDisposable();
        this.scheduler = scheduler;
    }

    public <Q extends UseCase.RequestModel, P extends UseCase.ResponseModel> void execute(
            UseCase<Q, P> useCase, DisposableObserver<P> disposableObserver) {
        DisposableObserver disposable = useCase.run()
                .observeOn(scheduler)
                .subscribeWith(disposableObserver);
        compositeDisposable.add(disposable);
    }

    public void dispose() {
        compositeDisposable.dispose();
    }
}
