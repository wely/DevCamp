package devCamp.WebApp.Utils;

import devCamp.WebApp.IncidentAPIClient.IncidentAPIClient;

public class IncidentAPIHelper {
    public static IncidentAPIClient getIncidentAPIClient() {

        String apiurl= System.getenv("INCIDENT_API_URL");
        return new IncidentAPIClient(apiurl);
    }
}