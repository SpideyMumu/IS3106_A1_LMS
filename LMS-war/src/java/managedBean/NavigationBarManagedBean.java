/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author muhdm
 */
@Named(value = "navigationBarManagedBean")
@SessionScoped
public class NavigationBarManagedBean implements Serializable {

    public NavigationBarManagedBean() {
    }
    
    public String navigateToLendBooks() {
        return "lendBooks.xhtml?faces-redirect=true";
    }
    
    public String navigateToReturnBooks() {
        return "returnBook.xhtml?faces-redirect=true";
    }
    
    public String navigateToHome() {
        return "home.xhtml?faces-redirect=true";
    }
    
     public String navigateToMembers() {
        return "members.xhtml?faces-redirect=true";
    }
    
}
