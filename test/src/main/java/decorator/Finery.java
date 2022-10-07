package decorator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author zheng
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Finery implements Persion {

    private Persion persion;
    @Override
    public void show() {
        if (persion != null){
            persion.show();
        }
    }
}
