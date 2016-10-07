package devCamp.WebApp.Utils;

import org.apache.commons.io.FilenameUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.*;
import com.microsoft.azure.storage.queue.CloudQueue;
import com.microsoft.azure.storage.queue.CloudQueueClient;
import com.microsoft.azure.storage.queue.CloudQueueMessage;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;

import javax.ws.rs.core.UriBuilder;

public class StorageHelper {

	//configuration values from the system Environment
	private static String account;
	private static String key;
	private static String AZURE_STORAGE_BLOB_CONTAINER;
	private static String AZURE_STORAGE_QUEUE;
	private static String blobStorageConnectionString;
	
	public static void init() {
		
		account = System.getenv("AZURE_STORAGE_ACCOUNT");;
		key = System.getenv("AZURE_STORAGE_ACCESS_KEY");;
		AZURE_STORAGE_QUEUE = System.getenv("AZURE_STORAGE_QUEUE");
		AZURE_STORAGE_BLOB_CONTAINER = System.getenv("AZURE_STORAGE_BLOB_CONTAINER");
		blobStorageConnectionString = String.format("DefaultEndpointsProtocol=http;AccountName=%s;AccountKey=%s", account,key);
	}

	
	
    /// <summary>
    /// Adds an incident message to the queue
    /// </summary>
    /// <param name="IncidentId">The incident ID from the service</param>
    /// <param name="ImageFileName">The file name of the image</param>
    /// <returns></returns>
    public static void AddMessageToQueue(String IncidentId, String ImageFileName)
    {
    	init();
        CloudStorageAccount storageAccount;
		try {
			storageAccount = CloudStorageAccount.parse(blobStorageConnectionString);
        CloudQueueClient queueClient = storageAccount.createCloudQueueClient();

        CloudQueue msgQ = queueClient.getQueueReference(AZURE_STORAGE_QUEUE);
        msgQ.createIfNotExists();

        JSONObject json = new JSONObject();
        json.append("IncidentId", IncidentId);
        json.append("BlobContainerName", AZURE_STORAGE_BLOB_CONTAINER);
        json.append("BlobName", getIncidentBlobFilename(IncidentId, ImageFileName));

        String msgPayload = json.toString();
        CloudQueueMessage qMsg = new CloudQueueMessage(msgPayload);
        msgQ.addMessage(qMsg);
		} catch (InvalidKeyException | URISyntaxException | StorageException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public static String UploadFileToBlobStorage(String IncidentId, MultipartFile imageFile){
		init();
		CloudStorageAccount account;
		try {
			account = CloudStorageAccount.parse(blobStorageConnectionString);
			CloudBlobClient serviceClient = account.createCloudBlobClient();

			// Container name must be lower case.
			CloudBlobContainer container = serviceClient.getContainerReference(AZURE_STORAGE_BLOB_CONTAINER);
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
			return builder.toString();
		} catch (InvalidKeyException | URISyntaxException | StorageException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;


	}

	public static String getIncidentBlobFilename(String IncidentId,String FileName) {
		String fileExt = FilenameUtils.getExtension(FileName);
		// TODO check this against the .NET code - 
		//	with this code in, we're generating filenames that don't have a period between the incident id and the extension 
//		if (fileExt.startsWith(".")){
//			fileExt = fileExt.substring(1);
//		}
		return String.format("%s%s", IncidentId,fileExt);
	}
}
