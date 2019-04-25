package com.healthcareproject.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.healthcareproject.R;
import com.healthcareproject.utilities.Constants;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class SignUpDoctor extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private EditText etName, etEmail, etPhoneNo, etPassword,etSpeciality,etQualification,etRegistrationNo; //EditText for Name,Email and PhoneNo
    private Button butSignUp;                  //Button for SignUp Up
    private TextInputLayout tilPhone, tilEmail,tilPassword;//TextInputLayout for Phone NO and Email
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference database;
    private ImageView ivPickImage,ivDoctorImage;
    private String currentPhotoPath;
    private DatabaseReference databaseChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_doctor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.sign_up));
        initId();

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference().child("users");
        databaseChat = FirebaseDatabase.getInstance().getReference().child("chats");

    }

    private void signUp() {

        mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.i("my1", "signup" + mAuth.getUid());

                if (task.isSuccessful()) {
                    String uId = mAuth.getCurrentUser().getUid();

                    DatabaseReference current_user_id = database.child(uId);

                    current_user_id.child("user_type").setValue(Constants.USER_DOCTOR);
                    current_user_id.child("name").setValue(etName.getText().toString());
                    current_user_id.child("phone_no").setValue(etPhoneNo.getText().toString());
                    current_user_id.child("speciality").setValue(etSpeciality.getText().toString());
                    current_user_id.child("qualification").setValue(etQualification.getText().toString());
                    current_user_id.child("registration_no").setValue(etRegistrationNo.getText().toString());
                    current_user_id.child("user_id").setValue(mAuth.getCurrentUser().getUid());
                    current_user_id.child("user_image").setValue(currentPhotoPath);
                    current_user_id.child("is_available").setValue("0");


                    DatabaseReference current_chat=databaseChat.child(uId);
                    current_chat.child("msg_send").setValue("send");
                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignUpDoctor.this, "Registration successful.Please check your email for verification", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpDoctor.this, LoginActivity.class));
                                finish();
                            }
                            else {
                                Toast.makeText(SignUpDoctor.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                }

            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.butSignUp:
                signUp();
                break;

            case R.id. pickImage1:
                String[] colors = {"Camera", "Gallery"};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select Option");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(SignUpDoctor.this, new String[]{Manifest.permission.CAMERA},Constants.PERMISSION_REQUEST_CODE_1);
                                    } else {
                                        dispatchTakePictureIntent();
                                    }
                                } else {
                                    dispatchTakePictureIntent();
                                }

                                break;

                            case 1:
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Constants.PERMISSION_REQUEST_CODE);
                                    } else {
                                        pickFromGallerySingle();
                                    }

                                } else {
                                    pickFromGallerySingle();

                                }
                                break;
                        }
                    }

                });
                builder.show();
                break;

        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


        String phoneNo = etPhoneNo.getText().toString();
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String speciality=etSpeciality.getText().toString();
        String qualification=etQualification.getText().toString();
        String registrationNo=etRegistrationNo.getText().toString();
        boolean flagEmail , flagPhoneNo, flagEmpty, flagSpace, flagPassword;

        flagEmail = validateEmail(email);
        flagPassword=validatePassword(password);
        flagPhoneNo = validatePhoneNo(phoneNo);
        flagEmpty = validateEmpty(name, email, phoneNo, password,speciality,qualification,registrationNo);
        flagSpace = validateSpace(name, email, phoneNo, password,speciality,qualification,registrationNo);

        if (flagPhoneNo && flagEmpty && flagSpace  && flagEmail&&flagPassword) {
            butSignUp.setEnabled(true);
        } else {
            butSignUp.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    //Method to initialize the globally declare views
    private void initId() {
        ivDoctorImage=findViewById(R.id.ivDoctorImage);
        ivPickImage=findViewById(R.id.pickImage1);
        butSignUp = findViewById(R.id.butSignUp);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNo = findViewById(R.id.etPhoneNo);
        tilPhone = findViewById(R.id.tilPhone);
        tilEmail = findViewById(R.id.tilEmail);
        etPassword = findViewById(R.id.etPassword);
        etQualification=findViewById(R.id.etQualification);
        etRegistrationNo=findViewById(R.id.etRegistrationNo);
        etSpeciality=findViewById(R.id.etSpeciality);
        butSignUp.setText(getString(R.string.sign_up));
        butSignUp.setOnClickListener(this);
        etEmail.addTextChangedListener(this);
        etName.addTextChangedListener(this);
        etPhoneNo.addTextChangedListener(this);
        etPassword.addTextChangedListener(this);
        etSpeciality.addTextChangedListener(this);
        etRegistrationNo.addTextChangedListener(this);
        etQualification.addTextChangedListener(this);
        ivPickImage.setOnClickListener(this);
        tilPassword=findViewById(R.id.tilPassword);
    }

    //Method to check the valid Email Address
    private boolean validateEmail(String email) {

        if (!email.trim().matches(getString(R.string.emailRegex)) && email.length() > 0) {
            tilEmail.setError(getString(R.string.emailValidation));
            return false;
        } else {

            tilEmail.setError(null);
            return true;
        }

    }

    //Method to check whether phone No is of 10 digits or not
    private boolean validatePhoneNo(String phoneNO) {
        if (phoneNO.length() < 10 && phoneNO.length() > 0) {
            tilPhone.setError(getString(R.string.phoneNoMinLength));
            return false;
        } else {
            tilPhone.setError(null);
            return true;
        }
    }

    //Method to check whether any field is empty or not
    private boolean validateEmpty( String name, String email, String phoneNo, String password,String speciality, String qualification, String registrationNo) {
        return !name.equals("") && !email.equals("") && !phoneNo.equals("") && !password.equals("") &&!speciality.equals("")&& !qualification.equals("")&&!registrationNo.equals("");
    }

    //Method to validate all EditText fields for space
    private boolean validateSpace( String name, String email, String phoneNo, String password,String speciality, String qualification, String registrationNo) {
        return !name.trim().equals("") && !email.trim().equals("") && !phoneNo.trim().equals("") && !password.trim().equals("")&&!speciality.equals("")&& !qualification.equals("")&&!registrationNo.equals("");
    }



    private void pickFromGallerySingle() {
        Intent captureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(captureIntent, Constants.GALLERY_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Constants.PERMISSION_REQUEST_CODE:
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

            case Constants.PERMISSION_REQUEST_CODE_1:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                } else {
                    int flag = 0;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                            flag++;
                            openSettingsForPermission();
                        }
                    }
                    if (flag == 0) {
                        givePrivateExternalPermissionsDetailsToUser();
                    }
                }

            default:
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SignUpDoctor.this)
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
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Constants.PERMISSION_REQUEST_CODE);
                        }

                    }
                });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.GALLERY_REQUEST_CODE:

                    Uri uri = data.getData();
                    Bitmap bitmapafterCheckRotation = null;
                    try {
                        bitmapafterCheckRotation = rotateImageIfRequired(MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Glide.with(this).load(bitmapafterCheckRotation).into(ivDoctorImage);
                    break;

                case Constants.CAMERA_REQUEST_CODE:
                    Log.i(Constants.TAG,"current"+currentPhotoPath);
                    Glide.with(SignUpDoctor.this).load(currentPhotoPath).into(ivDoctorImage);
                    break;
            }
        }
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

    private File createImageFilePrivate() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat(getString(R.string.yyyyMMdd_HHmmss)).format(new Date());
        String imageFileName = getString(R.string.JPEG_) + timeStamp + getString(R.string.specialCharacter);
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                getString(R.string.imageExtension),
                storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;

            try {
                photoFile = createImageFilePrivate();
            } catch (IOException e) {
                e.printStackTrace();
            }


            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        getString(R.string.com_healthcareproject_fileprovider),
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, Constants.CAMERA_REQUEST_CODE);
            }
        }
    }

    private void givePrivateExternalPermissionsDetailsToUser() {
        showMessageOKCancel(getString(R.string.permissionRequirement),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(SignUpDoctor.this, new String[]{Manifest.permission.CAMERA}, Constants.PERMISSION_REQUEST_CODE_1);

                    }
                });
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    private boolean validatePassword(String password){
        if(password.length()<8){
            tilPassword.setError("Min 8 characters are req");
            return false;
        }
        else {
            tilPassword.setError(null);
            return true;
        }
    }




}


