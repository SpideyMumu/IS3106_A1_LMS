/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import ejb.session.stateless.MemberLMSSessionBean;
import ejb.session.stateless.MemberLMSSessionBeanLocal;
import entity.Member;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import managedBean.BookManagedBean;
import util.exception.MemberNotFoundException;

/**
 *
 * @author muhdm
 */
@Named(value = "memberConverter")
@RequestScoped
public class MemberConverter implements Converter {

    @EJB
    private MemberLMSSessionBeanLocal memberLMSSessionBean;

    private List<Member> allMembers;

    @Inject
    private BookManagedBean bookManagedBean;

    public MemberConverter() {
    }

    @PostConstruct
    public void init() {
        //bookManagedBean = new BookManagedBean();
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        /*
        bookManagedBean.setMemberToLendTypedID(value);
        return bookManagedBean.selectMember();
         */
        try {
            //System.out.println(value);
            Member memberToLend = memberLMSSessionBean.retrieveMemberByIdentityNum(value);
            bookManagedBean.setMemberToLend(memberToLend);
            bookManagedBean.setMemberToLendTypedID(value);
            
            return memberToLend;
        } catch (MemberNotFoundException ex) {
            System.out.println("not found");
            return null;
        }

    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((Member) value).getIdentityNo();
    }

    public List<Member> getAllMembers() {
        return allMembers;
    }

    public void setAllMembers(List<Member> allMembers) {
        this.allMembers = allMembers;
    }

}
