package com.example.socialmediaintegration;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.login.LoginManager;

public class View_FbInfo extends AppCompatActivity {
    ImageView image;
    TextView name;
    TextView em;
    Button fb_logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_fbinfo);

        Intent intent = getIntent();
        String first = intent.getStringExtra("first");
        String last = intent.getStringExtra("last");
        String email = intent.getStringExtra("email");
        String img = intent.getStringExtra("image");

        image=(ImageView)findViewById(R.id.fbprofile_img);
        name=(TextView)findViewById(R.id.fbname_txt);
        em=(TextView)findViewById(R.id.fbemail_txt);
        fb_logout=findViewById(R.id.fblogout_btn);

        name.setText(first+" "+last);
        em.setText(email);
        RequestOptions requestOptions =new RequestOptions();
        requestOptions.dontAnimate();

        Glide.with(View_FbInfo.this).load(img).into(image);

        fb_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                name.setText("");
                em.setText("");
                Intent login = new Intent(View_FbInfo.this, MainActivity.class);
                startActivity(login);
                finish();
            }
        });
    }
    }
