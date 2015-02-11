package vitiellodatri.com.futstalmind;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
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

import Bean.Player;
import Bean.Team;
import DataBase.DataBaseHelper;

/**
 * Created by Alessandro on 06/02/2015.
 */
public class Player_Detail extends Activity {

    private Integer player_Id=0;
    private String player_Name;
    private String player_Surname;
    private Integer player_BirthDate;
    private Integer player_IdNation;
    private Integer player_IdRole;
    private String player_TelephoneNumber;
    private String player_Email;

    EditText editText_name;
    EditText editText_surname;
    Spinner spinner_dd;
    Spinner spinner_mm;
    Spinner spinner_yy;
    EditText editText_telephonenumber;
    EditText editText_email;
    Spinner spinner_role;
    Spinner spinner_jerseynumber;
    EditText editText_note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_detail);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_player_detail, container, false);

        // Popolo lo spinner giorni/mesi/anni
        spinner_dd = (Spinner) findViewById(R.id.spinner_dd);
        spinner_mm = (Spinner) findViewById(R.id.spinner_mm);
        spinner_yy = (Spinner) findViewById(R.id.spinner_yy);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_dd = ArrayAdapter.createFromResource(this,R.array.spinner_giorni, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter_mm = ArrayAdapter.createFromResource(this,R.array.spinner_mesi, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter_yy = ArrayAdapter.createFromResource(this,R.array.spinner_anni, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_dd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_mm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_yy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_dd.setAdapter(adapter_dd);
        spinner_mm.setAdapter(adapter_mm);
        spinner_yy.setAdapter(adapter_yy);

        // Popolo lo spinner dei ruoli
        spinner_role = (Spinner) findViewById(R.id.spinner_role);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_role = ArrayAdapter.createFromResource(this,R.array.spinner_role, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_dd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_role.setAdapter(adapter_role);

        // Popolo lo spinner dei numeri di maglia
        spinner_jerseynumber = (Spinner) findViewById(R.id.spinner_jerseynumber);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_jerseynumber = ArrayAdapter.createFromResource(this,R.array.spinner_jerseynumber, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_dd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_jerseynumber.setAdapter(adapter_jerseynumber);

        editText_name=(EditText) rootView.findViewById(R.id.edit_nome);

        return rootView;

    }

    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        //MenuInflater inflater = getSupportMenuInflater();
        //inflater.inflate(R.menu.menu_player_detail, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_save:

                startPlayerSave();
                return true;

            default:

                return super.onOptionsItemSelected(item);

        }

    }

    private void startPlayerSave(){

        AlertDialog.Builder miaAlert = new AlertDialog.Builder(getParent());
        miaAlert.setMessage("Conferma salvataggio giocatore?");
        miaAlert.setTitle("Attenzione!");
        miaAlert.setCancelable(true);

        miaAlert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                // Avvio salvataggio giocatore su db remoto

                player_Name =editText_name.getText().toString();
                player_Surname =editText_surname.getText().toString();
                player_BirthDate=0;
                player_IdNation =0;
                player_IdRole =0;
                player_TelephoneNumber =editText_telephonenumber.getText().toString();
                player_Email=editText_email.getText().toString();

                // Scrivo il giocatore nel database
                Player newPlayer = new Player(player_Id, player_Name, player_Surname, player_BirthDate, player_IdNation,player_IdRole,player_TelephoneNumber,player_Email);
                DataBaseHelper db = new DataBaseHelper(getBaseContext());

                // Direi che è ok dovrebbe funzionare
                // ok.. aggiungo gli altri campi??? Li hai gia' gestiti a video?
                db.writePlayer(newPlayer);

                db.close();
                //

                // Entrare nel dettaglio della squadra
                //Intent i = new Intent(getActivity(),Player_List.class);
                //startActivity(i);


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
