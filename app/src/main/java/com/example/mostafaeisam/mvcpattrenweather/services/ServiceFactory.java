package com.example.mostafaeisam.mvcpattrenweather.services;

import com.example.mostafaeisam.mvcpattrenweather.responses.GetObjectResponse;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ServiceFactory {


    public static void getData(final RequestListener context,final String url, final RequestListener listener) {

        final GetObjectResponse getObjectResponse = new GetObjectResponse();

        final Observable<GetObjectResponse> getDataObservable = Observable.create(new
                ObservableOnSubscribe <GetObjectResponse>() {
            @Override
            public void subscribe(final ObservableEmitter<GetObjectResponse> emitter) throws Exception {
                try {

                    final OkHttpClient okHttpClient = new OkHttpClient();
                    String a = url;
                    final Request request = new Request.Builder()
                            .url(a)
                            .build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                           // listener.onFailure(1000);
                            emitter.onError(e.getCause());
                        }

                        @Override
                        public void onResponse(Call call, okhttp3.Response response) throws IOException {

                            if (response == null || response.code() != 200) {
                                getObjectResponse.setId(response.code());
                                getObjectResponse.setBody(response.body().string());
                                emitter.onNext(getObjectResponse);
                                emitter.onComplete();
                            } else {
                                String body = response.body().string();
                                getObjectResponse.setBody(body);
                                getObjectResponse.setId(response.code());
                                call.cancel();
                                emitter.onNext(getObjectResponse);
                                emitter.onComplete();
                            }

                        }
                    });


                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });


        getDataObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetObjectResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(GetObjectResponse getObjectResponse) {

                if (getObjectResponse.getId()!= 200)
                    listener.onFailure(getObjectResponse.getId());
                else
                    listener.onSuccess(getObjectResponse.getBody());
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(1000);
            }

            @Override
            public void onComplete() {

            }
        });

    }
}
