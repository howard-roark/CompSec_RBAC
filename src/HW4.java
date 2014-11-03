import java.io.File;
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

    protected List<String> buildDescendantList(String role,
                                               Map<String, List<String>> roles) {
        List<String> descendants = new ArrayList<String>();
        List<String> reverseKeys = new ArrayList<String>(roles.keySet());
        Collections.reverse(reverseKeys);
        for (String key : reverseKeys) {
            if (roles.get(key).contains(role)) {
                descendants.add(key);
                role = key;
            }
        }
        return descendants;
    }

    /**
     * Grant control rights to
     * @param accessMatrix
     */
    protected void grantControlAndOwnRights(String[][] accessMatrix,
                                            Map<String, List<String>> roles) {
        String control = AccessRights.CONTROL.getAccessRightValue();
        String owner = AccessRights.OWNER.getAccessRightValue();

        //Grant all roles to control themselves
        for (int i = 1; i < accessMatrix.length; i++) {//Iterate rows
            for (int j = 1; j < accessMatrix[0].length; j++) {//Iterate columns
                if (accessMatrix[i][0].equals(accessMatrix[0][j])) {
                    accessMatrix = addRight(accessMatrix, i, j, control);

                    List<String> descendants = buildDescendantList(
                            accessMatrix[i][0], roles);
                    for (String desc : descendants) {
                        for (int k = 1; k < accessMatrix.length; k++) {
                            if (accessMatrix[k][0].equals(desc)) {
                                accessMatrix = addRight(accessMatrix, k, j,
                                        new String[]{control, owner});
                            }
                        }
                    }
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
        grantControlAndOwnRights(accessMatrix, roles);

        //Put permissions from file into data structure to manage more easily.
        Map<String, Map<String, List<String>>> permissionMap =
                Readers.readPermissionsFile(permissions);
        for (String role : permissionMap.keySet()) {
            for (int i = 1; i < accessMatrix.length; i++) {
                if (accessMatrix[i][0].equals(role)) {
                    Map<String, List<String>> objectsRights =
                            permissionMap.get(role);
                    for (String obj : objectsRights.keySet()) {
                        for (int j = 1; j < accessMatrix[0].length; j++) {
                            if (accessMatrix[0][j].equals(obj)) {
                                for (String right : objectsRights.get(obj)) {
                                    accessMatrix =
                                            addRight(accessMatrix,i, j, right);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected void showConstraints(Map<String, List<String>> roleConstraints) {
        int count = 1;
        for (String cons : roleConstraints.keySet()) {
            List<String> roles = roleConstraints.get(cons);
            cons = Readers.getTextByPattern("([0-9]+)", cons);
            String printRoles = "";
            for (String role : roles) {
                printRoles = printRoles + role + ", ";
            }
            printRoles = printRoles.substring(0, printRoles.length() - 2);
            pLn("Constraint " + count + ", n = " + cons + ", set of roles = {"
                    + printRoles + "}");
            count++;
        }
    }

    /**
     * Add Rights to the access matrix.
     *
     * @param aMatrix
     * @param row
     * @param col
     * @param rights
     * @return
     */
    protected String[][] addRight(String[][] aMatrix, int row, int col,
                                  String... rights) {
        for (String right : rights) {
            if ((aMatrix[row][col] != null) &&
                    (!aMatrix[row][col].contains(right))) {
                String existingRight = aMatrix[row][col];
                String newRight;
                if ((existingRight.equals("")) || (existingRight == null)) {
                    newRight = right;
                } else {
                    newRight = existingRight + "," +
                            right;
                }
                aMatrix[row][col] = newRight;
            } else if (aMatrix[row][col] == null) {
                aMatrix[row][col] = right;
            }
        }
        return aMatrix;
    }


    /**
     * Show users to roles map based on constraints from problem 5 and user to
     * roles file.
     *
     * @param userRoleMap
     * @param constraintsMap
     */
    protected Map<String, List<String>> showUserToRoles(Map<String, List<String>> userRoleMap,
                                   Map<String, List<String>> constraintsMap) {
        Map<String, List<String>> usersAllowedRoles =
                new HashMap<String, List<String>>();
        List<String> alLConstrainedRoles = new LinkedList<String>();
        for (String key : constraintsMap.keySet()) {
            List<String> constrainedRoles = constraintsMap.get(key);
            int rolesAllowed = Integer.parseInt(Readers.getTextByPattern("([0-9]+", key));
            for (String role : constrainedRoles) {
                for (int i = 0; i < rolesAllowed; i++) {
                    alLConstrainedRoles.add(role);
                }
            }
        }

        Set<String> checkForRestraints = new HashSet<String>(alLConstrainedRoles);
        for (String userKey : userRoleMap.keySet()) {
            usersAllowedRoles.put(userKey, new ArrayList<String>());
            List<String> requestedRoles = userRoleMap.get(userKey);
            for (String reqRole : requestedRoles) {
                if (checkForRestraints.contains(reqRole)) {
                    if (alLConstrainedRoles.contains(reqRole)) {
                        List<String> roles = usersAllowedRoles.get(userKey);
                        roles.add(reqRole);
                        usersAllowedRoles.put(userKey, roles);
                    }
                } else {
                    List<String> roles = usersAllowedRoles.get(userKey);
                    roles.add(reqRole);
                    usersAllowedRoles.put(userKey, roles);
                }
            }
        }
        return usersAllowedRoles;
    }

    /**
     * Method to avoid using System.out for every call to print to console.
     *
     * @param item The token that was found
     */
    protected static void pLn(Object item) { System.out.println(item); }
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
        File rolesSSD = new File(workingDirectory +
                "/Files/roleSetsSSD.txt");
        File usersToRoles = new File(workingDirectory +
                "/Files/usersRoles.txt");

        pLn("Problem 2:\n");
        hw4.printRoleMap(hw4.readRoleHierarchy(roleFile));

        pLn("\nProblem 3:\n");
        hw4.printRoleObjectMatrix(
                hw4.buildRoleObjectMatrix(
                        hw4.readRoleHierarchy(roleFile),
                        hw4.readResourceObjects(objectFile)));

        pLn("\nProblem 4:\nMATRIX IS POPULATED PROPERLY BUT NOT PRINTING TO CONSOLE");
        hw4.grantPermissions(hw4.readRoleHierarchy(roleFile),
                hw4.buildRoleObjectMatrix(
                        hw4.readRoleHierarchy(roleFile),
                        hw4.readResourceObjects(objectFile)),
                        permissionsFile);

        pLn("\nProblem 5:\n");
        hw4.showConstraints(Readers.readRoleSets(rolesSSD));

        pLn("\nProblem 6:\nUSERS AND ALLOWABLE ROLES BUILT BUT NOT PRINTING");
        hw4.showUserToRoles(Readers.readUsersToRoles(usersToRoles),
                Readers.readRoleSets(rolesSSD));

        pLn("\nProblem 7:\nNot Attempted.  Ran out of time.");
    }
}