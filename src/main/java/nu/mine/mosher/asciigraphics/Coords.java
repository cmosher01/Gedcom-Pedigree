package nu.mine.mosher.asciigraphics;

/**
 * Represents coordinates on the plane:
 *
 * <pre>
 *  012345 (length() == 6)
 * 0
 * 1
 * 2   *<--at:(3,2)
 * 3
 * 4
 * (size() == 5)
 * </pre>
 */
public class Coords {
    private final int x;
    private final int y;

    public static final class Invalid extends Exception {
    }

    public Coords(final int x, final int y) throws Invalid {
        this.x = x;
        this.y = y;
        if (this.x < 0 || this.y < 0) {
            throw new Invalid();
        }
    }

    public static Coords xy(final int x, final int y) throws Invalid {
        return new Coords(x, y);
    }

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }

    public Coords move(final int dx, final int dy) throws Invalid {
        return new Coords(this.x+dx, this.y+dy);
    }

    public static Coords min(final Coords a, final Coords b) {
        try {
            return new Coords(Math.min(a.x, b.x), Math.min(a.y, b.y));
        } catch (Invalid cannotHappen) {
            throw new IllegalStateException(cannotHappen);
        }
    }

    public static Coords max(final Coords a, final Coords b) {
        try {
            return new Coords(Math.max(a.x, b.x), Math.max(a.y, b.y));
        } catch (Invalid cannotHappen) {
            throw new IllegalStateException(cannotHappen);
        }
    }

    @Override
    public String toString()
    {
        return "("+this.x+","+this.y+")";
    }

    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof Coords)) {
            return false;
        }
        final Coords that = (Coords)object;
        return this.x == that.x && this.y == that.y;
    }

    @Override
    public int hashCode() {
        return this.x + this.y;
    }
}
