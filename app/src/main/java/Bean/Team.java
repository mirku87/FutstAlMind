package Bean;

/**
 * Created by Mirko on 25/01/2015.

 */
public class Team {

    /* Campi della tabella User
    * */
    public String TABLE_NAME             = "Team";
    public String FIELD_ID                = "id";
    public String FIELD_NAME              = "Name";
    public String FIELD_FOUNDATIONFOUNDER = "FoundationFounder";
    public String FIELD_FOUNDATIONDATE    = "FoundationDate";
    public String FIELD_COLOURS           = "Colours";
    public String FIELD_LOGO              = "Logo";
    public String FIELD_NOTE              = "Note";

    /* Campi del Bean */

    private int id;
    private String name;

    public Team(){}

    public Team(int id,String name){

        this.id = id;
        this.name = name;

    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}