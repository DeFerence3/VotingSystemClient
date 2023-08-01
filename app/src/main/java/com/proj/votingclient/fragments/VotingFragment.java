package com.proj.votingclient.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.agrawalsuneet.dotsloader.loaders.LinearDotsLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.net.HttpHeaders;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.proj.votingclient.HomeActivity;
import com.proj.votingclient.R;

import java.util.HashMap;
import java.util.Map;

/* loaded from: classes5.dex */
public class VotingFragment extends Fragment {
    String candidate;
    String date;

    /* renamed from: id */
    Integer f545id;
    LinearDotsLoader loadingprogress;
    String name;
    Integer notaid = 4862;

    /* renamed from: sb */
    StringBuilder f546sb = new StringBuilder();
    LinearLayout votedata;
    LinearLayout votedata2;
    TextView votenametext;
    String voterid;
    LinearLayout votesubmitbuttonlayout;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voting, container, false);
        return view;
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.votedata = (LinearLayout) view.findViewById(R.id.voteoption);
        this.votedata2 = (LinearLayout) view.findViewById(R.id.voteoption2);
        this.votenametext = (TextView) view.findViewById(R.id.votenametext);
        this.loadingprogress = (LinearDotsLoader) view.findViewById(R.id.loadingprogress);
        this.votesubmitbuttonlayout = (LinearLayout) view.findViewById(R.id.votesubmitbuttonlayout);
        this.f545id = HomeFragment.voteid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Election_Data").document(String.valueOf(this.f545id));
        documentReference.get().addOnSuccessListener(new OnSuccessListener() { // from class: com.test.onlinevotingsystem.VotingFragment$$ExternalSyntheticLambda9
            @Override // com.google.android.gms.tasks.OnSuccessListener
            public final void onSuccess(Object obj) {
                VotingFragment.this.m209xe440ea0f((DocumentSnapshot) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onViewCreated$15$com-test-onlinevotingsystem-VotingFragment */
    public /* synthetic */ void m209xe440ea0f(DocumentSnapshot documentSnapshot) {
        if (documentSnapshot.exists()) {
            Map<String, Object> data = documentSnapshot.getData();
            this.name = data.get("Name").toString();
            this.date = data.get(HttpHeaders.DATE).toString();
            int textcolor = ContextCompat.getColor(getContext(), R.color.lightgreytext);
            /*Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Lato-Regular.ttf");
            Typeface font2 = Typeface.createFromAsset(getContext().getAssets(), "fonts/abeezee.ttf");*/
            this.votenametext.setText(this.name);
            final RadioGroup voteoptionlist = new RadioGroup(getContext());
            RadioGroup.LayoutParams voteoptionlayout = new RadioGroup.LayoutParams(-2, -2);
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{16842912}, new ColorDrawable(Color.rgb(35, 62, 154)));
            stateListDrawable.addState(new int[]{-16842912}, new ColorDrawable(-16776961));
            ColorStateList colorStateList = new ColorStateList(new int[][]{new int[]{16842912}, new int[]{-16842912}}, new int[]{Color.rgb(49, 82, 194), -1});
            int i = 1;
            while (i < 10) {
                this.f546sb.setLength(0);
                this.f546sb.append("Contestant");
                this.f546sb.append(i);
                if (documentSnapshot.contains(String.valueOf(this.f546sb))) {
                    this.candidate = data.get(String.valueOf(this.f546sb)).toString();
                    RadioButton votecandidate = new RadioButton(getContext());
                    votecandidate.setText(this.candidate);
                    votecandidate.setTextSize(21.0f);
                    votecandidate.setId(i);
                    votecandidate.setTextColor(textcolor);
                    votecandidate.setButtonTintList(colorStateList);
                    votecandidate.setPadding(30, 35, 0, 35);
                    voteoptionlist.addView(votecandidate, voteoptionlayout);
                } else {
                    i = 11;
                }
                i++;
            }
            final RadioButton votecandidate2 = new RadioButton(getContext());
            votecandidate2.setText("NOTA");
            votecandidate2.setTextSize(21.0f);
            votecandidate2.setId(this.notaid.intValue());
            votecandidate2.setTextColor(textcolor);
            votecandidate2.setButtonTintList(colorStateList);
            votecandidate2.setPadding(30, 35, 0, 35);
            voteoptionlist.addView(votecandidate2, voteoptionlayout);
            Button votesubmit = new Button(getContext());
            votesubmit.setText("Submit Your Vote");
            votesubmit.setTextSize(22.0f);
            votesubmit.setBackgroundResource(R.drawable.button_bg);
            votesubmit.setPadding(10, 30, 10, 30);
            this.votedata2.addView(voteoptionlist);
            this.votesubmitbuttonlayout.addView(votesubmit);
            this.loadingprogress.setVisibility(View.GONE);
            votesubmit.setOnClickListener(new View.OnClickListener() { // from class: com.test.onlinevotingsystem.VotingFragment$$ExternalSyntheticLambda6
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    VotingFragment.this.m210x215480b0(votecandidate2, voteoptionlist, view);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onViewCreated$14$com-test-onlinevotingsystem-VotingFragment */
    @SuppressLint("ResourceType")
    public /* synthetic */ void m210x215480b0(RadioButton votecandidate, RadioGroup voteoptionlist, View v) {
        votecandidate.setError(null);
        if (voteoptionlist.getCheckedRadioButtonId() <= 0) {
            votecandidate.setError("Please select your option");
        } else if (voteoptionlist.getCheckedRadioButtonId() == this.notaid.intValue()) {
            this.loadingprogress.setVisibility(0);
            this.f545id = HomeFragment.voteid();
            this.voterid = HomeActivity.voteridreturn();
            final FirebaseFirestore update = FirebaseFirestore.getInstance();
            DocumentReference updateref = update.collection("Election_Stats").document(String.valueOf(this.f545id));
            Map<String, Object> voteupdate = new HashMap<>();
            voteupdate.put("NOTA", FieldValue.increment(1L));
            updateref.update(voteupdate).addOnSuccessListener(new OnSuccessListener() { // from class: com.test.onlinevotingsystem.VotingFragment$$ExternalSyntheticLambda11
                @Override // com.google.android.gms.tasks.OnSuccessListener
                public final void onSuccess(Object obj) {
                    VotingFragment.this.m207x718bfb59(update, (Void) obj);
                }
            }).addOnFailureListener(new OnFailureListener() { // from class: com.test.onlinevotingsystem.VotingFragment$$ExternalSyntheticLambda12
                @Override // com.google.android.gms.tasks.OnFailureListener
                public final void onFailure(Exception exc) {
                    VotingFragment.this.m206xf764ce17(exc);
                }
            });
        } else {
            this.loadingprogress.setVisibility(8);
            this.f545id = HomeFragment.voteid();
            this.f546sb.setLength(0);
            this.f546sb.append("Contestant");
            this.f546sb.append(voteoptionlist.getCheckedRadioButtonId());
            this.voterid = HomeActivity.voteridreturn();
            FirebaseFirestoreSettings firestoreSettings = new FirebaseFirestoreSettings.Builder().setCacheSizeBytes(-1L).build();
            final FirebaseFirestore update2 = FirebaseFirestore.getInstance();
            update2.setFirestoreSettings(firestoreSettings);
            DocumentReference updateref2 = update2.collection("Election_Stats").document(String.valueOf(this.f545id));
            Map<String, Object> voteupdate2 = new HashMap<>();
            voteupdate2.put(String.valueOf(this.f546sb), FieldValue.increment(1L));
            updateref2.update(voteupdate2).addOnSuccessListener(new OnSuccessListener() { // from class: com.test.onlinevotingsystem.VotingFragment$$ExternalSyntheticLambda13
                @Override // com.google.android.gms.tasks.OnSuccessListener
                public final void onSuccess(Object obj) {
                    VotingFragment.this.m212xd88f4493(update2, (Void) obj);
                }
            }).addOnFailureListener(new OnFailureListener() { // from class: com.test.onlinevotingsystem.VotingFragment$$ExternalSyntheticLambda14
                @Override // com.google.android.gms.tasks.OnFailureListener
                public final void onFailure(Exception exc) {
                    VotingFragment.this.m211x5e681751(exc);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onViewCreated$4$com-test-onlinevotingsystem-VotingFragment */
    public /* synthetic */ void m207x718bfb59(FirebaseFirestore update, Void unused) {
        DocumentReference updateuser = update.collection("Test_User").document(this.voterid);
        Map<String, Object> userupdate = new HashMap<>();
        userupdate.put(String.valueOf(this.f545id), FieldValue.increment(1L));
        updateuser.update(userupdate).addOnSuccessListener(new OnSuccessListener() { // from class: com.test.onlinevotingsystem.VotingFragment$$ExternalSyntheticLambda0
            @Override // com.google.android.gms.tasks.OnSuccessListener
            public final void onSuccess(Object obj) {
                VotingFragment.this.m214x28c6bf3c((Void) obj);
            }
        }).addOnFailureListener(new OnFailureListener() { // from class: com.test.onlinevotingsystem.VotingFragment$$ExternalSyntheticLambda7
            @Override // com.google.android.gms.tasks.OnFailureListener
            public final void onFailure(Exception exc) {
                VotingFragment.this.m208xae9f91fa(exc);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onViewCreated$1$com-test-onlinevotingsystem-VotingFragment */
    public /* synthetic */ void m214x28c6bf3c(Void unused1) {
        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(getContext());
        View alertbuild = getLayoutInflater().inflate(R.layout.dialog_positive, (ViewGroup) null);
        Button dialogbutton = (Button) alertbuild.findViewById(R.id.btnDialog);
        alertdialogbuilder.setView(alertbuild);
        alertdialogbuilder.setCancelable(false);
        final AlertDialog alertDialog = alertdialogbuilder.create();
        alertDialog.show();
        dialogbutton.setOnClickListener(new View.OnClickListener() { // from class: com.test.onlinevotingsystem.VotingFragment$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VotingFragment.this.m215x65da55dd(alertDialog, view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onViewCreated$0$com-test-onlinevotingsystem-VotingFragment */
    public /* synthetic */ void m215x65da55dd(AlertDialog alertDialog, View v12) {
        alertDialog.dismiss();
        NavController navController = Navigation.findNavController(getView());
        navController.navigate(R.id.frag_home);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onViewCreated$3$com-test-onlinevotingsystem-VotingFragment */
    public /* synthetic */ void m208xae9f91fa(Exception e) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        View alertbuild = getLayoutInflater().inflate(R.layout.dialog_negative, (ViewGroup) null);
        Button dialogbutton = (Button) alertbuild.findViewById(R.id.btnDialog);
        dialogBuilder.setView(alertbuild);
        dialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        dialogbutton.setOnClickListener(new View.OnClickListener() { // from class: com.test.onlinevotingsystem.VotingFragment$$ExternalSyntheticLambda10
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onViewCreated$6$com-test-onlinevotingsystem-VotingFragment */
    public /* synthetic */ void m206xf764ce17(Exception e) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        View alertbuild = getLayoutInflater().inflate(R.layout.dialog_negative, (ViewGroup) null);
        Button dialogbutton = (Button) alertbuild.findViewById(R.id.btnDialog);
        dialogBuilder.setView(alertbuild);
        dialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        dialogbutton.setOnClickListener(new View.OnClickListener() { // from class: com.test.onlinevotingsystem.VotingFragment$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onViewCreated$11$com-test-onlinevotingsystem-VotingFragment */
    public /* synthetic */ void m212xd88f4493(FirebaseFirestore update, Void unused) {
        DocumentReference updateuser = update.collection("Test_User").document(this.voterid);
        Map<String, Object> userupdate = new HashMap<>();
        userupdate.put(String.valueOf(this.f545id), FieldValue.increment(1L));
        updateuser.update(userupdate).addOnSuccessListener(new OnSuccessListener() { // from class: com.test.onlinevotingsystem.VotingFragment$$ExternalSyntheticLambda2
            @Override // com.google.android.gms.tasks.OnSuccessListener
            public final void onSuccess(Object obj) {
                VotingFragment.this.m204x7d3da0d5((Void) obj);
            }
        }).addOnFailureListener(new OnFailureListener() { // from class: com.test.onlinevotingsystem.VotingFragment$$ExternalSyntheticLambda3
            @Override // com.google.android.gms.tasks.OnFailureListener
            public final void onFailure(Exception exc) {
                VotingFragment.this.m213x15a2db34(exc);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onViewCreated$8$com-test-onlinevotingsystem-VotingFragment */
    public /* synthetic */ void m204x7d3da0d5(Void unused12) {
        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(getContext());
        View alertbuild = getLayoutInflater().inflate(R.layout.dialog_positive, (ViewGroup) null);
        Button dialogbutton = (Button) alertbuild.findViewById(R.id.btnDialog);
        alertdialogbuilder.setView(alertbuild);
        alertdialogbuilder.setCancelable(false);
        final AlertDialog alertDialog = alertdialogbuilder.create();
        alertDialog.show();
        dialogbutton.setOnClickListener(new View.OnClickListener() { // from class: com.test.onlinevotingsystem.VotingFragment$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VotingFragment.this.m205xba513776(alertDialog, view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onViewCreated$7$com-test-onlinevotingsystem-VotingFragment */
    public /* synthetic */ void m205xba513776(AlertDialog alertDialog, View v12) {
        alertDialog.dismiss();
        NavController navController = Navigation.findNavController(getView());
        navController.navigate(R.id.frag_home);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onViewCreated$10$com-test-onlinevotingsystem-VotingFragment */
    public /* synthetic */ void m213x15a2db34(Exception e) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        View alertbuild = getLayoutInflater().inflate(R.layout.dialog_negative, (ViewGroup) null);
        Button dialogbutton = (Button) alertbuild.findViewById(R.id.btnDialog);
        dialogBuilder.setView(alertbuild);
        dialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        dialogbutton.setOnClickListener(new View.OnClickListener() { // from class: com.test.onlinevotingsystem.VotingFragment$$ExternalSyntheticLambda8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onViewCreated$13$com-test-onlinevotingsystem-VotingFragment */
    public /* synthetic */ void m211x5e681751(Exception e) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        View alertbuild = getLayoutInflater().inflate(R.layout.dialog_negative, (ViewGroup) null);
        Button dialogbutton = (Button) alertbuild.findViewById(R.id.btnDialog);
        dialogBuilder.setView(alertbuild);
        dialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        dialogbutton.setOnClickListener(new View.OnClickListener() { // from class: com.test.onlinevotingsystem.VotingFragment$$ExternalSyntheticLambda15
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
}
