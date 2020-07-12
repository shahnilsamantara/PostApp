package com.shahnil.post;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FullPagePost extends AppCompatActivity {


    private TextView mName;
    private TextView mPost;
    private TextView mDate;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_data_layout);

        mName = findViewById(R.id.textViewName);
        mPost = findViewById(R.id.textViewPost);
        imageView = findViewById(R.id.imageid);
        mDate = findViewById(R.id.textViewDate);

        Intent intent = getIntent();

        userinformation user = (userinformation) intent.getSerializableExtra(FragmentProfile.LIST);

        if(user != null) {

            mName.setText(user.getmName());
            mPost.setText(user.getmText1());
            mDate.setText(user.getmDate());

            Picasso.get().load(user.getImageUrl())
                    .fit()
                    .centerCrop()
                    .into(imageView);
        }



    }
}