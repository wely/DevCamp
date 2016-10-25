package devCamp.WebApp.Controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.microsoft.aad.adal4j.AuthenticationResult;

import devCamp.WebApp.GraphAPIClient.GraphAPIClient;
import devCamp.WebApp.Utils.AuthHelper;
import devCamp.WebApp.ViewModels.UserProfileBean;
import com.microsoft.applicationinsights.TelemetryClient;


@Controller
@Scope("session")
public class ProfileController {


	
	@RequestMapping("/profile")
    public String index(Model model,HttpServletRequest request) {
		HttpSession session = request.getSession();
    	UserProfileBean userProfile = GraphAPIClient.getUserProfile(session);
    	TelemetryClient telemetry = new TelemetryClient();

    	Map<String, String> properties = new HashMap<String, String>();
    	properties.put("User", userProfile.getUserPrincipalName());
    	properties.put("displayname", userProfile.getDisplayName());

    	telemetry.trackEvent("Profile", properties, null);    	
    	
    	model.addAttribute("userProfileBean",userProfile);
    	
    	try { 
    		throw new Exception("This is only a test!"); 
    	} catch (Exception exc) { 
    		telemetry.trackException(exc); 
    		System.out.println("[6] Exception             -- message=\"This is only a test!\""); 
    	} 
    	
    	
        return "Profile/index";
    }
}