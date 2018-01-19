package william.course.summer.umeo.runway;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import info.hoang8f.widget.FButton;
import william.course.summer.umeo.runway.Controller.Controller;
import william.course.summer.umeo.runway.Model.Run;
import william.course.summer.umeo.runway.Model.RunData;
import william.course.summer.umeo.runway.Model.Timer;

public class Fragment3 extends Fragment {


    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";
    private final static String KEY_PAUSE_TIME = "pause_time_key";
    private final static String KEY_RUNS = "runs_recovery_key";
    private final static String KEY_TIMER = "timer_key";
    private final static String KEY_LAST_VELOCITY = "last_velocity";
    private final static String KEY_LAST_ALTITUDE = "last_altitude";
    private final static String KEY_LAST_DISTANCE = "last_distance";
    private static final String TAG = "Fragment1";

    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;

    private TextView mLastUpdateTimeTextView;
    private TextView mVelocityTextView;
    private TextView mAltitudeTextView;
    private TextView mDistanceTextView;
    private String mLastUpdateTimeLabel;
    private View view;

    private Boolean mRequestingLocationUpdates;
    private String mLastUpdateTime;
    private boolean recovered = false;
    private Controller mController;
    private Chronometer mChronometer;
    private FButton btnPause;
    private FButton btnResume;
    private FButton btnStop;
    private FButton btnStart;
    private long lastPause;
    private RunData runData;
    private Date date;


    /**
     * Inflates the third fragment of the application.
     * @param inflater Inflater to inflate
     * @param container Viewgroup container
     * @param savedInstanceState Used for restoring data on event
     * @return Returns a View
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment3_layout, container, false);
        setRetainInstance(true);
        mController = ((MainActivity) getActivity()).getController();

        mChronometer = (Chronometer) view.findViewById(R.id.chronometer);
        btnPause = (FButton) view.findViewById(R.id.btnPause);
        btnResume = (FButton) view.findViewById(R.id.btnResume);
        btnStop = (FButton) view.findViewById(R.id.btnStop);
        btnStart = (FButton) view.findViewById(R.id.btnStart);
        mVelocityTextView = (TextView) view.findViewById(R.id.velocityTextView);
        mAltitudeTextView = (TextView) view.findViewById(R.id.altitudeTextView);
        mLastUpdateTimeTextView = (TextView) view.findViewById(R.id.last_update_time_text);
        mDistanceTextView = (TextView) view.findViewById(R.id.distance_textview);

        mLastUpdateTimeLabel = getResources().getString(R.string.last_update_time_label);
        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        updateValuesFromBundle(savedInstanceState);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        mSettingsClient = LocationServices.getSettingsClient(getContext());

        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
        setup();

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mController.pauseRun();
                onPause();
                updateView();
            }
        });

        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mController.resumeRun();
                onResume();
                updateView();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mController.isRunActive()) {
                    mController.stopRun();
                    stopLocationUpdates();
                    saveRun();
                    updateView();
                }
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mController.startRun();
                if (!mRequestingLocationUpdates) {
                    mRequestingLocationUpdates = true;
                    startLocationUpdates();
                }
                runData = new RunData();
                runData.setDate(date = new Date());
                setup();
            }
        });

        return view;
    }

    /**
     * Checks the result provided by the user choosing to either
     * make required setting changes or not.
     * @param requestCode The requestcode for the request
     * @param resultCode The user provided result code
     * @param data Data provided by intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        updateUI();
                        break;
                }
                break;
        }
    }

    /**
     * Handles the Stop Updates button, and requests removal of location updates.
     */
    public void stopUpdatesButtonHandler(View view) {
        stopLocationUpdates();
    }

    /**
     * Handles the resume call for the LocationUpdates
     */
    @Override
    public void onResume() {
        super.onResume();
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        } else if (!checkPermissions()) {
            requestPermissions();
        }

