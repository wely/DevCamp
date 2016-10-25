package devCamp.WebApp.Utils;

import devCamp.WebApp.StorageAPIClient.StorageAPIClient;

public class StorageAPIHelper {

	public static StorageAPIClient getStorageAPIClient() {

		String account = System.getenv("AZURE_STORAGE_ACCOUNT");;
		String key = System.getenv("AZURE_STORAGE_ACCESS_KEY");;
		String queue = System.getenv("AZURE_STORAGE_QUEUE");
		String container = System.getenv("AZURE_STORAGE_BLOB_CONTAINER");

		return new StorageAPIClient(account, key,container,queue);	
	}
}