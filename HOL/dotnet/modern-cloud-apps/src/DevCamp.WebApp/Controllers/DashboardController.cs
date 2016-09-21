using IncidentAPI;
using IncidentAPI.Models;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;

namespace DevCamp.WebApp.Controllers
{
    public class DashboardController : Controller
    {

        public async Task<ActionResult> Index()
        {
            List<Incident> incidents;
            using (var client = GetIncidentAPIClient())
            {
                //TODO: Add caching here
                //Check Cache
                //If stale refresh

                var results = await client.Incident.GetAllIncidentsAsync();

                incidents = JsonConvert.DeserializeObject<List<Incident>>(results);
            }

            return View(incidents);
        }

        private static IncidentAPIClient GetIncidentAPIClient()
        {
            var client = new IncidentAPIClient(new Uri(ConfigurationManager.AppSettings["INCIDENT_API_URL"]));
            return client;
        }
    }
}