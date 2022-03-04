package com.example.mangmentsystem1.ui.addMatch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddMatchViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public AddMatchViewModel () {
        mText = new MutableLiveData<>();
        mText.setValue("This is add Match fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}