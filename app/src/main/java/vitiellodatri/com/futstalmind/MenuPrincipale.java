package vitiellodatri.com.futstalmind;


import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;

import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import Bean.Team;
import DataBase.DataBaseHelper;
import NavigationDrawer.NavDrawerItem;
import NavigationDrawer.NavDrawerListAdapter;

public class MenuPrincipale extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    int IdUser=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principale);

        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        //
        // Recupero ID user salvato nelle preferenze
        //
        pref = getApplicationContext().getSharedPreferences("Preferences", 0);
        editor = pref.edit();
        IdUser =pref.getInt("key_idUser", 0);
        //
        // Recycle the typed array

        navMenuIcons.recycle();

        // Setto che la lista è cliccabile

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        //
        // Richiamo metodo che comporra' il menu principale
        //
        updateDrawer();

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
    }

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_save).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {


        Fragment fragment = null;

        // Recupero l'oggetto cliccato e in base all'azione salvata apro il relativo menu
        NavDrawerItem n1 = (NavDrawerItem) adapter.getItem(position);

        switch (n1.getAzioneMenu()) {
            // Nessun azione da eseguire
            case NESSUNA:
                break;
            // Aggiunta squadra
            case AGGIUNTASQUADRA:
                fragment = new Squadra_Nuova(IdUser);
                break;
            // Dettaglio della squadra
            case DETTAGLIOSQUADRA:
                // Entrare nel dettaglio della squadra
                Intent i = new Intent(this,Squadra_Menu.class);
                startActivity(i);
            default:
                break;
        }
        // Fragment definito allora posso aprire
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void updateDrawer(){

        // Azzero elenco degli elementi del menu
        navDrawerItems.clear();

        // Creo intestazione
        navDrawerItems.add(new NavDrawerItem("",0, NavDrawerItem.TipoItem.HEADER, NavDrawerItem.AzioneItem.NESSUNA));
        // Creo macro sezione
        navDrawerItems.add(new NavDrawerItem("GESTIONE SQUADRA",0, NavDrawerItem.TipoItem.SEZIONE, NavDrawerItem.AzioneItem.NESSUNA));
        // Ciclo sulle squadre presenti nel database
        DataBaseHelper db = new DataBaseHelper(this);
        for (Team t : db.getTeams()){
            // Aggiungo la squadra ad un oggetto provvisorio
            NavDrawerItem itemTeam = new NavDrawerItem(t.getName(),0, NavDrawerItem.TipoItem.ITEM, NavDrawerItem.AzioneItem.DETTAGLIOSQUADRA);
            // Salvo anche id squadra all'elenco
            itemTeam.setIdSquadra(t.getId());
            // Aggiungo l'elemento al menu
            navDrawerItems.add(itemTeam);

        }
        // Chiudo database
        db.close();

        // Aggiungo possibilita' di aggiunta squadra
        navDrawerItems.add(new NavDrawerItem("Aggiunta squadra",0, NavDrawerItem.TipoItem.ITEM, NavDrawerItem.AzioneItem.AGGIUNTASQUADRA));

        // Adapter che conterra' il menu
        adapter = new NavDrawerListAdapter(getApplicationContext(),navDrawerItems);

        // Popolo fisicamente il menu
        mDrawerList.setAdapter(adapter);


    }

}