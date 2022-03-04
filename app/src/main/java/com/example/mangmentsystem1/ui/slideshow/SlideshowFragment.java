package com.example.mangmentsystem1.ui.slideshow;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangmentsystem1.R;
import com.example.mangmentsystem1.classes.Player;
import com.example.mangmentsystem1.classes.PlayerAdapter;
import com.example.mangmentsystem1.classes.Team;
import com.example.mangmentsystem1.classes.TeamAdapter;
import com.example.mangmentsystem1.databinding.FragmentSlideshowBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;
    LinearLayout myLayout;
    private RecyclerView recyclerView;
    PlayerAdapter adapter;
    DatabaseReference ref;
    ArrayList<String> names= new ArrayList<>();
    ArrayList<Team> teams= new ArrayList<>();
    Spinner spinner;
    Button btnShow;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSlideshow;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        myLayout = (LinearLayout)root.findViewById(R.id.mylayout3);

        ref = FirebaseDatabase.getInstance().getReference();
        recyclerView = root.findViewById(R.id.recycler2);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        btnShow=root.findViewById(R.id.btnShowPlayers);
        spinner=(Spinner)root.findViewById(R.id.spinner2);
        ref.child("Team").addValueEventListener(new ValueEventListener() {

                @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                names.clear();
                teams.clear();
                    for (DataSnapshot item: snapshot.getChildren())
                    {
                        teams.add(item.getValue(Team.class));
                        names.add(item.child("teamName").getValue(String.class));

                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,names);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
               btnShow.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference();
                        Team team1;
                        team1=teams.get(i);
                        Toast.makeText(getActivity(),"you selected Team :"+team1.getTeamName(),Toast.LENGTH_LONG).show();
                        Query query=ref2.child("Players").orderByChild("teamKey").equalTo(team1.getKey().toString());
                        FirebaseRecyclerOptions<Player> options
                               = new FirebaseRecyclerOptions.Builder<Player>().setQuery(query,Player.class).build();
                        adapter=new PlayerAdapter(options);
                        recyclerView.setAdapter(adapter);
                       adapter.startListening();
                   }
               });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getActivity(), "nothing selected ", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
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