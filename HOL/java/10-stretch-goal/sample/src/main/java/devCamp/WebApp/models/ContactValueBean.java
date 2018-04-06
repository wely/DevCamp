package devCamp.WebApp.models;

import org.codehaus.jackson.annotate.JsonProperty;

public class ContactValueBean {
	 @JsonProperty(value = "odata.metadata")
	    private String OdataMetadata;
	    private ContactBean[] Value;
	    
	    public String getOdataMetadata(){
	        return OdataMetadata;
	    }
	    
	    public ContactBean[] getValue() {
	        return Value;
	    }
	    public void setValue(ContactBean[] value) {
	    	Value = value;
	    }
}
