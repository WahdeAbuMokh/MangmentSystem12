package com.example.mangmentsystem1.ui.teamsEdit;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import com.example.mangmentsystem1.R;
import com.example.mangmentsystem1.classes.Team;
import com.example.mangmentsystem1.classes.TeamAdapter;
import com.example.mangmentsystem1.databinding.TeamsEditFragmentBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class TeamsEdit extends Fragment {
    private RecyclerView recyclerView;
    TeamAdapter adapter;
    DatabaseReference ref;
    private TeamsEditViewModel teamsEditViewModel;
    private TeamsEditFragmentBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        teamsEditViewModel=
                new ViewModelProvider(this).get(TeamsEditViewModel.class);

        binding = TeamsEditFragmentBinding.inflate(inflater, container, false);
        final TextView textView = binding.textEdit;
        teamsEditViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        View root = binding.getRoot();
        ref = FirebaseDatabase.getInstance().getReference("Team");

        recyclerView = root.findViewById(R.id.recycler1);

        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        FirebaseRecyclerOptions<Team> options
                = new FirebaseRecyclerOptions.Builder<Team>().setQuery(ref, Team.class).build();
        adapter=new TeamAdapter(options);
        recyclerView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}