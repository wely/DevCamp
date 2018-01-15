package devCamp.WebApp.Controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import devCamp.WebApp.models.IncidentBean;
import devCamp.WebApp.services.AzureStorageService;
import devCamp.WebApp.services.IncidentService;

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
/*
	@PostMapping("/new")
	public String Create(@ModelAttribute IncidentBean incident,@RequestParam("file") MultipartFile imageFile) {
		LOG.info("creating incident");
		return "redirect:/dashboard";
	}*/
	
	@Autowired
	private IncidentService incidentService;
	
	@Autowired
	private AzureStorageService storageService;

	@PostMapping("/new")
	public String Create(@ModelAttribute IncidentBean incident, @RequestParam("file") MultipartFile imageFile) {
		LOG.info("creating incident");
		//graphService.sendMail(OAuth2TokenUtils.getGivenName(),OAuth2TokenUtils.getMail());
		IncidentBean result = incidentService.createIncident(incident);
		String incidentID = result.getId();

		if (imageFile != null) {
			try {
				String fileName = imageFile.getOriginalFilename();
				if (fileName != null) {
					//save the file
					//now upload the file to blob storage
					
					LOG.info("Uploading to blob");
					storageService.uploadFileToBlobStorageAsync(incidentID, fileName, imageFile.getContentType(),
							imageFile.getBytes())
							.whenComplete((a, b) -> {
								//add a event into the queue to resize and attach to incident
								LOG.info("Successfully uploaded file to blob storage, now adding message to queue");
								storageService.addMessageToQueueAsync(incidentID, fileName);
							});
	                
				}
			} catch (Exception e) {
				return "Incident/details";
			}
			return "redirect:/dashboard";
		}
		return "redirect:/dashboard";
	}
}
