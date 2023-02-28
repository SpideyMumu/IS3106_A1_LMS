package ejb.session.stateless;

import entity.Member;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.MemberNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

/**
 *
 * @author muhdm
 */
@Stateless
public class MemberLMSSessionBean implements MemberLMSSessionBeanLocal {

    @PersistenceContext(unitName = "LMS-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewMember(Member newMember) throws UsernameExistException, UnknownPersistenceException {
        try {
            em.persist(newMember);
            em.flush();
            return newMember.getMemberId();
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new UsernameExistException("Member of Identity Number " + newMember.getIdentityNo() + " exists! Please try again.");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    @Override
    public Member retrieveMemberById(Long memberId) throws MemberNotFoundException {
        Member member = em.find(Member.class, memberId);
        if (member != null) {
            return member;
        } else {
            throw new MemberNotFoundException("Member with ID " + memberId + " does not exist!");
        }
    }

    @Override
    public Member retrieveMemberByIdentityNum(String idNo) throws MemberNotFoundException {
        Query query = em.createQuery("SELECT m FROM Member m WHERE m.identityNo = :idNo");
        query.setParameter("idNo", idNo);

        try {
            return (Member) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new MemberNotFoundException("Member with ID " + idNo + " does not exist!");
        }
    }

    @Override
    public List<Member> retrieveAllMembers() {
        Query query = em.createQuery("SELECT m FROM Member m");
        return query.getResultList();
    }

}
