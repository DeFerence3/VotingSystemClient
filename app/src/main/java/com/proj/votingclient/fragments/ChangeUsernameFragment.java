package com.proj.votingclient.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

/* loaded from: classes5.dex */
public class ChangeUsernameFragment extends Fragment {
    DatabaseOperations dbops = new DatabaseOperations();
    String passWordhash;
    String password;
    EditText passwordfield;
    Boolean pswdcheck;
    Button submitbutton;
    String userNamehash;
    String username;
    EditText usernamefield;
    Boolean usrcheck;
    public String voterid;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_username, container, false);
        this.usernamefield = (EditText) view.findViewById(R.id.username);
        this.passwordfield = (EditText) view.findViewById(R.id.password);
        this.submitbutton = (Button) view.findViewById(R.id.submitbutton);
        return view;
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);
        this.voterid = HomeActivity.voteridreturn();
        this.submitbutton.setOnClickListener(new View.OnClickListener() { // from class: com.test.onlinevotingsystem.ChangeUsernameFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ChangeUsernameFragment.this.m230x872ee91a(navController, view2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onViewCreated$2$com-test-onlinevotingsystem-ChangeUsernameFragment */
    public /* synthetic */ void m230x872ee91a(final NavController navController, View v) {
        if (this.usernamefield.getText().toString().isEmpty()) {
            this.usernamefield.setError("Please Enter Your Username");
        } else if (this.passwordfield.getText().toString().isEmpty()) {
            this.passwordfield.setError("Please Enter Your Password");
        } else {
            this.username = this.usernamefield.getText().toString();
            this.password = this.passwordfield.getText().toString();
            DocumentReference docRef = this.dbops.getUserCollectionRef().document(this.voterid);
            docRef.get().addOnSuccessListener(new OnSuccessListener() { // from class: com.test.onlinevotingsystem.ChangeUsernameFragment$$ExternalSyntheticLambda1
                @Override // com.google.android.gms.tasks.OnSuccessListener
                public final void onSuccess(Object obj) {
                    ChangeUsernameFragment.this.m232x2b7db45c(navController, (DocumentSnapshot) obj);
                }
            }).addOnFailureListener(new OnFailureListener() { // from class: com.test.onlinevotingsystem.ChangeUsernameFragment$$ExternalSyntheticLambda2
                @Override // com.google.android.gms.tasks.OnFailureListener
                public final void onFailure(Exception exc) {
                    toastMaker("Failed to Connect to Server. Please Try Again");
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onViewCreated$0$com-test-onlinevotingsystem-ChangeUsernameFragment */
    public /* synthetic */ void m232x2b7db45c(NavController navController, DocumentSnapshot documentSnapshot) {
        if (documentSnapshot.exists()) {
            Map<String, Object> data = documentSnapshot.getData();
            this.userNamehash = data.get("Username").toString();
            this.passWordhash = data.get("Password").toString();
            this.usrcheck = Boolean.valueOf(Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8().matches(this.username, this.userNamehash));
            this.pswdcheck = Boolean.valueOf(Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8().matches(this.password, this.passWordhash));
            if (this.usrcheck.booleanValue() && this.pswdcheck.booleanValue()) {
                navController.navigate(R.id.enterusernamelayout);
                toastMaker("Username and Password Correct");
                return;
            }
            toastMaker("Incorrect Username or Password. Please Try Again");
            return;
        }
        toastMaker("No User Account Found. Please Try Again");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onViewCreated$1$com-test-onlinevotingsystem-ChangeUsernameFragment */
    public /* synthetic */ void toastMaker(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
