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
import com.proj.votingclient.DatabaseHandler.DatabaseOperations;
import com.proj.votingclient.HomeActivity;
import com.proj.votingclient.R;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

/* loaded from: classes5.dex */
public class EnterNewUsernameFragment extends Fragment {
    EditText confirmnewusername;
    DatabaseOperations dbops = new DatabaseOperations();
    EditText newusername;
    String newusernamehash;
    Button submit;
    String usernamenew;
    String voterid;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enter_new_username, container, false);
        this.submit = (Button) view.findViewById(R.id.submitbutton);
        this.newusername = (EditText) view.findViewById(R.id.newusername);
        this.confirmnewusername = (EditText) view.findViewById(R.id.usernameconfirm);
        return view;
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);
        this.voterid = HomeActivity.voteridreturn();
        this.submit.setOnClickListener(new View.OnClickListener() { // from class: com.test.onlinevotingsystem.EnterNewUsernameFragment$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                EnterNewUsernameFragment.this.m224x837d9012(navController, view2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onViewCreated$2$com-test-onlinevotingsystem-EnterNewUsernameFragment */
    public /* synthetic */ void m224x837d9012(final NavController navController, View v) {
        if (this.newusername.getText().toString().isEmpty()) {
            this.newusername.setError("Please Enter Your Username");
        } else if (this.confirmnewusername.getText().toString().isEmpty()) {
            this.confirmnewusername.setError("Please Enter Your Password");
        } else if (this.newusername.getText().toString().equals(this.confirmnewusername.getText().toString())) {
            this.usernamenew = this.newusername.getText().toString();
            this.newusernamehash = new Argon2PasswordEncoder(16, 32, 1, 16384, 2).encode(this.usernamenew);
            DocumentReference ref = this.dbops.getUserCollectionRef().document(String.valueOf(this.voterid));
            Map<String, Object> hashdata = new HashMap<>();
            hashdata.put("Username", this.newusernamehash);
            ref.update(hashdata).addOnSuccessListener(new OnSuccessListener() { // from class: com.test.onlinevotingsystem.EnterNewUsernameFragment$$ExternalSyntheticLambda0
                @Override // com.google.android.gms.tasks.OnSuccessListener
                public final void onSuccess(Object obj) {
                    EnterNewUsernameFragment.this.m226x4f4692d4(navController, (Void) obj);
                }
            }).addOnFailureListener(new OnFailureListener() { // from class: com.test.onlinevotingsystem.EnterNewUsernameFragment$$ExternalSyntheticLambda1
                @Override // com.google.android.gms.tasks.OnFailureListener
                public final void onFailure(Exception exc) {
                    EnterNewUsernameFragment.this.m225x69621173(exc);
                }
            });
        } else {
            Toast.makeText(getActivity(), "Usernames do not match. Please Try Again", 1).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onViewCreated$0$com-test-onlinevotingsystem-EnterNewUsernameFragment */
    public /* synthetic */ void m226x4f4692d4(NavController navController, Void unused) {
        Toast.makeText(getActivity(), "Username Updated Successfully ", 1).show();
        navController.navigate(R.id.frag_home);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onViewCreated$1$com-test-onlinevotingsystem-EnterNewUsernameFragment */
    public /* synthetic */ void m225x69621173(Exception e) {
        Toast.makeText(getActivity(), "Failed to Update Username. Please Try Again", 1).show();
    }
}
