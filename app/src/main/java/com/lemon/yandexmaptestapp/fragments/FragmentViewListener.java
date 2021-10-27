package com.lemon.yandexmaptestapp.fragments;


public interface FragmentViewListener {

    void onFragmentSomeViewClicked(int viewId);
    void passFragmentResult(String[] result);
}
