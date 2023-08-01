package com.proj.votingclient.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
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
public class EnterNewPasswordFragment extends Fragment {
    Button Submit;
    EditText confirmpassword;
    EditText newpassword;
    String newpasswordconfirm;
    String newpasswordhash;
    String passwordnew;
    String voterid;
    DatabaseOperations dbops = new DatabaseOperations();
    OnBackPressedCallback callback = new OnBackPressedCallback(true) { // from class: com.test.onlinevotingsystem.EnterNewPasswordFragment.1
        @Override // androidx.activity.OnBackPressedCallback
        public void handleOnBackPressed() {
            NavController navController = Navigation.findNavController(EnterNewPasswordFragment.this.requireView());
            navController.navigate(R.id.frag_home);
        }
    };

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enter_new_password, container, false);
        this.newpassword = (EditText) view.findViewById(R.id.newpassword);
        this.confirmpassword = (EditText) view.findViewById(R.id.confirmnewpassword);
        this.Submit = (Button) view.findViewById(R.id.submitbutton);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), this.callback);
        return view;
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);
        this.voterid = HomeActivity.voteridreturn();
        this.Submit.setOnClickListener(new View.OnClickListener() { // from class: com.test.onlinevotingsystem.EnterNewPasswordFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                EnterNewPasswordFragment.this.m227xadc62cd7(navController, view2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onViewCreated$2$com-test-onlinevotingsystem-EnterNewPasswordFragment */
    public /* synthetic */ void m227xadc62cd7(final NavController navController, View v) {
        if (this.newpassword.getText().toString().isEmpty()) {
            this.newpassword.setError("Please Enter Your Username");
        } else if (this.confirmpassword.getText().toString().isEmpty()) {
            this.confirmpassword.setError("Please Enter Your Password");
        } else {
            this.passwordnew = this.newpassword.getText().toString();
            String obj = this.confirmpassword.getText().toString();
            this.newpasswordconfirm = obj;
            if (this.passwordnew.equals(obj)) {
                this.newpasswordhash = new Argon2PasswordEncoder(16, 32, 1, 16384, 2).encode(this.passwordnew);
                DocumentReference ref = this.dbops.getUserCollectionRef().document(String.valueOf(this.voterid));
                Map<String, Object> hashdata = new HashMap<>();
                hashdata.put("Password", this.newpasswordhash);
                ref.update(hashdata).addOnSuccessListener(new OnSuccessListener() { // from class: com.test.onlinevotingsystem.EnterNewPasswordFragment$$ExternalSyntheticLambda1
                    @Override // com.google.android.gms.tasks.OnSuccessListener
                    public final void onSuccess(Object obj2) {
                        EnterNewPasswordFragment.this.m229x798f2f99(navController, (Void) obj2);
                    }
                }).addOnFailureListener(new OnFailureListener() { // from class: com.test.onlinevotingsystem.EnterNewPasswordFragment$$ExternalSyntheticLambda2
                    @Override // com.google.android.gms.tasks.OnFailureListener
                    public final void onFailure(Exception exc) {
                        toastMaker("Failed to Update Password. Please Try Again");
                    }
                });
                return;
            }
            toastMaker("Passwords do not match. Please Try Again");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onViewCreated$0$com-test-onlinevotingsystem-EnterNewPasswordFragment */
    public /* synthetic */ void m229x798f2f99(NavController navController, Void unused) {
        toastMaker("Password Updated Successfully ");
        navController.navigate(R.id.frag_home);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onViewCreated$1$com-test-onlinevotingsystem-EnterNewPasswordFragment */
    public /* synthetic */ void toastMaker(String msg) {
        Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();
    }
}
