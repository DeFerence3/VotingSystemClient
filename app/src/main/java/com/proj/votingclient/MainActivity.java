package com.proj.votingclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.agrawalsuneet.dotsloader.loaders.LinearDotsLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.proj.votingclient.DatabaseHandler.DatabaseOperations;

/* loaded from: classes5.dex */
public class MainActivity extends AppCompatActivity {
    DatabaseOperations dataops = new DatabaseOperations();
    LinearDotsLoader linearDotsLoader;
    Button login;
    String passWord,passWordhash, profilepicUrl, userName, userNamehash, voterID;
    EditText password, voterid, username;
    TextView tv_register;
    boolean usrcheck, isAuthenticated, pswdcheck;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(isAuthenticated()) {
            login();
        } else {

            this.tv_register = (TextView) findViewById(R.id.registerhere);
            this.login = (Button) findViewById(R.id.loginbutton);
            this.voterid = (EditText) findViewById(R.id.voterid);
            this.username = (EditText) findViewById(R.id.username);
            this.password = findViewById(R.id.password);
            this.linearDotsLoader = findViewById(R.id.loginprogress);

            this.login.setOnClickListener(new View.OnClickListener() {
                @Override
                public final void onClick(View view) {
                    MainActivity.this.m831lambda$onCreate$2$comtestonlinevotingsystemMainActivity(view);
                }
            });

            login.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }
            });
            this.tv_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public final void onClick(View view) {
                    MainActivity.this.m832lambda$onCreate$3$comtestonlinevotingsystemMainActivity(view);
                }
            });
        }
    }

    private boolean isAuthenticated() {

        SharedPreferences sharedPreferences = getSharedPreferences("AuthToken", MODE_PRIVATE);
        return sharedPreferences.getBoolean("isAuthenticated", false);

    }

    public /* synthetic */ void m831lambda$onCreate$2$comtestonlinevotingsystemMainActivity(View v) {
        if (this.voterid.getText().toString().isEmpty()) {
            this.voterid.setError("Please Enter Your Voter's ID");
        } else if (this.username.getText().toString().isEmpty()) {
            this.username.setError("Please Enter Your Username");
        } else if (this.password.getText().toString().isEmpty()) {
            this.password.setError("Please Enter Your Password");
        } else {
            this.linearDotsLoader.setVisibility(View.VISIBLE);
            this.voterID = this.voterid.getText().toString();
            this.userName = this.username.getText().toString();
            this.passWord = this.password.getText().toString();
            credentialsAuthenticator(null);
            DocumentReference docRef = this.dataops.getUserCollectionRef().document(this.voterID);
            docRef.get().addOnSuccessListener(new OnSuccessListener() {
                @Override
                public final void onSuccess(Object obj) {
                    MainActivity.this.credentialsAuthenticator((DocumentSnapshot) obj);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public final void onFailure(Exception exc) {
                    ToastMaker("Failed to Connect to Server. Please Try Again");
                    linearDotsLoader.setVisibility(View.GONE);
                }
            });
        }
    }

    public void credentialsAuthenticator(DocumentSnapshot documentSnapshot) {

        SharedPreferences sharedPreferences = getSharedPreferences("AuthToken", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isAuthenticated",true);
        editor.apply();
        login();

/*        if (documentSnapshot.exists()) {
            Map<String, Object> data = documentSnapshot.getData();
            this.userNamehash = data.get("Username").toString();
            this.passWordhash = data.get("Password").toString();
            Object pictureObj = data.get("picture");
            if (pictureObj != null) {
                this.profilepicUrl = pictureObj.toString();
            } else {
                ToastMaker("No Profile Picture found");
            }
            usrcheck = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8().matches(this.userName, this.userNamehash);
            pswdcheck = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8().matches(this.passWord, this.passWordhash);
            if (usrcheck && pswdcheck ) {
                SharedPreferences sharedPreferences = getSharedPreferences("AuthToken", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isAuthenticated",true);
                editor.apply();
                login();
                return;
            }
            ToastMaker("Incorrect Username or Password. Please Try Again");
            this.linearDotsLoader.setVisibility(View.GONE);
            return;
        }
        ToastMaker("No User Account Found. Please Try Again");
        this.linearDotsLoader.setVisibility(View.GONE);*/
    }

    private void login() {
        Bundle userdata = new Bundle();
        userdata.putString("username", this.userName);
        userdata.putString("voterid", this.voterID);
        userdata.putString("profilepicture", this.profilepicUrl);
        Intent a = new Intent(this, HomeActivity.class);
        a.putExtra("userdatabundle", userdata);
        startActivity(a);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    public /* synthetic */ void m832lambda$onCreate$3$comtestonlinevotingsystemMainActivity(View v) {
        Intent toresiter = new Intent(this, RegisterActivity.class);
        startActivity(toresiter);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    void ToastMaker(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
