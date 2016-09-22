using Newtonsoft.Json;
using StackExchange.Redis;
using System;
using System.Configuration;

namespace DevCamp.WebApp.Utils
{
    public class CacheHelper
    {
        static string REDISCACHE_HOSTNAME = ConfigurationManager.AppSettings["REDISCACHE_HOSTNAME"];
        static string REDISCACHE_PORT = ConfigurationManager.AppSettings["REDISCACHE_PORT"];
        static string REDISCACHE_SSLPORT = ConfigurationManager.AppSettings["REDISCACHE_SSLPORT"];
        static string REDISCACHE_PRIMARY_KEY = ConfigurationManager.AppSettings["REDISCACHE_PRIMARY_KEY"];
        static string redisCacheConnectionString = $"{REDISCACHE_HOSTNAME}:{REDISCACHE_SSLPORT},password={REDISCACHE_PRIMARY_KEY},abortConnect=false,ssl=true";

        private static Lazy<ConnectionMultiplexer> lazyConnection = new Lazy<ConnectionMultiplexer>(() =>
        {
            return ConnectionMultiplexer.Connect(redisCacheConnectionString);
        });

        static ConnectionMultiplexer CacheConnection
        {
            get
            {
                return lazyConnection.Value;
            }
        }

        public static string GetDataFromCache(string CacheKey)
        {
            string cachedData = string.Empty;
            IDatabase cache = CacheConnection.GetDatabase();

            cachedData = cache.StringGet(CacheKey);
            return cachedData;
        }

        public static bool UseCachedDataSet(string CacheKey, out string CachedData)
        {
            bool retVal = false;
            CachedData = string.Empty;
            IDatabase cache = CacheConnection.GetDatabase();
            if (cache.Multiplexer.IsConnected)
            {
                if (cache.KeyExists(CacheKey))
                {
                    CachedData = GetDataFromCache(CacheKey);
                    retVal = true;
                }
            }
            return retVal;
        }

        public static void AddtoCache(string CacheKey, object ObjectToCache, int CacheExpiration = 60)
        {
            IDatabase cache = CacheConnection.GetDatabase();

            cache.StringSet(CacheKey, JsonConvert.SerializeObject(ObjectToCache), TimeSpan.FromSeconds(CacheExpiration));
        }

    }
}
