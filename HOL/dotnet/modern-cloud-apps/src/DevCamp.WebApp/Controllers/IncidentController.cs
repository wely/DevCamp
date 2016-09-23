using DevCamp.WebApp.Mappers;
using DevCamp.WebApp.Utils;
using DevCamp.WebApp.ViewModels;
using IncidentAPI;
using IncidentAPI.Models;
using Microsoft.WindowsAzure.Storage;
using Microsoft.WindowsAzure.Storage.Blob;
using Microsoft.WindowsAzure.Storage.Queue;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Configuration;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;

namespace DevCamp.WebApp.Controllers
{
    public class IncidentController : Controller
    {
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
            return View();
        }

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