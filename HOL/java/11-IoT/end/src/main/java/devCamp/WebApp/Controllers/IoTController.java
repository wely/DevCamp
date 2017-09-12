package devCamp.WebApp.Controllers;

import java.util.ArrayList;
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

@Controller
public class IoTController {
	private static final Logger LOG = LoggerFactory.getLogger(DashboardController.class);

	@Autowired
	IncidentService service;

	@RequestMapping("/iot")
	public String iot(Model model) {
		List<IncidentBean> list = new ArrayList<IncidentBean>(); //service.getAllIncidents();
		list.add(new IncidentBean());
		model.addAttribute("allIncidents", list);
		return "IoT/index";
	}	
}
