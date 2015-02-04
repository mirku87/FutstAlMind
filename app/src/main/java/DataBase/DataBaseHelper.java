package DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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

    public DataBaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        /* Creazione del database */

        Table_user = new User();
        Table_team = new Team();

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

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        /* In caso di aggiornamento database da una versione all'altra del db*/

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
    }
}
