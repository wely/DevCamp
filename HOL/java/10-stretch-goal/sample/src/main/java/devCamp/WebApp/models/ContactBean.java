package devCamp.WebApp.models;

import org.codehaus.jackson.annotate.JsonProperty;

public class ContactBean {
	 @JsonProperty(value = "odata.metadata")
	    private String OdataMetadata;
	    private String DisplayName;
	    private EmailAddressBean[] EmailAddresses;
	    
	    public String getOdataMetadata(){
	        return OdataMetadata;
	    }
	    
	    public String getDisplayName() {
	        return DisplayName;
	    }
	    public void setDisplayName(String displayName) {
	    	DisplayName = displayName;
	    }
	    
	    public EmailAddressBean[] getEmailAddresses() {
	        return EmailAddresses;
	    }
	    public void setEmailAddresses(EmailAddressBean[] emailAddresses) {
	    	EmailAddresses = emailAddresses;
	    }
}
