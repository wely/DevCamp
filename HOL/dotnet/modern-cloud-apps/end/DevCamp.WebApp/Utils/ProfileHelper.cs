using DevCamp.WebApp.ViewModels;
using Newtonsoft.Json;
using System.Configuration;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Threading.Tasks;
using System.Web;

namespace DevCamp.WebApp.Utils
{
    public class ProfileHelper
    {
        public static string ClientId = ConfigurationManager.AppSettings["ida:ClientId"];
        public static string AADInstance = ConfigurationManager.AppSettings["ida:AADInstance"];
        public static string RedirectUri = ConfigurationManager.AppSettings["ida:RedirectUri"];
        public const string TenantIdClaimType = "http://schemas.microsoft.com/identity/claims/tenantid";
        public readonly string GraphResourceId = ConfigurationManager.AppSettings["ida:GraphUrl"];
        public readonly string GraphUserUrl = "https://graph.windows.net/{0}/me?api-version=" + ConfigurationManager.AppSettings["ida:GraphApiVersion"];
        public static string AppId = ConfigurationManager.AppSettings["ida:AppId"];
        public static string AppSecret = ConfigurationManager.AppSettings["ida:AppSecret"];
        public static string GraphScopes = ConfigurationManager.AppSettings["ida:GraphScopes"];
        
        public static string AzureADAuthority = @"https://login.microsoftonline.com/common";
        public static string LogoutAuthority = @"https://login.microsoftonline.com/common/oauth2/logout?post_logout_redirect_uri=";

        public static async Task<UserProfileViewModel> GetCurrentUserProfile(string AuthRedirectUrl)
        {
            string userObjId = System.Security.Claims.ClaimsPrincipal.Current.FindFirst("http://schemas.microsoft.com/identity/claims/objectidentifier").Value;

            SessionTokenCache tokenCache = new SessionTokenCache(userObjId, HttpContext.Current.Request.RequestContext.HttpContext);
            string tenantId = System.Security.Claims.ClaimsPrincipal.Current.FindFirst("http://schemas.microsoft.com/identity/claims/tenantid").Value;
            string authority = string.Format(ProfileHelper.AADInstance, tenantId, "");
            AuthHelper authHelper = new AuthHelper(authority, ProfileHelper.AppId, ProfileHelper.AppSecret, tokenCache);

            string accessToken = await authHelper.GetUserAccessToken(AuthRedirectUrl);
            UserProfileViewModel userProfile = new UserProfileViewModel();
            using (var client = new HttpClient())
            {
                client.DefaultRequestHeaders.Accept.Clear();
                client.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", accessToken);
                client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));

                // New code:
                HttpResponseMessage response = await client.GetAsync("https://graph.microsoft.com/v1.0/me");
                if (response.IsSuccessStatusCode)
                {
                    string resultString = await response.Content.ReadAsStringAsync();

                    userProfile = JsonConvert.DeserializeObject<UserProfileViewModel>(resultString);
                }
            }
            return userProfile;
        }

        public static string O365UnifiedAPIResource = @"https://graph.microsoft.com/";

        public static string SendMessageUrl = @"https://graph.microsoft.com/v1.0/me/microsoft.graph.sendmail";
        public static string GetMeUrl = @"https://graph.microsoft.com/v1.0/me";
        public static string MessageBody => ConfigurationManager.AppSettings["MessageBody"];
        public static string MessageSubject => ConfigurationManager.AppSettings["MessageSubject"];
        // Get the current user's profile.

        public static string SessionKeyAccessToken = "accesstoken";
        public static string SessionKeyUserInfo= "userinfo";


    }
}
