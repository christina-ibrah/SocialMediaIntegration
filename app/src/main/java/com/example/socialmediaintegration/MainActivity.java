package com.example.socialmediaintegration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
     Button fb_login;
    Button gmail_login;
     CallbackManager callbackManager;
     LoginManager loginManager;
     GoogleApiClient googleApiClient;
     static final int SIGN_IN=1;
    AccessTokenTracker tokenTracker;
    ProfileTracker profileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         fb_login=findViewById(R.id.fb_btn);
         gmail_login=findViewById(R.id.gmail_btn);

        callbackManager = CallbackManager.Factory.create();

         fb_login.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList( "email",
                         "public_profile",
                         "user_birthday"));
                 LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                     @Override
                     public void onSuccess(LoginResult loginResult) {
                         fb_login.setVisibility(View.INVISIBLE);
                         gmail_login.setVisibility(View.INVISIBLE);
                         Toast.makeText(MainActivity.this, "logging in..", Toast.LENGTH_SHORT).show();

                         GraphRequest request =GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                             @Override
                             public void onCompleted(@Nullable JSONObject object, @Nullable GraphResponse graphResponse) {
                                 try {
                                     String first_name=object.getString("first_name");
                                     String last_name=object.getString("last_name");
                                     String email=object.getString("email");
                                     String image_url = object.getJSONObject("picture").getJSONObject("data").getString("url");

                                     Intent i=new Intent(MainActivity.this,View_FbInfo.class);
                                     i.putExtra("first",first_name);
                                     i.putExtra("last",last_name);
                                     i.putExtra("email",email);
                                     i.putExtra("image",image_url);
                                     startActivity(i);
                                     finish();


                                 } catch (JSONException e) {
                                     e.printStackTrace();
                                 }
                             }
                         });
                         Bundle pars=new Bundle();
                         pars.putString("fields","first_name,last_name,email,id,picture.type(large)");
                         request.setParameters(pars);
                         request.executeAsync();

                     }
                     @Override
                     public void onCancel() {
                         fb_login.setVisibility(View.VISIBLE);
                         gmail_login.setVisibility(View.VISIBLE);
                         Toast.makeText(MainActivity.this, "cancel", Toast.LENGTH_SHORT).show();
                     }

                     @Override
                     public void onError(FacebookException error) {
                         fb_login.setVisibility(View.VISIBLE);
                         gmail_login.setVisibility(View.VISIBLE);
                         Toast.makeText(MainActivity.this, "error : "+error, Toast.LENGTH_SHORT).show();
                     }
                 });
             }
         });
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();
        gmail_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         callbackManager.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Google Login Failed", Toast.LENGTH_LONG).show();
            }
        }
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }




}