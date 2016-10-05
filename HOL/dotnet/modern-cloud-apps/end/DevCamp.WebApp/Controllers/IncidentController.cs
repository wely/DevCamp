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
using System.Net.Http;
using System.Net.Http.Headers;
using DevCamp.WebApp.Models;

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

        [Authorize]
        public async Task<ActionResult> Create()
        {
            //####### FILL IN THE DETAILS FOR THE NEW INCIDENT BASED ON THE USER
            UserProfileViewModel userProfile = await ProfileHelper.GetCurrentUserProfile(Url.Action("Index", "Profile", null, Request.Url.Scheme));
            IncidentViewModel incident = new IncidentViewModel();

            incident.FirstName = userProfile.GivenName;
            incident.LastName = userProfile.Surname;
            //####### 
            return View(incident);
        }

        [Authorize]
        [HttpPost]
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
                    RedisCacheHelper.ClearCache(RedisCacheHelper.CacheKeys.IncidentData);
                    //##### CLEAR CACHE ####

                    //##### SEND EMAIL #####
                    EmailHelper.SendIncidentEmail(incidentToSave, Url.Action("Index", "Profile", null, Request.Url.Scheme));
                    //##### SEND EMAIL  #####

                    //##### CREATE CALENDAR EVENT #####
                    //CalendarHelper.CreateIncidentEvent(incidentToSave, Url.Action("Index", "Profile", null, Request.Url.Scheme));
                    //##### CREATE CALENDAR EVENT #####

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