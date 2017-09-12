package devCamp.WebApp.services;


import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public interface AzureStorageService {

    @Async
    CompletableFuture<Void> addMessageToQueueAsync(String IncidentId, String ImageFileName);

    @Async
    CompletableFuture<String> uploadFileToBlobStorageAsync(String IncidentId, String fileName, String contentType, byte[] fileBuffer);

}
