using DevCamp.API.Models;
using Microsoft.Azure.Documents;
using Microsoft.Azure.Documents.Client;
using Microsoft.Azure.Documents.Linq;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Diagnostics;
using System.Linq;
using System.Linq.Expressions;
using System.Threading.Tasks;

namespace DevCamp.API.Data
{
    public class DocumentDBRepository<T> where T : class
    {
        private static string AUTHORIZATIONKEY = ConfigurationManager.AppSettings["DOCUMENTDB_PRIMARY_KEY"];
        private static string COLLECTIONID = ConfigurationManager.AppSettings["DOCUMENTDB_COLLECTIONID"];
        private static string DATABASEID = ConfigurationManager.AppSettings["DOCUMENTDB_DATABASEID"];
        private static string ENDPOINTURL = ConfigurationManager.AppSettings["DOCUMENTDB_ENDPOINT"];
        private static DocumentClient client;

        public static async Task<Document> CreateItemAsync(T item)
        {
            return await client.CreateDocumentAsync(UriFactory.CreateDocumentCollectionUri(DATABASEID, COLLECTIONID), item);
        }

        public static async Task DeleteItemAsync(string id)
        {
            await client.DeleteDocumentAsync(UriFactory.CreateDocumentUri(DATABASEID, COLLECTIONID, id));
        }

        public static async Task<T> GetItemAsync(string id)
        {
            try
            {
                Document document = await client.ReadDocumentAsync(UriFactory.CreateDocumentUri(DATABASEID, COLLECTIONID, id));
                return (T)(dynamic)document;
            }
            catch (DocumentClientException e)
            {
                if (e.StatusCode == System.Net.HttpStatusCode.NotFound)
                {
                    return null;
                }
                else
                {
                    throw;
                }
            }
        }

        public static async Task<IEnumerable<T>> GetItemsAsync(Expression<Func<T, bool>> predicate)
        {
            IDocumentQuery<T> query = client.CreateDocumentQuery<T>(
                UriFactory.CreateDocumentCollectionUri(DATABASEID, COLLECTIONID),
                new FeedOptions { MaxItemCount = -1 })
                .Where(predicate)
                .AsDocumentQuery();

            List<T> results = new List<T>();
            while (query.HasMoreResults)
            {
                results.AddRange(await query.ExecuteNextAsync<T>());
            }

            return results;
        }

        public static int GetItemsCount()
        {
            return GetItemsCount(false);
        }
        public static int GetItemsCount(bool IncludeResolved)
        {
            string excludeResolvedQuery = "SELECT c.id FROM c WHERE c.Resolved = false";
            string allQuery = "SELECT c.id FROM c";
            string query = excludeResolvedQuery;

            if (IncludeResolved)
            {
                query = allQuery;
            }

            var docList = client.CreateDocumentQuery<T>(
                UriFactory.CreateDocumentCollectionUri(DATABASEID, COLLECTIONID),
                query)
                .ToList();

            return docList.Count;
        }

        /// <summary>
        /// Default constructor
        /// </summary>
        public static void Initialize()
        {
            client = new DocumentClient(new Uri(ENDPOINTURL), AUTHORIZATIONKEY);
            CreateDatabaseIfNotExistsAsync(DATABASEID).Wait();
            CreateCollectionIfNotExistsAsync(DATABASEID, COLLECTIONID).Wait();
        }

        /// <summary>
        /// Overrides for testing
        /// </summary>
        /// <param name="EndpointUrl"></param>
        /// <param name="AuthKey"></param>
        /// <param name="DatabaseId"></param>
        /// <param name="CollectionId"></param>
        public static void Initialize(string EndpointUrl, string AuthKey, string DatabaseId, string CollectionId)
        {
            client = new DocumentClient(new Uri(EndpointUrl), AuthKey);
            CreateDatabaseIfNotExistsAsync(DatabaseId).Wait();
            CreateCollectionIfNotExistsAsync(DatabaseId, CollectionId).Wait();
        }

        public static async Task<Document> UpdateItemAsync(string id, T item)
        {
            return await client.ReplaceDocumentAsync(UriFactory.CreateDocumentUri(DATABASEID, COLLECTIONID, id), item);
        }
        private static async Task CreateCollectionIfNotExistsAsync(string databaseId, string collectionId)
        {
            try
            {
                await client.ReadDocumentCollectionAsync(UriFactory.CreateDocumentCollectionUri(databaseId, collectionId));
            }
            catch (DocumentClientException e)
            {
                if (e.StatusCode == System.Net.HttpStatusCode.NotFound)
                {
                    await client.CreateDocumentCollectionAsync(
                        UriFactory.CreateDatabaseUri(databaseId),
                        new DocumentCollection { Id = collectionId },
                        new RequestOptions { OfferThroughput = 1000 });
                }
                else
                {
                    throw;
                }
            }
        }

        private static async Task CreateDatabaseIfNotExistsAsync(string databaseId)
        {
            try
            {
                await client.ReadDatabaseAsync(UriFactory.CreateDatabaseUri(databaseId));
            }
            catch (DocumentClientException e)
            {
                if (e.StatusCode == System.Net.HttpStatusCode.NotFound)
                {
                    await client.CreateDatabaseAsync(new Database { Id = databaseId });
                }
                else
                {
                    throw;
                }
            }
        }
    }
}