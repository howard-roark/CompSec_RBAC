import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.*;

/**
 * Created by matthewmcguire on 10/25/14.
 */
public class HW4Test {

    /* Role Hierarchy test file to make sure file is read properly */
    private File file;
    private File badFile;

    /*HW4 instance to test */
    HW4 hw4 = new HW4();

    /* Expected list returned from readFile */
    private final Stack<String> lines = new Stack<String>();

    /* Expected map returned from readRoleHierarchy */
    private final Map<String, List<String>> roleMap =
            new TreeMap<String, List<String>>();

    /* Expected map to be returned for readResourceObjects */
    private final Map<String, Set<String>> objectsMap =
            new TreeMap<String, Set<String>>();

    /* Set to add objects to for map above */
    private final Set<String> objectsSet = new LinkedHashSet<String>();

    /**
     * Build expected data structures to compare to actual returns.
     */
    @Before
    public void setUp() {
        file = new File("/Users/matthewmcguire/Documents/MSUD/" +
                "Fall_14/CS_3750/HW4/src/roleHierarchy.txt");
        badFile = new File("/Users/matthewmcguire/Documents/MSUD/" +
                "Fall_14/CS_3750/HW4/src/roleHierarchyBAD.txt");
        lines.push("R8");
        lines.push("R6");
        lines.push("R9");
        lines.push("R7");
        lines.push("R10");
        lines.push("R7");
        lines.push("R4");
        lines.push("R2");
        lines.push("R5");
        lines.push("R2");
        lines.push("R6");
        lines.push("R2");
        lines.push("R7");
        lines.push("R3");
        lines.push("R2");
        lines.push("R1");
        lines.push("R3");
        lines.push("R1");

        String[] l = new String[]{"R3", "R2"};
        roleMap.put("R1", Arrays.asList(l));
        l = new String[]{"R7"};
        roleMap.put("R3", Arrays.asList(l));
        l = new String[]{"R6", "R5", "R4"};
        roleMap.put("R2", Arrays.asList(l));
        l = new String[]{"R10", "R9"};
        roleMap.put("R7", Arrays.asList(l));
        l = new String[]{"R8"};
        roleMap.put("R6", Arrays.asList(l));

        objectsSet.add("F1");
        objectsSet.add("F2");
        objectsSet.add("F3");
        objectsSet.add("F4");
        objectsMap.put("File", objectsSet);

        objectsSet.clear();
        objectsSet.add("P1");
        objectsSet.add("P2");
        objectsMap.put("Process", objectsSet);

        objectsSet.clear();
        objectsSet.add("D1");
        objectsSet.add("D2");
        objectsMap.put("Disk", objectsSet);
    }

    /**
     * Ensure the file is being read in properly.
     * (Having problem mocking the user interaction for bad files.)
     *
     * @throws Exception
     */
    @Test
    public void testFile() throws Exception {
        assertEquals(lines, Readers.readRoleHierarchyFile(file));
    }

    /**
     * Ensure the map created in readRoleHierarchy is correct
     */
    @Test
    public void testReadRoleHierarchy() {
        assertEquals(roleMap, hw4.readRoleHierarchy(file));
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

    @Test
    public void testGetTextByPattern() {
        assertEquals("F1", Readers.getTextByPattern("^[\\s]*([FPD]{1}[0-9]+).*",
                "F1     F2  F4  D1"));
        assertEquals("D4", Readers.getTextByPattern("^[\\s]*([FPD]{1}[0-9]+).*",
                "       D4 D6D9"));
    }


    @Test
    public void testReadResourceObjectsFile() {
        assertEquals(objectsMap,
                Readers.readResourceObjectsFile(new
                        File("/Users/matthewmcguire/Documents/MSUD/Fall_14/CS_3750/HW4/src/resourceObjects.txt")));
    }
    @After
    public void breakDown() {

    }
}
