package devCamp.WebApp.Controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import devCamp.WebApp.IncidentAPIClient.IncidentAPIClient;
import devCamp.WebApp.IncidentAPIClient.IncidentService;
import devCamp.WebApp.IncidentAPIClient.Models.IncidentBean;
import devCamp.WebApp.Utils.IncidentApiHelper;
import devCamp.WebApp.Utils.StorageHelper;

@Controller
public class IncidentController {
	
	//the Autowired annotation makes sure Spring can manage/cache the incident service
	@Autowired
	IncidentService service;

	private Log log = LogFactory.getLog(IncidentController.class);

	@GetMapping("/details")
	public String Details( @RequestParam(value="Id", required=false, defaultValue="") String id,Model model) {
		//get the incident from the REST service   	
		IncidentBean incident = service.GetById(id);    	
		//plug incident into the Model
		model.addAttribute("incident", incident);
		return "Incident/details";
	}


	@GetMapping("/new")
	public String newIncidentForm( Model model) {
		model.addAttribute("incident", new IncidentBean());
		return "Incident/new";
	}

	@PostMapping("/new")
	public String Create(@ModelAttribute IncidentBean incident,@RequestParam("file") MultipartFile imageFile) {
		log.info("creating incident");
		IncidentBean result = service.CreateIncident(incident);
		if (result != null){
			String IncidentID = result.getId();

			if (imageFile != null) {
				try {
					String fileName = imageFile.getOriginalFilename();
					if (fileName != null) {
						//now upload the file to blob storage 
						log.info("uploading to blob");
						StorageHelper.UploadFileToBlobStorage(IncidentID, imageFile);
						//add a event into the queue to resize and attach to incident
						log.info("adding to queue");
						StorageHelper.AddMessageToQueue(IncidentID, fileName);
					}
				} catch (Exception e) {
					return "Incident/details";
				}
			}    	
			service.ClearCache();
			return "Incident/details";
		} else {
			return "/error";
		}
	}
}