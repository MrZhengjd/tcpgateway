package relation;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * @author zheng
 */
@Getter
@Setter
public class NRoom extends Observable {
    private List<NPlayer> nPlayerList = new ArrayList<>();

    public void addPlayer(NPlayer nPlayer){
        nPlayerList.add(nPlayer);
    }
}
