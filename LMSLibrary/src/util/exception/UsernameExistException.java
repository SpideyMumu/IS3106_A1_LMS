package util.exception;

/**
 *
 * @author muhdm
 */
public class UsernameExistException extends Exception{

    /**
     * Creates a new instance of <code>UserNameExistsException</code> without
     * detail message.
     */
    public UsernameExistException() {
    }

    /**
     * Constructs an instance of <code>UserNameExistsException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UsernameExistException(String msg) {
        super(msg);
    }
}
