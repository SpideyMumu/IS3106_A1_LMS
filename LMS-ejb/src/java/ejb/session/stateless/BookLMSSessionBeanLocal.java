/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Book;
import java.util.List;
import javax.ejb.Local;
import util.exception.BookNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

/**
 *
 * @author muhdm
 */
@Local
public interface BookLMSSessionBeanLocal {

    public Long createNewBook(Book newBook) throws UsernameExistException, UnknownPersistenceException;

    public Book retrieveBookById(Long bookId) throws BookNotFoundException;

    public List<Book> retrieveAllBooks();

    public List<Book> retrievAllAvailBooks();
}
