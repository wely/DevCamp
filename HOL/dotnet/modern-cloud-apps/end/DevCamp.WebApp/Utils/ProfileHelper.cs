using DevCamp.API.Models;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DevCamp.WebApp.Utils
{
    public class ProfileHelper
    {
        public static string clientId = ConfigurationManager.AppSettings["ida:ClientId"];
        public static string aadInstance = ConfigurationManager.AppSettings["ida:AADInstance"];
        public static string redirectUri = ConfigurationManager.AppSettings["ida:RedirectUri"];
        public const string TenantIdClaimType = "http://schemas.microsoft.com/identity/claims/tenantid";
        public static readonly string appKey = ConfigurationManager.AppSettings["ida:AppKey"];
        public readonly string graphResourceId = ConfigurationManager.AppSettings["ida:GraphUrl"];
        public readonly string graphUserUrl = "https://graph.windows.net/{0}/me?api-version=" + ConfigurationManager.AppSettings["ida:GraphApiVersion"];
        public static string appid => ConfigurationManager.AppSettings["ida:AppId"];
        public static string appSecret = ConfigurationManager.AppSettings["ida:AppSecret"];

        public static string AzureADAuthority = @"https://login.microsoftonline.com/common";
        public static string LogoutAuthority = @"https://login.microsoftonline.com/common/oauth2/logout?post_logout_redirect_uri=";
        public static string O365UnifiedAPIResource = @"https://graph.microsoft.com/";

        public static string SendMessageUrl = @"https://graph.microsoft.com/v1.0/me/microsoft.graph.sendmail";
        public static string GetMeUrl = @"https://graph.microsoft.com/v1.0/me";
        public static string MessageBody => ConfigurationManager.AppSettings["MessageBody"];
        public static string MessageSubject => ConfigurationManager.AppSettings["MessageSubject"];
        // Get the current user's profile.
   

    }
}
