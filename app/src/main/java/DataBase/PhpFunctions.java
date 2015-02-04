package DataBase;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import DataBase.JSONParser;

public class PhpFunctions {
    private JSONParser jsonParser;
    //URL of the PHP API
    private static String loginURL = "http://futsalmind.altervista.org/scriptMobile/UserFunctions.php";
    private static String registerURL = "http://futsalmind.altervista.org/scriptMobile/UserFunctions.php";
    private static String forpassURL = "http://futsalmind.altervista.org/scriptMobile/UserFunctions.php";
    private static String getUserTeam = "http://futsalmind.altervista.org/scriptMobile/UserFunctions.php";
    private static String newTeam = "http://futsalmind.altervista.org/scriptMobile/TeamFunctions.php";
    private static String chgpassURL = "http://mygymmv.altervista.org/Login1.php";
    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String forpass_tag = "forpass";
    private static String chgpass_tag = "chgpass";
    private static String getTeamUser_tag = "getTeamUser";

    // constructor
    public PhpFunctions(){
        jsonParser = new JSONParser();
    }
    /**
     * Function to Login
     **/
    public JSONObject loginUser(String username, String password){
        // Building Parameters
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        return json;
    }
    /**
     * Function to change password
     **/
    public JSONObject chgPass(String newpas, String email){
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", chgpass_tag));
        params.add(new BasicNameValuePair("newpas", newpas));
        params.add(new BasicNameValuePair("email", email));
        JSONObject json = jsonParser.getJSONFromUrl(chgpassURL, params);
        return json;
    }
    /**
     * Function to reset the password
     **/
    public JSONObject forPass(String forgotpassword){
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", forpass_tag));
        params.add(new BasicNameValuePair("forgotpassword", forgotpassword));
        JSONObject json = jsonParser.getJSONFromUrl(forpassURL, params);
        return json;
    }
    /**
     * Function to  Register
     **/
    public JSONObject registerUser(String username, String password, String mail, String nome, String cognome,String datanascita,String luogonascita,int telefono){
        // Building Parameters
        List params = new ArrayList();


        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("mail", mail));
        params.add(new BasicNameValuePair("nome", nome));
        params.add(new BasicNameValuePair("cognome", cognome));
        params.add(new BasicNameValuePair("datanascita", datanascita));
        params.add(new BasicNameValuePair("luogonascita", luogonascita));
        params.add(new BasicNameValuePair("telefono", String.valueOf(telefono)));
        JSONObject json = jsonParser.getJSONFromUrl(registerURL,params);
        return json;
    }

    public JSONObject newTeam(String nomeSquadra, int IdUser,String dataCreazione){
        // Building Parameters
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", "newTeam"));
        params.add(new BasicNameValuePair("nomeSquadra", nomeSquadra));
        params.add(new BasicNameValuePair("idUser", String.valueOf(IdUser)));
        params.add(new BasicNameValuePair("dataCreazione", dataCreazione));
        JSONObject json = jsonParser.getJSONFromUrl(newTeam, params);
        return json;
    }

    public String getUserTeam(int IdUser){
        // Building Parameters
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", getTeamUser_tag));
        params.add(new BasicNameValuePair("idUser", String.valueOf(IdUser)));
        String json = jsonParser.getJSONString(getUserTeam, params);
        return json;
    }

    /**
     * Function to logout user
     * Resets the temporary data stored in SQLite Database
     * *//*
    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    } */
}
