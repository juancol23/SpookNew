package relato.app.dems.com.relato;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by CORAIMA on 02/11/2017.
 */

public class SimpleBlogAplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
