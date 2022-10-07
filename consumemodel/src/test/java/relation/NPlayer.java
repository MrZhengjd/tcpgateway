package relation;

import javafx.beans.InvalidationListener;
import lombok.Getter;
import lombok.Setter;

import java.util.Observable;
import java.util.Observer;

/**
 * @author zheng
 */
@Getter
@Setter
public class NPlayer implements Observer {
    private Integer playerIndex;
    private Long playerId;
    private String name;

    @Override
    public void update(Observable o, Object arg) {

    }

//    @Override
//    public void addListener(InvalidationListener listener) {
//
//    }
//
//    @Override
//    public void removeListener(InvalidationListener listener) {
//
//    }
}
