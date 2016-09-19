using DevCamp.API.Models;
using Microsoft.Azure.Documents;
using Microsoft.Azure.Documents.Client;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;

namespace DevCamp.API.Data
{
    public class DocumentDB
    {
        //Read config
        private static readonly string databaseId = ConfigurationManager.AppSettings["DOCUMENTDB_DATABASEID"];
        private static readonly string endpointUrl = ConfigurationManager.AppSettings["DOCUMENTDB_ENDPOINT"];
        private static readonly string authorizationKey = ConfigurationManager.AppSettings["DOCUMENTDB_PRIMARY_KEY"];
        private static readonly string collectionId = ConfigurationManager.AppSettings["DOCUMENTDB_COLLECTIONID"];


        private static readonly ConnectionPolicy connectionPolicy = new ConnectionPolicy { UserAgentSuffix = " samples-net/2" };

        //Reusable instance of DocumentClient which represents the connection to a DocumentDB endpoint
        private static Database docDatabase;
        private static DocumentClient client;
        private static DocumentCollection collection;

        public DocumentDB()
        {
            initDocDbConnection();
        }

        private void initDocDbConnection()
        {
            try
            {
                //Connect to DocumentDB
                using (client = new DocumentClient(new Uri(endpointUrl), authorizationKey))
                {
                    docDatabase = client.CreateDatabaseQuery().Where(db => db.Id == databaseId).AsEnumerable().FirstOrDefault();

                    //check if a database was returned
                    if (docDatabase == null)
                    {
                        //Get, or Create, the Database
                        docDatabase = client.CreateDatabaseAsync(new Database { Id = databaseId }).Result;
                    }

                    docDatabase = client.ReadDatabaseAsync(UriFactory.CreateDatabaseUri(databaseId)).Result;

                    //Get, or Create, the Document Collection
                    collection = client.ReadDocumentCollectionAsync(collectionId).Result;
                }
            }
            catch (DocumentClientException de)
            {
                Exception baseException = de.GetBaseException();
                Debug.WriteLine("{0} error occurred: {1}, Message: {2}", de.StatusCode, de.Message, baseException.Message);
            }
            catch (Exception e)
            {
                Exception baseException = e.GetBaseException();
                Debug.WriteLine("Error: {0}, Message: {1}", e.Message, baseException.Message);
            }
            finally
            {
            }
        }

        public Incident GetIncidentById(int Id)
        {
            Incident incident = null;
            return incident;
        }

        public ValidatedIncident GetValidatedIncidentById(int Id)
        {
            ValidatedIncident incident = null;
            return incident;
        }


        public int GetIncidentCount()
        {
            int docCount = 0;
            return docCount;
        }

        public static async Task<Document> CreateNewIncident(Incident newIncident)
        {
            Document newDocument = await client.CreateDocumentAsync(collectionId, newIncident);
            return newDocument;
        }

        public Incident ValidateIncident(Incident incidentToValidate)
        {
            Incident incident = null;
            return incident;

        }

        public static async Task<List<Document>> GetIncidents()
        {
            List<Document> allIncidents = new List<Document>();
            allIncidents = client.CreateDocumentQuery<Incident>(collection.SelfLink).All();
            return allIncidents;
        }
        public List<ValidatedIncident> GetValidatedIncidents()
        {
            List<ValidatedIncident> allIncidents = new List<ValidatedIncident>()
            {
                
            };
            return allIncidents;
        }
    }
}