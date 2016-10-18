package devCamp.WebApp.Controllers;

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

@Controller
@Scope("session")
public class ProfileController {


	
	@RequestMapping("/profile")
    public String index(Model model,HttpServletRequest request) {
		HttpSession session = request.getSession();
    	UserProfileBean userProfile = GraphAPIClient.getUserProfile(session);
    	model.addAttribute("userProfileBean",userProfile);
        return "Profile/index";
    }
}