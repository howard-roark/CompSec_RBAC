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
    String workingDirectory;

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

    /* Sets to add objects to for map above */
    private final Set<String> objectsSetF = new LinkedHashSet<String>();
    private final Set<String> objectsSetP = new LinkedHashSet<String>();
    private final Set<String> objectsSetD = new LinkedHashSet<String>();

    /**
     * Build expected data structures to compare to actual returns.
     */
    @Before
    public void setUp() {
        workingDirectory = System.getProperty("user.dir") + "/src/";
        file = new File(workingDirectory + "roleHierarchy.txt");
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

        String[] l = new String[]{"R2", "R3"};
        roleMap.put("R1", Arrays.asList(l));
        l = new String[]{"R7"};
        roleMap.put("R3", Arrays.asList(l));
        l = new String[]{"R4", "R5", "R6"};
        roleMap.put("R2", Arrays.asList(l));
        l = new String[]{"R9", "R10"};
        roleMap.put("R7", Arrays.asList(l));
        l = new String[]{"R8"};
        roleMap.put("R6", Arrays.asList(l));

        objectsSetF.add("F1");
        objectsSetF.add("F2");
        objectsSetF.add("F3");
        objectsSetF.add("F4");
        objectsMap.put("File", objectsSetF);

        objectsSetP.clear();
        objectsSetP.add("P1");
        objectsSetP.add("P2");
        objectsSetP.add("P3");
        objectsMap.put("Process", objectsSetP);

        objectsSetD.clear();
        objectsSetD.add("D1");
        objectsSetD.add("D2");
        objectsMap.put("Disk", objectsSetD);
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

    /**
     * Ensure that resourceObjects.txt is being read in properly.
     */
    @Test
    public void testReadResourceObjectsFile() {
        assertEquals(objectsMap,
                Readers.readResourceObjectsFile(new File(workingDirectory +
                        "resourceObjects.txt")));
    }

    /**
     * Ensure the is setup to be printed properly.
     */
    @Test
    public void testPrintRoleObjectMatrix() {
        Map<String, List<String>> rolesMap = hw4.readRoleHierarchy(file);
        List<String> allRoles = new LinkedList<String>();
        for (String rolesKey : rolesMap.keySet()) {
            allRoles.add(rolesKey);
            List<String> ascendants = rolesMap.get(rolesKey);
            for (String ascendant : ascendants) {
                if (!allRoles.contains(ascendant)) {
                    allRoles.add(ascendant);
                }
            }
            Collections.sort(allRoles);
        }
        assertNotNull(allRoles);
    }


    @After
    public void breakDown() {

    }
}
