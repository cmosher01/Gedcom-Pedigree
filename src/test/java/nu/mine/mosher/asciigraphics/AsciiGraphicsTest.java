package nu.mine.mosher.asciigraphics;

import org.junit.jupiter.api.Test;

import java.io.PrintWriter;

import static nu.mine.mosher.asciigraphics.Coords.xy;

public class AsciiGraphicsTest {
    @Test
    void nominal() throws Coords.Invalid
    {
        final AsciiGraphics uut = new AsciiGraphics();
        uut.hString(xy(2,3), "Test");
        show(uut);
    }

    @Test
    void at_0_0() throws Coords.Invalid
    {
        final AsciiGraphics uut = new AsciiGraphics();
        uut.hString(xy(0,0), "Test");
        show(uut);
    }

    @Test
    void emptyString() throws Coords.Invalid
    {
        final AsciiGraphics uut = new AsciiGraphics();
        uut.hString(xy(89,99), "");
        show(uut);
    }

    @Test
    void nominalHLine() throws Coords.Invalid
    {
        final AsciiGraphics uut = new AsciiGraphics();
        uut.hLine(xy(3,1), 5, '-');
        show(uut);
    }

    @Test
    void nominalHLineSE() throws Coords.Invalid
    {
        final AsciiGraphics uut = new AsciiGraphics();
        uut.hLine(xy(3,1), 5, '\u2500', '\u251c', '\u2524');
        show(uut);
    }

    @Test
    void nominalVLine() throws Coords.Invalid
    {
        final AsciiGraphics uut = new AsciiGraphics();
        uut.vLine(xy(3,1), 5, '|');
        show(uut);
    }

    @Test
    void nominalVLineSE() throws Coords.Invalid
    {
        final AsciiGraphics uut = new AsciiGraphics();
        uut.vLine(xy(3,1), 5, '\u2502', '\u252C', '\u2534');
        show(uut);
    }


    private static void show(AsciiGraphics uut) {
        final PrintWriter out = new PrintWriter(System.out);
        uut.debugPrint(out);
        out.flush();
    }
}
