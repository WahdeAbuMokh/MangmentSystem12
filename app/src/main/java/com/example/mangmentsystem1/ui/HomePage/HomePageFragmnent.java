package com.example.mangmentsystem1.ui.HomePage;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangmentsystem1.R;

public class HomePageFragmnent extends Fragment {

    private HomePageFragmnentViewModel mViewModel;

    public static HomePageFragmnent newInstance() {
        return new HomePageFragmnent();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
     //   Toast.makeText(getActivity(),"DD",Toast.LENGTH_SHORT).show();


        return inflater.inflate(R.layout.home_page_fragmnent_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomePageFragmnentViewModel.class);
        // TODO: Use the ViewModel
    }

}