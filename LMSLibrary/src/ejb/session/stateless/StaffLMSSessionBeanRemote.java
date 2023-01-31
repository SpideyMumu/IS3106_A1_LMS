package ejb.session.stateless;

import entity.Staff;
import javax.ejb.Remote;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

/**
 *
 * @author muhdm
 */
@Remote
public interface StaffLMSSessionBeanRemote {

    Long createNewStaff(Staff newStaff) throws UsernameExistException, UnknownPersistenceException;

}
