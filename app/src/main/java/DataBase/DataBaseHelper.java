package DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import Bean.Player;
import Bean.Team;
import Bean.User;

/**
 * Created by Mirko on 19/01/2015.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FutsAlMind";
    private static final int DATABASE_VERSION = 1;

    User Table_user;
    Team Table_team;
    Player Table_player;

    public DataBaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        /* Creazione del database */

        Table_user = new User();
        Table_team = new Team();
        Table_player = new Player();

    }

    public void onCreate(SQLiteDatabase db) {

        // USER
        String create = "";
        create += "CREATE TABLE " + Table_user.TABLE_NAME + "(";
        create +=  Table_user.FIELD_ID + " INTEGER PRIMARY KEY ,";
        create +=  Table_user.FIELD_USERNAME + " TEXT,";
        create +=  Table_user.FIELD_NAME + " TEXT,";
        create +=  Table_user.FIELD_SURNAME + " TEXT,";
        create +=  Table_user.FIELD_BIRTHDATE + " INTEGER,";
        create +=  Table_user.FIELD_BIRTHPLACE + " TEXT,";
        create +=  Table_user.FIELD_CITY + " TEXT,";
        create +=  Table_user.FIELD_PROVINCE + " TEXT,";
        create +=  Table_user.FIELD_IDNATION + " INTEGER,";
        create +=  Table_user.FIELD_EMAIL + " TEXT,";
        create +=  Table_user.FIELD_TELEPHONE + " INTEGER)";
        db.execSQL(create);

        // TEAM
        create = "";
        create += "CREATE TABLE " + Table_team.TABLE_NAME + "(";
        create +=  Table_team.FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,";
        create +=  Table_team.FIELD_NAME + " TEXT,";
        create +=  Table_team.FIELD_FOUNDATIONFOUNDER + " TEXT,";
        create +=  Table_team.FIELD_FOUNDATIONDATE + " INTEGER,";
        create +=  Table_team.FIELD_COLOURS + " INTEGER,";
        create +=  Table_team.FIELD_LOGO + " TEXT,";
        create +=  Table_team.FIELD_NOTE + " TEXT)";
        db.execSQL(create);

        // PLAYER
        create = "";
        create += "CREATE TABLE " + Table_player.TABLE_NAME + "(";
        create +=  Table_player.FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,";
        create +=  Table_player.FIELD_NAME + " TEXT,";
        create +=  Table_player.FIELD_SURNAME + " TEXT,";
        create +=  Table_player.FIELD_BIRTHDATE + " INTEGER,";
        create +=  Table_player.FIELD_IDNATION + " INTEGER,";
        create +=  Table_player.FIELD_IDROLE + " INTEGER,";
        create +=  Table_player.FIELD_TELEPHONENUMBER + " TEXT)";
        create +=  Table_player.FIELD_EMAIL + " TEXT)";
        db.execSQL(create);


        // Scrittura dati di prova

        writeDatiStandard(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        /* In caso di aggiornamento database da una versione all'altra del db*/

    }


    public long writePlayer(Player newPlayer){

        ContentValues values = new ContentValues();
        values.put(Table_player.FIELD_ID, newPlayer.getId());
        values.put(Table_player.FIELD_NAME, newPlayer.getName());
        values.put(Table_player.FIELD_SURNAME, newPlayer.getSurname());
        values.put(Table_player.FIELD_BIRTHDATE, newPlayer.getBirthDate());
        values.put(Table_player.FIELD_IDNATION, newPlayer.getIdNation());
        values.put(Table_player.FIELD_IDROLE, newPlayer.getIdRole());
        values.put(Table_player.FIELD_TELEPHONENUMBER, newPlayer.getTelephoneNumber());
        values.put(Table_player.FIELD_EMAIL, newPlayer.getEmail());

        return this.getWritableDatabase().insert(Table_player.TABLE_NAME, null, values);
    }

    /**
     * Scrive una nuova squadra nel db
     */

    public long writeTeam(Team newTeam){

        ContentValues values = new ContentValues();
//        values.put(Table_team.FIELD_ID, newTeam.getId());
        values.put(Table_team.FIELD_NAME, newTeam.getName());
        return this.getWritableDatabase().insert(Table_team.TABLE_NAME, null, values);

    }
    /**
     * Restituisce tutte le squadre presenti nel db
    */
    public ArrayList<Team> getTeams(){

        ArrayList<Team> team = new ArrayList<Team>();

        String query = "SELECT * FROM " + Table_team.TABLE_NAME;

        Cursor cursor = this.getReadableDatabase().rawQuery(query,null);

        while (cursor.moveToNext()) {


            int id = cursor.getInt(cursor.getColumnIndex(Table_team.FIELD_ID));
            String name = cursor.getString(cursor.getColumnIndex(Table_team.FIELD_NAME));

            Team t = new Team(id,name);
            team.add(t);

        }

        cursor.close();

        return  team;

    }

    public int getTotInsultiPersonalizzati(){
        int numInsulti = 0;
        String query = "SELECT *  FROM " + Table_team.TABLE_NAME ;
        Cursor cursor = this.getReadableDatabase().rawQuery(query, null);

        while (cursor.moveToNext()) {

        }
        return numInsulti;

        // AHAHAHAHHAHAHAHAHAHAHAHH
    }

    public void writeDatiStandard(SQLiteDatabase db){

        // Scrivo squadra standard

        Team team = new Team(1,"Squadra di prova");
        ContentValues values = new ContentValues();
        values.put(Table_team.FIELD_ID, team.getId());
        values.put(Table_team.FIELD_NAME, team.getName());
        db.insert(Table_team.TABLE_NAME, null, values);

        // Scrivo giocatori standard

        Player player = new Player(1,"Mirko","Vitiello",0,0,0,"3382053299","mail@gmail.com");
        values.put(Table_player.FIELD_ID, player.getId());
        values.put(Table_player.FIELD_NAME, player.getName());
        values.put(Table_player.FIELD_SURNAME, player.getSurname());
        values.put(Table_player.FIELD_BIRTHDATE, player.getBirthDate());
        values.put(Table_player.FIELD_IDNATION, player.getIdNation());
        values.put(Table_player.FIELD_IDROLE, player.getIdRole());
        values.put(Table_player.FIELD_TELEPHONENUMBER, player.getTelephoneNumber());
        values.put(Table_player.FIELD_EMAIL, player.getEmail());
        db.insert(Table_player.TABLE_NAME, null, values);

        values = new ContentValues();
        player = new Player(2,"Alessandro","D'Atri",0,0,0,"3384332453299","mail1@gmail.com");
        values.put(Table_player.FIELD_ID, player.getId());
        values.put(Table_player.FIELD_NAME, player.getName());
        values.put(Table_player.FIELD_SURNAME, player.getSurname());
        values.put(Table_player.FIELD_BIRTHDATE, player.getBirthDate());
        values.put(Table_player.FIELD_IDNATION, player.getIdNation());
        values.put(Table_player.FIELD_IDROLE, player.getIdRole());
        values.put(Table_player.FIELD_TELEPHONENUMBER, player.getTelephoneNumber());
        values.put(Table_player.FIELD_EMAIL, player.getEmail());
        db.insert(Table_team.TABLE_NAME, null, values);

    }
}
