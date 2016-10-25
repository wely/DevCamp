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

import devCamp.WebApp.Utils.AuthHelper;
import devCamp.WebApp.Utils.GraphAPIClient;
import devCamp.WebApp.ViewModels.UserProfileBean;

@Controller
@Scope("session")
public class ProfileController {

    private UserProfileBean getUserProfile(HttpSession session) {
        
        //call REST API to create the incident
        final String uri = "https://graph.windows.net/me?api-version=1.6";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        HttpHeaders headers = new HttpHeaders();
        
        AuthenticationResult result = AuthHelper.getAuthSessionObject(session);
        String token = result.getAccessToken();
        
        headers.set("api-version", "2013-04-05");
        headers.set("Authorization", token);
        headers.set("Accept", "application/json;odata=minimalmetadata");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        ResponseEntity<UserProfileBean> response = restTemplate.exchange(uri, HttpMethod.GET, entity, UserProfileBean.class);

        UserProfileBean createdBean =response.getBody();
        return createdBean;		
    }	

    @RequestMapping("/profile")
    public String index(Model model,HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserProfileBean userProfile = GraphAPIClient.getUserProfile(session);
        model.addAttribute("userProfileBean",userProfile);
        return "Profile/index";
    }
}