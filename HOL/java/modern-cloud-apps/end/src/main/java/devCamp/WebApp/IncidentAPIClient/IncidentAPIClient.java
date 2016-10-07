package devCamp.WebApp.IncidentAPIClient;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import devCamp.WebApp.IncidentAPIClient.Models.IncidentBean;



public class IncidentAPIClient {
	private Log log = LogFactory.getLog(IncidentAPIClient.class);
	private String baseURI;	
	
	public String getBaseURI() {
		return baseURI;
	}

	public void setBaseURI(String baseURI) {
		this.baseURI = baseURI;
	}

	private String credentials;
	
	public String getCredentials() {
		return credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}

	@CacheEvict("incidents")
	public IncidentBean CreateIncident(IncidentBean incident) {
		//call REST API to create the incident
        final String uri = baseURI+"/incidents";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        
        IncidentBean retval = restTemplate.postForObject(uri, incident, IncidentBean.class);
        
        // set headers
        //HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.set("Authorization", "Basic " + "xxxxxxxxxxxx");
        //HttpEntity<String> entity = new HttpEntity<String>(input, headers);

        // send request and parse result
        //ResponseEntity<String> response = restTemplate
        //        .exchange(uri, HttpMethod.POST, entity, String.class);
        return retval;
	}
 
	
	public List<IncidentBean> GetAllIncidents() {
		log.info("Performing get /incidents web service");
		final String uri = baseURI+"/incidents";
        RestTemplate restTemplate = new RestTemplate();
 
        ResponseEntity<List<IncidentBean>> IncidentResponse =
		        restTemplate.exchange(uri,
		                    HttpMethod.GET, null, new ParameterizedTypeReference<List<IncidentBean>>() {
		            });
		
		return IncidentResponse.getBody();
	}
	
	public IncidentBean GetById(String incidentId) {
		//call REST API to create the incident
        final String uri = String.format("%s/incidents/%s", baseURI,incidentId);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        
        IncidentBean retval = restTemplate.getForObject(uri, IncidentBean.class);
        
        return retval;		
	}
 
	@CacheEvict("incidents")
	public IncidentBean UpdateIncident(String incidentId,IncidentBean newIncident){
		//call REST API to create the incident
        final String uri = baseURI+"/incidents";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        
        IncidentBean retval = null;
        //restTemplate.postForObject(uri, incident, IncidentBean.class);
        
        // set headers
        //HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.set("Authorization", "Basic " + "xxxxxxxxxxxx");
        //HttpEntity<String> entity = new HttpEntity<String>(input, headers);

        // send request and parse result
        //ResponseEntity<String> response = restTemplate
        //        .exchange(uri, HttpMethod.POST, entity, String.class);
        return retval;		
	}
	
	public IncidentAPIClient(String baseURI, String credentials) {
		if (baseURI == null){
			//throw argument null exception
		}
		if (credentials == null) {
			//throw argument null exception
		}
		this.baseURI = baseURI;
		this.credentials = credentials;
	}
}
