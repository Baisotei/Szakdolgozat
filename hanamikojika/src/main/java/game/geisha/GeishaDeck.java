package game.geisha;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class GeishaDeck implements Serializable {

    private List<Geisha> geishaDeck;

    public GeishaDeck() {
        this.geishaDeck = Arrays.asList(
                new Geisha(2,0),
                new Geisha(2,1),
                new Geisha(2,2),
                new Geisha(3,3),
                new Geisha(3,4),
                new Geisha(4,5),
                new Geisha(5,6)
          );
    }

    public Geisha getGeisha(int id) {
        return geishaDeck.get(id);
    }

    public List<Geisha> getGeishaDeck() {
        return geishaDeck;
    }
}
