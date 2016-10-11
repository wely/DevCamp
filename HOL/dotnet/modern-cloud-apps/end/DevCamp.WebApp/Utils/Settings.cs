using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Text;
using System.Web;

namespace DevCamp.WebApp.Utils
{
    public class Settings
    {
        //####    HOL 2    ######
        public static string INCIDENT_API_URL = ConfigurationManager.AppSettings["INCIDENT_API_URL"];
        public static string AZURE_STORAGE_ACCOUNT = ConfigurationManager.AppSettings["AZURE_STORAGE_ACCOUNT"];
        public static string AZURE_STORAGE_KEY = ConfigurationManager.AppSettings["AZURE_STORAGE_ACCESS_KEY"];
        public static string AZURE_STORAGE_BLOB_CONTAINER = ConfigurationManager.AppSettings["AZURE_STORAGE_BLOB_CONTAINER"];
        public static string AZURE_STORAGE_QUEUE = ConfigurationManager.AppSettings["AZURE_STORAGE_QUEUE"];
        public static string AZURE_STORAGE_CONNECTIONSTRING = String.Format("DefaultEndpointsProtocol=https;AccountName={0};AccountKey={1}", AZURE_STORAGE_ACCOUNT, AZURE_STORAGE_KEY);
        public static string REDISCCACHE_KEY_INCIDENTDATA = "incidentdata";

        public static string REDISCACHE_HOSTNAME = ConfigurationManager.AppSettings["REDISCACHE_HOSTNAME"];
        public static string REDISCACHE_PORT = ConfigurationManager.AppSettings["REDISCACHE_PORT"];
        public static string REDISCACHE_SSLPORT = ConfigurationManager.AppSettings["REDISCACHE_SSLPORT"];
        public static string REDISCACHE_PRIMARY_KEY = ConfigurationManager.AppSettings["REDISCACHE_PRIMARY_KEY"];
        public static string REDISCACHE_CONNECTIONSTRING = $"{REDISCACHE_HOSTNAME}:{REDISCACHE_SSLPORT},password={REDISCACHE_PRIMARY_KEY},abortConnect=false,ssl=true";

        //####    HOL 2   ######

        //####    HOL 3    ######
        // <add key = "AAD_APP_CLIENTID" value="4b14ee37-6a16-4aa7-ba81-3db330aeb648" />
        public static string AAD_APP_CLIENTID = ConfigurationManager.AppSettings["AAD_APP_CLIENTID"];

        //<add key = "AAD_INSTANCE" value="https://login.microsoftonline.com/{0}{1}" />
        public static string AAD_INSTANCE = ConfigurationManager.AppSettings["AAD_INSTANCE"];
        //<!--<add key = "AAD_APP_REDIRECTURI" value="https://localhost:40489/" />-->
        public static string AAD_APP_REDIRECTURI = ConfigurationManager.AppSettings["AAD_APP_REDIRECTURI"];
        public static string AAD_TENANTID_CLAIMTYPE = "http://schemas.microsoft.com/identity/claims/tenantid";
        //<!--<add key = "AAD_AUTHORITY" value="https://login.microsoftonline.com/common/" />-->
        public static string AAD_AUTHORITY = ConfigurationManager.AppSettings["AAD_AUTHORITY"];
        //<!--<add key = "AAD_LOGOUT_AUTHORITY" value="https://login.microsoftonline.com/common/oauth2/logout?post_logout_redirect_uri={0}" />-->
        public static string AAD_LOGOUT_AUTHORITY = ConfigurationManager.AppSettings["AAD_LOGOUT_AUTHORITY"];
        public static string GRAPH_API_URL = ConfigurationManager.AppSettings["GRAPH_API_URL"];
        //public static string GRAPH_USER_URL = "https://graph.windows.com/{0}/me?api-version=" + ConfigurationManager.AppSettings["ida:GraphApiVersion"];
        //<!--<add key = "AAD_APP_ID" value="4b14ee37-6a16-4aa7-ba81-3db330aeb648" />-->
        public static string AAD_APP_ID = ConfigurationManager.AppSettings["AAD_APP_ID"];
        //<!--<add key = "AAD_APP_SECRET" value="drZPnus9tmm77ZbAD2cU7ZjindcYu8fP/rirzeic4eE=" />-->
        public static string AAD_APP_SECRET = ConfigurationManager.AppSettings["AAD_APP_SECRET"];
        //<!--<add key = "AAD_GRAPH_SCOPES" value="openid email profile offline_access User.Read Mail.Send" />-->
        public static string AAD_GRAPH_SCOPES = ConfigurationManager.AppSettings["AAD_GRAPH_SCOPES"];

    //<!--<add key = "GRAPH_API_URL" value="https://graph.windows.net" />-->
    //<!--https://graph.windows.net/72f988bf-86f1-41af-91ab-2d7cd011db47-->

        public static string GRAPH_SENDMESSAGE_URL = GRAPH_CURRENT_USER_URL + "sendMail";
        public static string GRAPH_CURRENT_USER_URL = GRAPH_API_URL + "/v1.0/me";
        public static string SESSIONKEY_ACCESSTOKEN = "accesstoken";
        public static string SESSIONKEY_USERINFO = "userinfo";
        public static string EMAIL_MESSAGE_BODY = getEmailMessageBody();
        public static string EMAIL_MESSAGE_SUBJECT = "New Incident Reported";
        public static string EMAIL_MESSAGE_TYPE = "HTML";



        //####    HOL 3    ######

        //####    HOL 4   ######
        public static string APPINSIGHTS_KEY = ConfigurationManager.AppSettings["APPINSIGHTS_KEY"];
        //####    HOL 4   ######

        //####    HOL 3    ######
        static string getEmailMessageBody()
        {
            StringBuilder emailContent = new StringBuilder();
            emailContent.Append(@"<html><head><meta http-equiv='Content-Type' content='text/html; charset=us-ascii\'>");
            emailContent.Append(@"<title></title>");
            emailContent.Append(@"</head>");
            emailContent.Append(@"<body style='font-family:Calibri' > ");
            emailContent.Append(@"<div style='width:50%;background-color:#CCC;padding:10px;margin:0 auto;text-align:center;'> ");
            emailContent.Append(@"<h1>City Power &amp; Light</h1> ");
            emailContent.Append(@"<h2>New Incident Reported by {0}</h2> ");
            emailContent.Append(@"<p>A new incident has been reported to the City Power &amp; Light outage system.</p> ");
            emailContent.Append(@"<br /> ");
            emailContent.Append(@"</div> ");
            emailContent.Append(@"</body> ");
            emailContent.Append(@"</html>");
            return emailContent.ToString();
        }
    }
}