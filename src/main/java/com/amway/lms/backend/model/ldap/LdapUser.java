package com.amway.lms.backend.model.ldap;

import java.io.Serializable;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

@Entry(objectClasses = { "top", "inetOrgPerson", "organizationalPerson", "person" })
public class LdapUser implements Serializable {

    private static final long serialVersionUID = 3634245226305214985L;

    @Id
    private Name distinguisedName;

    @Attribute(name = "cn")
    // @DnAttribute(value = "cn", index = 1)
    private String userID;

    @Attribute(name = "mail")
    private String mail;

    @Attribute(name = "givenName")
    private String firstName;

    @Attribute(name = "sn")
    private String lastName;

    @Attribute(name = "physicalDeliveryOfficeName")
    private String officeLocation;

    @Attribute(name = "telephoneNumber")
    private String telephonNumber;

    @Attribute(name = "manager")
    // @DnAttribute(value = "manager", index = 0)
    private String managerUser;

    @Attribute(name = "title")
    private String title;

    //This attribute will change to department when deploy production
    //@Attribute(name = "department")
    @Attribute(name = "departmentNumber")
    private String department;

    @Attribute(name = "userAccountControl")
    private int userAccountControl;

    public synchronized final String getUserID() {
        return userID;
    }

    public synchronized final void setUserID(String userID) {
        this.userID = userID;
    }

    public synchronized final String getMail() {
        return mail;
    }

    public synchronized final void setMail(String mail) {
        this.mail = mail;
    }

    public synchronized final String getFirstName() {
        return firstName;
    }

    public synchronized final void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public synchronized final String getLastName() {
        return lastName;
    }

    public synchronized final void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public synchronized final String getOfficeLocation() {
        return officeLocation;
    }

    public synchronized final void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    public synchronized final String getTelephonNumber() {
        return telephonNumber;
    }

    public synchronized final void setTelephonNumber(String telephonNumber) {
        this.telephonNumber = telephonNumber;
    }

    public synchronized final String getManagerUser() {
        try {
            if (managerUser == null)
                return null;
            return managerUser.substring(managerUser.indexOf((int) (char) '=') + 1,
                    managerUser.indexOf((int) (char) ','));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized final void setManagerUser(String managerUser) {
        this.managerUser = managerUser;
    }

    public synchronized final String getTitle() {
        return title;
    }

    public synchronized final void setTitle(String title) {
        this.title = title;
    }

    public LdapUser() {
        super();
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getUserAccountControl() {
        return userAccountControl;
    }

    public void setUserAccountControl(int userAccountControl) {
        this.userAccountControl = userAccountControl;
    }

}
