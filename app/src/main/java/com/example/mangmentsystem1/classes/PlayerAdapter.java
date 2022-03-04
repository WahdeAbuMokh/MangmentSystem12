package com.example.mangmentsystem1.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangmentsystem1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class PlayerAdapter  extends FirebaseRecyclerAdapter<Player,PlayerAdapter.playersViewholder> {


    public PlayerAdapter(@NonNull @NotNull FirebaseRecyclerOptions<Player> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull PlayerAdapter.playersViewholder holder
                                    , int position, @NonNull @NotNull Player model)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Players");
        Picasso.get().load(model.getUri()).into(holder.playerImg);
        holder.playerName.setText(model.getName());
        holder.playerTeam.setText(model.getTeamName());
        holder.playerAge.setText(model.getAge());
        holder.playerShirtNumber.setText(model.getShirtNumber());
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child(model.getKey()).removeValue();
                StorageReference storageReference = firebaseStorage.getReferenceFromUrl(model.getUri());
                storageReference.delete();
            }
        });
        holder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.scrollView.getVisibility()== View.VISIBLE)
                {
                    holder.scrollView.setVisibility(View.GONE);

                }
                else if(holder.scrollView.getVisibility()== View.GONE)
                {
                    holder.scrollView.setVisibility(View.VISIBLE);
                    holder.goals.setText(String.valueOf(model.getGoals()));
                    holder.assists.setText(String.valueOf(model.getAssists()));
                    holder.redCards.setText(model.getRedCards());
                    holder.yellowCards.setText(model.getYellowCards());
                    holder.appearances.setText(model.getAppearances());
                    holder.done_btn.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view) {
                            HashMap playerUpdate=new HashMap();
                            playerUpdate.put("goals", Integer.parseInt(holder.goals.getText().toString()));
                            playerUpdate.put("assists", Integer.parseInt(holder.assists.getText().toString()));
                            playerUpdate.put("redCards", holder.redCards.getText().toString());
                            playerUpdate.put("yellowCards", holder.yellowCards.getText().toString());
                            playerUpdate.put("appearances", holder.appearances.getText().toString());
                            databaseReference.child(model.getKey()).updateChildren(playerUpdate).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    Toast.makeText(view.getContext(), "Player Data Updated Successfully",Toast.LENGTH_SHORT).show();
                                    holder.scrollView.setVisibility(View.GONE);

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
    public playersViewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_player_view,parent,false);
        return new PlayerAdapter.playersViewholder(view);
    }

     class playersViewholder extends RecyclerView.ViewHolder
    {//variables
        TextView playerName,playerTeam,playerAge,playerShirtNumber;
        EditText goals,assists,redCards,yellowCards,appearances;
        Button edit_btn,delete_btn,done_btn;
        ImageView playerImg;
        ScrollView scrollView;
        public playersViewholder(@NonNull @NotNull View itemView)
        {
            super(itemView);

        //find variables by id

            playerName=itemView.findViewById(R.id.Tv_PlayerName);
            playerTeam=itemView.findViewById(R.id.Tv_PlayerTeam);
            playerAge=itemView.findViewById(R.id.Tv_PlayerAge);
            playerShirtNumber=itemView.findViewById(R.id.Tv_PlayerNumber);
            goals=itemView.findViewById(R.id.UpdatePlayerGoals);
            assists=itemView.findViewById(R.id.UpdatePlayerAssist);
            redCards=itemView.findViewById(R.id.UpdatePlayerRedCards);
            yellowCards=itemView.findViewById(R.id.UpdatePlayerYellowCards);
            appearances=itemView.findViewById(R.id.UpdatePlayerAppearances);
            edit_btn=itemView.findViewById(R.id.rv_PlayerEdit);
            delete_btn=itemView.findViewById(R.id.rv_PlayerDelete);
            done_btn=itemView.findViewById(R.id.btn_PlayerDone);
            playerImg=itemView.findViewById(R.id.rv_Playerimg);
            scrollView=itemView.findViewById(R.id.EditPlayerScrollView);

        }
    }
}
