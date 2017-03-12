package jasmine.vsnick.gisty;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.GistService;
import org.eclipse.egit.github.core.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class GistView extends AppCompatActivity {

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gist_view);
        textView = (TextView) findViewById(R.id.gist_text);
        final ProgressDialog progress;
        progress = new ProgressDialog(this);
        progress.setMessage("Loading..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.setCancelable(false);
        progress.show();
        AsyncTask task = new AsyncTask() {
            StringBuilder s = new StringBuilder();
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    String url = getIntent().getStringExtra("url");
                    URL myURL = new URL(url);
                    URLConnection myURLConnection = myURL.openConnection();
                    myURLConnection.connect();
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            myURLConnection.getInputStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null)
                        s.append(inputLine+"\n");
                    in.close();
                }
                catch (MalformedURLException e) {
                    Log.d("vsn", "doInBackground: "+e);
                }
                catch (IOException e) {
                    Log.d("vsn", "doInBackground: "+e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                progress.dismiss();
                textView.setText(s);
            }
        };
        task.execute();
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setProgress(10);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
