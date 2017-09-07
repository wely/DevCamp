package devCamp.WebApp.Controllers;

import devCamp.WebApp.services.AzureStorageService;
import devCamp.WebApp.services.GraphService;
import devCamp.WebApp.services.IncidentService;
import devCamp.WebApp.services.TimeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import devCamp.WebApp.Utils.OAuth2TokenUtils;
import devCamp.WebApp.models.ContactValueBean;

//import devCamp.WebApp.IncidentAPIClient.IncidentService;

import devCamp.WebApp.models.IncidentBean;
/*
import devCamp.WebApp.IncidentAPIClient.IncidentAPIClient;
import devCamp.WebApp.IncidentAPIClient.IncidentService;
import devCamp.WebApp.Utils.IncidentApiHelper;
import devCamp.WebApp.Utils.StorageHelper;
*/

import java.util.Date;
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
	
	@Autowired
	private GraphService graphService;

	@Autowired
	private TimeService timeService;    	
	
	@PostMapping("/new")
	public String Create(@ModelAttribute IncidentBean incident, @RequestParam("file") MultipartFile imageFile) {
		LOG.info("creating incident");
		graphService.sendMail(OAuth2TokenUtils.getGivenName(),OAuth2TokenUtils.getMail());
		
        ContactValueBean contacts = graphService.getContacts();
        String address1 = contacts.getValue()[0].getEmailAddresses()[0].getAddress();
        String name1 = contacts.getValue()[0].getEmailAddresses()[0].getName();
        String address2 = contacts.getValue()[1].getEmailAddresses()[0].getAddress();
        String name2 = contacts.getValue()[1].getEmailAddresses()[0].getName();
        String address3 = contacts.getValue()[2].getEmailAddresses()[0].getAddress();
        String name3 = contacts.getValue()[2].getEmailAddresses()[0].getName();

        Date dt = timeService.getTime();
        String startTime = timeService.startDateToString(dt);
        String endTime = timeService.endDateToString(dt);
        
        graphService.createEvents(incident.getCity(), incident.getDescription(), incident.getOutageType(), startTime, endTime, address1, name1, address2, name2, address3, name3);
		
		
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
