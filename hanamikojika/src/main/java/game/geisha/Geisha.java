package game.geisha;

import java.io.Serializable;
import java.util.Objects;

public class Geisha implements Serializable {

    private int charmPoints;
    private int id;

    public Geisha(int charmPoints, int id) {
        this.charmPoints = charmPoints;
        this.id = id;
    }

    public int getCharmPoints() {
        return charmPoints;
    }

    public void setCharmPoints(int charmPoints) {
        this.charmPoints = charmPoints;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Geisha geisha = (Geisha) o;
        return getId() == geisha.getId();
    }

    @Override
    public int hashCode() {

        return Objects.hash(getCharmPoints(), getId());
    }

    @Override
    public String toString() {
        return "Geisha{" +
                "charmPoints=" + charmPoints +
                ", id=" + id +
                '}';
    }
}
