package vitiellodatri.com.futstalmind;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import Bean.Team;
import DataBase.DataBaseHelper;

public class Giocatori_Elenco extends Fragment {

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_giocatori_elenco, container, false);

        return rootView;
    }

    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        //MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.menu_giocatori_elenco, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_add:

                Intent i = new Intent(getActivity(),Giocatori_Dettaglio.class);
                startActivity(i);

                return true;

            default:

                return super.onOptionsItemSelected(item);

        }

    }
}
