using IncidentAPI;
using System;

namespace DevCamp.WebApp.Utils
{
    public class IncidentApiHelper
    {
        public static IncidentAPIClient GetIncidentAPIClient()
        {
            var client = new IncidentAPIClient(new Uri(Settings.INCIDENT_API_URL));
            return client;
        }
    }
}
