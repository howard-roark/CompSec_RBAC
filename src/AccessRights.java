/**
 * Created by matthewmcguire on 11/1/14.
 */
public enum AccessRights {
    OWNER("owner"),
    CONTROL("control"),
    CONTROL_WITH_COPY("control*"),
    WRITE("write"),
    WRITE_WITH_COPY("write*"),
    READ("read"),
    READ_WITH_COPY("read*"),
    EXECUTE("execute"),
    EXECUTE_WITH_COPY("execute*"),
    START("start"),
    START_WITH_COPY("start*"),
    STOP("stop"),
    STOP_WITH_COPY("stop*"),
    WAKEUP("wakeup"),
    WAKEUP_WITH_COPY("wakeup*"),
    SEEK("seek"),
    SEEK_WITH_COPY("seek*");

    /* Instance variable to construct each instance of the AccessRights Enum */
    private String accessRight;

    /**
     * Construct each AccessRights Enum.
     *
     * @param accessRight
     */
    private AccessRights(String accessRight) {
        this.accessRight = accessRight;
    }

    /**
     * Return actual value of Enum instance if needed from outside.
     *
     * @return
     */
    public String getAccessRightValue() {
        return this.accessRight;
    }

    /**
     * Logic to compare if a particular right already exists in the matrix.
     *
     * @param rightFromMatrix
     * @return
     */
    protected static boolean compareAccessRights(String rightFromMatrix) {
        boolean matches = false;
        for (AccessRights right : AccessRights.values()) {
            if (rightFromMatrix.equals(right.accessRight)) {
                matches = true;
                break;
            }
        }
        return matches;
    }
}
