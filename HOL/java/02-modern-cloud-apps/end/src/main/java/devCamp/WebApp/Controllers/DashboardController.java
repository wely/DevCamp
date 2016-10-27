package devCamp.WebApp.Controllers;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import devCamp.WebApp.models.IncidentBean;
import devCamp.WebApp.services.IncidentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

//import devCamp.WebApp.IncidentAPIClient.IncidentService;
//import devCamp.WebApp.IncidentAPIClient.IncidentService;
//import devCamp.WebApp.models.IncidentBean;

@Controller
public class DashboardController {
	private static final Logger LOG = LoggerFactory.getLogger(DashboardController.class);

	@Autowired
	//IncidentService service;
	private IncidentService service;

	@RequestMapping("/dashboard")
	public CompletableFuture<String> dashboard(Model model) {
		/*
		ArrayList<IncidentBean> theList = new ArrayList<>();
		for (int i = 1;i<=3;++i){
			IncidentBean bean = new IncidentBean();
			bean.setId("12345");
			bean.setStreet("123 Main St.");
			bean.setFirstName("Jane");
			bean.setLastName("Doe");
			bean.setCreated("1/01/2016");
			theList.add(bean);
		}
		/*
		IncidentAPIClient client = IncidentAPIHelper.getIncidentAPIClient();
		List<IncidentBean> theList = client.GetAllIncidents();
		model.addAttribute("allIncidents",theList);
		*/
	    return service.getAllIncidentsAsync()
							.thenApply(list -> {
			model.addAttribute("allIncidents", list);
			return "Dashboard/index";
		});

		//List<IncidentBean> theList = service.GetAllIncidents();
		//display the data on the dashboard screen
		
		//model.addAttribute("allIncidents", theList);
		//return "Dashboard/index";
	}
}
