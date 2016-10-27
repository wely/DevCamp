/*
package devCamp.WebApp.GraphAPIClient;

import java.io.OutputStream;

import javax.servlet.http.HttpSession;

import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.microsoft.aad.adal4j.AuthenticationResult;

import devCamp.WebApp.IncidentAPIClient.Models.IncidentBean;
import devCamp.WebApp.Utils.AuthHelper;
import devCamp.WebApp.ViewModels.UserProfileBean;

public class GraphAPIClient {

	public static UserProfileBean getUserProfile(HttpSession session) {
		
        //call REST API to create the incident
        // final String uri = "https://graph.windows.net/devcampross.onmicrosoft.com/me?api-version=1.6";
	//final String uri = "https://graph.windows.net/v1.0/me";
	final String uri="https://graph.windows.net/me?api-version=1.6";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        HttpHeaders headers = new HttpHeaders();
        
        AuthenticationResult result = AuthHelper.getAuthSessionObject(session);
        String token = result.getAccessToken();
        
        //headers.set("api-version", "2013-04-05");
        headers.set("Authorization", token);
        System.out.println("TOKEN:"+token);
        headers.set("Accept", "application/json;odata=minimalmetadata");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        
        ResponseEntity<UserProfileBean>  response = restTemplate.exchange(uri, HttpMethod.GET, entity, UserProfileBean.class);

        UserProfileBean createdBean =response.getBody();
        return createdBean;		
	}	
}
*/
