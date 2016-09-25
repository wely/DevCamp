using DevCamp.WebApp.Mappers;
using DevCamp.WebApp.Utils;
using DevCamp.WebApp.ViewModels;
using IncidentAPI;
using IncidentAPI.Models;
using Newtonsoft.Json;
using System.Security.Claims;
using System.Web;
using System.Web.Mvc;
using System.Threading.Tasks;

namespace DevCamp.WebApp.Controllers
{
    public class IncidentController : Controller
    {
        [Authorize]
        public ActionResult Details(string Id)
        {
            IncidentViewModel incidentView = null;

            using (IncidentAPIClient client = IncidentApiHelper.GetIncidentAPIClient())
            {
                var result = client.Incident.GetById(Id);
                if (!string.IsNullOrEmpty(result))
                {
                    Incident incident = JsonConvert.DeserializeObject<Incident>(result);
                    incidentView = IncidentMappers.MapIncidentModelToView(incident);
                }
            }

            return View(incidentView);
        }


        public ActionResult Create()
        {
            //####### FILL IN THE DETAILS FOR THE NEW INCIDENT BASED ON THE USER
            // The object ID claim will only be emitted for work or school accounts at this time.
            //Claim oid = ClaimsPrincipal.Current.FindFirst("http://schemas.microsoft.com/identity/claims/objectidentifier");
            //ViewBag.ObjectId = oid == null ? string.Empty : oid.Value;

            // The 'preferred_username' claim can be used for showing the user's primary way of identifying themselves
            //ViewBag.Username = ClaimsPrincipal.Current.FindFirst("preferred_username").Value;

            // The subject or nameidentifier claim can be used to uniquely identify the user
            //ViewBag.Subject = ClaimsPrincipal.Current.FindFirst("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier").Value;



            //####### 
            return View();
        }

        [Authorize]
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> Create([Bind(Include = "City,Created,Description,FirstName,ImageUri,IsEmergency,LastModified,LastName,OutageType,PhoneNumber,Resolved,State,Street,ZipCode")] IncidentViewModel incident, HttpPostedFileBase imageFile)
        {
            try
            {
                if (ModelState.IsValid)
                {
                    Incident incidentToSave = IncidentMappers.MapIncidentViewModel(incident);

                    using (IncidentAPIClient client = IncidentApiHelper.GetIncidentAPIClient())
                    {
                        var result = client.Incident.CreateIncident(incidentToSave);
                        if (!string.IsNullOrEmpty(result))
                        {
                            incidentToSave = JsonConvert.DeserializeObject<Incident>(result);
                        }
                    }

                    //Now upload the file if there is one
                    if (imageFile != null && imageFile.ContentLength > 0)
                    {
                        //### Add Blob Upload code here #####
                        //Give the image a unique name based on the incident id
                        var imageUrl = await StorageHelper.UploadFileToBlobStorage(incidentToSave.ID, imageFile);
                        //### Add Blob Upload code here #####


                        //### Add Queue code here #####
                        //Add a message to the queue to process this image
                        await StorageHelper.AddMessageToQueue(incidentToSave.ID, imageFile.FileName);
                        //### Add Queue code here #####

                    }

                    //##### CLEAR CACHE ####
                    CacheHelper.ClearCache(CacheHelper.CacheKeys.IncidentData);
                    //##### CLEAR CACHE ####

                    return RedirectToAction("Index", "Dashboard");
                }
            }
            catch
            {
                return View();
            }

            return View(incident);
        }
    }
}