using DevCamp.WebApp.Mappers;
using DevCamp.WebApp.ViewModels;
using IncidentAPI;
using IncidentAPI.Models;
using Microsoft.WindowsAzure.Storage;
using Microsoft.WindowsAzure.Storage.Blob;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Configuration;
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
            
            using (IncidentAPIClient client = GetIncidentAPIClient())
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

                    using (IncidentAPIClient client = GetIncidentAPIClient())
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
                        var imageUrl = await uploadFileToBlobStorage(incidentToSave.ID, imageFile);
                        //### Add Blob Upload code here #####


                        //### Add Queue code here #####
                        //Add a message to the queue to process this image

                        //### Add Queue code here #####

                    }
                    return RedirectToAction("Index", "Dashboard");
                }
            }
            catch
            {
                return View();
            }

            return View(incident);
        }

        /// <summary>
        /// Uploads a blob to the configured storage account
        /// </summary>
        /// <param name="IncidentId"></param>
        /// <param name="imageFile"></param>
        /// <returns></returns>
        private async Task<string> uploadFileToBlobStorage(string IncidentId, HttpPostedFileBase imageFile)
        {
            string account = ConfigurationManager.AppSettings["STORAGEACCOUNT_NAME"];
            string key = ConfigurationManager.AppSettings["STORAGEACCOUNT_KEY"];
            string IMAGECONTAINER_NAME = ConfigurationManager.AppSettings["IMAGECONTAINER_NAME"];
            string blobStorageConnectionString = String.Format("DefaultEndpointsProtocol=https;AccountName={0};AccountKey={1}", account, key);
            string imgUri = string.Empty;
            try
            {
                CloudStorageAccount storageAccount = CloudStorageAccount.Parse(blobStorageConnectionString);

                CloudBlobClient blobClient = storageAccount.CreateCloudBlobClient();
                CloudBlobContainer container = blobClient.GetContainerReference(IMAGECONTAINER_NAME);
                container.CreateIfNotExists();
                container.SetPermissions(new BlobContainerPermissions { PublicAccess = BlobContainerPublicAccessType.Blob });
                CloudBlockBlob imgBlob = container.GetBlockBlobReference(IncidentId);
                imgBlob.Properties.ContentType = imageFile.ContentType;

                await imgBlob.UploadFromStreamAsync(imageFile.InputStream);
                var uriBuilder = new UriBuilder(imgBlob.Uri);
                uriBuilder.Scheme = "https";
                imgUri = uriBuilder.ToString();
            }
            catch (Exception ex)
            {
                throw new HttpUnhandledException($"Unable to upload image for incident {IncidentId} to blob storage. Error:: ${ex.ToString()}");
            }
            return imgUri;
        }

        private static IncidentAPIClient GetIncidentAPIClient()
        {
            var client = new IncidentAPIClient(new Uri(ConfigurationManager.AppSettings["INCIDENT_API_URL"]));
            return client;
        }
    }
}