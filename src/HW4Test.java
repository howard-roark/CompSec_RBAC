import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewmcguire on 10/25/14.
 */
public class HW4Test {

    /* Role Hierarchy test file to make sure file is read properly */
    private File file;
    private File badFile;

    /* Expected list returned from readRoleHierarchy */
    private List<String> lines = new ArrayList<String>();

    @Before
    public void setUp() {
        file = new File("/Users/matthewmcguire/Documents/MSUD/" +
                "Fall_14/CS_3750/HW4/src/roleHierarchyTEST.txt");
        badFile = new File("/Users/matthewmcguire/Documents/MSUD/" +
                "Fall_14/CS_3750/HW4/src/roleHierarchyBAD.txt");
        lines.add("R8");
        lines.add("R6");
        lines.add("R9");
        lines.add("R7");
    }

    @Test
    public void testReadRoleHierarchy() throws Exception {
        assertEquals(lines, Readers.readFile(file));
    }

    @Test
    public void testBadFile() throws Exception {
        Readers reader = mock(Readers.class);
        when(reader.readFile(badFile));
    }

    /**
     * Ensure that the matches method in Readers is testing the lines read in
     * from the roleHierarchy files properly.
     */
    @Test
    public void testMatches() {
        assertEquals(true, Readers.matches("^[Rr][0-9]+{1}[\\s]+[Rr][0-9]+{1}",
                "r5\tR5"));
        assertEquals(false, Readers.matches("^[Rr][0-9]+{1}[\\s]+[Rr][0-9]+{1}",
                "rR5\tR5"));
        assertEquals(false, Readers.matches("^[Rr][0-9]+{1}[\\s]+[Rr][0-9]+{1}",
                "R5\tR5\\s"));
    }

    @After
    public void breakDown() {

    }
}
