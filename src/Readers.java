import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by matthewmcguire on 10/25/14.
 */
public class Readers {

    /**
     * Read roleHierarchy file.
     *
     * @param f roleHierarchy file
     * @return the String [] of all roles in file
     * @throws Exception thrown if array made from line in file is not proper
     *                   length
     */
    protected static Stack<String> readRoleHierarchyFile(File f) {
        Stack<String> lines = new Stack<String>();
        try {
            FileInputStream fileInputStream = new FileInputStream(f);
            BufferedReader bufferedReader = new BufferedReader(new
                    InputStreamReader(fileInputStream));
            String line = "";
            int lineNum = 1;
            while ((line = bufferedReader.readLine()) != null) {
                if (matches("^[Rr][0-9]+{1}[\\s]+[Rr][0-9]+{1}", line)) {
                    /* Split the line into two roles so that they can be added
                     * to the returned array as single elements */
                    String[] twoRoles = line.split("\\s+");
                    if (twoRoles.length == 2) {
                        lines.push(twoRoles[0].trim());
                        lines.push(twoRoles[1].trim());
                    } else {
                        /* Should not be thrown as format of file was checked in
                         * matches method. */
                        System.err.println("<twoRoles> array improperly built");
                        System.exit(1);
                    }
                } else {
                    fileInputStream.close();
                    bufferedReader.close();
                    promptUserFixLine(lineNum, f);
                }
                lineNum++;
            }
            /*Close the stream and reader */
            fileInputStream.close();
            bufferedReader.close();
        } catch (IOException ioe) {
            System.err.println("Problem Reading File: " + ioe);
        }
        return lines;
    }

    /**
     * Prompt the user to fix the file and then re-run the file through the
     * readFile method.
     *
     * @param lineNum with error in file
     * @param file file being read from
     * @throws Exception for calling readFile when twoRoles array is mal-formed
     */
    private static void promptUserFixLine(int lineNum, File file) {
        System.out.println("There was an error with line number " + lineNum +
                " in the " + file.getName() + "file.  " +
                "Please fix your file and Press any key to continue...");
        try {
            BufferedReader br = new BufferedReader(new
                    InputStreamReader(System.in));
            if (br.readLine() != null) {
                if (file.getName().equals("roleHierarchy.txt")) {
                    Readers.readRoleHierarchyFile(file);
                } else if (file.getName().equals("permissionsToRoles.txt")) {
                    Readers.readPermissionsFile(file);
                }
            }
            br.close();
        } catch (IOException ioe) {
            HW4.p("Error reading input from user: " + ioe);
        }
    }

    /**
     * Build resource object map.
     *
     * @param file
     * @return
     */
    protected static Map<String, Set<String>> readResourceObjectsFile(File file) {
        //Build empty map to fill in logic below
        Map<String, Set<String>> objectsMap =
                new HashMap<String, Set<String>>();
        String f = "File", p = "Process", d = "Disk";
        objectsMap.put(f, new LinkedHashSet<String>());
        objectsMap.put(p, new LinkedHashSet<String>());
        objectsMap.put(d, new LinkedHashSet<String>());

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new
                    InputStreamReader(fileInputStream));

            String line = bufferedReader.readLine();
            if (line != null) {
                while (line.length() >= 2) {
                    String object =
                            getTextByPattern("^[\\s]*([FPD]{1}[0-9]+).*", line);
                    String key = "";
                    char firstChar = Character.toUpperCase(object.charAt(0));
                    switch (firstChar) {
                        case 'F':
                            key = f;
                            break;
                        case 'P':
                            key = p;
                            break;
                        case 'D':
                            key = d;
                            break;
                        default:
                            System.err.println("Unknown Object Found in File");
                            System.exit(1);
                    }
                    Set<String> objectsSet = objectsMap.get(key);
                    objectsSet.add(object);
                    objectsMap.replace(key, objectsSet);
                    if (line.length() > 2) {
                        line = line.substring(2, line.length());
                    } else {
                        break;
                    }

                }
            }
            /*Close the stream and reader */
            fileInputStream.close();
            bufferedReader.close();
        } catch (IOException ioe) {
            System.err.println("Error reading resourceObjects.txt: " + ioe);
            System.exit(1);
        }
        return objectsMap;
    }

    /**
     * Build map to place permissions into access matrix being built in HW4.
     *
     * @param file
     * @return
     */
    protected static Map<String, Map<String, List<String>>> readPermissionsFile(
            File file) {
        Map<String, Map<String, List<String>>> permissionMap =
                new HashMap<String, Map<String, List<String>>>();
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new
                    InputStreamReader(fileInputStream));

            String line;
            int lineNum = 1;
            Map<String, List<String>> objectsRights;
            List<String> rights;
            while ((line = bufferedReader.readLine()) != null) {
                String role = "", right = "", object = "";
                if (matches("^[Rr][0-9]+[\\s]+[a-z*]+[\\s]+[FPD][0-9]+",
                        line)) {
                    String[] lineByParts = line.split("\\s+");
                    if (lineByParts.length == 3) {
                        role = lineByParts[0];
                        right = lineByParts[1];
                        object = lineByParts[2];

                        if (permissionMap.containsKey(role)) {
                            objectsRights =
                                    permissionMap.get(role);
                            if (objectsRights.containsKey(object)) {
                                rights = objectsRights.get(object);
                            } else {//Add new object as key for the rights list
                                rights = new ArrayList<String>();
                                rights.add(right);
                            }
                            rights.add(right);
                            objectsRights.put(object, rights);
                        } else {//New role being added to map
                            rights = new ArrayList<String>();
                            rights.add(right);
                            objectsRights = new HashMap<String, List<String>>();
                            objectsRights.put(object, rights);
                            permissionMap.put(role, objectsRights);
                        }
                    } else {
                        promptUserFixLine(lineNum, file);
                    }
                } else {
                    promptUserFixLine(lineNum, file);
                }
                lineNum++;
            }
            /*Close the stream and reader */
            fileInputStream.close();
            bufferedReader.close();
        } catch (IOException ioe) {
            System.err.println("Error reading permissions file: " + ioe);
            System.exit(1);
        }
        return permissionMap;
    }

    protected static Map<String, List<String>> readRoleSets(File file) {
        Map<String, List<String>> roleSets = new HashMap<String, List<String>>();
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new
                    InputStreamReader(fileInputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineByParts = line.split("\\s+");
                List<String> roles = new ArrayList<String>();
                for (int i = 1; i < lineByParts.length; i++) {
                    roles.add(lineByParts[i]);
                }
                if (roleSets.containsKey(lineByParts[0])) {
                    lineByParts[0] = lineByParts[0] + "A";
                }
                roleSets.put(lineByParts[0], roles);
            }
            /*Close the stream and reader */
            fileInputStream.close();
            bufferedReader.close();
        } catch (IOException ioe) {
            System.err.println("Problem reading roleSetsSSD file" + ioe);
            System.exit(1);
        }
        return roleSets;
    }

    /**
     * Will determine if the line in the file is valid to be used in the HW4
     * class.
     *
     * @param regex Regular expression to determine validity of string
     * @param line  Line from file being passed in
     * @return true if line is valid, false otherwise
     */
    protected static boolean matches(String regex, String line) {
        Pattern p = Pattern.compile(regex);
        return p.matches(regex, line);
    }

    /** Return a string based on the regex and string passed in.
     *
     * @param regex
     * @param string
     * @return
     */
    protected static String getTextByPattern(String regex, String string) {
        String found = "";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(string);
        if ((m.find()) && (m.groupCount() > 0)) {
            found = m.group(1);
        }
        return found;
    }
}