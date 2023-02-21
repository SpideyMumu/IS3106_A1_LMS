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
import javax.ejb.EJB;import javax.persistence.PersistenceException;
import util.exception.BookNotFoundException;
import util.exception.LendingNotFoundException;
import util.exception.MemberNotFoundException;
import util.exception.UnknownPersistenceException;
/**
 *
 * @author muhdm
 */
@Stateless
public class LendAndReturnLMSSessionBean implements LendAndReturnLMSSessionBeanRemote, LendAndReturnLMSSessionBeanLocal {

    @EJB
    private MemberLMSSessionBeanLocal memberLMSSessionBean;

    @EJB
    private BookLMSSessionBeanLocal bookLMSSessionBean;

    @PersistenceContext(unitName = "LMS-ejbPU")
    private EntityManager em;

    //Lending books
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
    
    
    //Returning Books
    @Override
    public Long returnBook(Long lrId) throws LendingNotFoundException {
        LendAndReturn returningEntity = this.retrieveLendById(lrId);
        
        Date dateReturned = new Date();
        returningEntity.setReturnDate(dateReturned);
        
        //Convert Date objects to Calendar
        Calendar calReturned = Calendar.getInstance();
        calReturned.setTime(dateReturned);
        
        Calendar deadline = Calendar.getInstance();
        deadline.setTime(returningEntity.getReturnDate());
        deadline.add(Calendar.DATE, 14);
        
        //Calculate fine amount
        if (calReturned.after(deadline)) {
            //add fine amount here
            long daysBetween = ChronoUnit.DAYS.between(deadline.toInstant(), calReturned.toInstant());
            BigDecimal fineAmount = BigDecimal.valueOf(0.5).multiply(BigDecimal.valueOf(daysBetween));
            returningEntity.setFineAmount(fineAmount);
        }
        
        returningEntity.getBook().setAvailable(true);
        
        em.merge(returningEntity);
        return returningEntity.getLendId();
    }
    
}
