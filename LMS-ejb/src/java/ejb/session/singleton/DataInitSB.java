/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.BookLMSSessionBeanLocal;
import ejb.session.stateless.LendAndReturnLMSSessionBeanLocal;
import ejb.session.stateless.MemberLMSSessionBeanLocal;
import ejb.session.stateless.StaffLMSSessionBeanLocal;
import entity.Book;
import entity.LendAndReturn;
import entity.Member;
import entity.Staff;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.BookNotFoundException;
import util.exception.MemberNotFoundException;
import util.exception.StaffNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

/**
 *
 * @author muhdm
 */
@Singleton
@LocalBean
@Startup
public class DataInitSB {

    @EJB
    private StaffLMSSessionBeanLocal staffLMSSessionBean;

    @EJB
    private MemberLMSSessionBeanLocal memberLMSSessionBean;

    @EJB
    private LendAndReturnLMSSessionBeanLocal lendAndReturnLMSSessionBean;

    @EJB
    private BookLMSSessionBeanLocal bookLMSSessionBean;
    
    @PersistenceContext(unitName = "LMS-ejbPU")
    private EntityManager em;
    
    
    public DataInitSB() {
    }

    @PostConstruct
    public void postConstruct() {
        try {
            staffLMSSessionBean.retrieveStaffByUsername("eric");
        } catch (StaffNotFoundException ex) {
            try {
                initializeData();
            } catch (UsernameExistException | UnknownPersistenceException ex2) {
                ex.printStackTrace();
            }
        }
    }

    private void initializeData() throws
            UsernameExistException, UnknownPersistenceException {
        // create staff here
        Staff eric = new Staff("Eric", "Some", "eric", "password");
        Staff sarah = new Staff("Sarah", "Brightman", "sarah", "password");
        Staff mursyid = new Staff("Muhd", "Mursyid", "mursyid", "password");

        Long ericId = staffLMSSessionBean.createNewStaff(eric);
        Long sarahId = staffLMSSessionBean.createNewStaff(sarah);
        Long mursyidId = staffLMSSessionBean.createNewStaff(mursyid);

        // create books here
        /*
        1 Anna Karenina 0451528611 Leo Tolstoy
        2 Madame Bovary 979-8649042031 Gustave Flaubert
        3 Hamlet 1980625026 William Shakespeare
        4 The Hobbit 9780007458424 J R R Tolkien
        5 Great Expectations 1521853592 Charles Dickens
        6 Pride and Prejudice 979-8653642272 Jane Austen
        7 Wuthering Heights 3961300224 Emily Brontë
         */
        Book book1 = new Book(1, "Anna Karenina", "0451528611", "Leo Tolstoy");
        Book book2 = new Book(2, "Madame Bovary", "979-8649042031", "Gustave Flaubert");
        Book book3 = new Book(3, "Hamlet", "1980625026", "William Shakespeare");
        Book book4 = new Book(4, "The Hobbit", "9780007458424", "J R R Tolkien");
        Book book5 = new Book(5, "Great Expectations", "1521853592", "Charles Dickens");
        Book book6 = new Book(6, "Pride and Prejudice", "979-8653642272", "Jane Austen");
        Book book7 = new Book(7, "Wuthering Heights", "3961300224", "Emily Brontë");

        bookLMSSessionBean.createNewBook(book1);
        bookLMSSessionBean.createNewBook(book2);
        bookLMSSessionBean.createNewBook(book3);
        bookLMSSessionBean.createNewBook(book4);
        bookLMSSessionBean.createNewBook(book5);
        bookLMSSessionBean.createNewBook(book6);
        bookLMSSessionBean.createNewBook(book7);

        // create members here
        /*
        1 Tony Shade M 31 S8900678A 83722773 13 Jurong East, Ave 3
        2 Dewi Tan F 35 S8581028X 94602711 15 Computing Dr
         */
        Member tony = new Member(1, "Tony", "Shade", 'M', 31, "S8900678A", "83722773", "13 Jurong East, Ave 3");
        Member dewi = new Member(2, "Dewi", "Tan", 'F', 35, "S8581028X", "94602711", "15 Computing Dr");

        memberLMSSessionBean.createNewMember(dewi);
        memberLMSSessionBean.createNewMember(tony);

        // Create dummy lend for testing fine amount
        if (em.find(LendAndReturn.class, 1l) == null) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date lendDate = dateFormat.parse("2023-02-07 03:59");
                try {
                    LendAndReturn dummyLR = new LendAndReturn();
                    dummyLR.setLendDate(lendDate);
                    lendAndReturnLMSSessionBean.createNewLendAndReturn(dummyLR, 1l, 1l);
                } catch (PersistenceException | UnknownPersistenceException ex) {
                    Logger.getLogger(DataInitSB.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (ParseException ex) {
                Logger.getLogger(DataInitSB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
