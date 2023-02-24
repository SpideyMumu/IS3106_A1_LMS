/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Member;
import java.util.List;
import javax.ejb.Local;
import util.exception.MemberNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

/**
 *
 * @author muhdm
 */
@Local
public interface MemberLMSSessionBeanLocal {

    public Long createNewMember(Member newMember) throws UsernameExistException, UnknownPersistenceException;

    public Member retrieveMemberById(Long memberId) throws MemberNotFoundException;

    public List<Member> retrieveAllMembers();
}
