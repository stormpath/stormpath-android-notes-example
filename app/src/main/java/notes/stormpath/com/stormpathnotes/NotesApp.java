package notes.stormpath.com.stormpathnotes;

import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathConfiguration;
import com.stormpath.sdk.StormpathLogger;

import android.app.Application;

/**
 * Created by ericlw on 3/15/16.
 */
public class NotesApp extends Application {

    public static final String baseUrl = "https://stormpathnotes.herokuapp.com/";

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            // we only want to show the logs in debug builds, for easier debugging
            Stormpath.setLogLevel(StormpathLogger.VERBOSE);
        }

        // Initialize Stormpath
        StormpathConfiguration stormpathConfiguration = new StormpathConfiguration.Builder()
                .baseUrl(baseUrl)
                .build();
        Stormpath.init(this, stormpathConfiguration);
    }

}
