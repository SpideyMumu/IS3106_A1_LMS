/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import ejb.session.stateless.MemberLMSSessionBeanLocal;
import entity.Member;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
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
        System.out.println("register member method called");
        try {
            memberLMSSessionBean.createNewMember(newMember);
            return "viewAllMembers.xhtml?faces-redirect=true";
        } catch (UsernameExistException |UnknownPersistenceException ex) {
            return "viewAllMembers.xhtml?faces-redirect=true";
        }
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
    
    
}
