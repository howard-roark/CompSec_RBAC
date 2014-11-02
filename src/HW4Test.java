import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.*;

/**
 * Created by matthewmcguire on 10/25/14.
 */
public class HW4Test {

    /* Role Hierarchy test roleFile to make sure roleFile is read properly */
    private File roleFile;
    String workingDirectory;

    /* File for resource objects */
    private File objsFile;

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

    /* Strings for Keys of objects map used to build matrix */
    private final String FILE = "File";
    private final String PROCESS = "Process";
    private final String DISK = "Disk";

    /* Map to test permission map with */
    private File permissionsFile;
    private final Map<String, Map<String, List<String>>> knownPermissions =
            new HashMap<String, Map<String, List<String>>>();
    private final Map<String, List<String>> objectsRightsA =
            new HashMap<String, List<String>>();
    private final Map<String, List<String>> objectsRightsB =
            new HashMap<String, List<String>>();
    private final Map<String, List<String>> objectsRightsC =
            new HashMap<String, List<String>>();
    private final Map<String, List<String>> objectsRightsD =
            new HashMap<String, List<String>>();

    /* Build example descndants list for role <R8> */
    private List<String> r8Descendants = new ArrayList<String>();

    /**
     * Build expected data structures to compare to actual returns.
     */
    @Before
    public void setUp() {
        workingDirectory = System.getProperty("user.dir");
        roleFile = new File(workingDirectory + "/Files/roleHierarchy.txt");
        objsFile = new File(workingDirectory + "/Files/resourceObjects.txt");
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

        //Build known permissions map
        permissionsFile =
                new File(workingDirectory + "/Files/permissionsToRoles.txt");
        objectsRightsA.put("F3",
                Arrays.asList(new String[]
                        {AccessRights.WRITE_WITH_COPY.getAccessRightValue()}));
        objectsRightsB.put("D1",
                Arrays.asList(new String[]
                        {AccessRights.SEEK.getAccessRightValue()}));
        objectsRightsC.put("P1",
                Arrays.asList(new String[]
                        {AccessRights.STOP.getAccessRightValue()}));
        objectsRightsD.put("D2",
                Arrays.asList(new String[]
                        {AccessRights.SEEK_WITH_COPY.getAccessRightValue()}));
        knownPermissions.put("R6", objectsRightsA);
        knownPermissions.put("R2", objectsRightsB);
        knownPermissions.put("R10", objectsRightsC);
        knownPermissions.put("R7", objectsRightsD);

        r8Descendants = Arrays.asList("R6", "R2", "R1");
    }

    /**
     * Ensure the roleFile is being read in properly.
     * (Having problem mocking the user interaction for bad roleFiles.)
     *
     * @throws Exception
     */
    @Test
    public void testFile() throws Exception {
        assertEquals(lines, Readers.readRoleHierarchyFile(roleFile));
    }

    /**
     * Ensure the map created in readRoleHierarchy is correct
     */
    @Test
    public void testReadRoleHierarchy() {
        assertEquals(roleMap, hw4.readRoleHierarchy(roleFile));
    }

