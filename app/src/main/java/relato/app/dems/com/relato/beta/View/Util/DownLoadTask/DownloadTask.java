package relato.app.dems.com.relato.beta.View.Util.DownLoadTask;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import relato.app.dems.com.relato.beta.MenuCustomizeNow;
import relato.app.dems.com.relato.beta.R;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by CORAIMA on 23/11/2017.
 */


public class DownloadTask extends AsyncTask<String, Integer, String> {

    ProgressDialog progressDialog;


    public DownloadTask(Context context){
        progressDialog = new ProgressDialog(context);
    }

    /**
     * Set up a ProgressDialog
     */
    @Override
    protected void onPreExecute() {

        progressDialog.setTitle("Descarga en progreso...");
       // progressDialog.setMessage("Porfavor espere...");
         progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.setProgress(0);
        progressDialog.show();
    }


    /**
     *  Background task
     */
    @Override
    protected String doInBackground(String... params) {
        String url_audio = params[0];
        String name_audio = params[1];
        String name_extension = params[2];
        int file_length;

        Log.i("Infoasdasd: path", url_audio);
        Log.i("Infoasdasd: path", name_audio);
        Log.i("Infoasdasd: path", name_extension);
        try {
            URL url = new URL(url_audio);
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();
            file_length = urlConnection.getContentLength();

            /**
             * Create a folder
             */
            File new_folder = new File( Environment
                    .getExternalStorageDirectory()
                    .getPath()+"/Android/data/"
                    +getApplicationContext()
                    .getPackageName()
                    +"/files/sdcard/DirName/"+name_audio);

            if (!new_folder.exists()) {
                if (new_folder.mkdir()) {
                    Log.i("playy", "Folder succesfully created");
                } else {
                    Log.i("playy", "Failed to create folder");
                }
            } else {
                Log.i("playy", "Folder already exists");
            }

            /**
             * Create an output file to store the image for download
             */
            File output_file = new File(new_folder, name_extension);

            Log.i("playy", "Donde guardo "+output_file);

            OutputStream outputStream = new FileOutputStream(output_file);

            InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
            byte [] data = new byte[1024];
            int total = 0;
            int count;
            while ((count = inputStream.read(data)) != -1) {
                total += count;

                outputStream.write(data, 0, count);
                int progress = 100 * total / file_length;
                publishProgress(progress);

                Log.i("Info", "Progress: " + Integer.toString(progress));
            }
            inputStream.close();
            outputStream.close();

            Log.i("Info", "file_length: " + Integer.toString(file_length));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Download complete.";
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        progressDialog.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        progressDialog.hide();
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        File folder = new  File( Environment.getExternalStorageDirectory().getPath()+"/Android/data/"+getApplicationContext().getPackageName()+"/files/sdcard/DirName/00");
        File output_file = new File(folder, "audio.mp3");
        String path = output_file.toString();
        //imageView.setImageDrawable(Drawable.createFromPath(path));
        MediaPlayer player;
        player = MediaPlayer.create(getApplicationContext(), Uri.parse(path));
        //player.start();
        Log.i("Infoasdasd", "Path: " + path);



    }

}

