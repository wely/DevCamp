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
        public static async Task<UserProfileViewModel> GetCurrentUserProfile(string AuthRedirectUrl)
        {
            string userObjId = System.Security.Claims.ClaimsPrincipal.Current.FindFirst("http://schemas.microsoft.com/identity/claims/objectidentifier").Value;

            SessionTokenCache tokenCache = new SessionTokenCache(userObjId, HttpContext.Current.Request.RequestContext.HttpContext);
            string tenantId = System.Security.Claims.ClaimsPrincipal.Current.FindFirst("http://schemas.microsoft.com/identity/claims/tenantid").Value;
            string authority = string.Format(Settings.AAD_INSTANCE, tenantId, "");
            AuthHelper authHelper = new AuthHelper(authority, Settings.AAD_APP_ID, Settings.AAD_APP_SECRET, tokenCache);

            string accessToken = await authHelper.GetUserAccessToken(AuthRedirectUrl);
            UserProfileViewModel userProfile = new UserProfileViewModel();
            using (var client = new HttpClient())
            {
                client.DefaultRequestHeaders.Accept.Clear();
                client.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", accessToken);
                client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));

                // New code:
                HttpResponseMessage response = await client.GetAsync(Settings.GRAPH_CURRENT_USER_URL);
                if (response.IsSuccessStatusCode)
                {
                    string resultString = await response.Content.ReadAsStringAsync();

                    userProfile = JsonConvert.DeserializeObject<UserProfileViewModel>(resultString);
                }
            }
            return userProfile;
        }
    }
}
