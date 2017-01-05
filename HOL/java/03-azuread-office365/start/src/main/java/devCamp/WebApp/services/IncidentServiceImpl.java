package devCamp.WebApp.services;

import devCamp.WebApp.models.IncidentBean;
import devCamp.WebApp.properties.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class IncidentServiceImpl implements IncidentService {
    private static final Logger LOG = LoggerFactory.getLogger(IncidentServiceImpl.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<IncidentBean> getAllIncidents() {
        LOG.info("Performing get {} web service", applicationProperties.getIncidentApiUrl()+"/incidents");
        final String restUri = applicationProperties.getIncidentApiUrl() +"/incidents";
        ResponseEntity<List<IncidentBean>> response = restTemplate.exchange(restUri, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<IncidentBean>>() {});
        LOG.info("Total Incidents {}", response.getBody().size());
        return response.getBody();
    }

    @Override
    public IncidentBean createIncident(IncidentBean incident) {
        LOG.info("Creating incident");
        final String restUri = applicationProperties.getIncidentApiUrl() +"/incidents";
        IncidentBean createdBean = restTemplate.postForObject(restUri, incident, IncidentBean.class);
        LOG.info("Done creating incident");
        return createdBean;

    }

    @Override
    public IncidentBean updateIncident(String incidentId, IncidentBean newIncident) {
        LOG.info("Updating incident");
        //Add update logic here


        LOG.info("Done updating incident");
        return null;
    }


    @Override
    public CompletableFuture<List<IncidentBean>> getAllIncidentsAsync() {
        CompletableFuture<List<IncidentBean>> cf = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            LOG.info("Performing get /incidents web service");
            final String restUri = applicationProperties.getIncidentApiUrl() +"/incidents";
            ResponseEntity<List<IncidentBean>> response = restTemplate.exchange(restUri, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<IncidentBean>>() {});
            LOG.info("Total Incidents {}", response.getBody().size());
            cf.complete(response.getBody());
            LOG.info("Done getting incidents");
        });
        return cf;
    }

    @Override
    public CompletableFuture<IncidentBean> createIncidentAsync(IncidentBean incident) {
        CompletableFuture<IncidentBean> cf = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            LOG.info("Creating incident");
            final String restUri = applicationProperties.getIncidentApiUrl() +"/incidents";
            IncidentBean createdBean = restTemplate.postForObject(restUri, incident, IncidentBean.class);
            cf.complete(createdBean);
            LOG.info("Done creating incident");
        });
        return cf;
    }

    @Override
    public CompletableFuture<IncidentBean> updateIncidentAsync(String incidentId, IncidentBean newIncident) {
        CompletableFuture<IncidentBean> cf = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            LOG.info("Updating incident");
            //Add update logic here

            cf.complete(null); //change null to data that this method will return after update
            LOG.info("Done updating incident");
        });
        return cf;
    }

    @Override
    public CompletableFuture<IncidentBean> getByIdAsync(String incidentId) {
        CompletableFuture<IncidentBean> cf = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            LOG.info("Getting incident by ID {}", incidentId);
            final String restUri = applicationProperties.getIncidentApiUrl() +"/incidents";
            IncidentBean result = restTemplate.getForObject(restUri, IncidentBean.class);

            cf.complete(result);
            LOG.info("Done getting incident by ID");
        });
        return cf;
    }

    @Override
    public void clearCache() {

    }
}
