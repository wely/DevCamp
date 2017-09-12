package devCamp.WebApp.services;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.*;
import com.microsoft.azure.storage.queue.CloudQueue;
import com.microsoft.azure.storage.queue.CloudQueueClient;
import com.microsoft.azure.storage.queue.CloudQueueMessage;
import devCamp.WebApp.properties.AzureStorageAccountProperties;
import org.apache.commons.io.FilenameUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

@Service
public class AzureStorageServiceImpl implements AzureStorageService {
    private static final Logger LOG = LoggerFactory.getLogger(AzureStorageServiceImpl.class);

    @Autowired
    private CloudStorageAccount cloudStorageAccount;

    @Autowired
    private AzureStorageAccountProperties azureStorageAccountProperties;

    @Async
    @Override
    public CompletableFuture<Void> addMessageToQueueAsync(String IncidentId, String ImageFileName) {
        CompletableFuture<Void> cf = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                CloudQueueClient queueClient = cloudStorageAccount.createCloudQueueClient();

                CloudQueue msgQ = queueClient.getQueueReference(azureStorageAccountProperties.getQueue());
                msgQ.createIfNotExists();

                JSONObject json = new JSONObject()
                        .put("IncidentId", IncidentId)
                        .put("BlobContainerName", azureStorageAccountProperties.getBlobContainer())
                        .put("BlobName", getIncidentBlobFilename(IncidentId, ImageFileName));

                String msgPayload = json.toString();
                CloudQueueMessage qMsg = new CloudQueueMessage(msgPayload);
                msgQ.addMessage(qMsg);
            } catch (URISyntaxException | StorageException | JSONException e) {
                // TODO Auto-generated catch block
                LOG.error("addMessageToQueue - error", e);
                cf.completeExceptionally(e);
            }
            cf.complete(null);
        });
        return cf;
    }

    @Async
    @Override
    public CompletableFuture<String> uploadFileToBlobStorageAsync(String IncidentId, String fileName,
                                                                  String contentType, byte[] fileBuffer) {
        CompletableFuture<String> cf = new CompletableFuture<>();
        CompletableFuture.runAsync(() ->{
            try {
                CloudBlobClient serviceClient = cloudStorageAccount.createCloudBlobClient();

                // Container name must be lower case.
                CloudBlobContainer container = serviceClient.getContainerReference(azureStorageAccountProperties.getBlobContainer());
                container.createIfNotExists();

                // Set anonymous access on the container.
                BlobContainerPermissions containerPermissions = new BlobContainerPermissions();
                containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
                container.uploadPermissions(containerPermissions);

                //
                CloudBlockBlob imgBlob = container.getBlockBlobReference(getIncidentBlobFilename(IncidentId,
                        fileName));
                imgBlob.getProperties().setContentType(contentType);
                imgBlob.uploadFromByteArray(fileBuffer, 0, fileBuffer.length);
                UriBuilder builder = UriBuilder.fromUri(imgBlob.getUri());
                builder.scheme("https");

                //return result
                cf.complete(builder.toString());
            } catch (URISyntaxException | StorageException | IOException e) {
                // TODO Auto-generated catch block
                LOG.error("uploadFileToBlobStorage - error {}", e);
                cf.completeExceptionally(e);
            }
        });
        return cf;
    }
    private String getIncidentBlobFilename(String IncidentId,String FileName) {
        String fileExt = FilenameUtils.getExtension(FileName);
        return String.format("%s.%s", IncidentId,fileExt);
    }
}
