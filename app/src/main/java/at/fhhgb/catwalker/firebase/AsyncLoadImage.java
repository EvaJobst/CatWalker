package at.fhhgb.catwalker.firebase;

import android.media.Image;
import android.os.AsyncTask;

import java.net.URL;

/**
 * Created by Lisa on 17.06.2016.
 */
public class AsyncLoadImage extends AsyncTask<URL,Integer, Image> {

    @Override
    protected Image doInBackground(URL... params) {
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Image s) {
        super.onPostExecute(s);
    }

}
