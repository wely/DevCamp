package devCamp.WebApp.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.microsoft.applicationinsights.TelemetryClient;

import devCamp.WebApp.models.UserProfileBean;
import devCamp.WebApp.services.GraphService;



@Configuration
@Controller
public class ProfileController {
    private static final Logger LOG = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private GraphService graphService;
    
    @RequestMapping("/profile")
    public String index(Model model) {
        LOG.info("getting user profile");
        UserProfileBean result = graphService.getUserProfile();

        model.addAttribute("userProfileBean",result);
        
        TelemetryClient telemetry = new TelemetryClient();

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("User", result.getUserPrincipalName());
        properties.put("displayname", result.getDisplayName());

        telemetry.trackEvent("Profile", properties, null);    	

        model.addAttribute("userProfileBean",result);

        try { 
            throw new Exception("This is only a test!"); 
        } catch (Exception exc) { 
            telemetry.trackException(exc); 
            System.out.println("[6] Exception             -- message=\"This is only a test!\""); 
        }         
        return "Profile/index";
    }
        
}