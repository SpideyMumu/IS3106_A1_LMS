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

    public Long createNewLendAndReturn(LendAndReturn newLR, Long memberId, Long bookId) throws UnknownPersistenceException {
        try {
            Book book = bookLMSSessionBean.retrieveBookById(bookId);
            Member member = memberLMSSessionBean.retrieveMemberById(memberId);
            
            newLR.setBook(book);
            newLR.setMember(member);
            
            member.addLendAndReturns(newLR);
            book.addLendAndReturns(newLR);
            
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

    
}
