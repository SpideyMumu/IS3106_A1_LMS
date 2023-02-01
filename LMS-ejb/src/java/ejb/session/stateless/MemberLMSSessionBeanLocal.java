/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Member;
import javax.ejb.Local;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

/**
 *
 * @author muhdm
 */
@Local
public interface MemberLMSSessionBeanLocal {

    public Long createNewMember(Member newMember) throws UsernameExistException, UnknownPersistenceException;
}
