package devCamp.WebApp.services;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.stereotype.Service;

import devCamp.WebApp.models.ContactValueBean;
import devCamp.WebApp.models.UserProfileBean;

@EnableOAuth2Client
@Service
public class GraphServiceImpl implements GraphService{
    private static final Logger LOG = LoggerFactory.getLogger(GraphServiceImpl.class);
    @Autowired
    private OAuth2RestOperations restTemplate;
        
    @Value("${oauth2.resource.userInfoUri:https://graph.microsoft.com/v1.0/me}")
    private String baseUrl;

    @Value("${oauth2.resource.userInfoUri:https://graph.microsoft.com/v1.0/me/contacts}")
    private String contactUrl;
    
    public UserProfileBean getUserProfile() {
        LOG.info("getting user profile");
        UserProfileBean result = restTemplate.getForObject(baseUrl, UserProfileBean.class);
        return result;
    }

    public ContactValueBean getContacts() {
    	LOG.info("getting contacts");
    	ContactValueBean result = restTemplate.getForObject(contactUrl, ContactValueBean.class);
        return result;
    }
    
    @Bean
    public OAuth2RestOperations restTemplate(OAuth2ClientContext oauth2ClientContext) {
        return new OAuth2RestTemplate(resource(), oauth2ClientContext);
    }

    @Bean
    protected OAuth2ProtectedResourceDetails resource() {
        AuthorizationCodeResourceDetails resource = new AuthorizationCodeResourceDetails();
        resource.setClientId("my-trusted-client");
        return resource ;
    }
    
    private static String emailContent1 = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=us-ascii\'>"
    		+ "<title></title></head><body style=\"font-family:Calibri\">"
    		+ "<div style=\"width:50%;background-color:#CCC;padding:10px;margin:0 auto;text-align:center;\">"
    		+"<h1>City Power &amp; Light</h1><h2>New Incident Reported by ";
    private static String emailContent2 = "</h2><p>A new incident has been reported to the City Power &amp; Light outage system.</p>"   
    		+"<br /></div></body></html>";

    @Value("${oauth2.resource.mailUri:https://graph.microsoft.com/v1.0/me/sendMail}")
    private String mailUrl;

    public void sendMail(String displayName,String emailAddr){
    	LOG.info("sending email {} {}",displayName,emailAddr);
    	String email = emailContent1 + displayName+ emailContent2;
    	
    	JSONObject body = null;
    	try {
    		body = new JSONObject()
    				.put("ContentType", "HTML")
    				.put("Content", email);
    			
    		JSONObject aa = new JSONObject()
    				.put("Address", emailAddr);
    		JSONObject ee = new JSONObject()
    				.put("EmailAddress", aa);
        	JSONArray recipients = new JSONArray()
        			.put(ee);
        	JSONObject jsonMessage = new JSONObject()
        			.put("Subject","New Incident Reported")
        			.put("Body",body)
        			.put("ToRecipients",recipients);
            JSONObject json = new JSONObject()
                    .put("Message", jsonMessage)
                    .put("SaveToSentItems", true);   
            String jsons = json.toString();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<String>(jsons,headers);
            
        	String result = restTemplate.postForObject(mailUrl, entity, String.class);   	
        	} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}   
    }    

    @Value("${oauth2.resource.mailUri:https://graph.microsoft.com/v1.0/me/events}")
    private String eventUrl;
    
    public void createEvents(String locationDisplayName,
    		String bodyContent,
    		String subject,
    		String startDateTime,
    		String endDateTime,
    		String attendee1EmailAddress,
    		String attendee1name,
    		String attendee2EmailAddress,
    		String attendee2name,
    		String attendee3EmailAddress,
    		String attendee3name){

    	LOG.info("creating event {} {}",subject);
    	
    	try {
    		JSONObject body = new JSONObject()
    				.put("ContentType", "HTML")
    				.put("Content", bodyContent);
    			
    		JSONObject location = new JSONObject()
    				.put("displayName", locationDisplayName);

    		JSONObject attendee1address = new JSONObject()
    				.put("address", attendee1EmailAddress)
    				.put("name", attendee1name);

    		JSONObject attendee2address = new JSONObject()
    				.put("address", attendee2EmailAddress)
    				.put("name", attendee1name);

    		JSONObject attendee3address = new JSONObject()
    				.put("address", attendee3EmailAddress)
    				.put("name", attendee1name);
    		
    		JSONObject attendee1 = new JSONObject()
    				.put("emailAddress", attendee1address)
    				.put("type", "Required");

    		JSONObject attendee2 = new JSONObject()
    				.put("emailAddress", attendee2address)
    				.put("type", "Required");

    		JSONObject attendee3 = new JSONObject()
    				.put("emailAddress", attendee3address)
    				.put("type", "Required");
    		
    		JSONArray attendees = new JSONArray();
    		attendees.put(attendee1);
    		attendees.put(attendee2);
    		attendees.put(attendee3);
        	
        	JSONObject start = new JSONObject()
        			.put("dateTime",startDateTime)
        			.put("timeZone","Pacific Standard Time");

        	JSONObject end = new JSONObject()
        			.put("dateTime",endDateTime)
        			.put("timeZone","Pacific Standard Time");
        	
            JSONObject json = new JSONObject()
                    .put("subject", subject)
                    .put("body", body)
                    .put("start", start)
                    .put("end", end)
                    .put("location", location)
                    .put("attendees", attendees);
            
            String jsons = json.toString();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<String>(jsons,headers);
            
        	String result = restTemplate.postForObject(eventUrl, entity, String.class);   	
        	} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}   
    }
}
