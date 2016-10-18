package devCamp.WebApp.StorageAPIClient;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;
import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.*;
import com.microsoft.azure.storage.queue.CloudQueue;
import com.microsoft.azure.storage.queue.CloudQueueClient;
import com.microsoft.azure.storage.queue.CloudQueueMessage;

import devCamp.WebApp.Controllers.IncidentController;

import java.io.*;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import javax.ws.rs.core.UriBuilder;

public class StorageAPIClient {
	private Log log = LogFactory.getLog(StorageAPIClient.class);
	
	//configuration values from the system Environment
	private String account;
	private String key;
	private String azureStorageContainer;
	private String azureStorageQueue;
	private String blobStorageConnectionString;
	
	public StorageAPIClient(String account, String key, String azureStorageContainer, String azureStorageQueue) {
		this.account = account;
		this.key = key;
		this.azureStorageContainer = azureStorageContainer;
		this.azureStorageQueue = azureStorageQueue;
		blobStorageConnectionString = String.format("DefaultEndpointsProtocol=http;AccountName=%s;AccountKey=%s", account,key);
	}

	
    public void AddMessageToQueue(String IncidentId, String ImageFileName)
    {
        CloudStorageAccount storageAccount;
		try {
			storageAccount = CloudStorageAccount.parse(blobStorageConnectionString);
			CloudQueueClient queueClient = storageAccount.createCloudQueueClient();

	        CloudQueue msgQ = queueClient.getQueueReference(azureStorageQueue);
	        msgQ.createIfNotExists();
	
	        JSONObject json = new JSONObject()
		    .put("IncidentId", IncidentId)
		    .put("BlobContainerName", azureStorageContainer)
		    .put("BlobName",getIncidentBlobFilename(IncidentId,ImageFileName));
	
	        String msgPayload = json.toString();
	        CloudQueueMessage qMsg = new CloudQueueMessage(msgPayload);
	        msgQ.addMessage(qMsg);
		} catch (InvalidKeyException | URISyntaxException | StorageException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public  String UploadFileToBlobStorage(String IncidentId, MultipartFile imageFile){
		CloudStorageAccount account;
		try {
			account = CloudStorageAccount.parse(blobStorageConnectionString);
			CloudBlobClient serviceClient = account.createCloudBlobClient();

			// Container name must be lower case.
			CloudBlobContainer container = serviceClient.getContainerReference(azureStorageContainer);
			container.createIfNotExists();

			// Set anonymous access on the container.
			BlobContainerPermissions containerPermissions = new BlobContainerPermissions();
			containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
			container.uploadPermissions(containerPermissions);

			// 
			CloudBlockBlob imgBlob = container.getBlockBlobReference(getIncidentBlobFilename(IncidentId,imageFile.getOriginalFilename()));
			imgBlob.getProperties().setContentType(imageFile.getContentType());
//			imgBlob.uploadProperties();
			imgBlob.upload(imageFile.getInputStream(),imageFile.getSize());
			UriBuilder builder = UriBuilder.fromUri(imgBlob.getUri());
			builder.scheme("https");
			log.info("uploaded blob:"+builder.build().toString());
			return builder.build().toString();
		} catch (InvalidKeyException | URISyntaxException | StorageException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;


	}

	private String getIncidentBlobFilename(String IncidentId,String FileName) {
		String fileExt = FilenameUtils.getExtension(FileName);
		// TODO check this against the .NET code - 
		//	with this code in, we're generating filenames that don't have a period between the incident id and the extension 
//		if (fileExt.startsWith(".")){
//			fileExt = fileExt.substring(1);
//		}
		return String.format("%s.%s", IncidentId,fileExt);
	}
}
