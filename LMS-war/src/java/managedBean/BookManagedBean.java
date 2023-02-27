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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;
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

    private Book selectedBook;

    public BookManagedBean() {
    }

    @PostConstruct
    public void init() {
        books = bookLMSSessionBean.retrieveAllBooks();
        availBooks = bookLMSSessionBean.retrievAllAvailBooks();
    }

    public void lendBook() {
        //System.out.println("lend book method called");
        if (selectedBook == null) {
            //System.out.println("no book selected");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please select a book"));
        } else {
            System.out.println("book selected is " + selectedBook.getTitle());
            //FacesContext.getCurrentInstance().addMessage(null,
            //        new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "You have selected " + selectedBook.getTitle()));

            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('dlg').show();");
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, null, 
                        "You have selected '" + selectedBook.getTitle() + "'. Please proceed to select a member to lend to."));
        }
    }

    /*
    public void test() {
        System.out.println("book selected is " + selectedBook.getTitle());
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", selectedBook.getTitle()));
    }
    */

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

    public Book getSelectedBook() {
        return selectedBook;
    }

    public void setSelectedBook(Book selectedBook) {
        this.selectedBook = selectedBook;
    }

}
