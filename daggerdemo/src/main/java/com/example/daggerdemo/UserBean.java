package com.example.daggerdemo;


import dagger.Module;
import dagger.Provides;

@Module
public class UserBean {


    @Provides
    Student getStudent(){
        return new Student();
    }
}
