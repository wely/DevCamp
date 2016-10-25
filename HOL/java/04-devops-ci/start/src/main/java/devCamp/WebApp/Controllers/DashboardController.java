package devCamp.WebApp.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import devCamp.WebApp.IncidentAPIClient.IncidentService;
import devCamp.WebApp.IncidentAPIClient.Models.IncidentBean;

@Controller
public class DashboardController {
	@Autowired
	IncidentService service;
		
	@RequestMapping("/dashboard")
	public String dashboard(Model model) {	
		List<IncidentBean> theList = service.GetAllIncidents();
		model.addAttribute("allIncidents", theList);
		//display the data on the dashboard screen
		return "Dashboard/index";
	}
}