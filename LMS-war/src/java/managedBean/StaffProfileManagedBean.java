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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import util.exception.StaffNotFoundException;

@Named(value = "staffProfileManagedBean")
@SessionScoped
public class StaffProfileManagedBean implements Serializable {

    @EJB
    private StaffLMSSessionBeanLocal staffLMSSessionBean;

    private String username = null;
    private String password = null;
    
    private Staff loggedInStaff;

    public StaffProfileManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        loggedInStaff = new Staff();
    }

    public String login() {
        System.out.println("log in method called");

        try {
            loggedInStaff = staffLMSSessionBean.retrieveStaffByUsername(username);
            System.out.println(loggedInStaff.toString() + loggedInStaff.getFirstName() + loggedInStaff.getLastName());
            if (loggedInStaff.getPassword().equals(password)) {
                return "/loggedIn/home.xhtml?faces-redirect=true";
            } else {
                //System.out.println("log in not successful");
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Wrong Password. Please Try Again"));
                return "";
            }
        } catch (StaffNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", ex.getMessage()));
            return "";
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

    public Staff getLoggedInStaff() {
        return loggedInStaff;
    }

    public void setLoggedInStaff(Staff loggedInStaff) {
        this.loggedInStaff = loggedInStaff;
    }

}
