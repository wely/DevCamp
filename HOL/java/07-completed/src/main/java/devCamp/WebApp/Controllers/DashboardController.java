package devCamp.WebApp.Controllers;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import devCamp.WebApp.models.IncidentBean;
import devCamp.WebApp.services.IncidentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

//import devCamp.WebApp.IncidentAPIClient.IncidentService;
//import devCamp.WebApp.IncidentAPIClient.IncidentService;
//import devCamp.WebApp.models.IncidentBean;

@Controller
public class DashboardController {
	private static final Logger LOG = LoggerFactory.getLogger(DashboardController.class);

/*
	@RequestMapping("/dashboard")
	public String dashboard(Model model) {
		
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

		model.addAttribute("allIncidents", theList);
		return "Dashboard/index";
	}
*/
	@Autowired
	IncidentService service;

	@RequestMapping("/dashboard")
	public String dashboard(Model model) {
		List<IncidentBean> list = service.getAllIncidents();
		model.addAttribute("allIncidents", list);
		return "Dashboard/index";
	}	
}
