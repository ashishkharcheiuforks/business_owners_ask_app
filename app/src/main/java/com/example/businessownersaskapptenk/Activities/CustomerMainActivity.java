package com.example.businessownersaskapptenk.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.businessownersaskapptenk.ApiService;
import com.example.businessownersaskapptenk.ApiServiceBuilder;
import com.example.businessownersaskapptenk.Fragments.OrderFragment;
import com.example.businessownersaskapptenk.Fragments.RestaurantListFragment;
import com.example.businessownersaskapptenk.Fragments.TrayFragment;
import com.example.businessownersaskapptenk.JsonModelObject.revenue.ResponseAvatar;
import com.example.businessownersaskapptenk.R;
import com.example.businessownersaskapptenk.Utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.businessownersaskapptenk.Activities.SignInActivity.BUTTON_SKIPPED;

public class CustomerMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private SharedPreferences sharedPref;
    NavigationView navigationView;
    String screen;
    Intent intent;
    private static final String TAG = "lgx_CustomerMainActivity";
    private ImageView customer_avatar;
    private TextView customer_name;
    public static final int REQUEST_SELECT_FILE = 100;
    public ValueCallback<Uri[]> uploadMessage;
    private final static int FCR = 1;
    private String mCM;
    private ValueCallback<Uri> mUM;
    private boolean multiple_files = false;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        intent = getIntent();
        screen = intent.getStringExtra("screen");
        mDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onDrawerClosed(View drawerView) {
                if (menuItem != null) {
                    int id = menuItem.getItemId();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    if (BUTTON_SKIPPED) {
                        Log.d(TAG, "onDrawerClosed: skipped below");
                        if (id == R.id.nav_restaurant) {
                            Log.d(TAG, "onDrawerClosed: nav restaurant skipped below IF");
                            Toast.makeText(CustomerMainActivity.this, "REST SKIPPED BELOW Customer", Toast.LENGTH_SHORT).show();
                        } else if (id == R.id.nav_tray) {
                            if (Objects.equals(screen, "tray")) {
                                Log.d(TAG, "onDrawerClosed: nav tray skipped below IF");
                                Toast.makeText(CustomerMainActivity.this, "TRAY SKIPPED BELOW Customer", Toast.LENGTH_SHORT).show();
                                handleLoginRequired();
                            } else {
                                Log.d(TAG, "onDrawerClosed: nav tray if_else Skipped");
                                handleLoginRequired();
                            }
                        } else if (id == R.id.nav_order) {
                            if (Objects.equals(screen, "order")) {
                                Log.d(TAG, "onDrawerClosed: nav order skipped below IF");
                                Toast.makeText(CustomerMainActivity.this, "ORDERS  SKIPPED BELOWCustomer", Toast.LENGTH_SHORT).show();
                                handleLoginRequired();
                            } else {
                                Log.d(TAG, "onDrawerClosed: nav order if_else Skipped");
                                handleLoginRequired();
                            }
                        } else {
                            Log.d(TAG, "onDrawerClosed: nav logout skipped below IF");
                            Toast.makeText(CustomerMainActivity.this, "LOGOUT SKIPPED BELOW Customer", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onDrawerClosed: logout clicked");
//                        finishAffinity();
                            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        BUTTON_SKIPPED = false;
                        if (id == R.id.nav_restaurant) {
                            Log.d(TAG, "onDrawerClosed: nav restaurant ELSE");
                            Toast.makeText(CustomerMainActivity.this, "REST Customer", Toast.LENGTH_SHORT).show();
                            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                            transaction.replace(R.id.content_frame, new RestaurantListFragment()).commit();
                        } else if (id == R.id.nav_tray) {
                            if (Objects.equals(screen, "tray")) {
                                Log.d(TAG, "onDrawerClosed: nav tray ELSE");
                                Toast.makeText(CustomerMainActivity.this, "TRAY Customer", Toast.LENGTH_SHORT).show();
                                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                                transaction.replace(R.id.content_frame, new TrayFragment()).commit();
                            } else {
                                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                                transaction.replace(R.id.content_frame, new TrayFragment()).commit();
                            }
                        } else if (id == R.id.nav_order) {
                            if (Objects.equals(screen, "order")) {
                                Toast.makeText(CustomerMainActivity.this, "ORDERS Customer", Toast.LENGTH_SHORT).show();
                                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                                transaction.replace(R.id.content_frame, new OrderFragment()).commit();
                            } else {
                                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                                transaction.replace(R.id.content_frame, new OrderFragment()).commit();
                            }
                        } else {
                            Toast.makeText(CustomerMainActivity.this, "LOGOUT Customer", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onDrawerClosed: logout clicked");
                            logoutToServer(sharedPref.getString("token", ""));
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.remove("token");
                            editor.apply();
                            finishAffinity();
                            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                            startActivity(intent);
                        }
                    }
                } else {
                    Log.d(TAG, "onDrawerClosed: menu item is null");
                }
            }
        });
        Toast.makeText(this, "onStart " + screen, Toast.LENGTH_SHORT).show();
        if (BUTTON_SKIPPED) {
            Log.d(TAG, "onCreate: BUTTON_SKIPPED IF --- " + BUTTON_SKIPPED);
            if (Objects.equals(screen, "tray")) {
                Log.d(TAG, "onCreate: tray skipped above IF");
                Toast.makeText(this, "Tray needs login", Toast.LENGTH_SHORT).show();
            } else if (Objects.equals(screen, "order")) {
                Log.d(TAG, "onCreate: order skipped above IF");
                Toast.makeText(this, "Order needs login", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "onCreate: restaurant skipped above IF");
                Toast.makeText(this, "SKIPPED ABOVE RESTAURANT", Toast.LENGTH_SHORT).show();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                transaction.replace(R.id.content_frame, new RestaurantListFragment()).commit();
            }
        } else {
            BUTTON_SKIPPED = false;
            Log.d(TAG, "onCreate: BUTTON_SKIPPED ELSE--- " + BUTTON_SKIPPED);
            if (Objects.equals(screen, "tray")) {
                Log.d(TAG, "onCreate: tray above ELSE");
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                transaction.replace(R.id.content_frame, new TrayFragment()).commit();
            } else if (Objects.equals(screen, "order")) {
                Log.d(TAG, "onCreate: order above ELSE");
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                transaction.replace(R.id.content_frame, new OrderFragment()).commit();
            } else {
                Log.d(TAG, "onCreate: restaurant above ELSE");
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                transaction.replace(R.id.content_frame, new RestaurantListFragment()).commit();
            }
        }
        sharedPref = getSharedPreferences("MY_KEY", Context.MODE_PRIVATE);
        View header = navigationView.getHeaderView(0);
        customer_avatar = (ImageView) header.findViewById(R.id.customer_avatar);
        customer_name = header.findViewById(R.id.customer_name);
        if (BUTTON_SKIPPED) {
            Log.d(TAG, "BUTTON SKIPPED");
            customer_name.setText("Guest");
            customer_avatar.setBackgroundResource(R.drawable.running);
            //ImageView display_image_view=findViewById(R.id.customer_avatar);
            customer_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(CustomerMainActivity.this, "You need to login to change your profile picture.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            BUTTON_SKIPPED = false;
            Log.d(TAG, "BUTTON NOT SKIPPED");
            customer_name.setText(sharedPref.getString("name", "You are Logged in"));
            Picasso.with(this).load(sharedPref.getString("avatar", "https://imgur.com/a/IhETAND")).transform(new CircleTransform()).into(customer_avatar);
            //ImageView display_image_view=findViewById(R.id.customer_avatar);
            customer_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!sharedPref.getString("login_method", "").equals("basic")) {
                        Toast.makeText(CustomerMainActivity.this, "You can not change your dp", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(CustomerMainActivity.this, "on dp clicked ", Toast.LENGTH_SHORT).show();
                    showImagePicker();
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @SuppressLint("LongLogTag")
    private void logoutToServer(final String token) {
        BUTTON_SKIPPED = true;
        Log.d(TAG, "logoutToServer: inside logoutToServer " + token);
        ApiService service = ApiServiceBuilder.getService();
        Call<Void> call = service.getToken(token, getString(R.string.CLIENT_ID), getString(R.string.CLIENT_SECRET));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, "onResponse: logoutToServer " + response.body());
                if (response.isSuccessful()) {
                    Toast.makeText(CustomerMainActivity.this, "CUSTOMER SUCCESS RESPONSE RETROFIT " + response.code(), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(CustomerMainActivity.this, "CUSTOMER ON RESPONSE ONLY RETROFIT" + +response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "onResponse: logoutToServer onFailure" + t.toString());
                Toast.makeText(CustomerMainActivity.this, "CUSTOMER FAILURE RETROFIT " + t.getMessage().toString() + t.getCause() + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    MenuItem menuItem;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        final DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        menuItem = item;
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void handleLoginRequired() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CustomerMainActivity.this);
        builder.setTitle(getString(R.string.skip_login_required_title));
        builder.setMessage(getString(R.string.skip_login_required_message));
        builder.setPositiveButton("Cancel", null);
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent loginIntent = new Intent(CustomerMainActivity.this, SignInActivity.class);
                startActivity(loginIntent);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showImagePicker() {
        if (file_permission()) {
            String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
            //checking for storage permission to write images for upload
            if (ContextCompat.checkSelfPermission(CustomerMainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(CustomerMainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CustomerMainActivity.this, perms, FCR);
                //checking for WRITE_EXTERNAL_STORAGE permission
            } else if (ContextCompat.checkSelfPermission(CustomerMainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CustomerMainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, FCR);
                //checking for CAMERA permissions
            } else if (ContextCompat.checkSelfPermission(CustomerMainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CustomerMainActivity.this, new String[]{Manifest.permission.CAMERA}, FCR);
            }
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Intent[] intentArray;
            intentArray = new Intent[]{pickPhoto};
            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            contentSelectionIntent.setType("*/*");
            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Choose an action");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
            startActivityForResult(chooserIntent, FCR);
        }
    }

    public boolean file_permission() {
        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(CustomerMainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, FCR);
            return false;
        } else {
            return true;
        }
    }

    //creating new image file here
    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Build.VERSION.SDK_INT >= 21) {
            Uri results = null;
            //checking if response is positive
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == FCR) {
                    results = data.getData();
                    handleSelectedImage(results);
                }
            }
            //////////////
        }
    }

    private void handleSelectedImage(Uri imageUri) {
        //////////////load the images on imageview
        View header = navigationView.getHeaderView(0);
        ImageView customer_avatar = header.findViewById(R.id.customer_avatar);
        Toast.makeText(CustomerMainActivity.this, "image select " + getPath(imageUri), Toast.LENGTH_SHORT).show();
        File image = new File(getPath(imageUri));
        Picasso.with(CustomerMainActivity.this).load(image).transform(new CircleTransform()).into(customer_avatar);
        uploadToServer(image);
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    private void uploadToServer(File file) {
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);
        String accessToken = sharedPref.getString("token", "");
        RequestBody token = RequestBody.create(MediaType.parse("multipart/form-data"), accessToken);
        ApiService service = ApiServiceBuilder.getService();
        Call<ResponseAvatar> call = service.uploadImage(part, token);
        call.enqueue(new Callback<ResponseAvatar>() {
            @Override
            public void onResponse(Call<ResponseAvatar> call, Response<ResponseAvatar> response) {
                if (response.body() == null)
                    return;
                if (response.isSuccessful()) {
                    Toast.makeText(CustomerMainActivity.this, "on response" + response.message(), Toast.LENGTH_SHORT).show();
                    ResponseAvatar responseAvatar = response.body();
                    SharedPreferences.Editor editor = sharedPref.edit();
                    try {
                        editor.putString("avatar", responseAvatar.getAvatar());
                        editor.commit();
                    } catch (Exception e) {
                    }
                }
                Log.d("PostSnapResponse", response.message());
            }

            @Override
            public void onFailure(Call<ResponseAvatar> call, Throwable t) {
                Log.d("PostSnapResponse", t.getMessage());
            }
        });
    }
}
