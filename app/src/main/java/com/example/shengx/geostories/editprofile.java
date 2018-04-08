package com.example.shengx.geostories;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shengx.geostories.ImageUtility.GallaryImage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.GenericArrayType;
import java.util.HashMap;
import java.util.Map;

public class editprofile extends AppCompatActivity {
    EditText username, about;
    FirebaseUser current_client;
    FirebaseFirestore db;
    DocumentReference docRef;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView replace_profile_img;

    private final int REQ_W_STORAGE_CODE=200;
    private final int GALLERY_REQUEST=100;
    TextView profile_img;
    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference mountainsRef;
    StorageReference gsReference;
    File storagePath;
    File myFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        // Get the default bucket from a custom FirebaseApp
         storage = FirebaseStorage.getInstance();

        // Create a storage reference from our app
         storageRef = storage.getReference();


        username=(EditText)findViewById(R.id.username_profile);
        about=(EditText)findViewById(R.id.about_profile);
        about.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event == null) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        // Capture soft enters in a singleLine EditText that is the last EditText
                        // This one is useful for the new list case, when there are no existing ListItems
                        about.clearFocus();
                        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    } else if (actionId == EditorInfo.IME_ACTION_NEXT) {
                        // Capture soft enters in other singleLine EditTexts
                    } else if (actionId == EditorInfo.IME_ACTION_GO) {
                    } else {
                        // Let the system handle all other null KeyEvents
                        return false;
                    }
                } else if (actionId == EditorInfo.IME_NULL) {
                    // Capture most soft enters in multi-line EditTexts and all hard enters;
                    // They supply a zero actionId and a valid keyEvent rather than
                    // a non-zero actionId and a null event like the previous cases.
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        // We capture the event when the key is first pressed.
                    } else {
                        // We consume the event when the key is released.
                        return true;
                    }
                } else {
                    // We let the system handle it when the listener is triggered by something that
                    // wasn't an enter.
                    return false;
                }
                return true;
            }
        });
        sharedPreferences=getApplicationContext().getSharedPreferences("Client",0);
        editor=sharedPreferences.edit();
        username.setCursorVisible(false);
        about.setCursorVisible(false);
        db=FirebaseFirestore.getInstance();
        current_client= FirebaseAuth.getInstance().getCurrentUser();

        String mUsrename=sharedPreferences.getString("username","").toString();
        String mAbout=sharedPreferences.getString("about","").toString();
        username.setText(mUsrename);
        about.setText(mAbout);

        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setCursorVisible(true);
                username.setText("");
                username.setBackgroundColor(Color.argb(50,180,180,180));
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                about.setCursorVisible(true);
                about.setText("");
                about.setBackgroundColor(Color.argb(50,180,180,180));

            }
        });
        gsReference = storage.getReferenceFromUrl("gs://geostories-87738.appspot.com/"+current_client.getUid()+".jpg");
        storagePath = new File(Environment.getExternalStorageDirectory(), "Geo_Images");
        // Create direcorty if not exists
        if(!storagePath.exists()) {
            storagePath.mkdirs();
            Log.d("log---G","Geo file created");
        }
        myFile = new File(storagePath,current_client.getUid()+".jpg");
        profile_img=(TextView)findViewById(R.id.user_img_profile);
        replace_profile_img=(TextView)findViewById(R.id.change_img_profile);
        replace_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isStoragePermissionGranted()){
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, GALLERY_REQUEST);

                    gsReference.getFile(myFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Local temp file has been created
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
                }
            }
        });





//        final long ONE_MEGABYTE = 1024 * 1024 *5;
//        gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                // Data for "images/island.jpg" is returns, use this as needed
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inMutable = true;
//                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
//                BitmapDrawable ob = new BitmapDrawable(getResources(), bmp);
//                profile_img.setBackgroundDrawable(ob);
//                Log.d("Log","success");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//                Log.d("Log","No ~!!success");
//            }
//        });


        String photoPath = Environment.getExternalStorageDirectory()+"/Geo_Images/"+current_client.getUid()+".jpg";
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);

        if(bitmap!=null){
            BitmapDrawable ob=new BitmapDrawable(getResources(),bitmap);
            profile_img.setBackgroundDrawable(ob);
        }
        Log.d("Log","success-->");


    }

    public void updateClientInfo(final String mUsername, final String mAbout){
        final Map<String,Object> user=new HashMap<>();
        user.put("username",mUsername);
        user.put("about",mAbout);
        db.collection("users").whereEqualTo("username",mUsername).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if(documentSnapshots.size()>0){
                            if(documentSnapshots.getDocuments().get(0).getId().toString().equals(FirebaseAuth.getInstance().getUid().toString())){
                                Toast.makeText(getApplicationContext(),"Saved.",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Username Exists!",Toast.LENGTH_SHORT).show();

                            }
                        }
                        else {
                            db.collection(Geocons.DBcons.USER_DB)
                                    .document(current_client.getUid())
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            editor.putString("username",username.getText().toString());
                                            editor.putString("about",about.getText().toString());
                                            editor.commit();
                                            Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(),"Failure",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        };
                    }
                });

    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editprofile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveprofile:
                updateUserProfile();
                return true;
            default:
                return  super.onOptionsItemSelected(item);
        }
    }

    public void updateUserProfile(){
        updateClientInfo(username.getText().toString(),about.getText().toString());
        username.setCursorVisible(false);
        about.setCursorVisible(false);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case GALLERY_REQUEST:
                    Uri selectedImage = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedImage);
                        BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
                        profile_img.setBackgroundDrawable(ob);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 5, baos);
                        byte[] mData = baos.toByteArray();
                        mountainsRef = storageRef.child(current_client.getUid()+".jpg");
                        UploadTask uploadTask = mountainsRef.putBytes(mData);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                gsReference.getFile(myFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        // Local temp file has been created
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                    }
                                });
                            }
                        });

                        Log.d("TAG", "Success" );
                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                    break;
            }
    }
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Log--p","Permission is granted");
                return true;
            } else {

                Log.v("Log--p","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},REQ_W_STORAGE_CODE );
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Log--p","Permission is granted");
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v("Log--p","Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
        }
        else {
            Toast.makeText(getApplicationContext(),"Please grant the permission to continue",Toast.LENGTH_SHORT).show();
        }
    }
}