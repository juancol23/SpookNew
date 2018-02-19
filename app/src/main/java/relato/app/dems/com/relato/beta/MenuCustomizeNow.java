package relato.app.dems.com.relato.beta;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import relato.app.dems.com.relato.beta.View.Acceso.AccessRelato;
import relato.app.dems.com.relato.beta.View.FeedRelatos;
import relato.app.dems.com.relato.beta.View.Fragments.Apariciones;
import relato.app.dems.com.relato.beta.View.Fragments.AsesinosSeriales;
import relato.app.dems.com.relato.beta.View.Fragments.EpisodiosPerdidos;
import relato.app.dems.com.relato.beta.View.Fragments.Fantasmas;
import relato.app.dems.com.relato.beta.View.Fragments.MisCreaciones;
import relato.app.dems.com.relato.beta.View.Fragments.Misticas;
import relato.app.dems.com.relato.beta.View.Fragments.FragmentoInicio;
import relato.app.dems.com.relato.beta.View.Fragments.LeyendasUrbanas;
import relato.app.dems.com.relato.beta.View.Fragments.Creepypastas;
import relato.app.dems.com.relato.beta.Service.Sounds.BackgroundSoundService;
import relato.app.dems.com.relato.beta.View.Postear.PostFeed;
import relato.app.dems.com.relato.beta.View.Postear.RandomCoins;

import static android.provider.Telephony.Mms.Part.TEXT;

public class MenuCustomizeNow extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ColorPickerDialogListener, RewardedVideoAdListener {

    private ImageView mMenu_profile_image;
    private TextView mMenu_profile_name;
    private TextView mMenu_profile_email;
    private Button mBtnSignOut;
    private Dialog MyDialog;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private InterstitialAd interstitialAd;
    private SharedPreferences prefs = null;
    private SharedPreferences colorFondo = null;
    private SharedPreferences monedas = null;
    private SharedPreferences monedasValidar = null;
    private static Typeface Pacifico,Nightmare,Double,BloodLust;
    private DatabaseReference mDatabaseVersionApp;
    /*Paleta de colores*/
    private static final int DIALOG_ID = 0;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    /*Anucnios*/
    private InterstitialAd mInterstitialAd;
    ImageView image_video;
    private AdView mAdView;

    private TextView mId_monedas_text;

    @BindView(R.id.text_spook_toolbar)
    TextView mText_spook_toolbar;

    private RewardedVideoAd mRewardedVideoAd;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_customize_now);


        ButterKnife.bind(this);
        String Fuente_Rutas = "fuentes/Feast.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), Fuente_Rutas);
        mText_spook_toolbar.setTypeface(tf);


        MobileAds.initialize(this,"ca-app-pub-2031757066481790/4437880601");

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        prefs = getSharedPreferences("relato.app.dems.com.relato.beta", MODE_PRIVATE);
        colorFondo = getSharedPreferences("relato.app.dems.com.relato.beta", MODE_PRIVATE);
        monedas = getSharedPreferences("relato.app.dems.com.relato.beta", MODE_PRIVATE);
        monedasValidar = getSharedPreferences("relato.app.dems.com.relato.beta", MODE_PRIVATE);

        mId_monedas_text = (TextView) findViewById(R.id.id_monedas_text);
        Toolbar mToolbar = null;

        if(monedas.getBoolean("firstMonedasTest1",true)){
            //colorFondo.edit().putString("colorFondo","#ff000000").commit();
           // colorFondo.edit().putString("colorFondo","#eee").commit();

            monedasValidar.edit().putBoolean("firstMonedasTest1", false).commit();
            monedas.edit().putInt("valorGuardadoTest", 45).apply();
            Log.v("modsss","IF"+monedas.getInt("valorGuardadoTest", -1)+colorFondo);
            //mMenu_profile_email.setText("Coins: "+monedas.getInt("valorGuardadoTest", -1));

            String valorColorFondo = colorFondo.getString("colorFondo","");

           // mToolbar.setBackgroundColor(Color.parseColor(valorColorFondo));
        }

        String valorColorFondo = colorFondo.getString("colorFondo","");


      //  NavigationView mNavigationView;

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
       // mToolbar = (Toolbar) findViewById(R.id.toolbar);

