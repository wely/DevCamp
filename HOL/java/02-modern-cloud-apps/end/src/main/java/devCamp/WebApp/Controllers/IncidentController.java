package devCamp.WebApp.Controllers;

import devCamp.WebApp.services.IncidentService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import devCamp.WebApp.Utils.StorageAPIHelper;

import java.util.concurrent.CompletableFuture;

@Controller
public class IncidentController {
	private static final Logger LOG = LoggerFactory.getLogger(IncidentController.class);

	//the Autowired annotation makes sure Spring can manage/cache the incident service
    
	@Autowired
	//IncidentService service;
    private IncidentService service;

	private Log log = LogFactory.getLog(IncidentController.class);

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
	public CompletableFuture<String> Create(@ModelAttribute IncidentBean incident,@RequestParam("file") MultipartFile imageFile) {
		log.info("creating incident");
		
		//IncidentBean result = service.CreateIncident(incident);
		return service.createIncidentAsync(incident).thenApply((result) -> {
			String IncidentID = result.getId();

			if (imageFile != null) {
				try {
					String fileName = imageFile.getOriginalFilename();
					if (fileName != null) {

						//now upload the file to blob storage
						log.info("uploading to blob");
						StorageAPIHelper.getStorageAPIClient().UploadFileToBlobStorage(IncidentID, imageFile);
						//add a event into the queue to resize and attach to incident
						log.info("adding to queue");
						StorageAPIHelper.getStorageAPIClient().AddMessageToQueue(IncidentID, fileName);

					}
				} catch (Exception e) {
					return "Incident/details";
				}
			}
			return "redirect:/dashboard";
		});

	}
	@ExceptionHandler(Exception.class)
	public String catchAllErrors(Exception e) {
		LOG.error("Error occurred in IncidentController", e);
		return "/error";
	}
}
