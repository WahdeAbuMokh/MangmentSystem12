package com.example.mangmentsystem1.ui.addMatch;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mangmentsystem1.ExcelFileActivity;
import com.example.mangmentsystem1.ExcelFileActivity3;
import com.example.mangmentsystem1.R;
import com.example.mangmentsystem1.classes.Dialog;
import com.example.mangmentsystem1.classes.Matches;
import com.example.mangmentsystem1.classes.Team;
import com.example.mangmentsystem1.databinding.AddMatchFragmentBinding;
import com.example.mangmentsystem1.databinding.TeamChangeFragmentBinding;
import com.example.mangmentsystem1.ui.TeamChange.TeamChangeViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;

public class addMatch extends Fragment {

    private AddMatchViewModel mViewModel;
    private AddMatchFragmentBinding binding;
    LinearLayout myLayout;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    StorageReference storageReference;
    FirebaseStorage storage;
    Uri filepath;
    Spinner team1Spinner,team2Spinner;
    Button btnAdd,btnExcel;
    TextView team1Tv,team2Tv;
    ImageView team1img,team2img;
    EditText dateEd,timeEd;
    ArrayList<String> names = new ArrayList<>();
    ArrayList<Team> teams = new ArrayList<>();
    final Dialog loadingdialog= new Dialog(this);

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel=new ViewModelProvider(this).get(AddMatchViewModel.class);

        binding= AddMatchFragmentBinding.inflate(inflater,container,false);

        View root = binding.getRoot();
        final TextView textView = binding.textAddMatch;
        mViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
       // myLayout = (LinearLayout)root.findViewById(R.id.mylayout5);
//        AnimationDrawable animationDrawable = (AnimationDrawable)myLayout.getBackground() ;
//        animationDrawable.setEnterFadeDuration(4000);
//        animationDrawable.setExitFadeDuration(4000);
       // animationDrawable.start();
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("imagessss");
        team1Spinner=root.findViewById(R.id.team1Spinner);
        team2Spinner=root.findViewById(R.id.team2Spinner);
        team1img=root.findViewById(R.id.team1img);
        team2img=root.findViewById(R.id.team2img);
        team1Tv=root.findViewById(R.id.team1tv);
        team2Tv=root.findViewById(R.id.team2tv);
        dateEd=root.findViewById(R.id.EdtMatchDate);
        timeEd=root.findViewById(R.id.EdtMatchTime);
        btnAdd=root.findViewById(R.id.btn_AddMatch);
        btnExcel=root.findViewById(R.id.btn_AddMatchExcel);

        //Toast.makeText(getActivity(),"dd",Toast.LENGTH_SHORT).show();
        btnExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), ExcelFileActivity3.class);
                view.getContext().startActivity(intent);
            }
        });

        databaseReference.child("Team").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                names.clear();
                teams.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    teams.add(item.getValue(Team.class));
                    names.add(item.child("teamName").getValue(String.class));

                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, names);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                team1Spinner.setAdapter(arrayAdapter);
                team2Spinner.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error ", Toast.LENGTH_SHORT).show();

            }
        });
            team1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Team team1;
                    team1=teams.get(i);
                    Toast.makeText(getActivity(),"you selected Team :"+team1.getTeamName(),Toast.LENGTH_LONG).show();
                    team1Tv.setText(team1.getTeamName());
                    Picasso.get().load(team1.getUri()).into(team1img);
                    team2Spinner.setSelection(1);
                    team2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int j, long l) {
                            Team team2;
                            team2=teams.get(j);
                           Toast.makeText(getActivity(),"you selected Team :"+team2.getTeamName(),Toast.LENGTH_LONG).show();
                            team2Tv.setText(team2.getTeamName());
                           Picasso.get().load(team2.getUri()).into(team2img);

                            dateEd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                            month=month+1;
                                            String date= day+"/"+month+"/"+year;
                                            dateEd.setText(date);
                                        }},year,month,day);
                                    datePickerDialog.show();
                                }
                            });
                            timeEd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                                            new TimePickerDialog.OnTimeSetListener() {

                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                                      int minute) {
                                                    String time= hourOfDay + ":" + minute;
                                                    timeEd.setText(time);
                                                }
                                            },hour,minute,false);
                                    timePickerDialog.show();
                                }
                            });
                            btnAdd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loadingdialog.startLoadingdialog();
                                    String url1 = team1.getUri();
                                    String url2 = team2.getUri();
                                    String team1Name=team1.getTeamName();
                                    String team2Name=team2.getTeamName();
                                    String team1Key=team1.getKey();
                                    String team2Key=team2.getKey();
                                    String date=dateEd.getText().toString().trim();
                                    String time=timeEd.getText().toString().trim();
                                    Matches match = new Matches(team1Name,team2Name,team1Key,team2Key,url1,url2,date,time);
                                    databaseReference=firebaseDatabase.getReference("Matches").push();
                                    match.setKey(databaseReference.getKey());
                                    databaseReference.setValue(match);
                                    loadingdialog.dismissdialog();
                                    Toast.makeText(getActivity(), "Match Added Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }


                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            Toast.makeText(getActivity(), "nothing selected ", Toast.LENGTH_SHORT).show();
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddMatchViewModel.class);
        // TODO: Use the ViewModel
    }


}