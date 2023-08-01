package com.proj.votingclient;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.proj.votingclient.DatabaseHandler.DatabaseOperations;
import com.yalantis.ucrop.UCrop;

import java.io.File;

/* loaded from: classes5.dex */
public class RegisterActivity extends AppCompatActivity {
    private static final int IMG_REQ = 104;
    private ActivityResultLauncher<Intent> pickSingleMediaLauncher;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    Bitmap bitmap;
    ImageButton btn_imagepicker;
    Button btn_register;
    Uri imageUri;
    String name, pass, uname;
    DatabaseOperations reg = new DatabaseOperations();
    TextView tv_login;
    EditText tv_name, tv_password, tv_username ;
    TextInputLayout tl_name, tl_password, tl_username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.tv_login = findViewById(R.id.loginhere);
        this.btn_register = findViewById(R.id.registerbtn);
        this.btn_imagepicker = findViewById(R.id.pickimgbtn);
        this.tv_password = findViewById(R.id.password);
        this.tv_username = findViewById(R.id.username);
        this.tv_name = findViewById(R.id.name);
        tl_name = findViewById(R.id.nameContainer);
        tl_password = findViewById(R.id.passwordContainer);
        tl_username = findViewById(R.id.usernameContainer);

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // Image was successfully picked
                        Intent data = result.getData();
                        if (data != null) {
                            Uri imagei = data.getData();
                            startCropActivity(imagei);
                        }
                    } else {
                        // Image picking was canceled or failed
                        showToast("Image picking canceled or failed");
                    }
                });

        btn_imagepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                imagePickerLauncher.launch(pickImageIntent);
            }
        });

        this.tv_login.setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                RegisterActivity.this.toLoginPage(view);
            }
        });

        this.btn_register.setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                submitToRegister();
            }
        });
    }

    private void startCropActivity(Uri sourceUri) {
        // Destination file for the cropped image
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "cropped_image"));

        // Start UCrop activity
        UCrop uCrop = UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(1, 1) // 1:1 aspect ratio
                .withMaxResultSize(500, 500); // Maximum image size after cropping

        uCrop.start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            // Get the result URI from UCrop
            imageUri = UCrop.getOutput(data);

            if (imageUri != null) {
                btn_imagepicker.setImageURI(imageUri);
            } else {
                // Handle the error, if the resultUri is null
                Toast.makeText(this, "Failed to crop image", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            // Handle the error from UCrop
            final Throwable cropError = UCrop.getError(data);
            if (cropError != null) {
                Log.e("UCrop", "Crop error: " + cropError.getMessage());
            }
        }
    }


    public void toLoginPage(View v) {
        Intent a = new Intent(this, MainActivity.class);
        startActivity(a);
        finish();
    }

    public /* synthetic */ void submitToRegister() {
        uname = tv_username.getText().toString();
        pass = tv_password.getText().toString();
        name = tv_name.getText().toString();
        final String id = String.valueOf(getRandomID());
        if (this.name.isEmpty()) {
            tl_name.setError("No name");
        } else if (uname.isEmpty()) {
            tl_username.setError("No username");
        } else if (this.pass.isEmpty()) {
            tl_password.setError("No password");
        } else {
            if (imageUri == null) {
                showToast("No profile image");
            } else {
                reg.createVoter( uname, pass, id, name, imageUri, status -> statusOfAcc(id, status));
            }
        }
    }

    public void statusOfAcc(String id, boolean success) {
        if (success) {
            showUserIDAlertdialouge(id, this);
        } else {
            showToast("Unable to Create Acc");
        }
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 104 && resultCode == -1) {
            if (data == null) {
                throw new AssertionError();
            }
            this.imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), this.imageUri);
                this.bitmap = bitmap;
                this.btn_imagepicker.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/

    private void showUserIDAlertdialouge(final String id, final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("This is your voters id,mark it down").setMessage(id).setPositiveButton("Copy To Clipboard", new DialogInterface.OnClickListener() { // from class: com.test.onlinevotingsystem.RegisterActivity.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboardManager != null) {
                    ClipData clipData = ClipData.newPlainText("Copied Text", id);
                    clipboardManager.setPrimaryClip(clipData);
                    showToast("Voters Id Copied to Clip Board");
                }
                RegisterActivity.this.goToHome(id);
            }
        }).setNegativeButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                RegisterActivity.this.goToHome(id);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void goToHome(String id) {
        Bundle userdata = new Bundle();
        userdata.putParcelable("image", this.imageUri);
        userdata.putString("username", this.uname);
        userdata.putString("voterid", id);
        Intent a = new Intent(this, HomeActivity.class);
        a.putExtra("userdatabundle", userdata);
        startActivity(a);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    int getRandomID() {
        int id = (int) ((Math.random() * ((99999999 - 10000000) + 1)) + 10000000);
        return id;
    }

    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
