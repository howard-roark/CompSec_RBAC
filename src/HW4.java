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
        printRoleMap(roleMap);
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
            HW4.p(descendant + " --> " + subRoles);
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
    protected void buildRoleObjectMatrix(Map<String, List<String>> rolesMap,
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
        Collections.sort(allObjects, new NaturalOrderComparator());

        //Determine how many rows and columns needed for matrix
        int rowsNeeded = allRoles.size() + 1;
        int colsNeeded = allRoles.size() + allObjects.size() + 1;
        String[][] roleObjectMatrix = new String[rowsNeeded][colsNeeded];

        boolean rowsDone = false;
        int i = 1, j = 1;
        while ((i < rowsNeeded) && (j < colsNeeded)) {
            //Fill in row titles (Roles)
            if (i < rowsNeeded) {
                roleObjectMatrix[i][0] = allRoles.get(i - 1);
                i++;
            } else if (i == rowsNeeded) {
                rowsDone = true;
            }

            //Fill in column headers (Roles and Objects)
            if (rowsDone) {
                if (j <= colsNeeded) {
                    if (j < allRoles.size()) {
                        roleObjectMatrix[0][j] = allRoles.get(j - 1);
                    }
                    roleObjectMatrix[0][j + allRoles.size() - 1] =
                            allObjects.get(j - 1);
                    j++;
                } else {
                    break;
                }
            }
        }
        printRoleObjectMatrix(roleObjectMatrix);
    }


    protected void printRoleObjectMatrix(String[][] roleObjectMatrix) {
        int hMatrix = roleObjectMatrix.length;
        int wMatrix = roleObjectMatrix[0].length;
        p("Height: " + hMatrix);
        p("Width: " + wMatrix);
    }

    /**
     * Method to avoid using System.out for every call to print to console.
     *
     * @param item The token that was found
     */
    protected static void p(Object item) {
        System.out.println(item);
    }
}