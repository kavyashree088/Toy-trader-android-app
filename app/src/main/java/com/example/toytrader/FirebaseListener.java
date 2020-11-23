package com.example.toytrader;

public interface FirebaseListener {
    // you can define any parameter as per your requirement
    public <T> void getFBData(T event);

    public <T> void updateFBResult(T event);
}
