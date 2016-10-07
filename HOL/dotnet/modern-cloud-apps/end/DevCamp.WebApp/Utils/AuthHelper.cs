using System.Threading.Tasks;
using System.Web;
using Microsoft.IdentityModel.Clients.ActiveDirectory;
using Microsoft.Owin.Security;
using Microsoft.Owin.Security.OpenIdConnect;

namespace DevCamp.WebApp.Utils
{
    public class AuthHelper
    {
        public string Authority { get; set; }
        public string AppId { get; set; }
        public string AppSecret { get; set; }
        public SessionTokenCache TokenCache { get; set; }

        public AuthHelper(string authority, string appId, string appSecret, SessionTokenCache tokenCache)
        {
            Authority = authority;
            AppId = appId;
            AppSecret = appSecret;
            TokenCache = tokenCache;
        }

        public async Task<string> GetUserAccessToken(string redirectUri)
        {
            AuthenticationContext authContext = new AuthenticationContext(Authority, false, TokenCache);

            ClientCredential credential = new ClientCredential(AppId, AppSecret);

            try
            {
                AuthenticationResult authResult = await authContext.AcquireTokenSilentAsync(Settings.GRAPH_API_URL, credential,
                  new UserIdentifier(TokenCache.UserObjectId, UserIdentifierType.UniqueId));
                return authResult.AccessToken;
            }
            catch (AdalSilentTokenAcquisitionException)
            {
                HttpContext.Current.Request.GetOwinContext().Authentication.Challenge(
                  new AuthenticationProperties() { RedirectUri = redirectUri },
                  OpenIdConnectAuthenticationDefaults.AuthenticationType);

                return null;
            }
        }
    }
}