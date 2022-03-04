package com.example.mangmentsystem1.ui.TeamChange;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangmentsystem1.R;
import com.example.mangmentsystem1.classes.Dialog;
import com.example.mangmentsystem1.classes.Player;
import com.example.mangmentsystem1.classes.Team;
import com.example.mangmentsystem1.databinding.TeamChangeFragmentBinding;
import com.example.mangmentsystem1.ui.teamsEdit.TeamsEditViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TeamChange extends Fragment {

    private TeamChangeViewModel mViewModel;
    private TeamChangeFragmentBinding binding;
    LinearLayout myLayout;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,databaseReference7;
    StorageReference storageReference;
    FirebaseStorage storage;
    Spinner spinner;
    ArrayList<String> names= new ArrayList<>();
    ArrayList<Team> teams= new ArrayList<>();
    EditText teamName,teamManger;
    Button   BtnChangeImg,BtnChangeTeam;
    ImageView teamImg;
    Uri filepath;
    final Dialog loadingdialog= new Dialog(this);




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel=new ViewModelProvider(this).get(TeamChangeViewModel.class);

        binding=TeamChangeFragmentBinding.inflate(inflater,container,false);

        View root = binding.getRoot();
        final TextView textView = binding.textChange;
        mViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
               // textView.setText(s);
            }
        });
        spinner=(Spinner)root.findViewById(R.id.spinner);
        firebaseDatabase=FirebaseDatabase.getInstance();
         databaseReference=firebaseDatabase.getReference();
         databaseReference7 = firebaseDatabase.getReference();
        storage=FirebaseStorage.getInstance();
        storageReference= storage.getReference("imagessss");
        teamName=root.findViewById(R.id.EdTChangeTeamName);
        teamManger=root.findViewById(R.id.EdTChangeTeamManger);
        BtnChangeImg=root.findViewById(R.id.BtnChangeImg);
        BtnChangeTeam=root.findViewById(R.id.BtnChangeTeam);
        teamImg=root.findViewById(R.id.teamChangeImg);
        myLayout = (LinearLayout)root.findViewById(R.id.mylayout1);

        databaseReference.child("Team").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                names.clear();
                teams.clear();
                for (DataSnapshot item: snapshot.getChildren()){
                    teams.add(item.getValue(Team.class));
                    names.add(item.child("teamName").getValue(String.class));

                }
                ArrayAdapter<String>arrayAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,names);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                    {
                        Team team1;
                        team1=teams.get(i);
                        Toast.makeText(getActivity(),"you selected Team :"+team1.getTeamName(),Toast.LENGTH_SHORT).show();
                        teamName.setText(team1.getTeamName().toString());
                        teamManger.setText(team1.getManger().toString());
                        Picasso.get().load(team1.getUri()).into(teamImg);



                        BtnChangeImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                StorageReference storageReference = storage.getReferenceFromUrl(team1.getUri());
                                storageReference.delete();
                                SelectImage();
                            }
                        });

                        BtnChangeTeam.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (filepath != null)
                                {     loadingdialog.startLoadingdialog();

                                    StorageReference ref = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(filepath));

                                    ref.putFile(filepath)
                                            .addOnSuccessListener(
                                                    new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                                        @Override
                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                                        {
                                                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri)
                                                                {
                                                                    String url = uri.toString();
                                                                    HashMap teamUpdate=new HashMap();
                                                                    teamUpdate.put("teamName", teamName.getText().toString());
                                                                    teamUpdate.put("manger", teamManger.getText().toString());
                                                                    teamUpdate.put("uri",url);
                                                                    databaseReference7.child("Players").addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                                                            for (DataSnapshot item :snapshot.getChildren())
                                                                            {
                                                                                if(item.child("teamKey").getValue(String.class).equals(team1.getKey()))
                                                                                {
                                                                                    Player player1=item.getValue(Player.class);
                                                                                    HashMap playerUpdate=new HashMap();
                                                                                    playerUpdate.put("teamName",teamName.getText().toString());
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
                                                                            Toast.makeText(view.getContext(), "Update Failed",Toast.LENGTH_SHORT).show();

                                                                        }
                                                                    });
                                                                    DatabaseReference databaseReference1=firebaseDatabase.getReference("Team");
                                                                    databaseReference1.child(team1.getKey()).updateChildren(teamUpdate).addOnSuccessListener(new OnSuccessListener() {
                                                                        @Override
                                                                        public void onSuccess(Object o) {
                                                                            loadingdialog.dismissdialog();
                                                                            Toast.makeText(getActivity(), "Team Data Updated Successfully",Toast.LENGTH_SHORT).show();

                                                                        }
                                                                    });

                                                                }
                                                            });

                                                        }
                                                    })
                                            .addOnFailureListener(new OnFailureListener()
                                            {
                                                @Override
                                                public void onFailure(@NonNull Exception e)
                                                {
                                                    Toast.makeText(getActivity(), "nothing selected ", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }else{
                                    loadingdialog.startLoadingdialog();
                                    HashMap teamUpdate=new HashMap();
                                    teamUpdate.put("teamName", teamName.getText().toString());
                                    teamUpdate.put("manger", teamManger.getText().toString());
                                    databaseReference7.child("Players").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                            for (DataSnapshot item :snapshot.getChildren())
                                            {
                                                if(item.child("teamKey").getValue(String.class).equals(team1.getKey()))
                                                {
                                                    Player player1=item.getValue(Player.class);
                                                    HashMap playerUpdate=new HashMap();
                                                    playerUpdate.put("teamName",teamName.getText().toString());
                                                    DatabaseReference databaseReference3 = firebaseDatabase.getReference("Players");
                                                    databaseReference3.child(player1.getKey()).updateChildren(playerUpdate).addOnSuccessListener(new OnSuccessListener() {
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
                                            Toast.makeText(view.getContext(), "Update Failed",Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                    DatabaseReference databaseReference1=firebaseDatabase.getReference("Team");
                                    databaseReference1.child(team1.getKey()).updateChildren(teamUpdate).addOnSuccessListener(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o)
                                        {
                                            loadingdialog.dismissdialog();
                                            Toast.makeText(getActivity(), "Team Data Updated Successfully",Toast.LENGTH_SHORT).show();

                                        }
                                    });


                                }

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
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error",Toast.LENGTH_SHORT).show();

            }
        });


        return root;
    }




    private void SelectImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        chooseImage1.launch(intent);

    }

    ActivityResultLauncher<Intent> chooseImage1 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result)
                {
                    Intent data = result.getData();
                    filepath = data.getData();

                        try {

                            ContentResolver resolver = getActivity().getContentResolver();
                            ImageDecoder.Source source = ImageDecoder.createSource(resolver, filepath);
                            Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                            teamImg.setImageBitmap(bitmap);

                        } catch (IOException e) {
                            // Log the exception
                            e.printStackTrace();
                        }


                }
            }
    );
    public String GetFileExtension(Uri uri) {
        ContentResolver contentResolver =getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}