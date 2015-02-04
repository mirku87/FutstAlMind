package vitiellodatri.com.futstalmind;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import DataBase.PhpFunctions;

/*
* - Dopo la registrazione avvenuta correttamente tornare alla login (usare startactivityForResult e farsi restituire username e password utilizzati?)
* - Tornare indietro Action Bar
* */
public class User_Registrati extends Activity {

    private static String KEY_SUCCESS = "success";

    private ProgressDialog nDialog;
    EditText edit_password;

    String username="";
    String password="";
    String mail="";
    String nome="";
    String cognome="";
    String datanascita="";
    String luogonascita="";
    int telefono=0;

    Spinner spinner_giorno;
    Spinner spinner_mese;
    Spinner spinner_anno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registrati);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        // Popolo lo spinner giorni/mesi/anni
        spinner_giorno = (Spinner) findViewById(R.id.spinner_giorno);
        spinner_mese = (Spinner) findViewById(R.id.spinner_mese);
        spinner_anno = (Spinner) findViewById(R.id.spinner_anno);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_giorni = ArrayAdapter.createFromResource(this,R.array.spinner_giorni, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter_mesi = ArrayAdapter.createFromResource(this,R.array.spinner_mesi, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter_anni = ArrayAdapter.createFromResource(this,R.array.spinner_anni, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_giorni.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_mesi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_anni.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_giorno.setAdapter(adapter_giorni);
        spinner_mese.setAdapter(adapter_mesi);
        spinner_anno.setAdapter(adapter_anni);

        edit_password = (EditText) findViewById(R.id.edit_password);

        // l'hint non verrebbe visualizzato uguale come font senza questo snippet
        edit_password.setTypeface(Typeface.DEFAULT);
        edit_password.setTransformationMethod(new PasswordTransformationMethod());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registrati, menu);
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


    public void Registrati(View v) {


        datanascita = "";
        String[] array_giorni = getResources().getStringArray(R.array.spinner_giorni);
        String[] array_mesi = getResources().getStringArray(R.array.spinner_mesi);
        String[] array_anni = getResources().getStringArray(R.array.spinner_anni);

        String text_giorno = spinner_giorno.getSelectedItem().toString();
        String text_anno = spinner_anno.getSelectedItem().toString();
        String text_mese = spinner_mese.getSelectedItem().toString();
        //
        // Data di nascita non compilata correttamente

        if ((text_giorno.compareTo(array_giorni[0])==0) || (text_mese.compareTo(array_mesi[0])==0) || (text_anno.compareTo(array_anni[0])==0)){

            // data da non impostare
            datanascita = "";

        } else {
            //
            int mese_int=0;

            // Recupero il mese sotto forma di numero e non di descrizione

            for (int i = array_mesi.length - 1; i >= 0; i--) {

                if (text_mese.compareTo(array_mesi[i])==0){

                    // Compongo la data
                    datanascita =  text_anno + "-" + String.valueOf(i) +"-" + text_giorno;
                    mese_int = i;
                    break;

                }

            }
            //
            // Controllo se la data è valida (magari metti 31 aprile che non esiste)
            //
            if (!isValidDate(text_giorno,mese_int,text_anno)){

                messaggioErrore(1);
                return;
            }

        }

        // Recupero campi a video
        EditText edit_username = (EditText) findViewById(R.id.edit_username);
        EditText edit_mail = (EditText) findViewById(R.id.edit_email);
        EditText edit_nome = (EditText) findViewById(R.id.edit_nome);
        EditText edit_cognome = (EditText) findViewById(R.id.edit_cognome);

        EditText edit_luogoNascita = (EditText) findViewById(R.id.edit_luogoNascita);
        EditText edit_telefono = (EditText) findViewById(R.id.edit_telefono);


        username=edit_username.getText().toString();
        password=edit_password.getText().toString();
        mail=edit_mail.getText().toString();
        nome=edit_nome.getText().toString();
        cognome=edit_cognome.getText().toString();
        luogonascita=edit_luogoNascita.getText().toString();

        if (mail.length() == 0){

            messaggioErrore(2);
            return;
        }

        if (username.length() == 0){

            messaggioErrore(4);
            return;
        }

        if (password.length() == 0){

            messaggioErrore(5);
            return;
        }

        try{

            telefono=Integer.parseInt(edit_telefono.getText().toString());

        }catch (NumberFormatException nfe){

            telefono=0;
        }

        // Controlla connesione internet e avvia processo di registrazione
        new NetCheck().execute();
    }

    private static boolean isValidDate(String gg,int mm,String yy) {
        //

        int gg_contr = 31;

        if (mm == 2){

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, Integer.valueOf(yy));

            // Anno bisestile a febbraio ci sono 29 giorni
            // altrimenti sono 28

            if (cal.getActualMaximum(cal.DAY_OF_YEAR) > 365){

                gg_contr = 29;

            }else {

                gg_contr = 28;
            }

        }

        if (mm == 4 || mm == 6 || mm == 9 || mm == 11){

            gg_contr = 30;

        }

        if (Integer.parseInt(gg) > gg_contr){

            return  false;
        }

        return true;
    }


    private class NetCheck extends AsyncTask<String,Void,Boolean>
    {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(User_Registrati.this);
            nDialog.setTitle("Registrazione in corso");
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
                new ProcessRegistrazione().execute();
            }
            else{
                nDialog.dismiss();
                Toast.makeText(User_Registrati.this, "Error in Network Connection", Toast.LENGTH_SHORT).show();

            }
        }

    }
    /**
     * Async Task to get and send data to My Sql database through JSON respone.
     **/
    private class ProcessRegistrazione extends AsyncTask<String,Void,JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected JSONObject doInBackground(String... args) {
            PhpFunctions userFunction = new PhpFunctions();

            JSONObject json = userFunction.registerUser(username,password,mail,nome,cognome,datanascita,luogonascita,telefono);

            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {

            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    String res = json.getString(KEY_SUCCESS);
                    if(Integer.parseInt(res) == 1){

                        nDialog.dismiss();
                        Toast.makeText(User_Registrati.this, "Complimenti registrazione effettuata correttamente", Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent();
                        intent.putExtra("username",username);
                        intent.putExtra("password",password);
                        setResult(RESULT_OK,intent);
                        finish();//finishing activity

                    }else{


                        String err = json.getString("error");
                        nDialog.dismiss();

                        if (Integer.parseInt(err)==2){

                            messaggioErrore(3);
                            return;

                        }
                        if (Integer.parseInt(err)==5){

                            messaggioErrore(6);
                            return;

                        }

                        if (Integer.parseInt(err)==6){

                            messaggioErrore(7);
                            return;

                        }

                        Toast.makeText(User_Registrati.this, "Registrazione non è andata a buon fine", Toast.LENGTH_SHORT).show();

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
                errore = "Data di nascita non corretta";
            break;
            case 2:
                errore = "Inserire l'e-mail";
                break;
            case 3:
                errore = "Username gia' presente";
                break;
            case 4:
                errore = "Inserire l'username";
                break;
            case 5:
                errore = "Inserire la password";
                break;
            case 6:
                errore = "E-mail non valida";
                break;
            case 7:
                errore = "E-mail gia' utilizzata";
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
