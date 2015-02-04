package vitiellodatri.com.futstalmind;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Giocatore_Profilo extends Fragment {

    public Giocatore_Profilo(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_giocatore_profilo, container, false);

        return rootView;
    }


}
