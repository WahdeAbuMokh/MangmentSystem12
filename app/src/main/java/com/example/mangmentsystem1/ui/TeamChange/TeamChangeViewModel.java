package com.example.mangmentsystem1.ui.TeamChange;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TeamChangeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TeamChangeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Team Change fragment");
    }

    public  LiveData<String> getText() {
        return mText;
    }
}