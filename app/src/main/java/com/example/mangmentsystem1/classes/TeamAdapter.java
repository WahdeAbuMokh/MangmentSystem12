package com.example.mangmentsystem1.classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangmentsystem1.R;
import com.example.mangmentsystem1.ui.HomePage.HomePageFragmnent;
import com.example.mangmentsystem1.ui.teamsEdit.TeamsEdit;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;


public class TeamAdapter extends FirebaseRecyclerAdapter
        <Team,TeamAdapter.teamsViewholder> {
    public TeamAdapter(@NonNull @NotNull FirebaseRecyclerOptions<Team> options)
    {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull TeamAdapter.teamsViewholder holder,
                                    int position, @NonNull @NotNull Team model)
    {


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Team");
        DatabaseReference databaseReference1 = firebaseDatabase.getReference();
        Picasso.get().load(model.getUri()).into(holder.teamImg);
        holder.teamname.setText(model.getTeamName());
        holder.teammanger.setText(model.getManger());
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child(model.getKey()).removeValue();
                StorageReference storageReference = firebaseStorage.getReferenceFromUrl(model.getUri());
                storageReference.delete();
            }
        });

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.EditScrollView.getVisibility()== View.VISIBLE)
                {
                    holder.EditScrollView.setVisibility(View.GONE);

                }
               else if(holder.EditScrollView.getVisibility()== View.GONE)
                {
                    holder.EditScrollView.setVisibility(View.VISIBLE);
                    holder.UpdateteamName.setText(model.getTeamName());
                    holder.UpdateteamManger.setText(model.getManger());
                    holder.UpdateteamPoints.setText(String.valueOf(model.getPoints()));
                    holder.UpdateteamMatchesPlayed.setText(model.getMatchesPlayed());
                    holder.UpdateteamWins.setText(model.getWins());
                    holder.UpdateteamDraws.setText(model.getDraws());
                    holder.UpdateteamLoses.setText(model.getLoses());
                    holder.UpdategoalsScored.setText(model.getGoalsScored());
                    holder.UpdategoalsAgainst.setText(model.getGoalsAgainst());
                    holder.UpdategoalsDifference.setText(model.getGoalsDifference());
                    holder.BtnDone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            HashMap teamUpdate=new HashMap();
                            teamUpdate.put("teamName", holder.UpdateteamName.getText().toString());
                            teamUpdate.put("manger", holder.UpdateteamManger.getText().toString());
                            teamUpdate.put("points", Integer.parseInt(holder.UpdateteamPoints.getText().toString()));
                            teamUpdate.put("matchesPlayed", holder.UpdateteamMatchesPlayed.getText().toString());
                            teamUpdate.put("wins", holder.UpdateteamWins.getText().toString());
                            teamUpdate.put("draws", holder.UpdateteamDraws.getText().toString());
                            teamUpdate.put("loses", holder.UpdateteamLoses.getText().toString());
                            teamUpdate.put("goalsScored", holder.UpdategoalsScored.getText().toString());
                            teamUpdate.put("goalsAgainst", holder.UpdategoalsAgainst.getText().toString());
                            teamUpdate.put("goalsDifference", holder.UpdategoalsDifference.getText().toString());

                            databaseReference1.child("Players").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                    for (DataSnapshot item :snapshot.getChildren())
                                    {
                                        //Player player=item.getValue(Player.class);

                                        if(item.child("teamKey").getValue(String.class).equals(model.getKey()))
                                        {
                                            Player player1=item.getValue(Player.class);
                                            HashMap playerUpdate=new HashMap();
                                            playerUpdate.put("teamName", holder.UpdateteamName.getText().toString());
                                            DatabaseReference databaseReference2 = firebaseDatabase.getReference("Players");
                                            databaseReference2.child(player1.getKey()).updateChildren(playerUpdate).addOnSuccessListener(new OnSuccessListener() {
                                                @Override
                                                public void onSuccess(Object o)
                                                {
                                                    Toast.makeText(view.getContext(), "Player Data Updated Successfully",Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                        }
                                    }

                                }


                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });
                            databaseReference.child(model.getKey()).updateChildren(teamUpdate).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {

                                    Toast.makeText(view.getContext(), "Team Data Updated Successfully",Toast.LENGTH_SHORT).show();
                                    holder.EditScrollView.setVisibility(View.GONE);
                                }
                            });
                        }
                    });


                }

            }
        });


    }


    @NonNull
    @NotNull
    @Override
    public teamsViewholder
    onCreateViewHolder(@NonNull @NotNull ViewGroup parent,
                       int viewType)
    {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_team_view,parent,false);
        return new TeamAdapter.teamsViewholder(view);
    }


     class teamsViewholder
        extends RecyclerView.ViewHolder
     {
        TextView teamname, teammanger;
        ImageView teamImg,UpdateteamImage;
        Button editBtn,deleteBtn,BtnUpdateteamImage,BtnDone;
        ScrollView EditScrollView;
         EditText UpdateteamName,UpdateteamManger,UpdateteamPoints,UpdateteamMatchesPlayed,UpdateteamWins,UpdateteamDraws,UpdateteamLoses
                 ,UpdategoalsScored,UpdategoalsAgainst,UpdategoalsDifference;
        public teamsViewholder(@NonNull @NotNull View itemView)
        {
            super(itemView);
            teamname=itemView.findViewById(R.id.TvteamName);
            teammanger=itemView.findViewById(R.id.TvteamManger);
            teamImg=itemView.findViewById(R.id.teamimg);
            editBtn=itemView.findViewById(R.id.btn_editTeam);
            deleteBtn=itemView.findViewById(R.id.btn_deleteTeam);
            EditScrollView=itemView.findViewById(R.id.EditScrollView);
            //scroll view update frame.
            BtnDone=itemView.findViewById(R.id.btnDone);
            UpdateteamName=itemView.findViewById(R.id.UpdateTeamName);
            UpdateteamManger =itemView.findViewById(R.id.UpdateTeamManger);
            UpdateteamPoints =itemView.findViewById(R.id.UpdateTeamPoints);
            UpdateteamMatchesPlayed=itemView.findViewById(R.id.UpdateTeamMatchesPlayed);
            UpdateteamWins=itemView.findViewById(R.id.UpdateTeamWins);
            UpdateteamDraws=itemView.findViewById(R.id.UpdateTeamDraws);
            UpdateteamLoses =itemView.findViewById(R.id.UpdateTeamLoses);
            UpdategoalsScored =itemView.findViewById(R.id.UpdateGoalsScored);
            UpdategoalsAgainst =itemView.findViewById(R.id.UpdateGoalsAgainst);
            UpdategoalsDifference =itemView.findViewById(R.id.UpdateGoalsDifference);

        }
     }
}
