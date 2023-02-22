/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import ejb.session.stateless.StaffLMSSessionBeanLocal;
import entity.Staff;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import util.exception.StaffNotFoundException;

@Named(value = "staffProfileManagedBean")
@SessionScoped
public class StaffProfileManagedBean implements Serializable {

    @EJB
    private StaffLMSSessionBeanLocal staffLMSSessionBean;

    private String username = null;
    private String password = null;

    public StaffProfileManagedBean() {
    }

    public String login() {
        System.out.println("log in method called");
        
        try {
            Staff loggedInStaff = staffLMSSessionBean.retrieveStaffByUsername(username);
            System.out.println(loggedInStaff.toString() + loggedInStaff.getFirstName() + loggedInStaff.getLastName());
            if (loggedInStaff.getPassword().equals(password)) {
                return "/loggedIn/home.xhtml?faces-redirect=true";
            } else {
                System.out.println("log in not successful");
                return "index.xhtml";
            }
        } catch (StaffNotFoundException ex) {
            return "index.xhtml";
        }
    }
    
    public String logout() {
        System.out.println("Log Out successful");
        username = null;
        password = null;
        return "/index.xhtml?faces-redirect=true";
    }
    
    public String logout2() {
        System.out.println("Log Out successful");
        username = null;
        password = null;
        return "/index.xhtml?faces-redirect=true";
    }
    
    public String test() {
        System.out.println("test successful");
        return "";
    }
    
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
