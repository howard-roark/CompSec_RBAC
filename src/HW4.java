import java.io.File;
import java.io.Reader;
import java.util.*;

/**
 * Created by matthewmcguire on 10/25/14.
 */
public class HW4 {
    /* Keys for objectsMap passed in from Readers */
    private final String FILE = "File";
    private final String PROCESS = "Process";
    private final String DISK = "Disk";

    /**
     * Problem 2 on the Homework Assignment.
     *
     * @param file
     * @return roleHierarchy in map data structure
     */
    public Map<String, List<String>> readRoleHierarchy(File file) {
        Stack<String> roleStack = Readers.readRoleHierarchyFile(file);
        Map<String, List<String>> roleMap =
                new TreeMap<String, List<String>>();
        List<String> ascendants;
        while (roleStack.size() >= 2) {
            String descendant = roleStack.pop();
            String ascendant = roleStack.pop();
            /* If the list of roles already has the descendant than add the
            ascendant to that list of its ascendants, else add the descendant
            to the list and make a new list of ascendants with the ascendant.
             */
            if (roleMap.containsKey(descendant)) {
                ascendants = roleMap.get(descendant);
                ascendants.add(ascendant);
                roleMap.put(descendant, ascendants);
            } else {
                ascendants = new ArrayList<String>();
                ascendants.add(ascendant);
                roleMap.putIfAbsent(descendant, ascendants);
            }
        }
        return roleMap;
    }

    /**
     * Print the role map to the console for the user to read.
     *
     * @param roleMap
     */
    private void printRoleMap(Map<String, List<String>> roleMap) {
        for (String descendant : roleMap.keySet()) {
            String subRoles = "";
            List<String> ascendants = roleMap.get(descendant);
            Collections.sort(ascendants, new NaturalOrderComparator());
            for (String ascendant : ascendants) {
                subRoles = subRoles + ascendant + ", ";
            }
            //Remove last comma before printing
            subRoles = subRoles.substring(0, subRoles.length() - 2);
            HW4.pLn(descendant + " --> " + subRoles);
        }
    }

    protected Map<String, Set<String>> readResourceObjects(File file) {
        Map<String, Set<String>> objectsMap =
                Readers.readResourceObjectsFile(file);
        return objectsMap;
    }

    /**
     * Take in the built maps for roleHierarchy and objectsResources and print
     * the matrix appropriately.
     *
     * @param rolesMap
     * @param objectsMap
     */
    protected String[][] buildRoleObjectMatrix(Map<String, List<String>> rolesMap,
                                               Map<String, Set<String>> objectsMap) {
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
        return roleObjectMatrix;
    }

    /**
     * Print the empty Role Object matrix.  This is easier for me to read and
     * understand when the columns are not broken up.
     *
     * @param roleObjectMatrix
     */
    protected void printRoleObjectMatrix(String[][] roleObjectMatrix) {
        int hMatrix = roleObjectMatrix.length;
        int wMatrix = roleObjectMatrix[0].length;
        for (int i = 0; i < hMatrix; i++) {
            for (int j = 0; j < wMatrix; j++) {
                if (roleObjectMatrix[i][j] != null) {
                    p(roleObjectMatrix[i][j] + "\t\t\t");
                } else {
                    roleObjectMatrix[i][j] = "";
                    p(roleObjectMatrix[i][j] + "\t\t\t");
                }
            }
            pLn("");
        }
    }

    /**
     * Grant control rights to
     * @param accessMatrix
     */
    protected void grantControlRights(String[][] accessMatrix) {
        //Grant all roles to control themselves
        for (int i = 1; i < accessMatrix.length; i++) {
            for (int j = 1; j < accessMatrix[0].length; j++) {
                if (accessMatrix[i][0].equals(accessMatrix[0][j])) {
                    accessMatrix[i][j] =
                            AccessRights.CONTROL.getAccessRightValue();
                    break;
                }
            }
        }
    }

    /**
     * Grant the permissions from the permissionsToRoles file.
     *
     * @param roles
     * @param accessMatrix
     * @param permissions
     */
    protected void grantPermissions(Map<String, List<String>> roles,
                                    String[][] accessMatrix,
                                    File permissions) {
        grantControlRights(accessMatrix);
        //Put permissions from file into data structure to manage more easily.
        Map<String, Map<String, List<String>>> permissionMap =
                Readers.readPermissionsFile(permissions);

    }

    /**
     * Method to avoid using System.out for every call to print to console.
     *
     * @param item The token that was found
     */
    protected static void pLn(Object item) {
        System.out.println(item);
    }

    protected static void p(Object item) {
        System.out.print(item);
    }

    public static void main(String[] args) {
        HW4 hw4 = new HW4();
        String workingDirectory = System.getProperty("user.dir");
        File roleFile = new File(workingDirectory +
                "/Files/roleHierarchy.txt");
        File objectFile = new File(workingDirectory +
                "/Files/resourceObjects.txt");
        File permissionsFile = new File(workingDirectory +
                "/Files/permissionsToRoles.txt");

        pLn("Problem 2:\n");
        hw4.printRoleMap(hw4.readRoleHierarchy(roleFile));

        pLn("\nProblem 3:\n");
        hw4.printRoleObjectMatrix(
                hw4.buildRoleObjectMatrix(
                        hw4.readRoleHierarchy(roleFile),
                        hw4.readResourceObjects(objectFile)));

        pLn("\nProblem 4:\n");
        hw4.grantPermissions(hw4.readRoleHierarchy(roleFile),
                hw4.buildRoleObjectMatrix(
                        hw4.readRoleHierarchy(roleFile),
                        hw4.readResourceObjects(objectFile)),
                        permissionsFile);
    }
}