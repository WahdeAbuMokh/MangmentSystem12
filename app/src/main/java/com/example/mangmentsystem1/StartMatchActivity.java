package com.example.mangmentsystem1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangmentsystem1.classes.MatchEvent;
import com.example.mangmentsystem1.classes.Matches;
import com.example.mangmentsystem1.classes.Player;
import com.example.mangmentsystem1.classes.Team;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class StartMatchActivity extends AppCompatActivity {

    ImageView team1img, team2img;
    TextView team1nameTv, team2nameTv, dateTv, timeTv, team1resultTv, team2resultTv, matchStatsTv1;
    Button btnExit, btnEnd, btnChangeStats,btnStart;
    Spinner teamSpinner, playerSpinner, statsSpinner;
    ArrayList<Team> teams = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> playernames = new ArrayList<>();
    ArrayList<Player> players = new ArrayList<>();
    ArrayList<Player> players2= new ArrayList<>();
    ArrayList<String> stats = new ArrayList<>();
    Team team1, team2, team3;
    Matches match, match1,match2;
    Intent intent;
    private Chronometer chronometer;
    private  boolean running;
    private  long pauseOffset;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_match);
        intent = getIntent();
        match = (Matches) intent.getSerializableExtra("match");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        DatabaseReference databaseReferenceP = firebaseDatabase.getReference();
        chronometer=findViewById(R.id.chronometer1);
        team1img = findViewById(R.id.team1StartMatchimg);
        team2img = findViewById(R.id.team2StartMatchimg);
        team1nameTv = findViewById(R.id.team1StartMatchtv);
        team2nameTv = findViewById(R.id.team2StartMatchtv);
        dateTv = findViewById(R.id.TvStartMatchDate);
        timeTv = findViewById(R.id.TvStartMatchTime);
        team1resultTv = findViewById(R.id.TvStartMatchResult1);
        team2resultTv = findViewById(R.id.TvStartMatchResult2);
        matchStatsTv1 = findViewById(R.id.StatsTV1);
        btnStart=findViewById(R.id.btnStartGame);
        btnExit = findViewById(R.id.btnExitMatch);
        btnEnd = findViewById(R.id.btnEndMatch);
        btnChangeStats = findViewById(R.id.btnChangeStats);
        teamSpinner = findViewById(R.id.spinnerTeam);
        playerSpinner = findViewById(R.id.spinnerPlayer);
        statsSpinner = findViewById(R.id.spinnerStatus);
        Picasso.get().load(match.getTeam1uri()).into(team1img);
        Picasso.get().load(match.getTeam2uri()).into(team2img);
        team1nameTv.setText(match.getTeam1name());
        team2nameTv.setText(match.getTeam2name());
        dateTv.setText(match.getDate());
        timeTv.setText(match.getTime());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(StartMatchActivity.this, android.R.layout.simple_spinner_item, names);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamSpinner.setAdapter(arrayAdapter);
        stats.add("Goal");
        stats.add("Assist");
        stats.add("redCard");
        stats.add("yellowCard");
        teamSpinner.setEnabled(false);
        playerSpinner.setEnabled(false);
        statsSpinner.setEnabled(false);
        btnChangeStats.setEnabled(false);



        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teamSpinner.setEnabled(true);
                playerSpinner.setEnabled(true);
                statsSpinner.setEnabled(true);
                btnChangeStats.setEnabled(true);
                if(!running)
                {
                   chronometer.setBase(SystemClock.elapsedRealtime()-pauseOffset);
                   chronometer.start();
                   running=true;
                }
            }
        });


        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int team1goals= Integer.parseInt(match1.getTeam1goals()),team2goals= Integer.parseInt(match1.getTeam2goals());
               FirebaseDatabase firebaseDatabase2 = FirebaseDatabase.getInstance();
                DatabaseReference newMatch = firebaseDatabase2.getReference("Ended Matches").push();
                if(team1goals > team2goals )
                {   int team1points=team2.getPoints()+3;
                    int team1wins=Integer.parseInt(team2.getWins())+1;
                    int team2lose=Integer.parseInt(team3.getLoses())+1;
                    int team1matchplayed=Integer.parseInt(team2.getMatchesPlayed())+1;
                    int team2matchplayed=Integer.parseInt(team3.getMatchesPlayed())+1;
                   // String Newteam1points=String.valueOf(team1points);
                    String Newteam1wins=String.valueOf(team1wins);
                    String Newteam2lose=String.valueOf(team2lose);
                    String Newteam1matchplayed=String.valueOf(team1matchplayed);
                    String Newteam2matchplayed=String.valueOf(team2matchplayed);

                    HashMap team2Update = new HashMap();
                    team2Update.put("wins",Newteam1wins);
                    team2Update.put("points",team1points);
                    team2Update.put("matchesPlayed",Newteam1matchplayed);
                    DatabaseReference databaseReference2 = firebaseDatabase.getReference("Team");
                    databaseReference2.child(team2.getKey()).updateChildren(team2Update).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {

                            Toast.makeText(StartMatchActivity.this, "Team Data Updated Successfully", Toast.LENGTH_SHORT).show();

                        }
                    });
                    HashMap team3Update = new HashMap();
                    team3Update.put("loses", Newteam2lose);
                    team3Update.put("matchesPlayed", Newteam2matchplayed);
                    DatabaseReference databaseReference3 = firebaseDatabase.getReference("Team");
                    databaseReference3.child(team3.getKey()).updateChildren(team3Update).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {

                            Toast.makeText(StartMatchActivity.this, "Team Data Updated Successfully", Toast.LENGTH_SHORT).show();

                        }
                    });



                }else if(team2goals > team1goals){
                    int team2points=team3.getPoints()+3;
                    int team2wins=Integer.parseInt(team3.getWins())+1;
                    int team1lose=Integer.parseInt(team2.getLoses())+1;
                    int team1matchplayed=Integer.parseInt(team2.getMatchesPlayed())+1;
                    int team2matchplayed=Integer.parseInt(team3.getMatchesPlayed())+1;
                   // String Newteam2points=String.valueOf(team2points);
                    String Newteam2wins=String.valueOf(team2wins);
                    String Newteam1lose=String.valueOf(team1lose);
                    String Newteam1matchplayed=String.valueOf(team1matchplayed);
                    String Newteam2matchplayed=String.valueOf(team2matchplayed);

                    HashMap team3Update = new HashMap();
                    team3Update.put("wins", Newteam2wins);
                    team3Update.put("points", team2points);
                    team3Update.put("matchesPlayed",Newteam2matchplayed);
                    DatabaseReference databaseReference2 = firebaseDatabase.getReference("Team");
                    databaseReference2.child(team3.getKey()).updateChildren(team3Update).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {

                            Toast.makeText(StartMatchActivity.this, "Team Data Updated Successfully", Toast.LENGTH_SHORT).show();

                        }
                    });
                    HashMap team2Update = new HashMap();
                    team2Update.put("loses", Newteam1lose);
                    team2Update.put("matchesPlayed",Newteam1matchplayed);
                    DatabaseReference databaseReference3 = firebaseDatabase.getReference("Team");
                    databaseReference3.child(team2.getKey()).updateChildren(team2Update).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {

                            Toast.makeText(StartMatchActivity.this, "Team Data Updated Successfully", Toast.LENGTH_SHORT).show();

                        }
                    });


                }else if (team1goals == team2goals){

                    int team1points=team2.getPoints()+1;
                    int team2points=team3.getPoints()+1;
                    int team1Draw=Integer.parseInt(team2.getDraws())+1;
                    int team2Draw=Integer.parseInt(team3.getDraws())+1;
                    int team1matchplayed=Integer.parseInt(team2.getMatchesPlayed())+1;
                    int team2matchplayed=Integer.parseInt(team3.getMatchesPlayed())+1;
                    //String Newteam1points=String.valueOf(team1points);
                    //String Newteam2points=String.valueOf(team2points);
                    String Newteam1Draw=String.valueOf(team1Draw);
                    String Newteam2Draw=String.valueOf(team2Draw);
                    String Newteam1matchplayed=String.valueOf(team1matchplayed);
                    String Newteam2matchplayed=String.valueOf(team2matchplayed);
                    HashMap team2Update = new HashMap();
                    team2Update.put("draws", Newteam1Draw);
                    team2Update.put("points", team1points);
                    team2Update.put("matchesPlayed",Newteam1matchplayed);
                    DatabaseReference databaseReference2 = firebaseDatabase.getReference("Team");
                    databaseReference2.child(team2.getKey()).updateChildren(team2Update).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {

                            Toast.makeText(StartMatchActivity.this, "Team Data Updated Successfully", Toast.LENGTH_SHORT).show();

                        }
                    });
                    HashMap team3Update = new HashMap();
                    team3Update.put("draws", Newteam2Draw);
                    team3Update.put("points", team2points);
                    team3Update.put("matchesPlayed",Newteam2matchplayed);
                    DatabaseReference databaseReference3 = firebaseDatabase.getReference("Team");
                    databaseReference3.child(team3.getKey()).updateChildren(team3Update).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {

                            Toast.makeText(StartMatchActivity.this, "Team Data Updated Successfully", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                databaseReferenceP.child("Players").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            for (DataSnapshot item : snapshot.getChildren()) {
                                if (item.child("teamKey").getValue(String.class).equals(team2.getKey()))
                                {
                                    players2.add(item.getValue(Player.class));

                                } else if (item.child("teamKey").getValue(String.class).equals(team3.getKey())) {
                                    players2.add(item.getValue(Player.class));

                                }

                            }
                        for (int i=0;i<players2.size();i++)
                        {
                            Player player4= players2.get(i);
                            int s =Integer.parseInt(player4.getAppearances())+1;
                            String s1=String.valueOf(s);
                            HashMap playerUpdate2= new HashMap();
                            playerUpdate2.put("appearances", s1);
                            DatabaseReference databaseReference21 = firebaseDatabase.getReference("Players");
                            databaseReference21.child(player4.getKey()).updateChildren(playerUpdate2).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    Toast.makeText(StartMatchActivity.this, "Player Data Updated Successfully", Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                        newMatch.setValue(match2);
                        DatabaseReference delete = firebaseDatabase2.getReference("Matches");
                        delete.child(match1.getKey()).removeValue();
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        Toast.makeText(StartMatchActivity.this, "Error", Toast.LENGTH_SHORT).show();

                    }
                });



            }
        });
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teamSpinner.setEnabled(false);
                playerSpinner.setEnabled(false);
                statsSpinner.setEnabled(false);
                btnChangeStats.setEnabled(false);
                if(running)
                {
                    chronometer.stop();
                    pauseOffset=SystemClock.elapsedRealtime()-chronometer.getBase();
                    running=false;
                }
            }
        });
        databaseReference.child("Match Event").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {matchStatsTv1.setText(" ");
                for (DataSnapshot item : snapshot.getChildren()) {
                    if (item.child("matchKey").getValue(String.class).equals(match.getKey())) {
                        MatchEvent newEvent=item.getValue(MatchEvent.class);
                        String oldText=matchStatsTv1.getText().toString();
                        String time= newEvent.getTime();
                        String event= newEvent.getEvent();
                        String playerName= newEvent.getPlayerName();
                        String teamName= newEvent.getTeamName();
                        String newText=oldText+"\n"+ time+" " + playerName + " " + event +" for " + teamName;
                        matchStatsTv1.setText(newText);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        databaseReference.child("Matches").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    if (item.child("key").getValue(String.class).equals(match.getKey())) {
                        match1 = item.getValue(Matches.class);
                        match2=item.getValue(Matches.class);
                        team1resultTv.setText(match1.getTeam1goals().toString().trim());
                        team2resultTv.setText(match1.getTeam2goals().toString().trim());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        databaseReference.child("Team").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                names.clear();
                teams.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    if (item.child("key").getValue(String.class).equals(match1.getTeam1Key())) {

                        teams.add(item.getValue(Team.class));
                        names.add(item.child("teamName").getValue(String.class));
                        team2 = item.getValue(Team.class);

                    } else if (item.child("key").getValue(String.class).equals(match1.getTeam2Key())) {

                        teams.add(item.getValue(Team.class));
                        names.add(item.child("teamName").getValue(String.class));
                        team3 = item.getValue(Team.class);
                    }

                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(StartMatchActivity.this, android.R.layout.simple_spinner_item, names);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                teamSpinner.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        teamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                databaseReference.child("Players").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        playernames.clear();
                        players.clear();
                        for (DataSnapshot item : snapshot.getChildren()) {

                            team1 = teams.get(i);
                            if (item.child("teamKey").getValue(String.class).equals(team1.getKey())) {
                                players.add(item.getValue(Player.class));
                                playernames.add(item.child("name").getValue(String.class));

                            }
                        }
                        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(StartMatchActivity.this, android.R.layout.simple_spinner_item, playernames);
                        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        playerSpinner.setAdapter(arrayAdapter1);

                        playerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int j, long l) {

                                ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(StartMatchActivity.this, android.R.layout.simple_spinner_item, stats);
                                arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                statsSpinner.setAdapter(arrayAdapter2);
                                statsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override

                                    public void onItemSelected(AdapterView<?> adapterView, View view, int m, long l) {
                                        btnChangeStats.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                String S1 = statsSpinner.getSelectedItem().toString();
                                                Player player2 = players.get(j);
                                                if (S1=="Goal") {
                                                    if (team2.getKey().equals(team1.getKey())) {

                                                        int goalNumTeam2 = Integer.parseInt(team2.getGoalsScored()) + 1;
                                                        int goalNumTeam3 = Integer.parseInt(team3.getGoalsAgainst()) + 1;
                                                        int goalDiffTeam2 = Integer.parseInt(team2.getGoalsDifference()) + 1;
                                                        int goalDiffTeam3 = Integer.parseInt(team3.getGoalsDifference()) - 1;
                                                        //int goalNumPlayer = Integer.parseInt(String.valueOf(player2.getGoals())) + 1;
                                                        int goalNumPlayer = (player2.getGoals())+ 1;
                                                        int teamgoal = Integer.parseInt(match1.getTeam1goals()) + 1;
                                                        String goalSumTeam2 = String.valueOf(goalNumTeam2);
                                                        String goalSumTeam3 = String.valueOf(goalNumTeam3);
                                                        String goalDiffSumTeam2 = String.valueOf(goalDiffTeam2);
                                                        String goalDiffSumTeam3 = String.valueOf(goalDiffTeam3);
                                                        String goalSumPlayer = String.valueOf(goalNumPlayer);
                                                        String team2newGoal = String.valueOf(teamgoal);
                                                        String time= chronometer.getText().toString();
                                                        MatchEvent event= new MatchEvent(S1,player2.getName(),match1.getKey(),time,team2.getTeamName());
                                                        DatabaseReference databaseReferenceEvent=firebaseDatabase.getReference("Match Event").push();
                                                        event.setEventKey(databaseReferenceEvent.getKey());
                                                        databaseReferenceEvent.setValue(event);
                                                        HashMap team2Update = new HashMap();
                                                        team2Update.put("goalsScored", goalSumTeam2);
                                                        team2Update.put("goalsDifference", goalDiffSumTeam2);
                                                        DatabaseReference databaseReference1 = firebaseDatabase.getReference("Team");
                                                        databaseReference1.child(team2.getKey()).updateChildren(team2Update).addOnSuccessListener(new OnSuccessListener() {
                                                            @Override
                                                            public void onSuccess(Object o) {
                                                                Toast.makeText(StartMatchActivity.this, "Team Data Updated Successfully", Toast.LENGTH_SHORT).show();
                                                               FirebaseMessaging.getInstance().subscribeToTopic("b");FcmNotificationsSender notificationsSender=new FcmNotificationsSender("/topics/a",
                                                                     "Goal",player2.getName()+" has scored a goal for "+team2.getTeamName(),getApplicationContext(),StartMatchActivity.this);
                                                              notificationsSender.SendNotifications();
                                                            }
                                                        });
                                                        HashMap team3Update = new HashMap();
                                                        team3Update.put("goalsAgainst", goalSumTeam3);
                                                        team3Update.put("goalsDifference", goalDiffSumTeam3);
                                                        DatabaseReference databaseReference2 = firebaseDatabase.getReference("Team");
                                                        databaseReference2.child(team3.getKey()).updateChildren(team3Update).addOnSuccessListener(new OnSuccessListener() {
                                                            @Override
                                                            public void onSuccess(Object o) {

                                                                Toast.makeText(StartMatchActivity.this, "Team Data Updated Successfully", Toast.LENGTH_SHORT).show();

                                                            }
                                                        });
                                                        HashMap playerUpdate = new HashMap();
                                                        playerUpdate.put("goals", goalNumPlayer);
                                                        DatabaseReference databaseReference3 = firebaseDatabase.getReference("Players");
                                                        databaseReference3.child(player2.getKey()).updateChildren(playerUpdate).addOnSuccessListener(new OnSuccessListener() {
                                                            @Override
                                                            public void onSuccess(Object o) {

                                                                Toast.makeText(StartMatchActivity.this, "Team Data Updated Successfully", Toast.LENGTH_SHORT).show();

                                                            }
                                                        });

                                                        HashMap matchUpdate = new HashMap();
                                                        matchUpdate.put("team1goals", team2newGoal);
                                                        DatabaseReference databaseReference4 = firebaseDatabase.getReference("Matches");
                                                        databaseReference4.child(match1.getKey()).updateChildren(matchUpdate).addOnSuccessListener(new OnSuccessListener() {
                                                            @Override
                                                            public void onSuccess(Object o) {

                                                                Toast.makeText(StartMatchActivity.this, "Team Data Updated Successfully", Toast.LENGTH_SHORT).show();

                                                            }
                                                        });


                                                    } else if (team3.getKey().equals(team1.getKey())) {
                                                        int goalNumTeam2 = Integer.parseInt(team3.getGoalsScored()) + 1;
                                                        int goalNumTeam3 = Integer.parseInt(team2.getGoalsAgainst()) + 1;
                                                        int goalDiffTeam2 = Integer.parseInt(team3.getGoalsDifference()) + 1;
                                                        int goalDiffTeam3 = Integer.parseInt(team2.getGoalsDifference()) - 1;
                                                        //int goalNumPlayer = Integer.parseInt(String.valueOf(player2.getGoals())) + 1;
                                                       int goalNumPlayer = (player2.getGoals()) + 1;
                                                        int teamgoal = Integer.parseInt(match1.getTeam2goals()) + 1;
                                                        String goalSumTeam2 = String.valueOf(goalNumTeam2);
                                                        String goalSumTeam3 = String.valueOf(goalNumTeam3);
                                                        String goalDiffSumTeam2 = String.valueOf(goalDiffTeam2);
                                                        String goalDiffSumTeam3 = String.valueOf(goalDiffTeam3);
                                                      //  String goalSumPlayer = String.valueOf(goalNumPlayer);
                                                        String team2newGoal = String.valueOf(teamgoal);
                                                        String time= chronometer.getText().toString();
                                                        MatchEvent event= new MatchEvent(S1,player2.getName(),match1.getKey(),time,team3.getTeamName());
                                                        DatabaseReference databaseReferenceEvent=firebaseDatabase.getReference("Match Event").push();
                                                        event.setEventKey(databaseReferenceEvent.getKey());
                                                        databaseReferenceEvent.setValue(event);
                                                        HashMap team2Update = new HashMap();
                                                        team2Update.put("goalsScored", goalSumTeam2);
                                                        team2Update.put("goalsDifference", goalDiffSumTeam2);
                                                        DatabaseReference databaseReference5 = firebaseDatabase.getReference("Team");
                                                        databaseReference5.child(team3.getKey()).updateChildren(team2Update).addOnSuccessListener(new OnSuccessListener() {
                                                            @Override
                                                            public void onSuccess(Object o) {

                                                                Toast.makeText(StartMatchActivity.this, "Team Data Updated Successfully", Toast.LENGTH_SHORT).show();
                                                                FirebaseMessaging.getInstance().subscribeToTopic("b");
                                                                FcmNotificationsSender notificationsSender=new FcmNotificationsSender("/topics/a",
                                                                        "Goal",player2.getName()+" has scored a goal for "+team3.getTeamName(),getApplicationContext(),StartMatchActivity.this);
                                                                notificationsSender.SendNotifications();
                                                            }
                                                        });
                                                        HashMap team3Update = new HashMap();
                                                        team3Update.put("goalsAgainst", goalSumTeam3);
                                                        team3Update.put("goalsDifference", goalDiffSumTeam3);
                                                        DatabaseReference databaseReference6 = firebaseDatabase.getReference("Team");
                                                        databaseReference6.child(team2.getKey()).updateChildren(team3Update).addOnSuccessListener(new OnSuccessListener() {
                                                            @Override
                                                            public void onSuccess(Object o) {

                                                                Toast.makeText(StartMatchActivity.this, "Team Data Updated Successfully", Toast.LENGTH_SHORT).show();

                                                            }
                                                        });
                                                        HashMap playerUpdate = new HashMap();
                                                        playerUpdate.put("goals", goalNumPlayer);
                                                        DatabaseReference databaseReference7 = firebaseDatabase.getReference("Players");
                                                        databaseReference7.child(player2.getKey()).updateChildren(playerUpdate).addOnSuccessListener(new OnSuccessListener() {
                                                            @Override
                                                            public void onSuccess(Object o) {

                                                                Toast.makeText(StartMatchActivity.this, "Team Data Updated Successfully", Toast.LENGTH_SHORT).show();

                                                            }
                                                        });

                                                        HashMap matchUpdate = new HashMap();
                                                        matchUpdate.put("team2goals", team2newGoal);
                                                        DatabaseReference databaseReference8 = firebaseDatabase.getReference("Matches");
                                                        databaseReference8.child(match1.getKey()).updateChildren(matchUpdate).addOnSuccessListener(new OnSuccessListener() {
                                                            @Override
                                                            public void onSuccess(Object o) {

                                                                Toast.makeText(StartMatchActivity.this, "Team Data Updated Successfully", Toast.LENGTH_SHORT).show();

                                                            }
                                                        });


                                                    }
                                                    team1resultTv.setText(match1.getTeam1goals());
                                                    team2resultTv.setText(match1.getTeam2goals());
                                                } else if (S1 == "Assist") {
                                                    //int assist = Integer.parseInt(String.valueOf(player2.getAssists())) + 1;
                                                    int assist =(player2.getAssists()) + 1;
                                                    //String assistSumPlayer = String.valueOf(assist);

                                                    HashMap playerUpdate = new HashMap();
                                                    playerUpdate.put("assists", assist);
                                                    DatabaseReference databaseReference9 = firebaseDatabase.getReference("Players");
                                                    databaseReference9.child(player2.getKey()).updateChildren(playerUpdate).addOnSuccessListener(new OnSuccessListener() {
                                                        @Override
                                                        public void onSuccess(Object o) {

                                                            Toast.makeText(StartMatchActivity.this, "Team Data Updated Successfully", Toast.LENGTH_SHORT).show();

                                                        }
                                                    });
                                                } else if (S1 == "redCard") {
                                                    if (team2.getKey().equals(team1.getKey())) {
                                                        int card = Integer.parseInt(player2.getRedCards()) + 1;
                                                        int matchcard = Integer.parseInt(match1.getTeam1redCards()) + 1;
                                                        String cardsum = String.valueOf(card);
                                                        String matchcardsum = String.valueOf(matchcard);
                                                        String time= chronometer.getText().toString();
                                                        MatchEvent event= new MatchEvent(S1,player2.getName(),match1.getKey(),time,team2.getTeamName());
                                                        DatabaseReference databaseReferenceEvent=firebaseDatabase.getReference("Match Event").push();
                                                        event.setEventKey(databaseReferenceEvent.getKey());
                                                        databaseReferenceEvent.setValue(event);
                                                        HashMap playerUpdate = new HashMap();
                                                        playerUpdate.put("redCards", cardsum);
                                                        DatabaseReference databaseReference10 = firebaseDatabase.getReference("Players");
                                                        databaseReference10.child(player2.getKey()).updateChildren(playerUpdate).addOnSuccessListener(new OnSuccessListener() {
                                                            @Override
                                                            public void onSuccess(Object o) {

                                                                Toast.makeText(StartMatchActivity.this, "Team Data Updated Successfully", Toast.LENGTH_SHORT).show();
                                                                FirebaseMessaging.getInstance().subscribeToTopic("b");
                                                                FcmNotificationsSender notificationsSender=new FcmNotificationsSender("/topics/a",
                                                                        "Red Card",player2.getName()+" Got a Red Card",getApplicationContext(),StartMatchActivity.this);
                                                                notificationsSender.SendNotifications();
                                                            }
                                                        });
                                                        HashMap matchUpdate = new HashMap();
                                                        matchUpdate.put("team1redCards", matchcardsum);
                                                        DatabaseReference databaseReference11 = firebaseDatabase.getReference("Matches");
                                                        databaseReference11.child(match1.getKey()).updateChildren(matchUpdate).addOnSuccessListener(new OnSuccessListener() {
                                                            @Override
                                                            public void onSuccess(Object o) {

                                                                Toast.makeText(StartMatchActivity.this, "Team Data Updated Successfully", Toast.LENGTH_SHORT).show();

                                                            }
                                                        });
                                                    } else if (team3.getKey().equals(team1.getKey())) {
                                                        int card = Integer.parseInt(player2.getRedCards()) + 1;
                                                        int matchcard = Integer.parseInt(match1.getTeam2redCards()) + 1;
                                                        String cardsum = String.valueOf(card);
                                                        String matchcardsum = String.valueOf(matchcard);
                                                        String time= chronometer.getText().toString();
                                                        MatchEvent event= new MatchEvent(S1,player2.getName(),match1.getKey(),time,team3.getTeamName());
                                                        DatabaseReference databaseReferenceEvent=firebaseDatabase.getReference("Match Event").push();
                                                        event.setEventKey(databaseReferenceEvent.getKey());
                                                        databaseReferenceEvent.setValue(event);
                                                        HashMap playerUpdate = new HashMap();
                                                        playerUpdate.put("redCards", cardsum);
                                                        DatabaseReference databaseReference12 = firebaseDatabase.getReference("Players");
                                                        databaseReference12.child(player2.getKey()).updateChildren(playerUpdate).addOnSuccessListener(new OnSuccessListener() {
                                                            @Override
                                                            public void onSuccess(Object o) {

                                                                Toast.makeText(StartMatchActivity.this, "Team Data Updated Successfully", Toast.LENGTH_SHORT).show();
                                                                FirebaseMessaging.getInstance().subscribeToTopic("b");
                                                                FcmNotificationsSender notificationsSender=new FcmNotificationsSender("/topics/a",
                                                                        "Red Card",player2.getName()+" Got a Red Card",getApplicationContext(),StartMatchActivity.this);
                                                                notificationsSender.SendNotifications();
                                                            }
                                                        });
                                                        HashMap matchUpdate = new HashMap();
                                                        matchUpdate.put("team2redCards", matchcardsum);
                                                        DatabaseReference databaseReference13 = firebaseDatabase.getReference("Matches");
                                                        databaseReference13.child(match1.getKey()).updateChildren(matchUpdate).addOnSuccessListener(new OnSuccessListener() {
                                                            @Override
                                                            public void onSuccess(Object o) {

                                                                Toast.makeText(StartMatchActivity.this, "Team Data Updated Successfully", Toast.LENGTH_SHORT).show();

                                                            }
                                                        });
                                                    }


                                                } else if (S1 == "yellowCard") {
                                                    if (team2.getKey().equals(team1.getKey())) {
                                                        int card = Integer.parseInt(player2.getYellowCards()) + 1;
                                                        int matchcard = Integer.parseInt(match1.getTeam1yellowCards()) + 1;
                                                        String cardsum = String.valueOf(card);
                                                        String matchcardsum = String.valueOf(matchcard);
                                                        String time= chronometer.getText().toString();
                                                        MatchEvent event= new MatchEvent(S1,player2.getName(),match1.getKey(),time,team2.getTeamName());
                                                        DatabaseReference databaseReferenceEvent=firebaseDatabase.getReference("Match Event").push();
                                                        event.setEventKey(databaseReferenceEvent.getKey());
                                                        databaseReferenceEvent.setValue(event);
                                                        HashMap playerUpdate = new HashMap();
                                                        playerUpdate.put("yellowCards", cardsum);
                                                        DatabaseReference databaseReference14 = firebaseDatabase.getReference("Players");
                                                        databaseReference14.child(player2.getKey()).updateChildren(playerUpdate).addOnSuccessListener(new OnSuccessListener() {
                                                            @Override
                                                            public void onSuccess(Object o) {

                                                                Toast.makeText(StartMatchActivity.this, "Team Data Updated Successfully", Toast.LENGTH_SHORT).show();
                                                                FirebaseMessaging.getInstance().subscribeToTopic("b");
                                                                FcmNotificationsSender notificationsSender=new FcmNotificationsSender("/topics/a",
                                                                        "Yellow Card",player2.getName()+" Got a Yellow Card",getApplicationContext(),StartMatchActivity.this);
                                                                notificationsSender.SendNotifications();
                                                            }
                                                        });
                                                        HashMap matchUpdate = new HashMap();
                                                        matchUpdate.put("team1yellowCards", matchcardsum);
                                                        DatabaseReference databaseReference15 = firebaseDatabase.getReference("Matches");
                                                        databaseReference15.child(match1.getKey()).updateChildren(matchUpdate).addOnSuccessListener(new OnSuccessListener() {
                                                            @Override
                                                            public void onSuccess(Object o) {

                                                                Toast.makeText(StartMatchActivity.this, "Team Data Updated Successfully", Toast.LENGTH_SHORT).show();

                                                            }
                                                        });
                                                    } else if (team3.getKey().equals(team1.getKey())) {
                                                        int card = Integer.parseInt(player2.getYellowCards()) + 1;
                                                        int matchcard = Integer.parseInt(match1.getTeam2yellowCards()) + 1;
                                                        String cardsum = String.valueOf(card);
                                                        String matchcardsum = String.valueOf(matchcard);
                                                        String time= chronometer.getText().toString();
                                                        MatchEvent event= new MatchEvent(S1,player2.getName(),match1.getKey(),time,team3.getTeamName());
                                                        DatabaseReference databaseReferenceEvent=firebaseDatabase.getReference("Match Event").push();
                                                        event.setEventKey(databaseReferenceEvent.getKey());
                                                        databaseReferenceEvent.setValue(event);
                                                        HashMap playerUpdate = new HashMap();
                                                        playerUpdate.put("yellowCards", cardsum);
                                                        DatabaseReference databaseReference16 = firebaseDatabase.getReference("Players");
                                                        databaseReference16.child(player2.getKey()).updateChildren(playerUpdate).addOnSuccessListener(new OnSuccessListener() {
                                                            @Override
                                                            public void onSuccess(Object o) {

                                                                Toast.makeText(StartMatchActivity.this, "Team Data Updated Successfully", Toast.LENGTH_SHORT).show();
                                                                FirebaseMessaging.getInstance().subscribeToTopic("b");
                                                                FcmNotificationsSender notificationsSender=new FcmNotificationsSender("/topics/a",
                                                                        "Yellow Card",player2.getName()+" Got a Yellow Card",getApplicationContext(),StartMatchActivity.this);
                                                                notificationsSender.SendNotifications();
                                                            }
                                                        });
                                                        HashMap matchUpdate = new HashMap();
                                                        matchUpdate.put("team2yellowCards", matchcardsum);
                                                        DatabaseReference databaseReference17 = firebaseDatabase.getReference("Matches");
                                                        databaseReference17.child(match1.getKey()).updateChildren(matchUpdate).addOnSuccessListener(new OnSuccessListener() {
                                                            @Override
                                                            public void onSuccess(Object o) {

                                                                Toast.makeText(StartMatchActivity.this, "Team Data Updated Successfully", Toast.LENGTH_SHORT).show();

                                                            }
                                                        });
                                                    }

                                                }


                                            }
                                        });
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                        Toast.makeText(StartMatchActivity.this, "nothing selected ", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                Toast.makeText(StartMatchActivity.this, "nothing selected ", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        Toast.makeText(StartMatchActivity.this, "Please wait ", Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(StartMatchActivity.this, "nothing selected ", Toast.LENGTH_SHORT).show();

            }
        });

    }


}