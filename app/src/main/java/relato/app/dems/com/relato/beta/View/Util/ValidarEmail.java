package relato.app.dems.com.relato.beta.View.Util;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * Created by CORAIMA on 05/12/2017.
 */

public class ValidarEmail {


    public ValidarEmail(Context context){

    }

    public boolean checkEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}
