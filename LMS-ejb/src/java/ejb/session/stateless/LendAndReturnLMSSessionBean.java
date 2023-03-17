/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Book;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import entity.LendAndReturn;
/**
 *
 * @author muhdm
 */
import entity.Member;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.BookNotFoundException;
import util.exception.LendingNotFoundException;
import util.exception.MemberNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author muhdm
 */
@Stateless
public class LendAndReturnLMSSessionBean implements LendAndReturnLMSSessionBeanLocal {

    @EJB
    private MemberLMSSessionBeanLocal memberLMSSessionBean;

    @EJB
    private BookLMSSessionBeanLocal bookLMSSessionBean;

    @PersistenceContext(unitName = "LMS-ejbPU")
    private EntityManager em;

    //Lending books
    @Override
    public Long createNewLendAndReturn(LendAndReturn newLR, Long memberId, Long bookId) throws UnknownPersistenceException {
        try {
            Book book = bookLMSSessionBean.retrieveBookById(bookId);
            Member member = memberLMSSessionBean.retrieveMemberById(memberId);

            newLR.setBook(book);
            newLR.setMember(member);

            member.addLendAndReturns(newLR);
            book.addLendAndReturns(newLR);
            book.setAvailable(false);

            em.persist(newLR);
            em.flush();

            return newLR.getLendId();
        } catch (PersistenceException | BookNotFoundException | MemberNotFoundException ex) {
            throw new UnknownPersistenceException(ex.getMessage());
        }
    }

    @Override
    public LendAndReturn retrieveLendById(Long lendId) throws LendingNotFoundException {
        LendAndReturn lendAndReturn = em.find(LendAndReturn.class, lendId);
        if (lendAndReturn != null) {
            return lendAndReturn;
        } else {
            throw new LendingNotFoundException("Lend with ID " + lendId + " does not exist!");
        }
    }

    @Override
    public List<LendAndReturn> retrieveAllLendAndReturns() {
        Query query = em.createQuery("SELECT lr FROM LendAndReturn lr");
        return query.getResultList();
    }

    @Override
    public List<LendAndReturn> retrieveUnreturnedLends() {
        Query query = em.createQuery("SELECT lr FROM LendAndReturn lr WHERE lr.returnDate IS NULL");
        return query.getResultList();
    }

    
    /*
    Check fine amount for lend and return (if any)
        - set fine amount
        - return fine amount
    */
    @Override
    public BigDecimal checkFineAmount(Long lrId) throws LendingNotFoundException {
        LendAndReturn returningEntity = this.retrieveLendById(lrId);
        Date dateReturned = new Date();
        //returningEntity.setReturnDate(dateReturned);

        //Convert Date objects to Calendar
        Calendar calReturned = Calendar.getInstance();
        calReturned.setTime(dateReturned);

        Calendar deadline = Calendar.getInstance();
        deadline.setTime(returningEntity.getLendDate());
        deadline.add(Calendar.DATE, 14);

        BigDecimal fineAmount = BigDecimal.ZERO;

        //Calculate fine amount
        if (calReturned.after(deadline)) {
            //add fine amount here
            long daysBetween = ChronoUnit.DAYS.between(deadline.toInstant(), calReturned.toInstant());
            fineAmount = BigDecimal.valueOf(0.5).multiply(BigDecimal.valueOf(daysBetween));
            returningEntity.setFineAmount(fineAmount);
        }

        return fineAmount;
    }

    /*
    Return Books after paying fine (if any)
        - Set Return Date
        - Set availability
        - Update in DB
    */
    @Override
    public Long returnBook(Long lrId) throws LendingNotFoundException {
        LendAndReturn returningEntity = this.retrieveLendById(lrId);

        Date dateReturned = new Date();
        returningEntity.setReturnDate(dateReturned);
        returningEntity.getBook().setAvailable(true);

        em.merge(returningEntity);
        return returningEntity.getLendId();
    }

}
