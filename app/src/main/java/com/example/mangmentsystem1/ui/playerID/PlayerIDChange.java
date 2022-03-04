package com.example.mangmentsystem1.ui.playerID;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.example.mangmentsystem1.databinding.FragmentSlideshowBinding;
import com.example.mangmentsystem1.databinding.PlayerIDChangeFragmentBinding;
import com.example.mangmentsystem1.ui.slideshow.SlideshowViewModel;
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
import java.util.Calendar;
import java.util.HashMap;

public class PlayerIDChange extends Fragment {

    private PlayerIDChangeViewModel playerIDChangeViewModel;
    private PlayerIDChangeFragmentBinding binding;
    Spinner playerSpinner, teamSpinner,newteamSpinner;
    Button btnDone, btnImg;
    EditText name, age, shirt;
    ImageView img1;
    LinearLayout myLayout;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseStorage storage;
    ArrayList<String> names = new ArrayList<>();
    ArrayList<Team> teams = new ArrayList<>();
    ArrayList<String> playerNames = new ArrayList<>();
    ArrayList<Player> players = new ArrayList<>();
    ArrayList<String> newnames = new ArrayList<>();
    ArrayList<Team> newteams = new ArrayList<>();
    Uri filepath;
    final Dialog loadingdialog= new Dialog(this);




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        playerIDChangeViewModel =
                new ViewModelProvider(this).get(PlayerIDChangeViewModel.class);

        binding = PlayerIDChangeFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final TextView textView = binding.textChangeID;
        playerIDChangeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
             //   textView.setText(s);
            }
        });
        myLayout = (LinearLayout) root.findViewById(R.id.mylayout4);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("PlayerImages");
        playerSpinner = root.findViewById(R.id.playerSpinner);
        teamSpinner = root.findViewById(R.id.teamSpinner);
        newteamSpinner = root.findViewById(R.id.newteamSpinner);
        btnDone = root.findViewById(R.id.btn_ChangePlayerID);
        btnImg = root.findViewById(R.id.BtnChangePlayerImg);
        img1=root.findViewById(R.id.playerChangeImg);
        name = root.findViewById(R.id.EdTChangePlayerName);
        age = root.findViewById(R.id.EdTChangePlayerAge);
        shirt = root.findViewById(R.id.EdTChangePlayerNumber);
        databaseReference.child("Team").addValueEventListener(new ValueEventListener() {
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
                teamSpinner.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        teamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                databaseReference.child("Players").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        playerNames.clear();
                        players.clear();
                        for (DataSnapshot item :snapshot.getChildren())
                        {
                            Team team1;
                            team1=teams.get(i);
                            if(item.child("teamKey").getValue(String.class).equals(team1.getKey()))
                            {
                                players.add(item.getValue(Player.class));
                                playerNames.add(item.child("name").getValue(String.class));

                            }
                        }
                        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, playerNames);
                        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        playerSpinner.setAdapter(arrayAdapter1);

                    }


                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        playerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            { Player player1;
              player1=players.get(i);
                name.setText(player1.getName());
                age.setText(player1.getAge());
                shirt.setText(player1.getShirtNumber());
                databaseReference.child("Team").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        newnames.clear();
                        newteams.clear();
                        for (DataSnapshot item : snapshot.getChildren()) {
                            newteams.add(item.getValue(Team.class));
                            newnames.add(item.child("teamName").getValue(String.class));

                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, newnames);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        newteamSpinner.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        Toast.makeText(getActivity(), "Error",Toast.LENGTH_SHORT).show();

                    }
                });
                    Picasso.get().load(player1.getUri()).into(img1);
                    newteamSpinner.setSelection(i);
                age.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                month=month+1;
                                String date= day+"/"+month+"/"+year;
                                age.setText(date);
                            }},year,month,day);
                        datePickerDialog.show();
                    }
                });
                btnImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        StorageReference storageReference = storage.getReferenceFromUrl(player1.getUri());
                        storageReference.delete();
                        SelectImage();

                    }
                });

                newteamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        btnDone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (filepath != null) {
                                    loadingdialog.startLoadingdialog();
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
                                                                    Team team2=newteams.get(i);
                                                                    String url = uri.toString();
                                                                    HashMap playerUpdate=new HashMap();
                                                                    playerUpdate.put("name", name.getText().toString());
                                                                    playerUpdate.put("teamName",team2.getTeamName());
                                                                    playerUpdate.put("teamKey",team2.getKey().toString());
                                                                    playerUpdate.put("age", age.getText().toString());
                                                                    playerUpdate.put("shirtNumber", shirt.getText().toString());

                                                                    playerUpdate.put("uri",url);
                                                                    DatabaseReference databaseReference1=firebaseDatabase.getReference("Players");
                                                                    databaseReference1.child(player1.getKey()).updateChildren(playerUpdate).addOnSuccessListener(new OnSuccessListener() {
                                                                        @Override
                                                                        public void onSuccess(Object o)
                                                                        {
                                                                            loadingdialog.dismissdialog();
                                                                            Toast.makeText(getActivity(), "Player Data Updated Successfully",Toast.LENGTH_SHORT).show();

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
                                                    Toast.makeText(getActivity(), "Error Failed to Update Player", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }else{
                                    loadingdialog.startLoadingdialog();
                                    Team team2=newteams.get(i);
                                    HashMap playerUpdate=new HashMap();
                                    playerUpdate.put("name", name.getText().toString());
                                    playerUpdate.put("teamName",team2.getTeamName());
                                    playerUpdate.put("teamKey",team2.getKey().toString());
                                    playerUpdate.put("age", age.getText().toString());
                                    playerUpdate.put("shirtNumber", shirt.getText().toString());
                                    DatabaseReference databaseReference1=firebaseDatabase.getReference("Players");
                                    databaseReference1.child(player1.getKey()).updateChildren(playerUpdate).addOnSuccessListener(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o) {
                                            loadingdialog.dismissdialog();
                                            Toast.makeText(getActivity(), "Player Data Updated Successfully",Toast.LENGTH_SHORT).show();

                                        }
                                    });


                                }

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
                        img1.setImageBitmap(bitmap);

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
}

