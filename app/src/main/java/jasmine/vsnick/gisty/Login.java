package jasmine.vsnick.gisty;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.egit.github.core.Authorization;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.OAuthService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {
    EditText username;
    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.usrname);
        password = (EditText) findViewById(R.id.password);

    }
    public void Login(View view){

        final String usr_name = username.getText().toString();
        final String pass = password.getText().toString();
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if(valid(usr_name,pass)) {
                final ProgressDialog progress;
                progress = new ProgressDialog(this);
                progress.setMessage("Authenticating..");
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setIndeterminate(true);
                progress.setProgress(0);
                progress.setCancelable(false);
                progress.show();
                AsyncTask task = new AsyncTask() {
                    Boolean err=false;
                    @Override
                    protected Object doInBackground(Object[] params) {
                            GitHubClient client = new GitHubClient();
                            client.setCredentials(usr_name,pass);
                        OAuthService service = new OAuthService(client);
                            try {
                                for(Authorization i : service.getAuthorizations())
                                {
                                    Log.d("hi", "doInBackground: "+i.getNote());
                                    if(i.getNote().equals("gisty"))
                                    {
                                        service.deleteAuthorization(i.getId());
                                    }
                                }
                                List<String> scopes = new ArrayList<String>();
                                scopes.add("repo");
                                scopes.add("user");
                                scopes.add("gist");
                                Authorization auth = new Authorization();
                                auth.setNote("gisty");
                                auth = service.createAuthorization(auth);
                                service.setScopes(auth.getId(),scopes);
                                SharedPreferences shared = getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor =shared.edit();
                                editor.putString("token",auth.getToken());
                                editor.commit();
                            }
                            catch (RequestException e){
                                Log.d("vsn", "doInBackground: "+e);
                                err=true;
                            }
                            catch (IOException e)
                            {
                                Log.d("vsn", "doInBackground: "+e);
                            }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        progress.dismiss();
                        if(err)
                            Toast.makeText(getApplicationContext(),"Incorrect Credentials",Toast.LENGTH_LONG).show();
                        else
                        {
                            finish();
                        }
                    }
                };
                task.execute();
            }
        } else {
            Toast.makeText(getApplicationContext(),"No Internet",Toast.LENGTH_SHORT).show();
        }
    }
    public boolean valid(String usr_name,String pass)
    {
        if(usr_name.isEmpty()||pass.isEmpty())
            return false;
        return  true;
    }
}
