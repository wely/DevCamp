using DevCamp.WebApp.Utils;
using IncidentAPI;
using IncidentAPI.Models;
using Newtonsoft.Json;
using System.Collections.Generic;
using System.Threading.Tasks;
using System.Web.Mvc;

namespace DevCamp.WebApp.Controllers
{
    public class DashboardController : Controller
    {
        public async Task<ActionResult> Index()
        {
            List<Incident> incidents;
            using (var client = IncidentApiHelper.GetIncidentAPIClient())
            {
                //##### Add caching here #####
                string CACHE_KEY = "incidentdata";
                int CACHE_EXPIRATION_SECONDS = 60;

                //Check Cache
                string cachedData = string.Empty;
                if (CacheHelper.UseCachedDataSet(CACHE_KEY, out cachedData))
                {
                    incidents = JsonConvert.DeserializeObject<List<Incident>>(cachedData);
                }
                else
                {
                    //If stale refresh
                    var results = await client.Incident.GetAllIncidentsAsync();
                    incidents = JsonConvert.DeserializeObject<List<Incident>>(results);
                    CacheHelper.AddtoCache(CACHE_KEY, incidents, CACHE_EXPIRATION_SECONDS);
                }
                //##### Add caching here #####
            }

            return View(incidents);
        }
    }
}