//        mNavigationView.setBackgroundColor((Color.parseColor(valorColorFondo)));
       // mToolbar.setBackgroundColor(Color.parseColor(valorColorFondo));


        Log.v("modsss","ELSE "+monedas.getInt("valorGuardadoTest", -1));



        int allItem = monedas.getInt("valorGuardadoTest", -1);
        String tmpStr10 = String.valueOf(allItem);
        mId_monedas_text.setText("Coins: "+allItem);


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2031757066481790/2003288956");
       // mInterstitialAd.setAdUnitId("ca-app-pub-2031757066481790/5186336245");

        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            Log.v("Anuncio","click");
        }

        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        loadRewardedVideoAd();


        //Log.v("modsss",""+monedasValidar);
        //Log.v("modsss",""+monedasValidar.getBoolean("firstMonedasTest",true));
        Log.v("modsss",""+monedasValidar.getBoolean("firstMonedasTest1",true));

        Log.v("modsss",""+monedas.getInt("valorGuardadoTest", -1));


      /*  if(monedasValidar == null){
            monedasValidar.edit().putBoolean("firstMonedas", false).commit();
            monedas.edit().putInt("valorGuardado", 2).commit();

            int miValorGuardado = monedas.getInt("valorGuardado", -1);

            mMenu_profile_email = (TextView) findViewById(R.id.menu_profile_email);
            mMenu_profile_email.setText("Coins: "+miValorGuardado);

        }else if(monedasValidar != null){
            int miValorGuardado = monedas.getInt("valorGuardado", -1);
            mMenu_profile_email = (TextView) findViewById(R.id.menu_profile_email);
            mMenu_profile_email.setText("Coins: "+miValorGuardado);
        }*/



        MyDialog = new Dialog(MenuCustomizeNow.this);


        validadVisibilidadSonido();
        setToolbar("Sangrienta");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*Comenzando Recycler*/
        //sonido

        setToolbar("Spook");

        FragmentoInicio inicio = new FragmentoInicio();
        getSupportFragmentManager().beginTransaction().replace(R.id.contenido_dinamico, inicio)
                .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                .addToBackStack(null).commit();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null) {

            Log.v("sesion","Inciado");
        }else{
            //needAccess();
            Log.v("sesion","Sin Iniciar");
        }


        validarActulizacion();

    }

    public void validarActulizacion(){

        mDatabaseVersionApp = FirebaseDatabase.getInstance().getReference().child("VersionApp");
        mDatabaseVersionApp.keepSynced(true);
        mDatabaseVersionApp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String appVersion = (String) dataSnapshot.child("version").getValue();
                String title = (String) dataSnapshot.child("title").getValue();
                String body = (String) dataSnapshot.child("body").getValue();

                Log.v("PackageInfo",""+appVersion);

                PackageInfo pInfo = null;

                try {
                    pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                String version = pInfo.versionName;
                int verCode = pInfo.versionCode;

                Log.v("PackageInfo",""+version);
                Log.v("PackageInfo",""+verCode);

                int verCodeActual = Integer.parseInt(appVersion);

                if(verCode < verCodeActual){
                    ModalCheckUpdate(title,body);
                    Log.v("PackageInfo",""+verCode+""+verCodeActual);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onclickFabStatusMusic(View v) {

        validadDisponibilidadSonido();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void validadDisponibilidadSonido(){
        if (prefs.getBoolean("firstrun", true)) {

            prefs.edit().putBoolean("firstrun", false).commit();

            FloatingActionButton mFabStatusMusic = (FloatingActionButton) findViewById(R.id.fabStatusMusic);

            mFabStatusMusic.setVisibility(View.VISIBLE);
            mFabStatusMusic.setImageDrawable(getDrawable(R.mipmap.ic_play));

            Intent svc = new Intent(getApplicationContext(), BackgroundSoundService.class);
            stopService(svc);


        }else{

            prefs.edit().putBoolean("firstrun", true).commit();

            FloatingActionButton mFabStatusMusic = (FloatingActionButton) findViewById(R.id.fabStatusMusic);

            mFabStatusMusic.setVisibility(View.VISIBLE);

            mFabStatusMusic.setImageDrawable(getDrawable(R.mipmap.ic_stop));

            Intent svc = new Intent(getApplicationContext(), BackgroundSoundService.class);
            startService(svc);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void validadVisibilidadSonido(){
        if (prefs.getBoolean("firstrun", true)) {

            FloatingActionButton mFabStatusMusic = (FloatingActionButton) findViewById(R.id.fabStatusMusic);

            //mFabStatusMusic.setVisibility(View.VISIBLE);
            mFabStatusMusic.setImageDrawable(getDrawable(R.mipmap.ic_stop));


        }else{

            FloatingActionButton mFabStatusMusic = (FloatingActionButton) findViewById(R.id.fabStatusMusic);

            //mFabStatusMusic.setVisibility(View.VISIBLE);

            mFabStatusMusic.setImageDrawable(getDrawable(R.mipmap.ic_play));

        }
    }


    public void score(View v) {
        Intent intent1 = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id="
                        + MenuCustomizeNow.this.getPackageName()));
        startActivity(intent1);
    }

    public void paletaColor(View v){

        int allItem = monedas.getInt("valorGuardadoTest", -1);

        if(allItem < 5){
            showSnackBar("Necesitas monedas");
            //openCoins();

        }else{
            ColorPickerDialog.newBuilder()
                    .setDialogType(ColorPickerDialog.TYPE_PRESETS)
                    .setAllowPresets(false)
                    .setDialogId(DIALOG_ID)
                    .setColor(Color.BLACK)
                    .setShowAlphaSlider(false)
                    .show(MenuCustomizeNow.this);
        }

    }


    @Override
    public void onColorSelected(int dialogId, int color) {
        Toast.makeText(MenuCustomizeNow.this, "Selected Color: #" + Integer.toHexString(color), Toast.LENGTH_SHORT).show();
        /*mRelative_profile_customize = (RelativeLayout) findViewById(R.id.relative_profile_customize);
        mRelative_profile_customize.setBackgroundColor((Color.parseColor("#"+Integer.toHexString(color))));*/

        colorFondo.edit().putString("colorFondo","#"+Integer.toHexString(color)).commit();
        String valorColorFondo = colorFondo.getString("colorFondo","");


        NavigationView mNavigationView;
        Toolbar mToolbar;

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mNavigationView.setBackgroundColor((Color.parseColor(valorColorFondo)));
        mToolbar.setBackgroundColor((Color.parseColor("#"+Integer.toHexString(color))));

        //mNavigationView.setBackgroundColor((Color.parseColor("#"+Integer.toHexString(color))));
        //mCoordinator_total.setBackgroundColor((Color.parseColor("#"+Integer.toHexString(color))));
       // if (prefs.getBoolean("firstrun", true)) {

        // prefs.edit().putBoolean("firstrun", false).commit();

        Log.v("modsss","Descontado "+monedas.getInt("valorGuardadoTest", -1));
        int allItem = monedas.getInt("valorGuardadoTest", -1);
        int coinsDesc = allItem - 5;

        monedas.edit().putInt("valorGuardadoTest", coinsDesc).apply();

        mId_monedas_text = (TextView) findViewById(R.id.id_monedas_text);

        mId_monedas_text.setText("Coins: "+coinsDesc);


        Log.v("colorFondo",""+colorFondo.getString("colorFondo",""));
        Log.v("colorFondo",""+valorColorFondo);

        showSnackBar("Menos 5 monedas");



    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }

    public void share(View v) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,"Esta app te hará sufrir un infarto con sus Sangrientas Lecturas, Descargala YA!! https://play.google.com/store/apps/details?id=relato.app.dems.com.relato.beta");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

    }

    public void needAccess(){
        Intent i = new Intent(getApplicationContext(),AccessRelato.class);
        finish();
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }

    @Override
    protected void onStart() {


        mId_monedas_text = (TextView) findViewById(R.id.id_monedas_text);

        int allItem = monedas.getInt("valorGuardadoTest", -1);
        String tmpStr10 = String.valueOf(allItem);
        mId_monedas_text.setText("Coins: "+allItem);



        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            Log.v("Anuncio","click");
        }

        if (prefs.getBoolean("firstrun", true)) {
            Intent svc = new Intent(getApplicationContext(), BackgroundSoundService.class);
            startService(svc);

        }else{
            Intent svc = new Intent(getApplicationContext(), BackgroundSoundService.class);
            stopService(svc);
        }
        super.onStart();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        validadVisibilidadSonido();
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            Log.v("Anuncio","click");
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        Intent svc = new Intent(this, BackgroundSoundService.class);
        //stopService(svc);
        /* //detener musica
        Intent svc = new Intent(getContext(), BackgroundSoundService.class);
        getActivity().stopService(svc);*/
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            Log.v("Anuncio","click");
        }
        super.onStop();
    }

    @Override
    protected void onPause() {
        Intent svc = new Intent(this, BackgroundSoundService.class);
        stopService(svc);
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            Log.v("Anuncio","click");
        }
        super.onPause();
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            Log.v("Anuncio","click");
        }
        finish();
    }


    public void inicioSign(){
        Intent i = new Intent(MenuCustomizeNow.this,AccessRelato.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.menu, menu);


        String valorColorFondo = colorFondo.getString("colorFondo","");

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);


        mBtnSignOut = (Button) findViewById(R.id.btnSignOut);

        mMenu_profile_image = (ImageView) findViewById(R.id.menu_profile_image);
        mMenu_profile_name = (TextView) findViewById(R.id.menu_profile_name);
        mMenu_profile_email = (TextView) findViewById(R.id.menu_profile_email);

        if(monedas.getBoolean("firstMonedasTest1",true)){
            monedasValidar.edit().putBoolean("firstMonedasTest1", false).commit();
            monedas.edit().putInt("valorGuardadoTest", 45).apply();
            Log.v("modsss","IF"+monedas.getInt("valorGuardadoTest", -1));
            //mMenu_profile_email.setText("Coins: "+monedas.getInt("valorGuardadoTest", -1));
        }

        Log.v("modsss","ELSE "+monedas.getInt("valorGuardadoTest", -1));
        int allItem = monedas.getInt("valorGuardadoTest", -1);
        String tmpStr10 = String.valueOf(allItem);
        //mMenu_profile_email.setText("Coins: "+allItem);


        mMenu_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(MenuCustomizeNow.this, relato.app.dems.com.relato.beta.View.Profile.Profile.class));

               // Intent i = new Intent(MenuCustomizeNow.this, relato.app.dems.com.relato.beta.View.Profile.Profile.class);
            }
        });
        Button mBtn_inicia_sesion = (Button) findViewById(R.id.btn_inicia_sesion);

        //mAuth = FirebaseAuth.getInstance();

        mBtn_inicia_sesion.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                MyCustomAlertDialog();
            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null) {

            Uri photoUrl = user.getPhotoUrl();
            String name = user.getDisplayName();
            String email = user.getEmail();
            String userId = user.getUid();

            mBtn_inicia_sesion.setVisibility(View.GONE);

            mMenu_profile_image.setVisibility(View.VISIBLE);
            mMenu_profile_name.setVisibility(View.VISIBLE);
            //mMenu_profile_email.setVisibility(View.VISIBLE);
            mBtnSignOut.setVisibility(View.GONE);

            mMenu_profile_name.setText(name);
           // mMenu_profile_email.setText("Puntos: 0");

            Log.v("estado","estado"+name+photoUrl);

            Glide.with(MenuCustomizeNow.this)
                    .load(photoUrl)
                    .thumbnail(Glide.with(MenuCustomizeNow.this).load(R.drawable.item_placeholder))
                    .into(mMenu_profile_image);

            if(name==null){

                DatabaseReference mDatabaseUser;

                mDatabaseUser = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("Users");
                mDatabaseUser.keepSynced(true);

                mDatabaseUser.child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String post_image = (String) dataSnapshot.child("image").getValue();
                        String name_ = (String) dataSnapshot.child("name").getValue();

                        Log.v("estado","estado"+post_image+name_);

                        Glide.with(MenuCustomizeNow.this)
                                .load(post_image)
                                .thumbnail(Glide.with(MenuCustomizeNow.this).load(R.mipmap.ic_launcher))
                                .into(mMenu_profile_image);

                        mMenu_profile_name.setText(name_);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });



            }

            Log.v("sesion","Inciado");
        }else{
            Log.v("sesion","Sin Iniciar");
        }


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {

                    Uri photoUrl = user.getPhotoUrl();
                    String name = user.getDisplayName();
                    //String email = user.getEmail();

                    mMenu_profile_name.setText(name);

                    //mMenu_profile_email.setText(email);

                    Glide.with(MenuCustomizeNow.this)
                            .load(photoUrl)
                            .thumbnail(Glide.with(MenuCustomizeNow.this).load(R.drawable.item_placeholder))
                            .into(mMenu_profile_image);

                    Log.v("sesion","Inciado");
                }else{
                    Log.v("sesion","Sin Iniciar");
                }
            }
        };

        mBtnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
                needAccess();


            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_coins) {


            return true;
        }


        return super.onOptionsItemSelected(item);




    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {
            setToolbar("Spook");
            menu_inicio();

        } else if (id == R.id.nav_creepypastas) {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            //if(user != null) {
                //showSnackBar("Terror");
                setToolbar("Creepypastas");
                Creepypastas terror = new Creepypastas();
                getSupportFragmentManager().beginTransaction().replace(R.id.contenido_dinamico, terror)
                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                        .addToBackStack(null).commit();
                Log.v("sesion","Inciado");
            /*}else{
                //needAccess();
                //showSnackBar("Para Aceeder a esta categoria Inicia Sesión");
                MyCustomAlertDialog();
                Log.v("sesion","Sin Iniciar");
            }*/

        } else if (id == R.id.nav_fantasmas) {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

           // if(user != null) {
                setToolbar("Fantasmas");
                Fantasmas fantasmas = new Fantasmas();
                getSupportFragmentManager().beginTransaction().replace(R.id.contenido_dinamico, fantasmas)
                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                        .setTransition(5000)
                        .addToBackStack(null).commit();

            /*Intent i = new Intent(MenuCustomizeNow.this, RandomCoins.class);
            startActivity(i);*/

            /*}
            else{
                MyCustomAlertDialog();
                Log.v("sesion","Sin Iniciar");
            }
*/

        }   else if (id == R.id.nav_leyendas_urbanas) {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            //if(user != null) {
                setToolbar("Leyendas Urbanas");

                LeyendasUrbanas leyendasUrbanas = new LeyendasUrbanas();
                getSupportFragmentManager().beginTransaction().replace(R.id.contenido_dinamico, leyendasUrbanas)
                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                        .addToBackStack(null).commit();
            /*}else{
                MyCustomAlertDialog();

                Log.v("sesion","Sin Iniciar");
            }*/


        } else if (id == R.id.nav_misticas) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

           // if(user != null) {
                setToolbar("Místicas");

                Misticas misticas = new Misticas();
                getSupportFragmentManager().beginTransaction().replace(R.id.contenido_dinamico, misticas)
                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                        .addToBackStack(null).commit();

           /* }else{
                MyCustomAlertDialog();
                Log.v("sesion","Sin Iniciar");
            }*/

        } else if (id == R.id.nav_apariciones) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

          if(user != null) {
              // setToolbar("Apariciones");

              /* int allItem = monedas.getInt("valorGuardadoTest", -1);

               if (allItem < 55) {
                   showSnackBar("Necesitas 55 monedas para publicar");
                   openCoins();

               } else {
                   Intent i = new Intent(MenuCustomizeNow.this, PostFeed.class);
                   startActivity(i);
               }*/
              Intent i = new Intent(MenuCustomizeNow.this, PostFeed.class);
              startActivity(i);
           }else{
               needAccess();
           }

           /* }else{
                MyCustomAlertDialog();
                Log.v("sesion","Sin Iniciar");
            }*/

        } else if (id == R.id.nav_episodios_perdidos) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

           // if(user != null) {
                setToolbar("Episodios Pérdidos");

                EpisodiosPerdidos episodiosPerdidos = new EpisodiosPerdidos();
                getSupportFragmentManager().beginTransaction().replace(R.id.contenido_dinamico, episodiosPerdidos)
                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                        .addToBackStack(null).commit();

           /* }else{
                MyCustomAlertDialog();
                Log.v("sesion","Sin Iniciar");
            }*/

        } else if (id == R.id.nav_asesinos_seriales) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

           // if(user != null) {
                setToolbar("Asesinos Seriales");

                AsesinosSeriales asesinosSeriales = new AsesinosSeriales();
                getSupportFragmentManager().beginTransaction().replace(R.id.contenido_dinamico, asesinosSeriales)
                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                        .addToBackStack(null).commit();

           /* }else{
                MyCustomAlertDialog();
                Log.v("sesion","Sin Iniciar");
            }*/

        } else if (id == R.id.btnSignOut) {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if(user != null) {
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
                needAccess();
            }

        }

        else if (id == R.id.nav_creaciones) {

            setToolbar("Mis Creaciones");
           // setToolbar("Mis Creaciones");
            //showSnackBar("Mis Creaciones");

            misCreaciones();

        }

        else if (id == R.id.nav_favorite) {

            //setToolbar("Favoritos");
            showSnackBar("Favoritos");

        }

        else if (id == R.id.nav_notificaciones) {

            //setToolbar("Notificaciones");
            showSnackBar("Mis Notificaciones");

        }
        else if (id == R.id.nav_logout) {
            LoginManager.getInstance().logOut();
            FirebaseAuth.getInstance().signOut();
            needAccess();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void menu_inicio() {
        FragmentoInicio inicio = new FragmentoInicio();
        getSupportFragmentManager().beginTransaction().replace(R.id.contenido_dinamico, inicio)
                .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                .addToBackStack(null).commit();
    }

    private void misCreaciones() {
        MisCreaciones misCreaciones = new MisCreaciones();
        getSupportFragmentManager().beginTransaction().replace(R.id.contenido_dinamico, misCreaciones)
                .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                .addToBackStack(null).commit();
    }

    public void showSnackBar(String msg) {
        Snackbar
                .make(findViewById(R.id.drawer_layout), msg, Snackbar.LENGTH_LONG)
                .show();
    }


    public void MyCustomAlertDialog(){

        MyDialog = new Dialog(MenuCustomizeNow.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.activity_modal__need__try__feature);
        MyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        Button btnModalAcessoRelato = MyDialog.findViewById(R.id.modal_need_try_feature_btn_inicia_sesion);
        Button btnModalCancel = MyDialog.findViewById(R.id.modal_need_try_feature_btn_cancel);
        TextView mModal_need_try_feature_text_body = MyDialog.findViewById(R.id.modal_need_try_feature_text_body);
        mModal_need_try_feature_text_body.setText("Pronto Estará Disponible");

        btnModalAcessoRelato.setEnabled(true);

        btnModalAcessoRelato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.dismiss();
               // needAccess();

            }
        });

        btnModalCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.dismiss();
            }
        });

        MyDialog.show();

    }

    public void modalasdasd(){
        MyDialog = new Dialog(this);

        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Log.v("Modal","Modal");

    }

    public void ModalCheckUpdate(String title,String body){

        MyDialog = new Dialog(MenuCustomizeNow.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.activity_check_update_version_app);
        MyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView mModal_check_update_title = MyDialog.findViewById(R.id.modal_check_update_title);
        TextView mModal_check_update_body = MyDialog.findViewById(R.id.modal_check_update_body);

        mModal_check_update_title.setText(title);
        mModal_check_update_body.setText(body);

        Button btnModalActualizar = MyDialog.findViewById(R.id.modal_check_update_actualizar);
        Button btnModalCancel = MyDialog.findViewById(R.id.modal_check_update_later);

        btnModalActualizar.setEnabled(true);

        btnModalActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.dismiss();
                Intent intent1 = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id="
                                + MenuCustomizeNow.this.getPackageName()));
                startActivity(intent1);
            }
        });

        btnModalCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.hide();
            }
        });

        MyDialog.show();

    }


    private void setToolbar(String title) {
        // Añadir la Toolbar

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);

        setSupportActionBar(toolbar);


        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // openCoins();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


    }


    public void startVideoAds(){
        if(mRewardedVideoAd.isLoaded()){
           // mRewardedVideoAd.show();
            //image_video.setVisibility(View.GONE);
        }
    }


    public void openCoins() {
        MyDialog = new Dialog(MenuCustomizeNow.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.activity_modal_coins_needs);
        MyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button btnModalCoinsNeed = MyDialog.findViewById(R.id.modal_coins_need_ver_video);
        Button btnModalSalir = MyDialog.findViewById(R.id.modal_coins_need_salir);

        if(mRewardedVideoAd.isLoaded()){
            btnModalCoinsNeed.setText("Ver Anuncio");
        }


        btnModalCoinsNeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mRewardedVideoAd.isLoaded()){

                  //  mRewardedVideoAd.show();
                }else{
                    showSnackBar("Video no disponible.");
                }

            }
        });

        btnModalSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.dismiss();
            }
        });

        MyDialog.show();
    }

    @Override
    public void onRewardedVideoAdLoaded() {


        if(mRewardedVideoAd.isLoaded()){

        }




    }


    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardedVideoAd();

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
       // Log.v("rewardItem",""+rewardItem);
        Log.v("rewardItem",""+rewardItem.getType());
        Log.v("rewardItem",""+ rewardItem.getAmount());

        Toast.makeText(this,
                String.format("Amount", rewardItem.getType(),
                        rewardItem.getAmount()),
                Toast.LENGTH_SHORT).show();
        addCoins(rewardItem.getAmount());


    }

    private void addCoins(int coins) {
        int miValorGuardado = monedas.getInt("valorGuardadoTest", -1);
        int allCoins  = miValorGuardado +coins;


        mId_monedas_text = (TextView) findViewById(R.id.id_monedas_text);


        monedas.edit().putInt("valorGuardadoTest", allCoins).apply();


        // mId_monedas_text.setText("Coins: "+allCoins);

        Log.v("rewardItem",""+allCoins+miValorGuardado+coins);


    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    private void loadRewardedVideoAd() {
     //mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",new AdRequest.Builder().build());

            mRewardedVideoAd.loadAd("ca-app-pub-2031757066481790/4887319272",new AdRequest.Builder().build());
    }
}
