package devCamp.WebApp.Controllers;

import devCamp.WebApp.services.AzureStorageService;
import devCamp.WebApp.services.IncidentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

//import devCamp.WebApp.IncidentAPIClient.IncidentService;

import devCamp.WebApp.models.IncidentBean;
/*
import devCamp.WebApp.IncidentAPIClient.IncidentAPIClient;
import devCamp.WebApp.IncidentAPIClient.IncidentService;
import devCamp.WebApp.Utils.IncidentApiHelper;
import devCamp.WebApp.Utils.StorageHelper;
*/

import java.util.concurrent.CompletableFuture;

@Controller
public class IncidentController {
	private static final Logger LOG = LoggerFactory.getLogger(IncidentController.class);



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
	}
	*/
	
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
