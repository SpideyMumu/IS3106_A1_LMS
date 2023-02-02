/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Book;
import entity.LendAndReturn;
import javax.ejb.Remote;
import util.exception.BookNotFoundException;
import util.exception.LendingNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author muhdm
 */
@Remote
public interface LendAndReturnLMSSessionBeanRemote {

    public Long createNewLendAndReturn(LendAndReturn newLR, Long memberId, Long bookId) throws UnknownPersistenceException;

    public LendAndReturn retrieveLendById(Long lendId) throws LendingNotFoundException;
}
