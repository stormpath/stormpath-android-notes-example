package com.stormpath.demo.stormpathnotes;

import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathConfiguration;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NotesServerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(com.stormpath.demo.stormpathnotes.R.layout.activity_server);

        Button done = (Button)findViewById(com.stormpath.demo.stormpathnotes.R.id.button);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ipAddress = ((TextInputEditText)findViewById(com.stormpath.demo.stormpathnotes.R.id.editText)).getText().toString();
                String port = ((TextInputEditText)findViewById(com.stormpath.demo.stormpathnotes.R.id.editText2)).getText().toString();

                if(!ipAddress.isEmpty() && Patterns.IP_ADDRESS.matcher(ipAddress).matches()) {

                    Stormpath.reset();

                    NotesApp.baseUrl = "http://" + ipAddress + ":" + port + "/";

                    StormpathConfiguration stormpathConfiguration = new StormpathConfiguration.Builder()
                            .baseUrl(NotesApp.baseUrl)
                            .build();
                    Stormpath.init(getApplicationContext(), stormpathConfiguration);
                    finish();

                    //send broadcast to login again
                }
                Snackbar.make(v, getString(com.stormpath.demo.stormpathnotes.R.string.saving), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

    }
}
