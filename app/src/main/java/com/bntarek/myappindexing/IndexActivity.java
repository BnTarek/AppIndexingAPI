package com.bntarek.myappindexing;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class IndexActivity extends AppCompatActivity {

    private GoogleApiClient mClient;
    private static final String TAG = IndexActivity.class.getName();
    private static final Uri BASE_APP_URI = Uri.parse("android-app://com.bntarek.myappindexing/http/myappindexing.bntarek.com/index");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.APP_INDEX_API).build();

        onNewIntent(getIntent());
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Connect your client
        mClient.connect();

        // Define a title for your current page, shown in autocompletion UI
        final String TITLE = "My index activity title";
        final Uri APP_URI = BASE_APP_URI.buildUpon().appendPath("1").build();

        Action viewAction = Action.newAction(Action.TYPE_VIEW, TITLE, APP_URI);

        // Call the App Indexing API view method
        PendingResult<Status> result = AppIndex.AppIndexApi.start(mClient, viewAction);

        result.setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                if (status.isSuccess()) {
                    Log.d(TAG, "App Indexing API: Recorded view successfully.");
                } else {
                    Log.e(TAG, "App Indexing API: There was an error recording the recipe view."
                            + status.toString());
                }
            }
        });
    }

    @Override
    protected void onStop() {
        final String TITLE = "My index activity title";
        final Uri APP_URI = BASE_APP_URI.buildUpon().appendPath("1").build();

        Action viewAction = Action.newAction(Action.TYPE_VIEW, TITLE, APP_URI);
        PendingResult<Status> result = AppIndex.AppIndexApi.end(mClient, viewAction);

        result.setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                if (status.isSuccess()) {
                    Log.d(TAG, "App Indexing API: Recorded view end successfully.");
                } else {
                    Log.e(TAG, "App Indexing API: There was an error recording the recipe view."
                            + status.toString());
                }
            }
        });

        mClient.disconnect();

        super.onStop();
    }
}
