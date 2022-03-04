package com.example.mangmentsystem1.ui.home;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mangmentsystem1.ExcelFileActivity;
import com.example.mangmentsystem1.R;
import com.example.mangmentsystem1.StartMatchActivity;
import com.example.mangmentsystem1.classes.Dialog;
import com.example.mangmentsystem1.classes.Team;
import com.example.mangmentsystem1.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.poi.xdgf.geom.Dimension2dDouble;

import java.io.IOException;

public class HomeFragment extends Fragment {

    public static final int cellCount = 3;
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    StorageReference storageReference;
    FirebaseStorage storage;
    LinearLayout myLayout;
    private Button btn_AddTeam,btn_show,btn_Image,btn_Excel;
    private EditText edit_teamName,edit_teamManger;
     Uri filepath;
    final Dialog loadingdialog= new Dialog(this);


    private ImageView imageView;
    int Image_Request_Code = 7;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        final TextView textView = binding.textHome;
        View root = binding.getRoot();
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
             //   textView.setText(s);
            }
        });

        edit_teamName=root.findViewById(R.id.EdtTeamName);
        edit_teamManger=root.findViewById(R.id.EdtTeamManger);
        btn_AddTeam=root.findViewById(R.id.btnAddTeam);
        btn_show=root.findViewById(R.id.btnShowTeams);
        btn_Image=root.findViewById(R.id.btnImage);
        btn_Excel=root.findViewById(R.id.btnExcelTeam);
        imageView =root.findViewById(R.id.imgView);
        firebaseDatabase = FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        storageReference= storage.getReference("imagessss");
        myLayout = (LinearLayout)root.findViewById(R.id.mylayout1);


        btn_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_AddTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();

            }
        });

        btn_Excel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), ExcelFileActivity.class);
                view.getContext().startActivity(intent);
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

    private void uploadImage() {
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
                                                String teamName=edit_teamName.getText().toString().trim();
                                                String teamManger=edit_teamManger.getText().toString().trim();
                                                Toast.makeText(getActivity(), "Team Added Successfully",Toast.LENGTH_SHORT).show();
                                                Team team = new Team(teamName,teamManger,url);
                                                databaseReference=firebaseDatabase.getReference("Team").push();
                                                team.setKey(databaseReference.getKey());
                                                databaseReference.setValue(team);
                                                loadingdialog.dismissdialog();

                                            }
                                        });

                                }
                            })
                    .addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Toast.makeText(getActivity(), "Failed ", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    ActivityResultLauncher<Intent> chooseImage1 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result)
                {
                    Intent data= result.getData();
                    filepath=data.getData();
                    try {

                        ContentResolver resolver= getActivity().getContentResolver();
                        ImageDecoder.Source source = ImageDecoder.createSource(resolver,filepath);
                        Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                        imageView.setImageBitmap(bitmap);
                    }

                    catch (IOException e) {
                        // Log the exception
                        e.printStackTrace();
                    }

                }
            }
    );
    public String GetFileExtension(Uri uri) {
        ContentResolver contentResolver =getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}