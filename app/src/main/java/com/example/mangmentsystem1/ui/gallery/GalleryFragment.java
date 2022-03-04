package com.example.mangmentsystem1.ui.gallery;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mangmentsystem1.ExcelFileActivity2;
import com.example.mangmentsystem1.ExcelFileActivity3;
import com.example.mangmentsystem1.R;
import com.example.mangmentsystem1.classes.Dialog;
import com.example.mangmentsystem1.classes.Player;
import com.example.mangmentsystem1.classes.Team;
import com.example.mangmentsystem1.databinding.FragmentGalleryBinding;
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

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;
    LinearLayout myLayout;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    StorageReference storageReference;
    FirebaseStorage storage;
    Uri filepath;
    final Dialog loadingdialog= new Dialog(this);
    DatePickerDialog.OnDateSetListener setListener;
    ArrayList<String> names= new ArrayList<>();
    ArrayList<Team> teams= new ArrayList<>();
    private Spinner spinner;
    private EditText player_name,shirt_number,player_age;
    private Button btn_add_player,btn_Image_player,btnExcel;
    private ImageView imageView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        spinner=(Spinner)root.findViewById(R.id.spinner1);
        player_name=root.findViewById(R.id.EdtPlayerName);
        player_age=root.findViewById(R.id.EdtPlayerAge);
        shirt_number=root.findViewById(R.id.EdtShirtNumber);
        btn_Image_player=root.findViewById(R.id.btnPlayerImage);
        btnExcel=root.findViewById(R.id.btnAddPlayerExcel);
        Calendar calendar = Calendar.getInstance();
        final int year= calendar.get(Calendar.YEAR);
        final int month= calendar.get(Calendar.MONTH);
        final int day= calendar.get(Calendar.DAY_OF_MONTH);
        btn_add_player=root.findViewById(R.id.btnAddPlayer);
        imageView =root.findViewById(R.id.imgViewPlayer);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
        storage=FirebaseStorage.getInstance();
        storageReference= storage.getReference("PlayerImages");
        btnExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), ExcelFileActivity2.class);
                view.getContext().startActivity(intent);
            }
        });
        databaseReference.child("Team").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot)
            {
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
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                Team team1;
                team1=teams.get(i);
                Toast.makeText(getActivity(),"you selected Team :"+team1.getTeamName(),Toast.LENGTH_LONG).show();

                btn_Image_player.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SelectImage();
                    }
                });

                player_age.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                month=month+1;
                                String date= day+"/"+month+"/"+year;
                                player_age.setText(date);
                            }},year,month,day);
                        datePickerDialog.show();
                    }
                });

                btn_add_player.setOnClickListener(new View.OnClickListener() {
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
                                                            String url = uri.toString();
                                                            String TeamName=team1.getTeamName();
                                                            String TeamKey=team1.getKey();
                                                            String number=shirt_number.getText().toString();
                                                            String playerName=player_name.getText().toString();
                                                            String date=player_age.getText().toString();
                                                            Player player=new Player(playerName,TeamKey,TeamName,url,date,number);
                                                            databaseReference=firebaseDatabase.getReference("Players").push();
                                                            player.setKey(databaseReference.getKey());
                                                            databaseReference.setValue(player);
                                                            loadingdialog.dismissdialog();

                                                            Toast.makeText(getActivity(), "Player Added successfully",Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                }
                                            })
                                    .addOnFailureListener(new OnFailureListener()
                                    {
                                        @Override
                                        public void onFailure(@NonNull Exception e)
                                        {
                                            Toast.makeText(getActivity(), "Error Failed to add Player ", Toast.LENGTH_SHORT).show();
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
                        imageView.setImageBitmap(bitmap);

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