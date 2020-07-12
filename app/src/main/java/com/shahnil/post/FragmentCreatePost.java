package com.shahnil.post;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class FragmentCreatePost extends Fragment {


    static int PICK_IMAGE_REQUEST = 1;


    private Button mChooseButton;
    private    Button mUploadButton;
    private  EditText mEditTextPost;

    private ImageView mimageview;
    private ProgressBar mprogressbar;
    private Uri mimageuri;

    private  userinformation userinformation;

    private String Date;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private FirebaseFirestore fdb = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private String UserId;
    private CollectionReference notebookref ;

    private int count = 1 ;






    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_create_post,container,false);



        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.hide();


        mChooseButton = rootView.findViewById(R.id.chooseImage);
        mUploadButton = rootView.findViewById(R.id.uploadButton);

        mEditTextPost = rootView.findViewById(R.id.editTextPost);



        mprogressbar = rootView.findViewById(R.id.progressbar);
        mimageview = rootView.findViewById(R.id.image_view);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("UserDataPost");
        mStorageRef = FirebaseStorage.getInstance().getReference("UserDataPost");
        mAuth = FirebaseAuth.getInstance();
        UserId = mAuth.getCurrentUser().getUid();
        notebookref = fdb.collection("UserDataPost");


        Calendar calendar = Calendar.getInstance();

         Date = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());








         if(count ==1 ) {

             mUploadButton.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {

                     count = 2;
                     uploadfile();
                 }

             });
         }


        mChooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openFileChooser();

            }
        });




        return rootView;

    }



    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null){
            mimageuri = data.getData();
            mimageview.setImageURI(mimageuri);
        }
    }

    private String getFileExtention(Uri uri){
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(cR.getType(uri));
    }



    private void uploadfile(){

        String PostData = mEditTextPost.getText().toString().trim();

        if (PostData.isEmpty()) {
            mEditTextPost.setError("Set Caption");
            mEditTextPost.requestFocus();
            count = 1 ;
            return;
        }

        if(mimageuri != null){

            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtention(mimageuri));

            fileReference.putFile(mimageuri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mprogressbar.setProgress(0);
                                }
                            }, 500);




                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;
                                    String usableUrl = downloadUrl.toString();

                                    String PostData = mEditTextPost.getText().toString().trim();

                                    UserId = mAuth.getCurrentUser().getUid();
                                    userinformation = new userinformation(UserId,PostData,Date,usableUrl);

                                    fdb.collection("Users").document(UserId).collection("posts")
                                            .add(userinformation)

                                          .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                               public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getActivity(), "Posted", Toast.LENGTH_SHORT).show();
                                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                                            new FragmentHome()).commit();


                                            }
                                         })
                                        .addOnFailureListener(new OnFailureListener() {
                                               public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getActivity(), "failed to update", Toast.LENGTH_SHORT).show();
                                                   count = 1 ;
                                             }
                                          });






                                }
                            });








                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            count = 1 ;

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            mprogressbar.setProgress((int) progress);

                        }
                    });



        } else{
            Toast.makeText(getActivity(), "no image selected", Toast.LENGTH_SHORT).show();
            count = 1 ;
        }

    }







}
