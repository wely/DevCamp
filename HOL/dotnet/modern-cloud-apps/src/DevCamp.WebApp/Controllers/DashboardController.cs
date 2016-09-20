using DevCamp.API.Models;
using System;
using System.Collections.Generic;
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
            API.APIClient webApi = new API.APIClient();
            API.IncidentOperations incIos = new API.IncidentOperations(webApi);
            Task<List<Incident>> incidents = await incIos.GetAllIncidentsWithOperationResponseAsync();



            return View(incidents);
        }


    }
}