    /**
     * Ensure that the matches method in Readers is testing the lines read in
     * from the roleHierarchy roleFiles properly.
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
                Readers.readResourceObjectsFile(objsFile));
    }

    /**
     * Ensure the full matrix is setup to be build properly.
     */
    @Test
    public void buildRoleObjectMatrix() {
        Map<String, List<String>> rolesMap = hw4.readRoleHierarchy(roleFile);
        Map<String, Set<String>> objectsMap = hw4.readResourceObjects(objsFile);

        //Pull all roles from rolesMap Map
        List<String> allRoles = new ArrayList<String>();
        for (String rolesKey : rolesMap.keySet()) {
            if (!allRoles.contains(rolesKey)) {
                allRoles.add(rolesKey);
            }
            List<String> ascendants = rolesMap.get(rolesKey);
            for (String ascendant : ascendants) {
                if (!allRoles.contains(ascendant)) {
                    allRoles.add(ascendant);
                }
            }
        }
        Collections.sort(allRoles, new NaturalOrderComparator());

        //Pull all objects from
        List<String> allObjects = new ArrayList<String>();
        if (objectsMap.get(FILE).size() > 0) {
            allObjects.addAll(objectsMap.get(FILE));
        }
        if (objectsMap.get(PROCESS).size() > 0) {
            allObjects.addAll(objectsMap.get(PROCESS));
        }
        if (objectsMap.get(DISK).size() > 0) {
            allObjects.addAll(objectsMap.get(DISK));
        }

        //Determine how many rows and columns needed for matrix
        int rowsNeeded = allRoles.size() + 1;
        int colsNeeded = allRoles.size() + allObjects.size() + 1;
        String[][] roleObjectMatrix = new String[rowsNeeded][colsNeeded];

        int i = 1, j = 0;
        while (i < colsNeeded) {
            //Fill in row titles (Roles)
            if (i < rowsNeeded) {
                roleObjectMatrix[i][0] = allRoles.get(i - 1);
                roleObjectMatrix[0][i] = allRoles.get(i - 1);
            }

            //Rows done start building the columns
            if (i >= rowsNeeded) {
                roleObjectMatrix[0][i] = allObjects.get(j);
                j++;
            }
            i++;
        }

        //Build matrix to test with
        String[][] testRoleObjectMatrix = new String[rowsNeeded][colsNeeded];
        testRoleObjectMatrix[1][0] = "R1";
        testRoleObjectMatrix[2][0] = "R2";
        testRoleObjectMatrix[3][0] = "R3";
        testRoleObjectMatrix[4][0] = "R4";
        testRoleObjectMatrix[5][0] = "R5";
        testRoleObjectMatrix[6][0] = "R6";
        testRoleObjectMatrix[7][0] = "R7";
        testRoleObjectMatrix[8][0] = "R8";
        testRoleObjectMatrix[9][0] = "R9";
        testRoleObjectMatrix[10][0] = "R10";

        testRoleObjectMatrix[0][1] = "R1";
        testRoleObjectMatrix[0][2] = "R2";
        testRoleObjectMatrix[0][3] = "R3";
        testRoleObjectMatrix[0][4] = "R4";
        testRoleObjectMatrix[0][5] = "R5";
        testRoleObjectMatrix[0][6] = "R6";
        testRoleObjectMatrix[0][7] = "R7";
        testRoleObjectMatrix[0][8] = "R8";
        testRoleObjectMatrix[0][9] = "R9";
        testRoleObjectMatrix[0][10] = "R10";
        testRoleObjectMatrix[0][11] = "F1";
        testRoleObjectMatrix[0][12] = "F2";
        testRoleObjectMatrix[0][13] = "F3";
        testRoleObjectMatrix[0][14] = "F4";
        testRoleObjectMatrix[0][15] = "P1";
        testRoleObjectMatrix[0][16] = "P2";
        testRoleObjectMatrix[0][17] = "P3";
        testRoleObjectMatrix[0][18] = "D1";
        testRoleObjectMatrix[0][19] = "D2";
        assertEquals(testRoleObjectMatrix, roleObjectMatrix);
    }

    @Test
    public void testReadPermissionFile() {
        assertEquals(knownPermissions,
                Readers.readPermissionsFile(permissionsFile));
    }

    @Test
    public void testAccessRightsCompareMethod() {
        for (AccessRights right : AccessRights.values()) {
            HW4.pLn(right.getAccessRightValue());
        }
        assertEquals(true, AccessRights.compareAccessRights("seek"));
    }

    @Test
    public void testBuildDescendantsList() {
        assertEquals(r8Descendants, hw4.buildDescendantList("R8",
                hw4.readRoleHierarchy(roleFile)));
    }

    @After
    public void breakDown() {

    }
}
