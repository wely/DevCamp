using Microsoft.WindowsAzure.Storage;
using Microsoft.WindowsAzure.Storage.Blob;
using Microsoft.WindowsAzure.Storage.Queue;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Configuration;
using System.IO;
using System.Threading.Tasks;
using System.Web;

namespace DevCamp.WebApp.Utils
{
    public class StorageHelper
    {
        static string account = ConfigurationManager.AppSettings["AZURE_STORAGE_ACCOUNT"];
        static string key = ConfigurationManager.AppSettings["AZURE_STORAGE_ACCESS_KEY"];
        static string AZURE_STORAGE_BLOB_CONTAINER = ConfigurationManager.AppSettings["AZURE_STORAGE_BLOB_CONTAINER"];
        static string AZURE_STORAGE_QUEUE = ConfigurationManager.AppSettings["AZURE_STORAGE_QUEUE"];
        static string blobStorageConnectionString = String.Format("DefaultEndpointsProtocol=https;AccountName={0};AccountKey={1}", account, key);

        /// <summary>
        /// Adds an incident message to the queue
        /// </summary>
        /// <param name="IncidentId">The incident ID from the service</param>
        /// <param name="ImageFileName">The file name of the image</param>
        /// <returns></returns>
        public static async Task AddMessageToQueue(string IncidentId, string ImageFileName)
        {
            CloudStorageAccount storageAccount = CloudStorageAccount.Parse(blobStorageConnectionString);
            CloudQueueClient queueClient = storageAccount.CreateCloudQueueClient();
            CloudQueue msgQ = queueClient.GetQueueReference(AZURE_STORAGE_QUEUE);
            msgQ.CreateIfNotExists();

            JObject qMsgJson = new JObject();
            qMsgJson.Add("IncidentId", IncidentId);
            qMsgJson.Add("BlobContainerName", AZURE_STORAGE_BLOB_CONTAINER);
            qMsgJson.Add("BlobName", getIncidentBlobFilename(IncidentId, ImageFileName));

            var qMsgPayload = JsonConvert.SerializeObject(qMsgJson);
            CloudQueueMessage qMsg = new CloudQueueMessage(qMsgPayload);

            await msgQ.AddMessageAsync(qMsg);
        }

        /// <summary>
        /// Uploads a blob to the configured storage account
        /// </summary>
        /// <param name="IncidentId">The IncidentId the image is associated with</param>
        /// <param name="imageFile">The File</param>
        /// <returns>The Url to the blob</returns>
        public static async Task<string> UploadFileToBlobStorage(string IncidentId, HttpPostedFileBase imageFile)
        {
            string imgUri = string.Empty;

            try
            {
                CloudStorageAccount storageAccount = CloudStorageAccount.Parse(blobStorageConnectionString);

                CloudBlobClient blobClient = storageAccount.CreateCloudBlobClient();
                CloudBlobContainer container = blobClient.GetContainerReference(AZURE_STORAGE_BLOB_CONTAINER);
                container.CreateIfNotExists();
                container.SetPermissions(new BlobContainerPermissions { PublicAccess = BlobContainerPublicAccessType.Blob });

                CloudBlockBlob imgBlob = container.GetBlockBlobReference(getIncidentBlobFilename(IncidentId, imageFile.FileName));
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

        private static string getIncidentBlobFilename(string IncidentId, string FileName)
        {
            string fileExt = Path.GetExtension(FileName);
            //Remove the starting . if exists
            if (fileExt.StartsWith("."))
            {
                fileExt.TrimStart(new char[] { '.' });
            }
            return $"{IncidentId}{fileExt}";
        }
    }
}