package nu.mine.mosher.asciigraphics;

import com.google.common.base.Strings;

import java.io.PrintWriter;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import static nu.mine.mosher.asciigraphics.Coords.xy;

public class AsciiGraphics {
    private static final String SPACE = " ";

    private final List<StringBuilder> tab = new ArrayList<>();

    public void debugPrint(final PrintWriter out) {
        final int w = w();
        final int h = h();
        out.println("plane size: "+w+"x"+h);

        out.print(" ");
        int c = 0;
        for (int i = 0; i < w; ++i) {
            out.print(""+c++);
            if (c >= 10) {
                c = 0;
            }
        }
        out.println();

        c = 0;
        for (final StringBuilder sb : this.tab) {
            out.print(""+c++);
            if (c >= 10) {
                c = 0;
            }
            out.println(sb.toString()+"\u2592");
        }

        out.println("\u2592");
    }

    public void print(final PrintWriter out) {
        this.tab.forEach(out::println);
    }

    public void hString(final Coords at, final String s) {
        if (s.isEmpty()) {
            return;
        }

        final Coords to = to(at, getGraphemeCount(s));
        resize(to);
        final StringBuilder row = this.tab.get(at.y());
        row.replace(at.x(), to.x(), s);
    }

    public void hLine(final Coords at, final int length, final char c) {
        hLine(at, length, c, c, c);
    }

    public void hLine(final Coords at, final int length, final char c, final char start, final char end) {
        final String s;
        if (length <= 0) {
            return;
        } else if (length == 1) {
            s = ""+c;
        } else if (length == 2) {
            s = ""+start+end;
        } else {
            assert 3 <= length;
            s = ""+start+Strings.repeat("" + c, length-2)+end;
        }

        final Coords to = to(at, length);
        resize(to);
        final StringBuilder row = this.tab.get(at.y());
        row.replace(at.x(), to.x(), s);
    }

    /* positive length is down, negative length is up  (+1 or -1 means plot one character, 0 does nothing) */
    public void vLine(final Coords at, final int length, final char c) throws Coords.Invalid {
        vLine(at, length, c, c, c);
    }

    public void vLine(final Coords at, final int length, final char c, final char start, final char end) throws Coords.Invalid {
        if (length == 0) {
            return;
        }

        final int d = Integer.signum(length);
        for (int y = at.y(); y != at.y()+length; y += d) {
            resize(xy(at.x()+1, y));
            final StringBuilder row = this.tab.get(y);
            final char chr;
            if (y == at.y()) {
                chr = start;
            } else if (y == at.y()+length-d) {
                chr = end;
            } else {
                chr = c;
            }
            row.setCharAt(at.x(), chr);
        }
    }


    private Coords to(final Coords at, final int length) {
        try {
            return at.move(length, 0);
        } catch (final Coords.Invalid cannotHappen) {
            throw new IllegalStateException(cannotHappen);
        }
    }

    private void resize(final Coords min) {
        while (h() <= min.y()) {
            this.tab.add(new StringBuilder());
        }

        /*
        example:

        current length 2:
          01
          cc

        start at x 5:
          012345
          cc...s

        string of length 3:
          01234567
          cc...sss
            ^^^^^^

        so, need to append 6 chars:
        ((5+1)-2)+(3-1)

        ((x+1)-clen)+(slen-1)
        x+1-clen+slen-1
        x-clen+slen
        x+slen-clen
        (pass x+slen in x)
         */
        final StringBuilder row = this.tab.get(min.y());
        final int needed = min.x() - getGraphemeCount(row.toString());
        if (0 < needed) {
            row.append(Strings.repeat(SPACE, needed));
        }
    }

    private int h() {
        return this.tab.size();
    }

    private int w() {
        return this.tab
            .stream()
            .map(StringBuilder::length)
            .mapToInt(Integer::intValue)
            .max()
            .orElse(0);
    }

    public static int getGraphemeCount(final String text) {
        return text.length();
        //int graphemeCount = 0;
        //BreakIterator graphemeCounter = BreakIterator.getCharacterInstance();
        //graphemeCounter.setText(text);
        //while (graphemeCounter.next() != BreakIterator.DONE)
        //    graphemeCount++;
        //return graphemeCount;
    }
}
