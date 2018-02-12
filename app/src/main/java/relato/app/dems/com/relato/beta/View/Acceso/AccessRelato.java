package relato.app.dems.com.relato.beta.View.Acceso;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import relato.app.dems.com.relato.beta.MenuCustomizeNow;
import relato.app.dems.com.relato.beta.R;

public class AccessRelato extends AppCompatActivity {
    public TextView mAcessRelatoTextTitle;
    public TextView mAcessRelatoTextRegister;
    public TextView mAcessRelatoTextForget;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private LoginButton mAccessRelatoFacebook;
    private FrameLayout mAccessRelatoGoogle;
    private CallbackManager mCallbackManager;

    private ProgressDialog mProgress;
    private GoogleApiClient mGoogleApiClient;

    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "Login Sign";

    private VideoView mVideoViewAcceso;

    private DatabaseReference mDatabaseUser;

    private static Typeface Pacifico,Nightmare,Double,BloodLust;

    /*Animación*/
    private Animation mUp_to_down,mhide_to_bottom;

    private LinearLayout mLinear_auth,mLinear_auth_footer;

    @BindView(R.id.accesso_login_email) EditText mAccesso_login_email;
    @BindView(R.id.accesso_login_password) EditText mAccesso_login_password;
    @BindView(R.id.accesso_login_btn) Button mAccesso_login_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_relato_extra);
        mProgress = new ProgressDialog(this);
        ButterKnife.bind(this);
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");



        checkaccess();

        mAcessRelatoTextTitle = (TextView) findViewById(R.id.title_sangrienta_lectura);
        mAcessRelatoTextRegister = (TextView) findViewById(R.id.AcessRelatoTextRegister);
        mAcessRelatoTextForget = (TextView) findViewById(R.id.AcessRelatoTextForget);

        mLinear_auth = (LinearLayout) findViewById(R.id.linear_auth);
        mLinear_auth_footer = (LinearLayout) findViewById(R.id.linear_auth_footer);

        mUp_to_down = AnimationUtils.loadAnimation(AccessRelato.this,R.anim.up_to_down);
        mhide_to_bottom = AnimationUtils.loadAnimation(AccessRelato.this,R.anim.down_to_up);

//        mAcessRelatoTextTitle.setAnimation(mUp_to_down);
  //      mLinear_auth.setAnimation(mhide_to_bottom);
//        mLinear_auth_footer.setAnimation(mhide_to_bottom);

        String bloodLustFuente= "fuentes/BloodLust.ttf";
        String nightmareFuente= "fuentes/Nigh.ttf";
        this.BloodLust = Typeface.createFromAsset(getAssets(),bloodLustFuente);
        this.Nightmare = Typeface.createFromAsset(getAssets(),nightmareFuente);

//        mAcessRelatoTextTitle.setTypeface(BloodLust);
  //      mAcessRelatoTextRegister.setTypeface(Nightmare);
    //    mAcessRelatoTextForget.setTypeface(Nightmare);


        String textTitle = "<font color='#da152c'>Sa</font>ngrien<font color='#da152c'>ta</font><br/><font color='#da152c'>Lec</font>tura";
        String textRegister = "<font color='#da152c'>REGIS</font>TRATE";
        String textForget = "<font color='#da152c'>¿OLVIDÉ LA</font> CONTRASEÑA";
//        mAcessRelatoTextTitle.setText(Html.fromHtml(textTitle));

