package vitiellodatri.com.futstalmind;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import DataBase.PhpFunctions;


public class User_RecuperaPassword extends Activity {

    private static String KEY_SUCCESS = "success";

    String check_email = "";
    private ProgressDialog nDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_recupera_password);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recupera_password, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void RecuperaPassword(View v) {

        EditText edit_email =  (EditText) findViewById(R.id.edit_email);

        check_email = edit_email.getText().toString();

        if (check_email.length() <=0){

            messaggioErrore(1);
            return;
        }


        new NetCheck().execute();
    }
    private class NetCheck extends AsyncTask<String,Void,Boolean>
    {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(User_RecuperaPassword.this);
            nDialog.setTitle("Recupero password in corso");
            nDialog.setMessage("Attendere..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            /**
             * Gets current device state and checks for working internet connection by trying Google.
             **/
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;

        }
        @Override
        protected void onPostExecute(Boolean th){

            // Connessione attiva, allora avvio processo di Login
            if(th == true){
                //nDialog.dismiss();
                new ProcessRecuperaPassword().execute();
            }
            else{
                nDialog.dismiss();
                Toast.makeText(User_RecuperaPassword.this, "Error in Network Connection", Toast.LENGTH_SHORT).show();

            }
        }

    }
    /**
     * Async Task to get and send data to My Sql database through JSON respone.
     **/
    private class ProcessRecuperaPassword extends AsyncTask<String,Void,JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected JSONObject doInBackground(String... args) {
            PhpFunctions userFunction = new PhpFunctions();

            JSONObject json = userFunction.forPass(check_email);

            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {

            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    String res = json.getString(KEY_SUCCESS);
                    if(Integer.parseInt(res) == 1){

                        nDialog.dismiss();
                        messaggioErrore(3);

                    }else{


                        String err = json.getString("error");
                        nDialog.dismiss();

                        if (Integer.parseInt(err)==2){

                            messaggioErrore(2);
                            return;

                        }

                        Toast.makeText(User_RecuperaPassword.this, "Controllo mail fallito", Toast.LENGTH_SHORT).show();

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void messaggioErrore(int tipoErrore){
        String errore = "";
        switch (tipoErrore) {
            case 1:
                errore = "Inserire l'e-mail";
                break;
            case 2:
                errore = "E-mail non presente";
                break;
            case 3:
                errore = "Ti abbiamo inviato una nuova password, controlla la mail";
                break;
        }

        AlertDialog.Builder miaAlert = new AlertDialog.Builder(this);
        miaAlert.setMessage(errore);
        miaAlert.setTitle("Attenzione!");
        miaAlert.setCancelable(true);
        miaAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //tv.setText("Ho cliccato il tasto SI");
            }
        });
        miaAlert.show();
    }

}
