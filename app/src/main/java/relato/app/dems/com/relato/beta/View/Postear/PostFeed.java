package relato.app.dems.com.relato.beta.View.Postear;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;

import relato.app.dems.com.relato.beta.MenuCustomizeNow;
import relato.app.dems.com.relato.beta.R;

public class PostFeed extends AppCompatActivity {
    private ImageButton mPostImageSelect;

    private TextInputEditText mAutor;
    private TextInputEditText mPostTitle;
    private TextInputEditText mPostDesciption;


    private Button mBtnAddPost;
    private Uri mImageUri = null;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private static final int GALLERY_REQUEST = 1;
    private ProgressDialog mProgresDialog;
    private SharedPreferences monedas = null;
    private Spinner spinner;
    private GestureDetector mDetector;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_history);


        monedas = getSharedPreferences("relato.app.dems.com.relato.beta", MODE_PRIVATE);

        spinner = (Spinner) findViewById(R.id.planets_spinner);
        spinner.setBackgroundColor(getColor(R.color.white));
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        R.array.planets_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);



        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Historias");
        mProgresDialog= new ProgressDialog(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mPostTitle = (TextInputEditText) findViewById(R.id.postTitle);

        mAutor = (TextInputEditText) findViewById(R.id.postAutor);

        mAutor.setText(user.getDisplayName());

        mPostDesciption = (TextInputEditText) findViewById(R.id.postDescription);

        mBtnAddPost = (Button) findViewById(R.id.btnAddPost);

        mPostImageSelect = (ImageButton) findViewById(R.id.postImageSelect);

        mPostImageSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });
        mBtnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });
    }

    public void showSnackBar(String msg) {
        Snackbar
                .make(findViewById(R.id.coordinator), msg, Snackbar.LENGTH_LONG)
                .show();
    }
    private void startPosting(){

        final String title_val = mPostTitle.getText().toString().trim();
        final String desc_val = mPostDesciption.getText().toString().trim();
        final String author_val = mAutor.getText().toString().trim();

        spinner = (Spinner) findViewById(R.id.planets_spinner);
        final String textSpinner = spinner.getSelectedItem().toString();




        if(mImageUri == null){
            showSnackBar("Seleccione una imagen para su portada.");
            Toast.makeText(getApplicationContext(), "Porfavor seleccione una imagen ", Toast.LENGTH_SHORT).show();

        }

        else if(title_val.length() < 1) {
            showSnackBar("Título muy corto.");
            mPostTitle.setError("Este título es muy corto.");

        }
        else if(author_val.length() < 1){
            showSnackBar("Ingrese el nombre del autor(a).");
            mAutor.setError("Ingrese el nombre del autor(a).");
        }

        else if(desc_val.length() < 10){
            showSnackBar("Esta Descripción es demasiado corto.");
            mPostDesciption.setError("Esta Descripción es demasiado corto.");

        }
        else if(textSpinner.equals("Seleccion una Categoria:")){
            showSnackBar("Seleccione una categoria");
        }
        else{
            mProgresDialog.setMessage("Publicando Lectura");
            mProgresDialog.setCancelable(false);
            mProgresDialog.show();
            if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && mImageUri !=null){
                final StorageReference filepath = mStorage.child("Blog_images").child(mImageUri.getLastPathSegment());
                filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        DatabaseReference newPost = mDatabase.push();



                        showSnackBar(textSpinner);

                        newPost.child("author").setValue(author_val);

                        newPost.child("title").setValue(title_val);
                        newPost.child("desc").setValue(desc_val);

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String userId = user.getUid();

                        newPost.child("IdMiLectura").setValue(userId);

                        newPost.child("image").setValue(downloadUrl.toString());
                        newPost.child("category").setValue(textSpinner);

                        // startActivity(new Intent(PostFeed.this,MenuCustomizeNow.class));

                        int allItem = monedas.getInt("valorGuardadoTest", -1);
                        //int coinsDesc = allItem - 75;
                        int coinsDesc = allItem - 55;

                        monedas.edit().putInt("valorGuardadoTest", coinsDesc).apply();


                        Intent i = new Intent(PostFeed.this,MenuCustomizeNow.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);

                        mProgresDialog.dismiss();

                    }
                });
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            mImageUri = data.getData();
            mPostImageSelect.setImageURI(mImageUri);
        }else {
            showSnackBar("Seleccione una Imagen");
        }
    }
}

