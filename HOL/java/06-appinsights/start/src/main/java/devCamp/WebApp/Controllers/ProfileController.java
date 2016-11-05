package devCamp.WebApp.Controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
        return "Profile/index";
    }
        
}