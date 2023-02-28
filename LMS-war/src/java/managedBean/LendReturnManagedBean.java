/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import ejb.session.stateless.LendAndReturnLMSSessionBeanLocal;
import entity.Book;
import entity.LendAndReturn;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.util.LangUtils;
import util.exception.LendingNotFoundException;

/**
 *
 * @author muhdm
 */
@Named(value = "lendReturnManagedBean")
@ViewScoped
public class LendReturnManagedBean implements Serializable {

    @EJB
    private LendAndReturnLMSSessionBeanLocal lendAndReturnLMSSessionBean;

    private List<LendAndReturn> lendAndReturns;

    private List<LendAndReturn> filteredLendAndReturns;

    private LendAndReturn selectedLendAndReturn;

    public LendReturnManagedBean() {
    }

    @PostConstruct
    public void init() {
        lendAndReturns = lendAndReturnLMSSessionBean.retrieveUnreturnedLends();
    }

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (LangUtils.isValueBlank(filterText)) {
            return true;
        }

        System.out.println("Printing FILTER TEXT: " + filterText);
        long filterLong;
        try {
            filterLong = Long.parseLong(filterText);
        } catch (NumberFormatException ex) {
            filterLong = -1;
        }
        LendAndReturn lendAndReturn = (LendAndReturn) value;
        return lendAndReturn.getLendDate().toString().toLowerCase().contains(filterText)
                || lendAndReturn.getBook().toString().toLowerCase().contains(filterText)
                || lendAndReturn.getMember().toString().toLowerCase().contains(filterText)
                || lendAndReturn.getLendId() == filterLong;
    }

    public void returnBook() {
        try {
            lendAndReturnLMSSessionBean.returnBook(selectedLendAndReturn.getLendId());
            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('dlgSuccess').show();");
        } catch (LendingNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage("lendForm:dlgMessages",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", ex.getMessage()));
        }
    }

    public String refresh() {
        return "viewAllBooks.xhtml?faces-redirect=true";
    }

    public LendAndReturnLMSSessionBeanLocal getLendAndReturnLMSSessionBean() {
        return lendAndReturnLMSSessionBean;
    }

    public void setLendAndReturnLMSSessionBean(LendAndReturnLMSSessionBeanLocal lendAndReturnLMSSessionBean) {
        this.lendAndReturnLMSSessionBean = lendAndReturnLMSSessionBean;
    }

    public List<LendAndReturn> getLendAndReturns() {
        return lendAndReturns;
    }

    public void setLendAndReturns(List<LendAndReturn> lendAndReturns) {
        this.lendAndReturns = lendAndReturns;
    }

    public List<LendAndReturn> getFilteredLendAndReturns() {
        return filteredLendAndReturns;
    }

    public void setFilteredLendAndReturns(List<LendAndReturn> filteredLendAndReturns) {
        this.filteredLendAndReturns = filteredLendAndReturns;
    }

    public LendAndReturn getSelectedLendAndReturn() {
        return selectedLendAndReturn;
    }

    public void setSelectedLendAndReturn(LendAndReturn selectedLendAndReturn) {
        this.selectedLendAndReturn = selectedLendAndReturn;
    }

}
