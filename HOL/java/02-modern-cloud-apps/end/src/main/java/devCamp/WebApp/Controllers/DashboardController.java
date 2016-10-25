package devCamp.WebApp.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import devCamp.WebApp.IncidentAPIClient.IncidentAPIClient;
import devCamp.WebApp.IncidentAPIClient.IncidentService;
//import devCamp.WebApp.IncidentAPIClient.IncidentService;
import devCamp.WebApp.IncidentAPIClient.Models.IncidentBean;
import devCamp.WebApp.Utils.IncidentAPIHelper;

@Controller
public class DashboardController {
    
	@Autowired
	IncidentService service;
    		
	@RequestMapping("/dashboard")
	public String dashboard(Model model) {
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
	    
	    List<IncidentBean> theList = service.GetAllIncidents();
		//display the data on the dashboard screen
		
		model.addAttribute("allIncidents", theList);
		return "Dashboard/index";
	}
}
