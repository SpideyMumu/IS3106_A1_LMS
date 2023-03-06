/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import ejb.session.stateless.BookLMSSessionBeanLocal;
import ejb.session.stateless.LendAndReturnLMSSessionBeanLocal;
import ejb.session.stateless.MemberLMSSessionBeanLocal;
import entity.Book;
import entity.LendAndReturn;
import entity.Member;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;
import org.primefaces.util.LangUtils;
import util.exception.MemberNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author muhdm
 */
@Named(value = "bookManagedBean")
@ViewScoped
public class BookManagedBean implements Serializable {

    @EJB
    private LendAndReturnLMSSessionBeanLocal lendAndReturnLMSSessionBean;

    @EJB
    private MemberLMSSessionBeanLocal memberLMSSessionBean;

    @EJB
    private BookLMSSessionBeanLocal bookLMSSessionBean;

    private List<Book> books;

    private List<Book> availBooks;

    private List<Book> filteredBooks;

    private Book selectedBook;

    private String memberToLendTypedID;

    private Member memberToLend;

    public BookManagedBean() {
    }

    @PostConstruct
    public void init() {
        books = bookLMSSessionBean.retrieveAllBooks();
        availBooks = bookLMSSessionBean.retrievAllAvailBooks();
    }

    // method to open dialog for lending book form
    public void lendBookDialog() {
        //System.out.println("lend book method called");
        if (selectedBook == null) {
            //System.out.println("no book selected");
            FacesContext.getCurrentInstance().addMessage("tableForm:messages",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please select a book"));
        } else {
            System.out.println("book selected is " + selectedBook.getTitle());
            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('dlg').show();");
            FacesContext.getCurrentInstance().addMessage("lendForm:dlgMessages",
                    new FacesMessage(null,
                            "You have selected '" + selectedBook.getTitle() + "'. Please proceed to select a member to lend to."));
        }
    }

    // Confirm lending book after selecting member
    public void lendBook() {
        try {
            memberToLend = memberLMSSessionBean.retrieveMemberByIdentityNum(memberToLendTypedID);
            FacesContext.getCurrentInstance().addMessage("lendForm:dlgMessages",
                    new FacesMessage(null,
                            "You have selected '" + selectedBook.getTitle() + "' that will be lent to " + memberToLend.getFirstName() + " " + memberToLend.getLastName()));
            LendAndReturn newLend = new LendAndReturn();
            newLend.setLendDate(new Date());
            try {
                lendAndReturnLMSSessionBean.createNewLendAndReturn(newLend, memberToLend.getMemberId(), selectedBook.getBookId());
                //open another dialog maybe then that dialog redirects to refreshed paged
                PrimeFaces current = PrimeFaces.current();
                current.executeScript("PF('dlg').hide();");
                current.executeScript("PF('dlgSuccess').show();");
            } catch (UnknownPersistenceException ex) {
                FacesContext.getCurrentInstance().addMessage("lendForm:dlgMessages",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", ex.getMessage()));

            }

        } catch (MemberNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage("lendForm:dlgMessages",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", ex.getMessage()));

        }
    }

    public Member selectMember() {
        try {
            return memberToLend = memberLMSSessionBean.retrieveMemberByIdentityNum(memberToLendTypedID);
        } catch (MemberNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage("lendForm:dlgMessages",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", ex.getMessage()));
            return null;
        }
    }

    public String refresh() {
        return "lendBooks.xhtml?faces-redirect=true";
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

    public Book getSelectedBook() {
        return selectedBook;
    }

    public void setSelectedBook(Book selectedBook) {
        this.selectedBook = selectedBook;
    }

    public Member getMemberToLend() {
        return memberToLend;
    }

    public void setMemberToLend(Member memberToLend) {
        this.memberToLend = memberToLend;
    }

    public String getMemberToLendTypedID() {
        return memberToLendTypedID;
    }

    public void setMemberToLendTypedID(String memberToLendTypedID) {
        this.memberToLendTypedID = memberToLendTypedID;
    }

}
