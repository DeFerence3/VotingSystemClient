package com.proj.votingclient.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.agrawalsuneet.dotsloader.loaders.LinearDotsLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.net.HttpHeaders;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.proj.votingclient.DatabaseHandler.DatabaseOperations;
import com.proj.votingclient.HomeActivity;
import com.proj.votingclient.R;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
public class HomeFragment extends Fragment {
    static Integer f506id;
    String date, name, voterid;
    Integer startTime, endTime;
    Long startT, endT;
    LinearLayout electionview;
    LinearDotsLoader progressloader;
    TextView tv_noelection;
    DatabaseOperations dbops = new DatabaseOperations();
    OnBackPressedCallback callback = new C21171(true);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), this.callback);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.electionview = view.findViewById(R.id.electionlinear);
        this.progressloader = view.findViewById(R.id.loadingprogress);
        tv_noelection = view.findViewById(R.id.tv_noElection);
        read();
    }

    protected void read() {
        this.dbops.getAllElections().addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object obj) {
                HomeFragment.this.electionDataPopulator((QuerySnapshot) obj);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exc) {
                tv_noelection.setText(R.string.failed_to_read_documents);
                tv_noelection.setVisibility(View.VISIBLE);
                progressloader.setVisibility(View.GONE);
                electionview.setVisibility(View.GONE);
            }
        });
    }

    public void electionDataPopulator(QuerySnapshot queryDocumentSnapshots) {
        if (queryDocumentSnapshots.isEmpty()) {
            tv_noelection.setVisibility(View.VISIBLE);
            progressloader.setVisibility(View.GONE);
            electionview.setVisibility(View.GONE);
        } else {
            Iterator<QueryDocumentSnapshot> it = queryDocumentSnapshots.iterator();
            while (it.hasNext()) {
                DocumentSnapshot documentSnapshot = it.next();
                name = documentSnapshot.getString("Name");
                date = documentSnapshot.getString(HttpHeaders.DATE);
                f506id = Integer.valueOf(documentSnapshot.getId());
                startT = (Long) documentSnapshot.get("startTime");
                endT = (Long) documentSnapshot.get("endTime");
                startTime = Math.toIntExact(startT);
                endTime = Math.toIntExact(endT);

                StringBuilder timeframe = new StringBuilder();
                timeframe.append(startTime);
                timeframe.append(":00:00 to ");
                timeframe.append(endTime);
                timeframe.append(":00:00");
                final LinearLayout linearLayout = new LinearLayout(getContext());
                GradientDrawable shape = new GradientDrawable();
                shape.setShape(GradientDrawable.RECTANGLE);
                float[] cornerRadii = {40.0f, 40.0f, 40.0f, 40.0f, 40.0f, 40.0f, 40.0f, 40.0f};
                shape.setCornerRadii(cornerRadii);
                int colorint = Color.parseColor("#AA121212");
                shape.setColors(new int[]{colorint, colorint});
                linearLayout.setPadding(20, 10, 20, 5);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setClickable(true);
                linearLayout.setId(f506id.intValue());
                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(-1, -2);
                Iterator<QueryDocumentSnapshot> it2 = it;
                layoutParams1.setMargins(0, 20, 0, 40);
                linearLayout.setLayoutParams(layoutParams1);
                linearLayout.setBackground(shape);
                int textcolor = ContextCompat.getColor(requireContext(), R.color.lightgreytext);
                TextView nameview = new TextView(getContext());
                nameview.setText(this.name);
                nameview.setTextSize(25.0f);
                nameview.setTextColor(textcolor);
                nameview.setPadding(5, 10, 0, 10);
                nameview.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
                linearLayout.addView(nameview);
                TextView dateview = new TextView(getContext());
                dateview.setText(this.date);
                dateview.setTextSize(18.0f);
                dateview.setTextColor(textcolor);
                dateview.setPadding(5, 10, 0, 10);
                dateview.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
                linearLayout.addView(dateview);
                TextView timeview = new TextView(getContext());
                timeview.setText(timeframe);
                timeview.setTextSize(18.0f);
                timeview.setTextColor(textcolor);
                timeview.setPadding(5, 10, 0, 15);
                timeview.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
                linearLayout.addView(timeview);
                this.progressloader.setVisibility(View.GONE);
                this.electionview.addView(linearLayout);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public final void onClick(View view) {
                        HomeFragment.this.m826lambda$read$4$comtestonlinevotingsystemHomeFragment(linearLayout, view);
                    }
                });
                it = it2;
            }
        }
    }

    public /* synthetic */ void m826lambda$read$4$comtestonlinevotingsystemHomeFragment(LinearLayout linearLayout, View v) {
        this.progressloader.setVisibility(View.GONE);
        f506id = Integer.valueOf(linearLayout.getId());
        this.voterid = HomeActivity.voteridreturn();
        ZonedDateTime indiazoneddatetime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
        LocalDateTime indiaLocalDateTime = indiazoneddatetime.toLocalDateTime();
        final int indiatime = indiaLocalDateTime.getHour();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH);
        final String indiadate = indiaLocalDateTime.format(formatter);
        DocumentReference reference = this.dbops.getAllCollection().document(String.valueOf(f506id));
        reference.get().addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object obj) {
                HomeFragment.this.m824lambda$read$2$comtestonlinevotingsystemHomeFragment(indiadate, indiatime, (DocumentSnapshot) obj);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exc) {
                snackBarMaker("Failed to show details. Please Try Again.");
            }
        });
    }

    public void m824lambda$read$2$comtestonlinevotingsystemHomeFragment(String indiadate, final int indiatime, DocumentSnapshot documentSnapshot2) {
        Map<String, Object> data = documentSnapshot2.getData();
        String date = data.get(HttpHeaders.DATE).toString();
        Long startTm = (Long) data.get("startTime");
        Long endTm = (Long) data.get("endTime");
        final Integer startTime1 = Integer.valueOf(Math.toIntExact(startTm.longValue()));
        final Integer endTime1 = Integer.valueOf(Math.toIntExact(endTm.longValue()));
        if (indiadate.equals(date)) {
            DocumentReference documentReference = this.dbops.getAllCollection().document(this.voterid);
            documentReference.get().addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object obj) {
                    HomeFragment.this.m822lambda$read$0$comtestonlinevotingsystemHomeFragment(indiatime, startTime1, endTime1, (DocumentSnapshot) obj);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception exc) {
                    snackBarMaker("Failed to show details. Please Try Again");
                }
            });
            return;
        }
        snackBarMaker("Please ensure that you attempt to vote on the day of the election.");
    }

    public void m822lambda$read$0$comtestonlinevotingsystemHomeFragment(int indiatime, Integer startTime1, Integer endTime1, DocumentSnapshot documentSnapshot1) {
        if (documentSnapshot1.exists()) {
            if (indiatime < startTime1.intValue()) {
                snackBarMaker("The Election has not yet Started");
                return;
            } else if (indiatime >= endTime1.intValue()) {
                snackBarMaker("The Election has already Ended");
                return;
            } else {
                Double uservotestatus = documentSnapshot1.getDouble(String.valueOf(f506id));
                if (uservotestatus.doubleValue() > 0.0d) {
                    snackBarMaker("You Vote has already been Placed");
                    return;
                }
                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.voting);
                return;
            }
        }
        snackBarMaker("Failed to show details. Please Try Again");
    }

    public class C21171 extends OnBackPressedCallback {
        C21171(boolean enabled) {
            super(enabled);
        }

        @Override
        public void handleOnBackPressed() {

            AlertDialog.Builder exitdialog = new AlertDialog.Builder(HomeFragment.this.getActivity());
            exitdialog.setIcon(R.drawable.baseline_how_to_vote_40);
            exitdialog.setTitle(R.string.app_name);
            exitdialog.setMessage("Are you sure you want to Exit?");
            exitdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public final void onClick(DialogInterface dialogInterface, int i) {
                    HomeFragment.this.getActivity().finish();
                }
            });
            exitdialog.setNegativeButton("Cancel", (DialogInterface.OnClickListener) null);
            exitdialog.show();
        }
    }

    public static Integer voteid() {
        return f506id;
    }

    private void snackBarMaker(String message) {
        final Snackbar snackbar = Snackbar.make(requireView(), message, -2);
        snackbar.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                snackbar.dismiss();
            }
        });
        this.progressloader.setVisibility(View.GONE);
        snackbar.show();
    }
}
