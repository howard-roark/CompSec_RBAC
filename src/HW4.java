import java.io.File;

/**
 * Created by matthewmcguire on 10/25/14.
 */
public class HW4 extends Tree<String> {

    /**
     * Problem 2 on the Homework Assignment.
     *
     * @param file
     * @return -1 for valid file, any other int for the line number of the file
     * with the error.
     */
    public int readRoleHierarchy(File file) {
        int lineNum = -1;
        return lineNum;
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
