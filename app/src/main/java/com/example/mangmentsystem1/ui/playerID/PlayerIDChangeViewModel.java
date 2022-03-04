package com.example.mangmentsystem1.ui.playerID;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlayerIDChangeViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public PlayerIDChangeViewModel()
    {
        mText = new MutableLiveData<>();
        mText.setValue("Player ID Change");
    }

    public LiveData<String> getText() {
        return mText;
    }

}