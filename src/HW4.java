import java.io.File;
import java.util.*;

/**
 * Created by matthewmcguire on 10/25/14.
 */
public class HW4 {

    /**
     * Problem 2 on the Homework Assignment.
     *
     * @param file
     * @return -1 for valid file, any other int for the line number of the file
     * with the error.
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
            for (String ascendant : roleMap.get(descendant)) {
                subRoles = subRoles + ascendant + ", ";
            }
            //Remove last comma before printing
            subRoles = subRoles.substring(0, subRoles.length() - 2);
            HW4.p(descendant + " --> " + subRoles);
        }
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
