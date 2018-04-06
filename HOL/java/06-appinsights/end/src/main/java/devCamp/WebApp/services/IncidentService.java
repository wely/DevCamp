package devCamp.WebApp.services;

import devCamp.WebApp.models.IncidentBean;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public interface IncidentService {

	@Cacheable("incidents")
	List<IncidentBean> getAllIncidents();

    @Async
    CompletableFuture<List<IncidentBean>> getAllIncidentsAsync();

    @CacheEvict(cacheNames="incidents", allEntries=true)
    public IncidentBean createIncident(IncidentBean incident);
    
    @Async
    @CacheEvict(cacheNames="incidents", allEntries=true)
    CompletableFuture<IncidentBean> createIncidentAsync(IncidentBean incident);
    
    @Async
    @CacheEvict(cacheNames="incidents", allEntries=true)
    CompletableFuture<IncidentBean> updateIncidentAsync(String incidentId,IncidentBean newIncident);

    @Async
    CompletableFuture<IncidentBean> getByIdAsync(String incidentId);

    void clearCache();
}