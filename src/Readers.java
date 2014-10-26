import java.io.*;
import java.util.ArrayList;
import java.util.List;
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
    public static List<String> readFile(File f) throws Exception {
        List<String> lines = new ArrayList<String>();
        try {
            FileInputStream fileInputStream = new FileInputStream(f);
            BufferedReader bufferedReader = new BufferedReader(new
                    InputStreamReader(fileInputStream));
            String line = "";
            int lineNum = 0;
            while ((line = bufferedReader.readLine()) != null) {
                if (matches("^[Rr][0-9]+{1}[\\s]+[Rr][0-9]+{1}", line)) {
                    /* Split the line into two roles so that they can be added
                     * to the returned array as single elements */
                    String[] twoRoles = line.split("\\s+");
                    if (twoRoles.length == 2) {
                        lines.add(twoRoles[0].trim());
                        lines.add(twoRoles[1].trim());
                    } else {
                        /* Should not be thrown as format of file was checked in
                         * matches method. */
                        throw new Exception("Array of Roles not formatted " +
                                "properly");
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

    protected static void promptUserFixLine(int lineNum, File file)
            throws Exception {
        System.out.println("There was an error with line number " + lineNum +
                ".  Please fix your file and Press any key to continue...");
        try {
            BufferedReader br = new BufferedReader(new
                    InputStreamReader(System.in));
            if (br.readLine() != null) {
                Readers.readFile(file);
            }
        } catch (IOException ioe) {
            HW4.p("Error reading input from user: " + ioe);
        }
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
}
