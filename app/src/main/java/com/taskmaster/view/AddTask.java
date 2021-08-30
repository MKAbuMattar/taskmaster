package com.taskmaster.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.FileUtils;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.taskmaster.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class AddTask extends AppCompatActivity {

  private static final String TAG = "AddTask";
  private static final int PERMISSION_ID = 44;
  private String spinner_task_status = null;
  private String teamName = null;
  private Team teamData = null;
  static String pattern = "yyMMddHHmmssZ";
  @SuppressLint("SimpleDateFormat")
  static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
  private static String FileUploadName = simpleDateFormat.format(new Date());
  private static String fileUploadExtension = null;
  private static File uploadFile = null;

  private FusedLocationProviderClient mFusedLocationClient;
  private double latitude;
  private double longitude;

  GoogleMap googleMap;

  @RequiresApi(api = Build.VERSION_CODES.Q)
  @SuppressLint("RestrictedApi")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_task);

    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    getLastLocation();

    Log.i(TAG, "onCreate: latitude => "+ latitude);
    Log.i(TAG, "onCreate: longitude => "+ longitude);

    if (Amplify.Auth.getCurrentUser() != null) {
      Log.i(TAG, "Auth: " + Amplify.Auth.getCurrentUser().toString());
    } else {
      Log.i(TAG, "Auth:  no user " + Amplify.Auth.getCurrentUser());
      Intent goToLogin = new Intent(this, LoginActivity.class);
      startActivity(goToLogin);
    }

    Spinner spinner = findViewById(R.id.spinner_status);

    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        R.array.task_status_array, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);

    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinner_task_status = (String) parent.getItemAtPosition(position);
        System.out.println(spinner_task_status);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
        spinner_task_status = (String) parent.getItemAtPosition(0);
      }
    });

    Intent intent = getIntent();
    String action = intent.getAction();
    String type = intent.getType();

    if (Intent.ACTION_SEND.equals(action) && type != null) {
      if (type.startsWith("image/")) {
        handleSendImage(intent); // Handle single image being sent
      }
    }

    Button uploadFile = findViewById(R.id.uploadFileBtn);
    uploadFile.setOnClickListener(v1 -> getFileFromDevice());

    TextView successLabel = findViewById(R.id.newTaskSubmitSuccess);
    successLabel.setVisibility(View.GONE);

    Button newTaskCreateButton = findViewById(R.id.newTaskSubmit);
    newTaskCreateButton.setOnClickListener(newTaskCreateListener);

    Objects.requireNonNull(getSupportActionBar()).setDefaultDisplayHomeAsUpEnabled(true);

  }

  private final View.OnClickListener newTaskCreateListener = v -> {
    String taskTitle = ((EditText) findViewById(R.id.newTaskName)).getText().toString();
    String taskBody = ((EditText) findViewById(R.id.newTaskBody)).getText().toString();
    String taskStatus = spinner_task_status;


    getTeamDetailFromAPIByName(teamName);

    try {
      Thread.sleep(1500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    Task item = Task.builder().
        title(taskTitle).
        description(taskBody).
        team(teamData).
        status(taskStatus).
        locationLatitude(latitude).
        locationLongitude(longitude).
        fileName(FileUploadName + "." + fileUploadExtension.split("/")[1]).
        build();

    saveTaskToAPI(item);

    TextView successLabel = findViewById(R.id.newTaskSubmitSuccess);
    successLabel.setVisibility(View.VISIBLE);
  };

  @RequiresApi(api = Build.VERSION_CODES.Q)
  public void handleSendImage(Intent intent) {
    Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
    if (imageUri != null) {
      // Update UI to reflect image being shared
      fileUploadExtension = getContentResolver().getType(imageUri);

      Log.i(TAG, "onActivityResult: gg is " + fileUploadExtension);
      Log.i(TAG, "onActivityResult: returned from file explorer");
      Log.i(TAG, "onActivityResult: success choose image");


      uploadFile = new File(getApplicationContext().getFilesDir(), "uploadFile");

      try {
        InputStream inputStream = getContentResolver().openInputStream(imageUri);
        FileUtils.copy(inputStream, new FileOutputStream(uploadFile));
      } catch (Exception exception) {
        Log.e(TAG, "onActivityResult: file upload failed" + exception.toString());
      }
    }
  }

  public void saveTaskToAPI(Task item) {
    Amplify.Storage.uploadFile(
        FileUploadName + "." + fileUploadExtension.split("/")[1],
        uploadFile,
        success -> {
          Log.i(TAG, "uploadFileToS3: succeeded " + success.getKey());
        },
        error -> {
          Log.e(TAG, "uploadFileToS3: failed " + error.toString());
        }
    );
    Amplify.API.mutate(ModelMutation.create(item),
        success -> Log.i(TAG, "Saved item to api : " + success.getData()),
        error -> Log.e(TAG, "Could not save item to API/dynamodb", error));
  }

  @RequiresApi(api = Build.VERSION_CODES.Q)
  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == 999 && resultCode == RESULT_OK) {
      Uri uri = data.getData();
      fileUploadExtension = getContentResolver().getType(uri);

      Log.i(TAG, "onActivityResult: gg is " + fileUploadExtension);
      Log.i(TAG, "onActivityResult: returned from file explorer");
      Log.i(TAG, "onActivityResult: => " + data.getData());
      Log.i(TAG, "onActivityResult:  data => " + data.getType());

      uploadFile = new File(getApplicationContext().getFilesDir(), "uploadFile");

      try {
        InputStream inputStream = getContentResolver().openInputStream(data.getData());
        FileUtils.copy(inputStream, new FileOutputStream(uploadFile));
      } catch (Exception exception) {
        Log.e(TAG, "onActivityResult: file upload failed" + exception.toString());
      }

    }
  }

  private void getFileFromDevice() {
    Intent upload = new Intent(Intent.ACTION_GET_CONTENT);
    upload.setType("*/*");
    upload = Intent.createChooser(upload, "Choose a File");
    startActivityForResult(upload, 999); // deprecated
  }

  @SuppressLint("NonConstantResourceId")
  public void onClickRadioButton(View view) {
    boolean checked = ((RadioButton) view).isChecked();
    switch (view.getId()) {
      case R.id.team1:
        if (checked)
          Log.i(TAG, "onClickRadioButton: team 1");
        teamName = "team 1";
        break;
      case R.id.team2:
        if (checked)
          Log.i(TAG, "onClickRadioButton: team 2");
        teamName = "team 2";
        break;
      case R.id.team3:
        if (checked)
          Log.i(TAG, "onClickRadioButton: team 3");
        teamName = "team 3";
        break;
    }
  }

  public void getTeamDetailFromAPIByName(String name) {
    Amplify.API.query(
        ModelQuery.list(Team.class, Team.NAME.contains(name)),
        response -> {
          for (Team teamDetail : response.getData()) {
            Log.i(TAG, teamDetail.getName());
            teamData = teamDetail;
          }
        },
        error -> Log.e(TAG, "Query failure", error)
    );
  }


  @SuppressLint("MissingPermission")
  private void getLastLocation() {
    // check if permissions are given
    if (checkPermissions()) {

      // check if location is enabled
      if (isLocationEnabled()) {

        // getting last
        // location from
        // FusedLocationClient
        // object
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
          @Override
          public void onComplete(@NonNull com.google.android.gms.tasks.Task<Location> task) {
            Location location = task.getResult();
            if (location == null) {
              requestNewLocationData();
            } else {
//                            latitudeTextView.setText(location.getLatitude() + "");
//                            longitTextView.setText(location.getLongitude() + "");

              latitude = location.getLatitude();
              longitude = location.getLongitude();

//                            googleMap.addMarker(new MarkerOptions()
//                                    .position(new LatLng(latitude, longitude))
//                                    .title("Marker"));
              Log.i(TAG, "onCreate: latitude => "+ latitude);
              Log.i(TAG, "onCreate: longitude => "+ longitude);
            }
          }
        });
      } else {
        Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
      }
    } else {
      // if permissions aren't available,
      // request for permissions
      requestPermissions();
    }
  }



  private boolean checkPermissions() {
    return ActivityCompat
        .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
        PackageManager.PERMISSION_GRANTED
        &&
        ActivityCompat
            .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED;

    // If we want background location
    // on Android 10.0 and higher,
    // use:
    // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
  }
  private boolean isLocationEnabled() {
    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
  }

  @SuppressLint("MissingPermission")
  private void requestNewLocationData() {

    // Initializing LocationRequest
    // object with appropriate methods
    LocationRequest mLocationRequest = new LocationRequest();
    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    mLocationRequest.setInterval(5);
    mLocationRequest.setFastestInterval(0);
    mLocationRequest.setNumUpdates(1);

    // setting LocationRequest
    // on FusedLocationClient
    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
  }
  private LocationCallback mLocationCallback = new LocationCallback() {

    @Override
    public void onLocationResult(LocationResult locationResult) {
      Location mLastLocation = locationResult.getLastLocation();
//            latitudeTextView.setText("Latitude: " + mLastLocation.getLatitude() + "");
//            longitTextView.setText("Longitude: " + mLastLocation.getLongitude() + "");
    }
  };
  private void requestPermissions() {
    ActivityCompat.requestPermissions(this, new String[]{
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
  }
}
