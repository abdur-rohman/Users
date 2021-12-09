package id.refactory.users.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import id.refactory.users.clients.UserClient;
import id.refactory.users.models.ResultsItem;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserViewModel extends ViewModel {
    private MutableLiveData<String> error = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<List<ResultsItem>> results = new MutableLiveData<>();
    private List<ResultsItem> temp = new ArrayList<>();

    public LiveData<String> getError() {
        return error;
    }
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    public LiveData<List<ResultsItem>> getResults() {
        return results;
    }

    private CompositeDisposable disposable = new CompositeDisposable();

    public void getUsers() {
        error.setValue("");

        UserClient.userService().getUsers(1000)
                .flatMap(it -> Observable.fromIterable(it.getResults()))
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<ResultsItem>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        isLoading.setValue(true);

                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull List<ResultsItem> resultsItems) {
                        isLoading.setValue(false);

                        results.setValue(resultsItems);

                        temp = resultsItems;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isLoading.setValue(false);

                        e.printStackTrace();

                        error.setValue(e.getMessage());
                    }
                });
    }

    public void filterByName(String s) {
        error.setValue("");

        Observable.just(temp)
                .flatMap(Observable::fromIterable)
                .filter(it -> it.getName().getFullname().toLowerCase().contains(s))
                .toList()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<ResultsItem>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull List<ResultsItem> resultsItems) {
                        results.setValue(resultsItems);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();

                        error.setValue(e.getMessage());
                    }
                });
    }

    public void onClear() {
        disposable.dispose();
    }
}
