package com.example.daggerdemo;

import dagger.Component;

@Component(modules = {UserBean.class})
public interface MainActivityComponent {
    void inject(MainActivity mainActivity);
}
