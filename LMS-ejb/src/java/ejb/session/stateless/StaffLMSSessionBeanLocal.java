package ejb.session.stateless;

import entity.Staff;
import javax.ejb.Local;
import util.exception.StaffNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

/**
 *
 * @author muhdm
 */
@Local
public interface StaffLMSSessionBeanLocal {

    Long createNewStaff(Staff newStaff) throws UsernameExistException, UnknownPersistenceException;

    public Staff retrieveStaffByUsername(String username) throws StaffNotFoundException;

}
