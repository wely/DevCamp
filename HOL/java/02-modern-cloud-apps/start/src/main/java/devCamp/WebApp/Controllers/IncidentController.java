package devCamp.WebApp.Controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import devCamp.WebApp.models.IncidentBean;

@Controller
public class IncidentController {
	
	private Log LOG = LogFactory.getLog(IncidentController.class);

	@GetMapping("/details")
	public String Details( @RequestParam(value="Id", required=false, defaultValue="") String id,Model model) {
		//get the incident from the REST service
	    /*
		IncidentBean incident = service.GetById(id);    	
		//plug incident into the Model
		model.addAttribute("incident", incident);
	    */
		return "Incident/details";
	}


	@GetMapping("/new")
	public String newIncidentForm( Model model) {
		model.addAttribute("incident", new IncidentBean());
		return "Incident/new";
	}

	@PostMapping("/new")
	public String Create(@ModelAttribute IncidentBean incident,@RequestParam("file") MultipartFile imageFile) {
		LOG.info("creating incident");
		return "redirect:/dashboard";
	}
}
