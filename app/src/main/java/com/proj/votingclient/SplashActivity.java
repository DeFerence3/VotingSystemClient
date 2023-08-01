package com.proj.votingclient;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

/* loaded from: classes5.dex */
public class SplashActivity extends AppCompatActivity {
    Handler handler = new Handler();
    Runnable runnable = new Runnable() { // from class: com.test.onlinevotingsystem.SplashActivity$$ExternalSyntheticLambda1
        @Override // java.lang.Runnable
        public final void run() {
            SplashActivity.this.m833lambda$new$0$comtestonlinevotingsystemSplashActivity();
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-test-onlinevotingsystem-SplashActivity  reason: not valid java name */
    public /* synthetic */ void m833lambda$new$0$comtestonlinevotingsystemSplashActivity() {
        Intent ab = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(ab);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final ImageView imageView = (ImageView) findViewById(R.id.logo);
        final AnimationSet animationSet = new AnimationSet(true);
        Thread animation = new Thread(new Runnable() { // from class: com.test.onlinevotingsystem.SplashActivity.1
            @Override // java.lang.Runnable
            public void run() {
                ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f, 1, 0.5f, 1, 0.5f);
                scaleAnimation.setDuration(1000L);
                scaleAnimation.setStartOffset(100L);
                animationSet.addAnimation(scaleAnimation);
            }
        });
        Thread animation2 = new Thread(new Runnable() { // from class: com.test.onlinevotingsystem.SplashActivity.2
            @Override // java.lang.Runnable
            public void run() {
                TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, -0.5f);
                translateAnimation.setDuration(1000L);
                translateAnimation.setStartOffset(1500L);
                animationSet.addAnimation(translateAnimation);
                imageView.startAnimation(animationSet);
            }
        });
        Thread postdelay = new Thread(new Runnable() { // from class: com.test.onlinevotingsystem.SplashActivity$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                SplashActivity.this.m834lambda$onCreate$1$comtestonlinevotingsystemSplashActivity();
            }
        });
        animation.start();
        animation2.start();
        postdelay.start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$1$com-test-onlinevotingsystem-SplashActivity  reason: not valid java name */
    public /* synthetic */ void m834lambda$onCreate$1$comtestonlinevotingsystemSplashActivity() {
        this.handler.postDelayed(this.runnable, 1800L);
    }
}
