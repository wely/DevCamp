package devCamp.WebApp.models;

import org.codehaus.jackson.annotate.JsonProperty;

public class UserProfileBean {
    @JsonProperty(value = "odata.metadata")
    private String OdataMetadata;
    private String Entity;
    private String objectId;
    private String TelephoneNumber;
    private String DisplayName;
    private String GivenName;
    private String JobTitle;
    private String Mail;
    private String Mobile;
    private String PhysicalDeliveryOfficeName;
    private String PreferredLanguage;
    private String Surname;
    private String UserPrincipalName;
    
    public String getOdataMetadata(){
        return OdataMetadata;
    }
    public void setOdataMetadata(String data){
        OdataMetadata = data;
    }
    public String getEntity(){
        return Entity;
    }
    public void setEntity(String entity) {
        Entity = entity;
    }
    public String getObjectId() {
        return objectId;
    }
    public void setObjectId(String id) {
        objectId = id;
    }
    public String getTelephoneNumber() {
        return TelephoneNumber;
    }
    public void setTelephoneNumber(String telephoneNumber) {
        TelephoneNumber=telephoneNumber;
    }
    public String getDisplayName() {
        return DisplayName;
    }
    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }
    public String getGivenName() {
        return GivenName;
    }
    public void setGivenName(String givenName) {
        GivenName = givenName;
    }
    public String getJobTitle() {
        return JobTitle;
    }
    public void setJobTitle(String jobTitle) {
        JobTitle = jobTitle;
    }
    public String getMail() {
        return Mail;
    }
    public void setMail(String mail) {
        Mail = mail;
    }
    public String getMobile() {
        return Mobile;
    }
    public void setMobile(String mobile) {
        Mobile = mobile;
    }
    public String getPhysicalDeliveryOfficeName() {
        return PhysicalDeliveryOfficeName;
    }
    public void setPhysicalDeliveryOfficeName(String officeLocation) {
        PhysicalDeliveryOfficeName = officeLocation;
    }
    public String getPreferredLanguage() {
        return PreferredLanguage;
    }
    public void setPreferredLanguage(String preferredLanguage) {
        PreferredLanguage = preferredLanguage;
    }
    public String getSurname() {
        return Surname;
    }
    public void setSurname(String surname) {
        Surname = surname;
    }
    public String getUserPrincipalName() {
        return UserPrincipalName;
    }
    public void setUserPrincipalName(String userPrincipalName) {
        UserPrincipalName = userPrincipalName;
    }
}
