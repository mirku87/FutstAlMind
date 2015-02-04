package vitiellodatri.com.futstalmind;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import DataBase.DataBaseHelper;
import DataBase.PhpFunctions;
import DataBase.PopulateDatabase;

// To do
// Immagine del logo da inserire
// Login salvata su preferenze di modo che la prox volta non è da rieffettuare login -> Ok
// Dimenticato password -> Far indicare username, verra' mandata una mail al rispettivo username con la passowrd -> Ok
// Action bar per tornare indietro nell'activity Recupera Password e Registrati -> Ok
// Avvio altra activity -> navigation drawer + detail -> OK
// Mettere hint sia in recupara password che Registrati -> ok
// Visualizzare info Account per poter salvare altre modifiche -> Salvarle nelle shared preference-> to do
// Effettuare log out per azzerare tutti i dati delle preferenze -> OK
// Salvare nelle sharedPreferences anche il relativo ID? Da farselo restituire da script php-
// Menu principale da versione obsoleta. cm usare la nuova versione?

// Splash screen per login? Si riesce ad evitare refresh? -> googlare
// Come si fa a lanciare l'emulatore su device fisico?
// Trasformare l'errore network da toast a alert

public class User_Login extends Activity {

    private static String KEY_SUCCESS = "success";
    private ProgressDialog nDialog;

    String global_username = "";
    String global_password = "";
    boolean global_ricordami = true;

    EditText edit_username;
    EditText edit_password;
    CheckBox check_ricordami;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = getApplicationContext().getSharedPreferences("Preferences", 0);
        editor = pref.edit();

        Boolean pref_ricordami =pref.getBoolean("key_ricordami", false); // getting String
        String pref_username = pref.getString("key_username", null); // getting Integer
        String pref_password = pref.getString("key_password", null); // getting Integer

        if (pref_ricordami){

            global_username = pref_username;
            global_password = pref_password;
            global_ricordami = true;
            avviaControlloLogin();

        }else{


            setContentView(R.layout.activity_user_login);

            edit_username = (EditText) findViewById(R.id.edit_username);
            edit_password = (EditText) findViewById(R.id.edit_password);
            check_ricordami =  ((CheckBox) findViewById(R.id.check_ricordami));

            if (pref_username != null){

                edit_username.setText(pref_username);
                edit_password.setText(pref_password);
                check_ricordami.setSelected(false);

            }else{

                check_ricordami.setSelected(true);
            }

            // l'hint non verrebbe visualizzato uguale come font senza questo snippet
            edit_password.setTypeface(Typeface.DEFAULT);
            edit_password.setTransformationMethod(new PasswordTransformationMethod());

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

        return super.onOptionsItemSelected(item);
    }

    public void Registrati(View v) {

        // Avviare activity per registrarsi

        Intent i = new Intent(this, User_Registrati.class);

        startActivityForResult(i, 2);// Activity is started with requestCode 2

    }

    public void RecuperaPassword(View v) {

        // Avviare activity per registrarsi

        Intent i = new Intent(this, User_RecuperaPassword.class);

        startActivity(i);

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2 && resultCode == RESULT_OK)
        {
            edit_username.setText(data.getStringExtra("username"));
            edit_password.setText(data.getStringExtra("password"));
        }
    }

    public void ControlloLogin(View v){

        // Controllo validita' username e password

        if (edit_username.getText().toString().length() <= 0) {
            messaggioErrore(1);
            return;
        }


        if (edit_password.getText().toString().length() <= 0) {
            messaggioErrore(2);
            return;
        }

        global_username = edit_username.getText().toString();
        global_password = edit_password.getText().toString();
        global_ricordami = check_ricordami.isChecked();

        // Controllo esistenza user\pass su db

        avviaControlloLogin();

    }

    private void avviaControlloLogin(){


        NetAsync();

    }

    public void NetAsync(){


        // Controlla connesione internet
        new NetCheck().execute();
    }

    /**
     * Async Task per controllare se la connessione internet è attiva.
     **/
    private class NetCheck extends AsyncTask<String,Void,Boolean>
    {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(User_Login.this);
            nDialog.setTitle("Log in corso");
            nDialog.setMessage("Attendere....");
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
                new ProcessLogin().execute();
            }
            else{
                nDialog.dismiss();
                Toast.makeText(User_Login.this, "Error in Network Connection", Toast.LENGTH_SHORT).show();

            }
        }

    }
    /**
     * Async Task to get and send data to My Sql database through JSON respone.
     **/
    private class ProcessLogin extends AsyncTask<String,Void,JSONObject> {
        //private ProgressDialog pDialog;
        String username,password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            username = global_username;
            password = global_password;
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            PhpFunctions userFunction = new PhpFunctions();
            JSONObject json = userFunction.loginUser(username, password);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {

            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    String res = json.getString(KEY_SUCCESS);
                    if(Integer.parseInt(res) == 1){

                        nDialog.dismiss();


                        JSONObject json_user = json.getJSONObject("user");



                        editor.putBoolean("key_ricordami", global_ricordami);
                        editor.putString("key_username", username);
                        editor.putString("key_password", password);
                        editor.putInt("key_idUser", json_user.getInt("id"));
                        editor.commit(); // commit changes

                        finish();


                        /* Scrittura database */

                        PopulateDatabase p = new PopulateDatabase(getBaseContext(),User_Login.this,json_user.getInt("id"));
                        p.writeUserAndTeam();

                        Intent i = new Intent(getApplicationContext(),MenuPrincipale.class);
                        startActivity(i);

                    }else{
                        nDialog.dismiss();
                        messaggioErrore(3);

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
                errore = "Inserire l'username";
                break;
            case 2:
                errore = "Inserire la password";
                break;
            case 3:
                errore = "Username o password non corrette";
                break;
        }

        AlertDialog.Builder miaAlert = new AlertDialog.Builder(this);
        miaAlert.setMessage(errore);
        miaAlert.setTitle("Attenzione!");
        miaAlert.setCancelable(true);
        miaAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        miaAlert.show();
    }


}
