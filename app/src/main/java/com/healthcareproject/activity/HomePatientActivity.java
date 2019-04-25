package com.healthcareproject.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.healthcareproject.R;
import com.healthcareproject.utilities.Constants;
import com.healthcareproject.utilities.SharedPref;
import com.mikhaellopez.circularimageview.CircularImageView;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class HomePatientActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 200;
    private CircularImageView civ;
    private FirebaseAuth auth;

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_patient);
        init();
        auth = FirebaseAuth.getInstance();

    }

    private void init() {
        ImageView ivConsultation = findViewById(R.id.ivConsultation);
        ImageView ivChat = findViewById(R.id.ivChat);
        ImageView ivQuery=findViewById(R.id.ivQuery);
        TextView tvName = findViewById(R.id.tvName);
        ImageView ivShowMore = findViewById(R.id.ivShowMore);
        tvName.setText(SharedPref.getInstance().getString(Constants.PAT_NAME));
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle(getString(R.string.home));
        civ = findViewById(R.id.ivDp);
        ImageView ivPickImage = findViewById(R.id.pickImage);
        ivPickImage.setOnClickListener(this);
        ivShowMore.setOnClickListener(this);
        ivChat.setOnClickListener(this);
        ivConsultation.setOnClickListener(this);
        ivQuery.setOnClickListener(this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:

                    Uri uri = data.getData();
                    Bitmap bitmapafterCheckRotation = null;
                    try {
                        bitmapafterCheckRotation = rotateImageIfRequired(MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Glide.with(HomePatientActivity.this).load(bitmapafterCheckRotation).into(civ);
                    }
                    break;


                default:
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickFromGallerySingle();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            openSettingsForPermission();
                        } else {
                            giveSingleImagePermissionsDetailsToUser();
                        }
                    }
                }
                break;


            default:

        }
    }

    //Method to pick single image from gallery
    private void pickFromGallerySingle() {
        Intent captureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(captureIntent, GALLERY_REQUEST_CODE);
    }

    //This method shows the dialog box to go to settings
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(HomePatientActivity.this)
                .setMessage(message)
                .setPositiveButton(R.string.ok, okListener)
                .setNegativeButton(R.string.cancel, null)
                .create()
                .show();
    }

    //Method to open settings so that user can give permission
    private void openSettingsForPermission() {
        showMessageOKCancel(getString(R.string.go_to_settings),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent viewIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                        startActivity(viewIntent);
                    }
                });
    }

    //Method to give permission requirement to the user
    private void giveSingleImagePermissionsDetailsToUser() {
        showMessageOKCancel(getString(R.string.permissionRequirement),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                        }

                    }
                });
    }

    private Bitmap rotateImageIfRequired(Bitmap bitmap, Uri uri) throws IOException {

        InputStream input = this.getContentResolver().openInputStream(uri);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23) {
            assert input != null;
            ei = new ExifInterface(input);
        } else
            ei = new ExifInterface(Objects.requireNonNull(uri.getPath()));

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(bitmap, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(bitmap, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(bitmap, 270);
            default:
                return bitmap;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pickImage:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    } else {
                        pickFromGallerySingle();
                    }

                } else {
                    pickFromGallerySingle();

                }
                break;

            case R.id.ivConsultation:

                startActivity(new Intent(HomePatientActivity.this, OnlineConsultationList.class));
                break;

            case R.id.ivChat:
                startActivity(new Intent(HomePatientActivity.this, ChatListActivity.class));
                break;

            case R.id.ivShowMore:
                showMessageOKCancel1("Do you want to Logout",
                        new DialogInterface.OnClickListener() {


                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPref.getInstance().delete();
                                SharedPref.getInstance().setBoolean(Constants.LOGIN_CHECK, false);
                                startActivity(new Intent(HomePatientActivity.this, LoginActivity.class));
                                auth.signOut();
                                finish();
                            }
                        });
                break;

            case R.id.ivQuery:
                break;
        }


    }


    private void showMessageOKCancel1(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(HomePatientActivity.this)
                .setMessage(message)
                .setPositiveButton(R.string.ok, okListener)
                .setNegativeButton(R.string.cancel, null)
                .create()
                .show();
    }


}
