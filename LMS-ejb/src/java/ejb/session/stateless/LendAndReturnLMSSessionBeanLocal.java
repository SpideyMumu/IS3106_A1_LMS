/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Book;
import entity.LendAndReturn;
import java.util.List;
import javax.ejb.Local;
import util.exception.BookNotFoundException;
import util.exception.LendingNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author muhdm
 */
@Local
public interface LendAndReturnLMSSessionBeanLocal {

    public Long createNewLendAndReturn(LendAndReturn newLR, Long memberId, Long bookId) throws UnknownPersistenceException;

    public LendAndReturn retrieveLendById(Long lendId) throws LendingNotFoundException;
    
    public Long returnBook(Long lrId) throws LendingNotFoundException;

    public List<LendAndReturn> retrieveAllLendAndReturns();

    public List<LendAndReturn> retrieveUnreturnedLends();
}
