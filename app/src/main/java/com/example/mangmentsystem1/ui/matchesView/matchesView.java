package com.example.mangmentsystem1.ui.matchesView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangmentsystem1.R;
import com.example.mangmentsystem1.classes.MatchAdapter;
import com.example.mangmentsystem1.classes.Matches;
import com.example.mangmentsystem1.classes.Team;
import com.example.mangmentsystem1.classes.TeamAdapter;
import com.example.mangmentsystem1.databinding.MatchesViewFragmentBinding;
import com.example.mangmentsystem1.databinding.TeamsEditFragmentBinding;
import com.example.mangmentsystem1.ui.teamsEdit.TeamsEditViewModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class matchesView extends Fragment {

    private MatchesViewViewModel mViewModel;
    private MatchesViewFragmentBinding binding;
    private RecyclerView recyclerView;
    MatchAdapter adapter;
    DatabaseReference ref;

    public static matchesView newInstance() {
        return new matchesView();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root=null;
        try {
            mViewModel=
                    new ViewModelProvider(this).get(MatchesViewViewModel.class);

            binding = MatchesViewFragmentBinding.inflate(inflater, container, false);
            final TextView textView = binding.textMatchView;
            root= binding.getRoot();

            mViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(@Nullable String s) {
                    //    textView.setText(s);
                }
            });

            ref = FirebaseDatabase.getInstance().getReference("Matches");

            recyclerView = root.findViewById(R.id.recycler5);

            recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

            FirebaseRecyclerOptions<Matches> options
                    = new FirebaseRecyclerOptions.Builder<Matches>().setQuery(ref, Matches.class).build();

             adapter = new MatchAdapter(options);
            // if(adapter!=null)
             recyclerView.setAdapter(adapter);
        }
        catch (Exception ex){
            //Toast.makeText(getActivity(),ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MatchesViewViewModel.class);

    }


    @Override
    public void onStart() {
        super.onStart();
        if(adapter!=null)
        adapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}