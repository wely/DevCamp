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
        [AllowAnonymous]
        public async Task<ViewResult> Index()
        {
            //##### API DATA HERE #####
            List<Incident> incidents;
            using (var client = IncidentApiHelper.GetIncidentAPIClient())
            {
                //##### Add caching here #####
                int CACHE_EXPIRATION_SECONDS = 60;

                //Check Cache
                string cachedData = string.Empty;
                if (RedisCacheHelper.UseCachedDataSet(Settings.REDISCCACHE_KEY_INCIDENTDATA, out cachedData))
                {
                    incidents = JsonConvert.DeserializeObject<List<Incident>>(cachedData);
                }
                else
                {
                    //If stale refresh
                    var results = await client.Incident.GetAllIncidentsAsync();
                    incidents = JsonConvert.DeserializeObject<List<Incident>>(results);
                    RedisCacheHelper.AddtoCache(Settings.REDISCCACHE_KEY_INCIDENTDATA, incidents, CACHE_EXPIRATION_SECONDS);
                }
                //##### Add caching here #####
            }
            return View(incidents);
            //##### API DATA HERE #####
        }
    }
}