package com.example.mangmentsystem1.classes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangmentsystem1.R;
import com.example.mangmentsystem1.StartMatchActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class MatchAdapter extends FirebaseRecyclerAdapter<Matches,MatchAdapter.matchesViewholder>
{
    public MatchAdapter(@NonNull @NotNull FirebaseRecyclerOptions<Matches> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull MatchAdapter.matchesViewholder holder, int position, @NonNull @NotNull Matches model)
    { FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Matches");
        holder.team1Tv.setText(model.getTeam1name());
        holder.team2Tv.setText(model.getTeam2name());
        holder.date.setText(model.getDate());
       holder.time.setText(model.getTime());
        Picasso.get().load(model.getTeam1uri()).into(holder.team1img);
       Picasso.get().load(model.getTeam2uri()).into(holder.team2img);
        holder.btndeleteMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                databaseReference.child(model.getKey()).removeValue();
            }
        });
        holder.btnStartMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), StartMatchActivity.class);
                intent.putExtra("match",model);
                view.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @NotNull
    @Override
    public MatchAdapter.matchesViewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_match_view,parent,false);
        return new MatchAdapter.matchesViewholder(view);
    }

    public class matchesViewholder extends RecyclerView.ViewHolder {

        ImageView team1img,team2img;
        TextView team1Tv,team2Tv,date,time;
        Button btndeleteMatch,btnStartMatch;
        public matchesViewholder(@NonNull @NotNull View itemView) {
            super(itemView);

            team1img=itemView.findViewById(R.id.team1Rvimg);
            team2img=itemView.findViewById(R.id.team2Rvimg);
            team1Tv=itemView.findViewById(R.id.team1Rvtv);
            team2Tv=itemView.findViewById(R.id.team2Rvtv);
            date=itemView.findViewById(R.id.TvRvMatchDate);
            time=itemView.findViewById(R.id.TvRvMatchTime);
            btndeleteMatch=itemView.findViewById(R.id.BtnDeleteMatch);
            btnStartMatch=itemView.findViewById(R.id.BtnStartMatch);

        }
    }
}
