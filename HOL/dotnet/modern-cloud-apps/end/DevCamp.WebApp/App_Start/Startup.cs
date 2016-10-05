using System;
using Owin;
using Microsoft.Owin.Security;
using Microsoft.Owin.Security.Cookies;
using Microsoft.Owin.Security.OpenIdConnect;
using System.Configuration;
using System.Globalization;
using Microsoft.IdentityModel.Protocols;
using Microsoft.Owin.Security.Notifications;
using System.Threading.Tasks;
using System.IdentityModel.Tokens;
using Microsoft.Owin;
using DevCamp.WebApp.App_Start;
using System.Web.Helpers;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.IdentityModel.Claims;
using Microsoft.Identity.Client;
using DevCamp.WebApp.Utils;
using ADAL = Microsoft.IdentityModel.Clients.ActiveDirectory;

[assembly: OwinStartup(typeof(Startup))]
namespace DevCamp.WebApp.App_Start
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            app.SetDefaultSignInAsAuthenticationType(CookieAuthenticationDefaults.AuthenticationType);

            app.UseCookieAuthentication(new CookieAuthenticationOptions());

            app.UseOpenIdConnectAuthentication(
                new OpenIdConnectAuthenticationOptions
                {
                    // The `Authority` represents the v2.0 endpoint - https://login.microsoftonline.com/common/v2.0
                    // The `Scope` describes the permissions that your app will need.  See https://azure.microsoft.com/documentation/articles/active-directory-v2-scopes/
                    // In a real application you could use issuer validation for additional checks, like making sure the user's organization has signed up for your app, for instance.

                    ClientId = ProfileHelper.ClientId,
                    Authority = String.Format(CultureInfo.InvariantCulture, ProfileHelper.AADInstance, "common", ""),
                    RedirectUri = ProfileHelper.RedirectUri,
                    Scope = ConfigurationManager.AppSettings["ida:GraphScopes"],
                    ResponseType = "code id_token",
                    PostLogoutRedirectUri = ProfileHelper.RedirectUri,
                    TokenValidationParameters = new TokenValidationParameters
                    {
                        ValidateIssuer = false
                    },
                    Notifications = new OpenIdConnectAuthenticationNotifications
                    {
                        AuthorizationCodeReceived = OnAuthorizationCodeReceived,
                        AuthenticationFailed = OnAuthenticationFailed
                    }
                });
        }

        private async Task OnAuthorizationCodeReceived(AuthorizationCodeReceivedNotification notification)
        {// Get the user's object id (used to name the token cache)

            string userObjId = notification.AuthenticationTicket.Identity
              .FindFirst("http://schemas.microsoft.com/identity/claims/objectidentifier").Value;

            // Create a token cache
            HttpContextBase httpContext = notification.OwinContext.Get<HttpContextBase>(typeof(HttpContextBase).FullName);
            SessionTokenCache tokenCache = new SessionTokenCache(userObjId, httpContext);

            // Exchange the auth code for a token
            ADAL.ClientCredential clientCred = new ADAL.ClientCredential(ProfileHelper.AppId, ProfileHelper.AppSecret);

            // Create the auth context
            ADAL.AuthenticationContext authContext = new ADAL.AuthenticationContext(
              string.Format(CultureInfo.InvariantCulture, ProfileHelper.AADInstance, "common", ""),
              false, tokenCache);

            ADAL.AuthenticationResult authResult = await authContext.AcquireTokenByAuthorizationCodeAsync(
              notification.Code, notification.Request.Uri, clientCred, "https://graph.microsoft.com");
        }

        private Task OnAuthenticationFailed(AuthenticationFailedNotification<OpenIdConnectMessage, OpenIdConnectAuthenticationOptions> notification)
        {
            notification.HandleResponse();
            notification.Response.Redirect("/Error?message=" + notification.Exception.Message);
            return Task.FromResult(0);
        }

    }
}