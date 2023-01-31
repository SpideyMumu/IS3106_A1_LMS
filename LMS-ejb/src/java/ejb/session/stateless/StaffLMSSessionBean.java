package ejb.session.stateless;

import entity.Staff;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

/**
 *
 * @author muhdm
 */
@Stateless
public class StaffLMSSessionBean implements StaffLMSSessionBeanRemote, StaffLMSSessionBeanLocal {

    @PersistenceContext(unitName = "LMS-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewStaff(Staff newStaff) throws UsernameExistException, UnknownPersistenceException {
        try {
            em.persist(newStaff);
            em.flush();
            return newStaff.getStaffId();
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new UsernameExistException();
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

}
