package com.proj.votingclient.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.proj.votingclient.DatabaseHandler.DatabaseOperations;
import com.proj.votingclient.HomeActivity;
import com.proj.votingclient.R;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import java.util.Map;

public class ChangePasswordFragment extends Fragment {
    DatabaseOperations dbops = new DatabaseOperations();
    String passWordhash, password, userNamehash, username;
    EditText passwordfield, usernamefield;
    Boolean pswdcheck, usrcheck;
    Button submitbutton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        this.usernamefield = (EditText) view.findViewById(R.id.username);
        this.passwordfield = (EditText) view.findViewById(R.id.password);
        this.submitbutton = (Button) view.findViewById(R.id.submitbutton);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);
        final String voterid = HomeActivity.voteridreturn();
        this.submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                ChangePasswordFragment.this.validator(voterid, navController, view2);
            }
        });
    }

    public void validator(String voterid, final NavController navController, View v) {
        if (this.usernamefield.getText().toString().isEmpty()) {
            this.usernamefield.setError("Please Enter Your Username");
        } else if (this.passwordfield.getText().toString().isEmpty()) {
            this.passwordfield.setError("Please Enter Your Password");
        } else {
            this.username = this.usernamefield.getText().toString();
            this.password = this.passwordfield.getText().toString();
            DocumentReference docRef = this.dbops.getUserCollectionRef().document(voterid);
            docRef.get().addOnSuccessListener(new OnSuccessListener() {
                @Override // com.google.android.gms.tasks.OnSuccessListener
                public final void onSuccess(Object obj) {
                    ChangePasswordFragment.this.m235x55c65121(navController, (DocumentSnapshot) obj);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override // com.google.android.gms.tasks.OnFailureListener
                public final void onFailure(Exception exc) {
                    toastMaker("Failed to Connect to Server. Please Try Again");
                }
            });
        }
    }

    public void m235x55c65121(NavController navController, DocumentSnapshot documentSnapshot) {
        if (documentSnapshot.exists()) {
            Map<String, Object> data = documentSnapshot.getData();
            this.userNamehash = data.get("Username").toString();
            this.passWordhash = data.get("Password").toString();
            this.usrcheck = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8().matches(this.username, this.userNamehash);
            this.pswdcheck = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8().matches(this.password, this.passWordhash);
            if (this.usrcheck && this.pswdcheck) {
                toastMaker("Username and Password Correct");
                navController.navigate(R.id.enterpasswordlayout);
                return;
            }
            toastMaker("Incorrect Username or Password. Please Try Again");
            return;
        }
        toastMaker("No User Account Found. Please Try Again");
    }
    public void toastMaker(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}