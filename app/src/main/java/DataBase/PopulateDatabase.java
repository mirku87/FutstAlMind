package DataBase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import Bean.Team;
import vitiellodatri.com.futstalmind.MenuPrincipale;

/**
 * Created by Mirko on 19/01/2015.
 */
public class PopulateDatabase {

    Activity activity;
    Context context;
    int IdUser = 0;
    private ProgressDialog nDialog;
    public  boolean populateTerminate;

    DataBaseHelper db;

    public PopulateDatabase(Context context,Activity activity,int idUser){

        /* Pulire database prima di ricreare */

        this.context = context;
        this.activity=activity;
        this.IdUser = idUser;
        populateTerminate=false;

    }
/*
    public void createDatabase( context,int idUser){


        new NetCheck().execute();


    }*/


    public void writeUserAndTeam(){

        nDialog = new ProgressDialog(activity);
        nDialog.setTitle("Write user and team");
        nDialog.setMessage("Attendere....");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();

        new WriteUserAndTeam().execute();

    }


    private class NetCheck extends AsyncTask<String,Void,Boolean>
    {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            /*
            nDialog = new ProgressDialog(Login.this);
            nDialog.setTitle("Log in corso");
            nDialog.setMessage("Attendere....");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();/*/
        }

        @Override
        protected Boolean doInBackground(String... params) {

            /**
             * Gets current device state and checks for working internet connection by trying Google.
             **/
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
                /*new ProcessLogin().execute();*/
            }
            else{
                /*nDialog.dismiss();*/
                /*Toast.makeText(Login.this, "Error in Network Connection", Toast.LENGTH_SHORT).show();*/

            }
        }

    }

    /**
     * Async Task to get and send data to My Sql database through JSON respone.
     **/
    private class WriteUserAndTeam extends AsyncTask<String,Void,String> {
        //private ProgressDialog pDialog;
        String username,password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            populateTerminate=true;
        }

        @Override
        protected String doInBackground(String... args) {
            PhpFunctions userFunction = new PhpFunctions();
            String json = userFunction.getUserTeam(IdUser);
            return json;
        }

        @Override
        protected void onPostExecute(String json) {

            Log.e("prova1","dai");

            try {

                JSONArray jArray = new JSONArray(json);
                JSONObject json_data=null;

                Team table_team = new Team();

                ArrayList<Team> team_User = new ArrayList<Team>();

                for(int i=0;i<jArray.length();i++){


                    json_data = jArray.getJSONObject(i);

                    int id = json_data.getInt(table_team.FIELD_ID);
                    String name = json_data.getString(table_team.FIELD_NAME);

                    team_User.add(new Team(id,name));

                }


                // Richiamo scrittura su db delle tabelle

                db = new DataBaseHelper(context);

//                db.writeTeam(team_User,1);

                db.close();


            } catch (JSONException e) {

                Log.e("prova1","dai23442");
                e.printStackTrace();
            }
        }
    }
}
