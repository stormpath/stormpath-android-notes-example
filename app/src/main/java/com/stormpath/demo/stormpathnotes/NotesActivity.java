package com.stormpath.demo.stormpathnotes;

import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathCallback;
import com.stormpath.sdk.models.StormpathError;
import com.stormpath.sdk.models.UserProfile;
import com.stormpath.sdk.ui.StormpathLoginActivity;
import com.stormpath.sdk.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class NotesActivity extends AppCompatActivity {

    EditText mNote;
    Context context;
    private OkHttpClient okHttpClient;
    public static final String ACTION_GET_NOTES = "notes.get";
    public static final String ACTION_POST_NOTES = "notes.post";

    //used to intercept the social media oauth callbacks
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // getIntent() should always return the most recent
        setIntent(intent);



        //check contents of intent
        if(getIntent().getData()!=null && getIntent().getData().getScheme()!=null){

            if(getIntent().getData().getScheme().contentEquals(getString(com.stormpath.demo.stormpathnotes.R.string.fb_app_id))){

                //should retrieve the access tokens here and pass it to Stormpath

                /*Stormpath.socialLogin(SocialProvidersResponse.FACEBOOK, loginResult.getAccessToken().getToken(),
                        new StormpathCallback<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // we are logged in via fb!
                                Toast.makeText(NotesActivity.this, "Success! " + loginResult.getAccessToken().getToken(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(StormpathError error) {
                                Toast.makeText(NotesActivity.this, error.message(), Toast.LENGTH_LONG).show();
                            }
                        });
                */


            }//end if
            else if(getIntent().getData().getScheme().contentEquals(getString(com.stormpath.demo.stormpathnotes.R.string.google_app_id))){
                /*
                //google requires something more

                Stormpath.socialLogin(SocialProvidersResponse.GOOGLE, loginResult.getAccessToken().getToken(),
                        new StormpathCallback<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // we are logged in via Google+!
                                Toast.makeText(NotesActivity.this, "Success! " + loginResult.getAccessToken().getToken(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(StormpathError error) {
                                Toast.makeText(NotesActivity.this, error.message(), Toast.LENGTH_LONG).show();
                            }
                        });
                */
            }


        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.stormpath.demo.stormpathnotes.R.layout.activity_notes);
        Toolbar toolbar = (Toolbar) findViewById(com.stormpath.demo.stormpathnotes.R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        serverNotification();

        //initialize OkHttp library
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Stormpath.logger().d(message);
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        this.okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(httpLoggingInterceptor)
                .build();

        FloatingActionButton fab = (FloatingActionButton) findViewById(com.stormpath.demo.stormpathnotes.R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, getString(com.stormpath.demo.stormpathnotes.R.string.saving), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                saveNote();
            }
        });

        mNote = (EditText)findViewById(com.stormpath.demo.stormpathnotes.R.id.note);
        mNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




    }

    private void serverNotification(){
        //connect w/ own IP, launch notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(com.stormpath.demo.stormpathnotes.R.mipmap.ic_launcher)
                        .setContentTitle(getString(com.stormpath.demo.stormpathnotes.R.string.app_name))
                        .setContentText(getString(com.stormpath.demo.stormpathnotes.R.string.change_server));

        Intent resultIntent = new Intent(this, NotesServerActivity.class);
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    private void serverNotification(){
        //connect w/ own IP, launch notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(com.stormpath.demo.stormpathnotes.R.mipmap.ic_launcher)
                        .setContentTitle(getString(com.stormpath.demo.stormpathnotes.R.string.app_name))
                        .setContentText(getString(com.stormpath.demo.stormpathnotes.R.string.change_server));

        Intent resultIntent = new Intent(this, NotesServerActivity.class);
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }


    @Override
    public void onResume(){
        super.onResume();

        IntentFilter noteGetFilter = new IntentFilter(ACTION_GET_NOTES);
        IntentFilter notePostFilter = new IntentFilter(ACTION_POST_NOTES);

        LocalBroadcastManager.getInstance(this).registerReceiver(onNoteReceived, noteGetFilter);
        LocalBroadcastManager.getInstance(this).registerReceiver(onNoteReceived, notePostFilter);


    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onNoteReceived);
    }

    private void getNotes(){

        Request request = new Request.Builder()
                .url(NotesApp.baseUrl + "notes")
                .headers(buildStandardHeaders(Stormpath.accessToken()))
                .get()
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                JSONObject mNotes;
                try {
                    mNotes = new JSONObject(response.body().string());
                    String noteCloud = mNotes.getString("notes");

                    Intent intent = new Intent(ACTION_GET_NOTES);
                    // You can also include some extra data.
                    intent.putExtra("notes", noteCloud);

                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                } catch (JSONException e){

                }

            }

        });

    }

    private void saveNote(){

        RequestBody requestBody = new FormBody.Builder()
                .add("notes", mNote.getText().toString())
                .build();

        Request request = new Request.Builder()
                .url(NotesApp.baseUrl + "notes")
                .headers(buildStandardHeaders((Stormpath.accessToken())))
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                call.isCanceled();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Intent intent = new Intent(ACTION_POST_NOTES);

                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

            }
        });


    }

    private Headers buildStandardHeaders(String accessToken) {
        Headers.Builder builder = new Headers.Builder();
        builder.add("Accept", "application/json");
        if (StringUtils.isNotBlank(accessToken)) {
            builder.add("Authorization", "Bearer " + accessToken);
        }
        return builder.build();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.stormpath.demo.stormpathnotes.R.menu.menu_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.stormpath.demo.stormpathnotes.R.id.action_logout) {

            mNote.setText(""); //clears edit text, could alternatively save to shared preferences


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private BroadcastReceiver onNoteReceived = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().contentEquals(ACTION_GET_NOTES))
                mNote.setText(intent.getExtras().getString("notes"));
            else if(intent.getAction().contentEquals(ACTION_POST_NOTES))
                Snackbar.make(mNote, getString(com.stormpath.demo.stormpathnotes.R.string.saved), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

        }
    };
}
