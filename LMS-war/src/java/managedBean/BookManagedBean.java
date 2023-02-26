/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import ejb.session.stateless.BookLMSSessionBeanLocal;
import entity.Book;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.Locale;
import org.primefaces.util.LangUtils;

/**
 *
 * @author muhdm
 */
@Named(value = "bookManagedBean")
@ViewScoped
public class BookManagedBean implements Serializable {

    @EJB
    private BookLMSSessionBeanLocal bookLMSSessionBean;

    private List<Book> books;
    
    private List<Book> availBooks;
    
    private List<Book> filteredBooks;

    public BookManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        books = bookLMSSessionBean.retrieveAllBooks();
        availBooks = bookLMSSessionBean.retrievAllAvailBooks();
    }
    
    public void lendBook() {
        
    }
    
    // For data table global search
    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (LangUtils.isValueBlank(filterText)) {
            return true;
        }
        
        //System.out.println("Printing FILTER TEXT: "+ filterText);
        long filterLong;
        try {
            filterLong = Long.parseLong(filterText);
        } catch (NumberFormatException ex) {
            filterLong = -1;
        }
        Book book = (Book) value;
        return book.getTitle().toLowerCase().contains(filterText)
                || book.getAuthor().toLowerCase().contains(filterText)
                || book.getIsbn().contains(filterText)
                || book.getBookId() == filterLong;
    }
    

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public List<Book> getAvailBooks() {
        return availBooks;
    }

    public void setAvailBooks(List<Book> availBooks) {
        this.availBooks = availBooks;
    }

    public List<Book> getFilteredBooks() {
        return filteredBooks;
    }

    public void setFilteredBooks(List<Book> filteredBooks) {
        this.filteredBooks = filteredBooks;
    }
    
    
    
}
