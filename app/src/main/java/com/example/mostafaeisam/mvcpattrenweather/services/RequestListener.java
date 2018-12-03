package com.example.mostafaeisam.mvcpattrenweather.services;

public interface RequestListener {
    void onSuccess(Object object);
    void  onFailure(int errorCode);
}
