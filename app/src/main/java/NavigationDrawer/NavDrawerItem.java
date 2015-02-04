package NavigationDrawer;

public class NavDrawerItem {

    private String title;
    private int icon;
    private String count = "0";

    private int idSquadra = 0;

    private TipoItem tipoItem;
    private AzioneItem azioneItem;

    // boolean to set visiblity of the counter
    private boolean isCounterVisible = false;

    public NavDrawerItem(){}

    public NavDrawerItem(String title, int icon, TipoItem tipoItem,AzioneItem azioneItem ){
        this.title = title;
        this.icon = icon;
        this.tipoItem = tipoItem;
        this.azioneItem = azioneItem;
    }

    public NavDrawerItem(String title, int icon, boolean isCounterVisible, String count){
        this.title = title;
        this.icon = icon;
        this.isCounterVisible = isCounterVisible;
        this.count = count;
    }

    public String getTitle(){
        return this.title;
    }

    public int getIcon(){
        return this.icon;
    }

    public AzioneItem getAzioneMenu(){
        return this.azioneItem;
    }

    public TipoItem getTipo() {return this.tipoItem;}

    public String getCount(){
        return this.count;
    }

    public boolean getCounterVisibility(){
        return this.isCounterVisible;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }

    public void setCount(String count){
        this.count = count;
    }

    public void setCounterVisibility(boolean isCounterVisible){
        this.isCounterVisible = isCounterVisible;
    }

    public int getIdSquadra() { return this.idSquadra;}

    public  void setIdSquadra(int idSquadra) { this.idSquadra=idSquadra;}


    public enum TipoItem {HEADER,SEZIONE,ITEM

    }


    public enum AzioneItem {NESSUNA,AGGIUNTASQUADRA,DETTAGLIOSQUADRA,GIOCATORI

    }
}