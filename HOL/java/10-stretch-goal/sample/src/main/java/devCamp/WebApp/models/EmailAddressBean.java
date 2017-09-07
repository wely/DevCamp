package devCamp.WebApp.models;

import org.codehaus.jackson.annotate.JsonProperty;

public class EmailAddressBean {
	 @JsonProperty(value = "odata.metadata")
	    private String OdataMetadata;
	    private String Name;
	    private String Address;
	    
	    public String getOdataMetadata(){
	        return OdataMetadata;
	    }
	    
	    public String getName() {
	        return Name;
	    }
	    public void setName(String name) {
	    	Name = name;
	    }
	    
	    public String getAddress() {
	        return Address;
	    }
	    public void setAddress(String address) {
	    	Address = address;
	    }
}
