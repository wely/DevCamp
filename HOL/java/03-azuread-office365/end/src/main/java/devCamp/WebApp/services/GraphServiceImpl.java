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

import devCamp.WebApp.models.UserProfileBean;

@EnableOAuth2Client
@Service
public class GraphServiceImpl implements GraphService{
    private static final Logger LOG = LoggerFactory.getLogger(GraphServiceImpl.class);
    @Autowired
    private OAuth2RestOperations restTemplate;
        
    @Value("${oauth2.resource.userInfoUri:https://graph.microsoft.com/v1.0/me}")
    private String baseUrl;
    
    public UserProfileBean getUserProfile() {
        LOG.info("getting user profile");
        UserProfileBean result = restTemplate.getForObject(baseUrl, UserProfileBean.class);
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
            
}