//        mAcessRelatoTextRegister.setText(Html.fromHtml(textRegister), TextView.BufferType.SPANNABLE);
  //      mAcessRelatoTextForget.setText(Html.fromHtml(textForget), TextView.BufferType.SPANNABLE);


        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("Login", user.getUid());
                    accesoPermitido();
                } else {
                    Log.d("Login", "Signed Out");
                }
            }
        };

        mCallbackManager = CallbackManager.Factory.create();
        mAccessRelatoFacebook = (LoginButton) findViewById(R.id.accessRelatoFacebook);
        mAccessRelatoFacebook.setReadPermissions("email", "public_profile");

        mAccessRelatoFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                signInWithFacebook(loginResult.getAccessToken());
                showSnackBar(" Autenticación Satisfactoria");
               // Toast.makeText(getApplicationContext(), "Autenticación Satisfactoria ", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancel() {
                showSnackBar(" Autenticación Cancelada");
                //Toast.makeText(getApplicationContext(), "Autenticación Cancelada ", Toast.LENGTH_SHORT).show();
                mProgress.dismiss();

            }
            @Override
            public void onError(FacebookException error) {
                showSnackBar("Error en la Autenticación");
                //Toast.makeText(getApplicationContext(), "Error en la Autenticación ", Toast.LENGTH_SHORT).show();
                mProgress.dismiss();


            }
        });



        mAccessRelatoGoogle = (FrameLayout) findViewById(R.id.AccessRelatoGoogle);

        //-------------------GOOGLE-----------------------//
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        mAccessRelatoGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.GoogleSignInApi.signOut (mGoogleApiClient);
                signIn();
            }
        });
        //-------------------FIN GOOGLE-----------------------//

        checkUserExist();


        mAccesso_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLogin();

            }
        });

    }

    private void startLogin() {
       String login_email = mAccesso_login_email.getText().toString();
       String login_password = mAccesso_login_password.getText().toString();
       loginUser(login_email,login_password);
    }

    private void loginUser(String display_email, String display_password) {

        mAuth.signInWithEmailAndPassword(display_email,display_password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    accesoPermitido();
                }else{
                    Log.v("task",""+task);
                    mProgress.hide();
                    showSnackBar("Acceso denegado, Intente nuevamente.");
                }
            }
        });

    }




    @Override
    protected void onPause() {
        mProgress.dismiss();
        super.onPause();
    }

    private void checkaccess() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null) {
            Log.v("sesion","Inciado");
            accesoPermitido();
        }else{

            Log.v("sesion","Sin Iniciar");
        }
    }

    private void startVideo() {

       /* mVideoViewAcceso = (VideoView) findViewById(R.id.videoViewAcceso);

        Uri uri  = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.fondo_video__);
        mVideoViewAcceso.setVideoURI(uri);
        mVideoViewAcceso.start();

        mVideoViewAcceso.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });*/
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        startVideo();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null) {
            accesoPermitido();
        }
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            mProgress.setMessage("Accediendo ...");
            mProgress.show();
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                mProgress.dismiss();
            }
        }
    }


    private void signInWithFacebook(AccessToken accessToken) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + accessToken.getUserId());
        mProgress.setMessage("Accediento...");
        mProgress.show();

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                           accesoPermitido();
                        }
                    }
                });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            showSnackBar("Error en la Autenticación Google");
                        }else {
                            mProgress.dismiss();
                            checkUserExist();
                        }
                    }
                });
    }


    public void showSnackBar(String msg) {
        Snackbar
                .make(findViewById(R.id.accesso_relato_sangrienta_lectura), msg, Snackbar.LENGTH_SHORT)
                .show();
    }
    public void inicio(View v){
        Intent i = new Intent(AccessRelato.this,MenuCustomizeNow.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }

    public void login(View v){
        Intent i = new Intent(AccessRelato.this,LoginRelato.class);
        //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }
    public void register(View v){
        Intent i = new Intent(AccessRelato.this,RegisterRelato.class);
        //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }

    public void forgetPassword(View v){
        Intent i = new Intent(AccessRelato.this,ForgetPasswordRelato.class);
        //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }


    public void accesoPermitido(){
        Intent i = new Intent(AccessRelato.this,MenuCustomizeNow.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        mProgress.dismiss();
        finish();
    }



    private void checkUserExist() {
        if(mAuth.getCurrentUser()!= null){

            if (mAuth.getCurrentUser() != null) {
                accesoPermitido();

                final String user_id = mAuth.getCurrentUser().getUid();
                mDatabaseUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.hasChild(user_id)){

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        }

    }
}





























