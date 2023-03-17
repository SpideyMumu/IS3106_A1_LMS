/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import ejb.session.stateless.MemberLMSSessionBeanLocal;
import entity.Member;
import java.io.Serializable;
import static java.lang.Integer.getInteger;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.primefaces.util.LangUtils;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

/**
 *
 * @author muhdm
 */
@Named(value = "memberManagedBean")
@RequestScoped
public class MemberManagedBean {

    @EJB
    private MemberLMSSessionBeanLocal memberLMSSessionBean;

    private Member selectedMember;
    private List<Member> members;

    private List<Member> filteredMembers;

    private Member newMember;

    public MemberManagedBean() {
    }

    @PostConstruct
    public void init() {
        members = memberLMSSessionBean.retrieveAllMembers();
        newMember = new Member();
    }

    public String registerMember() {
        try {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Member>> violations = validator.validate(newMember);
            if (!violations.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please fill in all fields"));
                return "";
            } else {
                memberLMSSessionBean.createNewMember(newMember);
                return "members.xhtml?faces-redirect=true";
            }
        } catch (UsernameExistException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", ex.getMessage()));
            return "";
        } catch (UnknownPersistenceException | ConstraintViolationException ex1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please fill in all fields"));
            return "";
        }
    }

    // For data table global search
    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (LangUtils.isValueBlank(filterText)) {
            return true;
        }

        //System.out.println("Printing FILTER TEXT: "+ filterText);
        int filterInt;
        try {
            filterInt = Integer.parseInt(filterText);
        } catch (NumberFormatException ex) {
            filterInt = -1;
        }
        Member member = (Member) value;
        return member.getFirstName().toLowerCase().contains(filterText)
                || member.getLastName().toLowerCase().contains(filterText)
                || member.getGender().equals(filterText)
                || member.getIdentityNo().toLowerCase().contains(filterText)
                || member.getPhone().toLowerCase().contains(filterText)
                || member.getAddress().contains(filterText)
                || member.getAge() == filterInt;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public List<Member> getFilteredMembers() {
        return filteredMembers;
    }

    public void setFilteredMembers(List<Member> filteredMembers) {
        this.filteredMembers = filteredMembers;
    }

    public Member getNewMember() {
        return newMember;
    }

    public void setNewMember(Member newMember) {
        this.newMember = newMember;
    }

    public Member getSelectedMember() {
        return selectedMember;
    }

    public void setSelectedMember(Member selectedMember) {
        this.selectedMember = selectedMember;
    }

}
