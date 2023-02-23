/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Book;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.BookNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

/**
 *
 * @author muhdm
 */
@Stateless
public class BookLMSSessionBean implements BookLMSSessionBeanLocal {

    @PersistenceContext(unitName = "LMS-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewBook(Book newBook) throws UsernameExistException, UnknownPersistenceException {
        try {
            em.persist(newBook);
            em.flush();
            return newBook.getBookId();
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

    @Override
    public Book retrieveBookById(Long bookId) throws BookNotFoundException {
        Book book = em.find(Book.class, bookId);
        if (book != null) {
            return book;
        } else {
            throw new BookNotFoundException("Book with ID " + bookId + " does not exist!");
        }
    }
    
    
    @Override
    public List<Book> retrieveAllBooks() {
        Query query = em.createQuery("SELECT b FROM Book b");
        return query.getResultList();
    }
    
    @Override
    public List<Book> retrievAllAvailBooks() {
        Query query = em.createQuery("SELECT b FROM Book b WHERE b.available = TRUE");
        return query.getResultList();
    }
    
}
