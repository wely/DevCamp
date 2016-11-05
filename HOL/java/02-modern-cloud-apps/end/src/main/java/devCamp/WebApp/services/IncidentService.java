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

    @CacheEvict(cacheNames="incidents", allEntries=true)
    IncidentBean createIncident(IncidentBean incident);

    @CacheEvict(cacheNames="incidents", allEntries=true)
    IncidentBean updateIncident(String incidentId, IncidentBean newIncident);

    @Cacheable("incidents") 
    @Async
    CompletableFuture<List<IncidentBean>> getAllIncidentsAsync();

	@CacheEvict(cacheNames="incidents", allEntries=true)
    @Async
    CompletableFuture<IncidentBean> createIncidentAsync(IncidentBean incident);

    @CacheEvict(cacheNames="incidents", allEntries=true)
    @Async
    CompletableFuture<IncidentBean> updateIncidentAsync(String incidentId,IncidentBean newIncident);

    @CacheEvict(cacheNames="incidents", allEntries=true)
    @Async
    CompletableFuture<IncidentBean> getByIdAsync(String incidentId);

    @CacheEvict(cacheNames="incidents", allEntries=true)
    void clearCache();
}
