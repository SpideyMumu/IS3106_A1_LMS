package ejb.session.stateless;

import entity.Staff;
import javax.ejb.Remote;
import util.exception.StaffNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

/**
 *
 * @author muhdm
 */
@Remote
public interface StaffLMSSessionBeanRemote {

    Long createNewStaff(Staff newStaff) throws UsernameExistException, UnknownPersistenceException;

    public Staff retrieveStaffByUsername(String username) throws StaffNotFoundException;

}
