package devCamp.WebApp.Utils;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import devCamp.WebApp.IncidentAPIClient.IncidentAPIClient;

public class IncidentApiHelper {
	public static IncidentAPIClient getIncidentAPIClient() {
		
		String apiurl= System.getenv("INCIDENT_API_URL");
		IncidentAPIClient client = null;
		
		String uri = String.format("https://%s",apiurl);
		client = new IncidentAPIClient(uri,"");
		return client;
	}
}