        updateUI();
    }

    /**
     * Handles the pause call for the LocationUpdates
     */
    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    /**
     * Stores activity data in the Bundle.
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
        savedInstanceState.putParcelable(KEY_RUNS, mController.getRun());
        savedInstanceState.putParcelable(KEY_TIMER, mController.getTimer());
        savedInstanceState.putLong(KEY_PAUSE_TIME, mChronometer.getBase() - SystemClock.elapsedRealtime());
        savedInstanceState.putFloat(KEY_LAST_ALTITUDE, Float.parseFloat(String.valueOf(mAltitudeTextView.getText())));
        savedInstanceState.putFloat(KEY_LAST_VELOCITY, Float.parseFloat(String.valueOf(mVelocityTextView.getText())));
        savedInstanceState.putFloat(KEY_LAST_DISTANCE, Float.parseFloat(String.valueOf(mDistanceTextView.getText())));
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Callback received when the permissionRequest has completed
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mRequestingLocationUpdates) {
                    Log.i(TAG, "Permission granted, updates requested, starting location updates");
                    startLocationUpdates();
                }
            } else {

                showSnackbar(R.string.permission_denied_explanation,
                        R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    private void startLocationUpdates() {
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());
                        updateUI();
                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);
                                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                                mRequestingLocationUpdates = false;
                        }

                        updateUI();
                        updateView();
                    }
                });
    }

    /**
     * Updates some some UI fields
     */
    private void updateUI() {
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            mAltitudeTextView.setText(String.format(Locale.ENGLISH, "%.2f", mCurrentLocation.getAltitude()));
            mVelocityTextView.setText(String.format(Locale.ENGLISH, "%.2f", mCurrentLocation.getSpeed()));
            mLastUpdateTimeTextView.setText(String.format(Locale.ENGLISH, "%s:\n %s",
                    mLastUpdateTimeLabel, mLastUpdateTime));
            mDistanceTextView.setText(String.format(Locale.ENGLISH, "%.2f", Float.parseFloat(String.valueOf(mDistanceTextView.getText())) + mController.getLengthTraveled(mController.getTime(), mCurrentLocation.getSpeed())));
            updateStorageElement();
        }
    }

    private void updateStorageElement() {
        runData.setAltitude(mCurrentLocation.getAltitude());
        runData.setSpeed(mCurrentLocation.getSpeed());
        runData.setLatitude(mCurrentLocation.getLatitude());
        runData.setLongitude(mCurrentLocation.getLongitude());
        runData.setDistanceTraveled(Float.parseFloat(String.valueOf(mDistanceTextView.getText())));
    }

    private void stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            Log.d(TAG, "stopLocationUpdates: updates never requested, no-op.");
            return;
        }

        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mRequestingLocationUpdates = false;
                    }
                });
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                view.findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void updateView() {
        if (mController.isRunPaused()) {
            lastPause = mChronometer.getBase() - SystemClock.elapsedRealtime();
            mChronometer.stop();
            btnPause.setVisibility(View.GONE);
            btnResume.setVisibility(View.VISIBLE);
            Log.d(TAG, "updateView: paused");

        } else if (mController.isRunActive()) {
            mChronometer.setBase(SystemClock.elapsedRealtime() + lastPause);
            mChronometer.start();
            btnStart.setVisibility(View.GONE);
            btnPause.setVisibility(View.VISIBLE);
            btnResume.setVisibility(View.GONE);
            Log.d(TAG, "updateView: resumed");
        } else {
            mChronometer.stop();
            mChronometer.setBase(SystemClock.elapsedRealtime());
            lastPause = 0;
            mChronometer.setText("00:00");
            btnPause.setVisibility(View.GONE);
            btnResume.setVisibility(View.GONE);
            btnStart.setVisibility(View.VISIBLE);
            Log.d(TAG, "updateView: stopped");
        }
    }

    private void setup() {
        if (mController.isRunActive()) {
            mChronometer.setBase(SystemClock.elapsedRealtime() + lastPause);
            mChronometer.start();
            btnStart.setVisibility(View.GONE);
            btnPause.setVisibility(View.VISIBLE);
            Log.d(TAG, "setup: chrono started");
        }
    }

    private void saveRun() {
        runData.setTime(mController.getTime());
        mController.addCompletedRun(runData);
        try {
            mController.saveData(getContext());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "We are sorry, an error occured. Trying to fix it. Don't worry.", Toast.LENGTH_SHORT).show();
        }

        try {
            mController.updateHistory(getContext());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "We are sorry, an error occured. Trying to fix it. Don't worry.", Toast.LENGTH_SHORT).show();
        }
        resetScreenText();
        mLastUpdateTimeTextView.setText("");
        Toast.makeText(getContext(), "History entry added! \nSwipe right to see it.", Toast.LENGTH_LONG).show();

    }

    private void resetScreenText(){
        mChronometer.setText("00:00");
        mAltitudeTextView.setText("0.00");
        mVelocityTextView.setText("0.00");
        mDistanceTextView.setText("0.00");
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES);
            }

            if (savedInstanceState.keySet().contains(KEY_PAUSE_TIME)) {
                lastPause = savedInstanceState.getLong(KEY_PAUSE_TIME);
                mChronometer.setBase(SystemClock.elapsedRealtime() + lastPause);
            }
            if (savedInstanceState.keySet().contains(KEY_RUNS)) {
                mController.setRun((Run) savedInstanceState.getParcelable(KEY_RUNS));
            }

            if (savedInstanceState.keySet().contains(KEY_TIMER)) {
                mController.setTimer((Timer) savedInstanceState.getParcelable(KEY_TIMER));
            }

            if (savedInstanceState.keySet().contains(KEY_LAST_ALTITUDE)) {
                mAltitudeTextView.setText(Float.toString(savedInstanceState.getFloat(KEY_LAST_ALTITUDE)));
            }

            if (savedInstanceState.keySet().contains(KEY_LAST_DISTANCE)) {
                mDistanceTextView.setText(Float.toString(savedInstanceState.getFloat(KEY_LAST_DISTANCE)));
            }

            if (savedInstanceState.keySet().contains(KEY_LAST_VELOCITY)) {
                mVelocityTextView.setText(Float.toString(savedInstanceState.getFloat(KEY_LAST_VELOCITY)));
            }

            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }

            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
            }
            updateUI();
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateLocationUI();
            }
        };
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

}
