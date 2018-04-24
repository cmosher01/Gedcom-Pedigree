package nu.mine.mosher.gedcom;

import java.io.*;
import java.nio.file.*;
import java.util.Objects;
import nu.mine.mosher.asciigraphics.AsciiGraphics;
import nu.mine.mosher.asciigraphics.Coords;
import nu.mine.mosher.gedcom.exception.InvalidLevel;
import nu.mine.mosher.gedcom.model.Loader;
import nu.mine.mosher.gedcom.model.Person;

import static nu.mine.mosher.asciigraphics.Coords.xy;

public class GedcomPedigree {
    public static void main(final String... args) throws IOException, InvalidLevel {
        if (args.length != 2) {
            throw new IllegalArgumentException("Usage: gedcom-pedigree input.ged generation-count");
        }
        final Path pathInput = Paths.get(args[0]).toRealPath();
        final GedcomTree tree = Gedcom.readFile(new BufferedInputStream(Files.newInputStream(pathInput)));
        new GedcomConcatenator(tree).concatenate();
        final Loader loader = new Loader(tree, pathInput.toString());
        loader.parse();

        run(loader.getFirstPerson(), Integer.parseInt(args[1])-1);

        System.out.flush();
    }

    private static void run(final Person root, final int g) {
        if (g < 1) {
            throw new IllegalArgumentException("must have at least 2 generations");
        }

        final int h = 1 << (g+1); // leaves one blank row at the bottom

        final AsciiGraphics gr = new AsciiGraphics();

        int y = (h >> 1) - 1;
        int dy = 1 << (g - 1);

        final int len = maxlen(root, 0, g);

        try {
            genx(0, y, dy, root, len+2, gr);
        } catch (Coords.Invalid invalid) {
            throw new IllegalStateException(invalid);
        }



        //gr.debugPrint(new PrintWriter(System.out, true));
        gr.print(new PrintWriter(System.out, true));
    }

    private static int maxlen(Person p, int m, final int g) {
        final int namelen = AsciiGraphics.getGraphemeCount(p.getNameSortedDisplay());
        if (namelen > m) {
            m = namelen;
        }
        if (0 < g) {
            Person x;
            x = p.getFather();
            if (Objects.nonNull(x)) {
                m = maxlen(x, m, g - 1);
            }
            x = p.getMother();
            if (Objects.nonNull(x)) {
                m = maxlen(x, m, g - 1);
            }
        }
        return m;
    }

    private static void genx(final int g, final int y, final int dy, final Person person, int nlen, final AsciiGraphics gr) throws Coords.Invalid {
        final String name = person.getNameSortedDisplay();
        final int x = g * nlen;
        //if (0 < dy) {
            gr.hLine(xy(x, y), nlen-1, '\u2500');
        //}
        gr.hString(xy(x,y), name);
        if (dy <= 0) {
            return;
        }

        geny(g, y, dy, person.getFather(), nlen, gr, x, '\u250C', -1);
        geny(g, y, dy, person.getMother(), nlen, gr, x, '\u2514', +1);
    }

    private static void geny(final int g, final int y, final int dy, final Person person, final int nlen, final AsciiGraphics gr, final int x, final char end, final int s) throws Coords.Invalid {
        if (Objects.isNull(person)) {
            return;
        }
        gr.vLine(xy(x + nlen - 1, y), s*(dy+1), '\u2502', '\u2524', end);
        genx(g + 1, y+s*dy, dy >> 1, person, nlen, gr);
    }
}
