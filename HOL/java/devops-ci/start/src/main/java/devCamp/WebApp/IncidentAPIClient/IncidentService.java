package devCamp.WebApp.IncidentAPIClient;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import devCamp.WebApp.IncidentAPIClient.Models.IncidentBean;
import devCamp.WebApp.Utils.IncidentApiHelper;

@Service
public class IncidentService {
	
	private Log log = LogFactory.getLog(IncidentService.class);

	@Cacheable("incidents")
	public List<IncidentBean> GetAllIncidents() {			
		IncidentAPIClient client = IncidentApiHelper.getIncidentAPIClient();
		return client.GetAllIncidents();
	}
	
	@CacheEvict(cacheNames="incidents", allEntries=true)
	public IncidentBean CreateIncident(IncidentBean incident) {
		return IncidentApiHelper.getIncidentAPIClient().CreateIncident(incident);		
	}
	
	@CacheEvict(cacheNames="incidents", allEntries=true)
	public IncidentBean UpdateIncident(String incidentId,IncidentBean newIncident){
		return IncidentApiHelper.getIncidentAPIClient().UpdateIncident(incidentId,newIncident);
	}
	
	public IncidentBean GetById(String incidentId) {
		return IncidentApiHelper.getIncidentAPIClient().GetById(incidentId);		
	}

	@CacheEvict(cacheNames="incidents", allEntries=true)
	public void ClearCache() {
	}

}