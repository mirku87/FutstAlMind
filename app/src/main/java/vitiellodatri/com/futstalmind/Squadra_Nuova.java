package vitiellodatri.com.futstalmind;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import Bean.Team;
import DataBase.DataBaseHelper;
import DataBase.PhpFunctions;

public class Squadra_Nuova extends Fragment {

    private ProgressDialog nDialog;
    private static String KEY_SUCCESS = "success";

    private String nomeSquadra = "";
    private String dataCreazione = "";
    Spinner spinner_giorno;
    Spinner spinner_mese;
    Spinner spinner_anno;

    EditText edit_nomeSquadra;

    int IdUser = 0;

    public Squadra_Nuova(int IdUser){

        this.IdUser = IdUser;

    }

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_squadra_nuova, container, false);

        // Popolo lo spinner giorni/mesi/anni
        spinner_giorno = (Spinner) rootView.findViewById(R.id.spinner_giorno);
        spinner_mese = (Spinner) rootView.findViewById(R.id.spinner_mese);
        spinner_anno = (Spinner) rootView.findViewById(R.id.spinner_anno);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_giorni = ArrayAdapter.createFromResource(getActivity(),R.array.spinner_giorni, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter_mesi = ArrayAdapter.createFromResource(getActivity(),R.array.spinner_mesi, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter_anni = ArrayAdapter.createFromResource(getActivity(),R.array.spinner_anni, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_giorni.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_mesi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_anni.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_giorno.setAdapter(adapter_giorni);
        spinner_mese.setAdapter(adapter_mesi);
        spinner_anno.setAdapter(adapter_anni);

        edit_nomeSquadra = (EditText) rootView.findViewById(R.id.edit_squadra);



        return rootView;
    }

    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        //MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.menu_squadra_nuova, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_save:

                avvioSalvataggioSquadra();
                return true;

            default:

                return super.onOptionsItemSelected(item);

        }

    }

    private void avvioSalvataggioSquadra(){

        AlertDialog.Builder miaAlert = new AlertDialog.Builder(getActivity());
        miaAlert.setMessage("Conferma salvataggio squadra?");
        miaAlert.setTitle("Attenzione!");
        miaAlert.setCancelable(true);

        miaAlert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                // Avvio salvataggio squadra su db remoto

                nomeSquadra =edit_nomeSquadra.getText().toString();

                // Scrivo la nuova squadra nel database
                Team nuovaSquadra = new Team(0,nomeSquadra);
                DataBaseHelper db = new DataBaseHelper(getActivity());
                db.writeTeam(nuovaSquadra);

                //
                // Aggiorno navigation drawer
                ((MenuPrincipale) getActivity()).updateDrawer();

                db.close();
                //

                // Entrare nel dettaglio della squadra
                Intent i = new Intent(getActivity(),Squadra_Menu.class);
                startActivity(i);


            }
        });
        miaAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //tv.setText("Ho cliccato il tasto SI");
            }
        });

        miaAlert.show();
    }

}
