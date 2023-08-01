package com.proj.votingclient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.proj.votingclient.utils.BitmapFromUrl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* loaded from: classes5.dex */
public class HomeActivity extends AppCompatActivity {
    static String voterid;
    String profilepicurl, username;
    ImageButton logoutbutton, reloadbutton;
    RoundedImageView profile_image;
    TextView title, usernameview, voteridview;

    public Toolbar toolbar;
    public DrawerLayout drawerLayout;
    public NavController navController;
    public NavigationView navigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupNavigation();


        /*navigationlayout = (DrawerLayout) findViewById(R.id.navigationlayout);
        this.nav_menu = (NavigationView) findViewById(R.id.nav_menu);
        this.title = (TextView) findViewById(R.id.apptitle);
        NavController navController = Navigation.findNavController(this, R.id.hostactivity);
        NavigationUI.setupWithNavController(this.nav_menu, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() { // from class: com.test.onlinevotingsystem.HomeActivity$$ExternalSyntheticLambda1
            @Override // androidx.navigation.NavController.OnDestinationChangedListener
            public final void onDestinationChanged(NavController navController2, NavDestination navDestination, Bundle bundle) {
                HomeActivity.this.m219xc013124(navController2, navDestination, bundle);
            }
        });
        findViewById(R.id.menubutton).setOnClickListener(new View.OnClickListener() { // from class: com.test.onlinevotingsystem.HomeActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HomeActivity.this.m218x993be2a5(view);
            }
        });
        Intent userdata = getIntent();
        Bundle userdatabundle = userdata.getBundleExtra("userdatabundle");
        this.username = userdatabundle.getString("username");
        voterid = userdatabundle.getString("voterid");
        this.profilepicurl = userdatabundle.getString("profilepicture");
        this.usernameview.setText(this.username);
        this.voteridview.setText(voterid);
        setProfilePicture(this.profilepicurl);
        this.usernameview.setText(this.username);
        this.voteridview.setText(voterid);*/
        /*findViewById(R.id.navigationtoolbar).setOnTouchListener(new View.OnTouchListener() { // from class: com.test.onlinevotingsystem.HomeActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view2, MotionEvent motionEvent) {
                return HomeActivity.lambda$onCreate$2(view2, motionEvent);
            }
        });*/
        ImageButton imageButton = findViewById(R.id.logoutbuttonab);
        this.logoutbutton = imageButton;
        imageButton.bringToFront();
        this.logoutbutton.setEnabled(true);
        findViewById(R.id.logoutbuttonab).setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                HomeActivity.this.logoutAlert(view2);
            }
        });
    }

    private void setupNavigation() {
        toolbar = findViewById(R.id.toolbarlayout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        View view = navigationView.inflateHeaderView(R.layout.navigation_header);
        profile_image = (RoundedImageView) view.findViewById(R.id.profile_picture);
        usernameview = (TextView) view.findViewById(R.id.usernameview);
        voteridview = (TextView) view.findViewById(R.id.voteridview);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                System.out.println("menuItem.getItemId()");
                return false;
            }
        });

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host);
        navController = navHostFragment.getNavController();

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);

        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host), drawerLayout);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void m219xc013124(NavController navController1, NavDestination navDestination, Bundle bundle) {
        this.title.setText(navDestination.getLabel());
    }

    public static /* synthetic */ boolean lambda$onCreate$2(View v, MotionEvent event) {
        return false;
    }

    public /* synthetic */ void logoutAlert(View v) {

        AlertDialog.Builder logoutalert = new AlertDialog.Builder(this);
        logoutalert.setIcon(R.drawable.baseline_how_to_vote_40).setTitle(R.string.app_name);
        logoutalert.setMessage("Are you sure you want to logout?");
        logoutalert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public final void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences sharedPreferences = getSharedPreferences("AuthToken", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isAuthenticated",false);
                editor.apply();
                HomeActivity.this.m217xb3b145a7(dialogInterface, i);
            }
        }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null);
        logoutalert.show();
    }

    public /* synthetic */ void m217xb3b145a7(DialogInterface dialog, int which) {
        Intent a = new Intent(this, MainActivity.class);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
        startActivity(a);
    }

    private void setProfilePicture(final String profilepicurl) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.getMainLooper());
        service.execute(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = BitmapFromUrl.BitmnapFetcher(profilepicurl);
                handler.post(new Runnable() {
                    @Override // java.lang.Runnable
                    public void run() {
                        if (bitmap != null) {
                            HomeActivity.this.profile_image.setImageBitmap(bitmap);
                        } else {
                            HomeActivity.this.profile_image.setImageResource(R.drawable.round_person_24);
                        }
                    }
                });
            }
        });
    }

    public static String voteridreturn() {
        return voterid;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
