/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author muhdm
 */
@Entity
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 1, max = 32)
    private String firstName;

    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 1, max = 32)
    private String lastName;

    //other attributes
    @Column(nullable = false)
    @NotNull
    private Character gender;

    @Column(nullable = false)
    @NotNull
    private Integer age;

    @Column(nullable = false, unique = true, length = 32)
    @NotNull
    @Size(min = 9, max = 32)
    private String identityNo;

    @Column(nullable = false, unique = true, length = 32)
    @NotNull
    @Size(min = 8, max = 32)
    private String phone;

    @Column(nullable = false, length = 255)
    @NotNull
    @Size(min = 1)
    private String address;

    //Relationship attribute
    @OneToMany(mappedBy = "member")
    private List<LendAndReturn> lendAndReturns;

    public Member() {
        this.lendAndReturns = new ArrayList<>();
    }

    public Member(long memberId, String firstName, String lastName, Character gender, Integer age, String identityNo, String phone, String address) {
        this.memberId = memberId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.identityNo = identityNo;
        this.phone = phone;
        this.address = address;
        this.lendAndReturns = new ArrayList<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<LendAndReturn> getLendAndReturns() {
        return lendAndReturns;
    }

    public void setLendAndReturns(List<LendAndReturn> lendAndReturns) {
        this.lendAndReturns = lendAndReturns;
    }

    public void addLendAndReturns(LendAndReturn lendAndReturn) {
        this.lendAndReturns.add(lendAndReturn);
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getIdentityNo() {
        return identityNo;
    }

    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (memberId != null ? memberId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the memberId fields are not set
        if (!(object instanceof Member)) {
            return false;
        }
        Member other = (Member) object;
        if ((this.memberId == null && other.memberId != null) || (this.memberId != null && !this.memberId.equals(other.memberId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName;
    }

}
