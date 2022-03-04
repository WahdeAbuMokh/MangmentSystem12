package com.example.mangmentsystem1.ui.matchesView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MatchesViewViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public MatchesViewViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Matches view fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}