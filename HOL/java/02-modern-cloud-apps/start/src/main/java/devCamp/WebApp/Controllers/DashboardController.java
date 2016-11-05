package devCamp.WebApp.Controllers;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

//import devCamp.WebApp.IncidentAPIClient.IncidentService;
import devCamp.WebApp.models.IncidentBean;


@Controller
public class DashboardController {
    /*
	@Autowired
	IncidentService service;
    */		
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

	/*
	@Autowired
	IncidentService service;
	
	@RequestMapping("/dashboard")
	public String dashboard(Model model) {
		List<IncidentBean> list = service.getAllIncidents();
		model.addAttribute("allIncidents", list);
		return "Dashboard/index";
	}	

	*/
}
