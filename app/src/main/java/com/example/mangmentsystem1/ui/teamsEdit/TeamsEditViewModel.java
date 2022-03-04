package com.example.mangmentsystem1.ui.teamsEdit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TeamsEditViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TeamsEditViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Team edit fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}