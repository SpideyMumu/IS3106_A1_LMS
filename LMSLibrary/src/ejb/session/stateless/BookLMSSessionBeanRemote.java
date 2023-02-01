/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Book;
import javax.ejb.Remote;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

/**
 *
 * @author muhdm
 */
@Remote
public interface BookLMSSessionBeanRemote {

    public Long createNewBook(Book newBook) throws UsernameExistException, UnknownPersistenceException;
}
