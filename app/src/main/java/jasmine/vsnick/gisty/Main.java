package jasmine.vsnick.gisty;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.GistService;
import org.eclipse.egit.github.core.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);
        final String token;
        if((token=sharedPreferences.getString("token",null))!=null)
        {
            final ListView listView = (ListView) findViewById(R.id.listview);
            final ProgressDialog progress;
            progress = new ProgressDialog(this);
            progress.setMessage("Grabing gists..");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setProgress(0);
            progress.setCancelable(false);
            progress.show();
            AsyncTask task = new AsyncTask() {
                List<Gist> allgists;
                @Override
                protected Object doInBackground(Object[] params) {
                    GitHubClient client = new GitHubClient();
                    client.setOAuth2Token(token);
                    UserService uservice = new UserService(client);
                    try {

                        User user = uservice.getUser();
                        GistService gservice = new GistService(client);
                        allgists = gservice.getGists(user.getLogin());
                    }
                    catch(RequestException e)
                    {
                        Log.d("vsn", "onCreate: "+e);
                    }
                    catch(IOException e)
                    {
                        Log.d("vsn", "onCreate: "+e);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                    progress.dismiss();
                    ListAdapter adapter = new ListAdapter(getApplicationContext(),allgists);
                    listView.setAdapter(adapter);
                }
            };
            task.execute();

        }
        else
        {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            onCreate(savedInstanceState);
        }

    }
}
