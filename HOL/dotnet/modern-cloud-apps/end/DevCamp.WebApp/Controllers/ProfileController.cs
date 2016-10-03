using DevCamp.WebApp.ViewModels;
using Microsoft.Owin.Security;
using Microsoft.Owin.Security.Cookies;
using Microsoft.Owin.Security.OpenIdConnect;
using System.Security.Claims;
using System.Web;
using System.Web.Mvc;
using Microsoft.Graph;


namespace DevCamp.WebApp.Controllers
{
    //BASED ON THE SAMPLE https://azure.microsoft.com/en-us/documentation/articles/active-directory-v2-devquickstarts-dotnet-web/
    //AND https://github.com/microsoftgraph/aspnet-connect-rest-sample
    //AND https://github.com/microsoftgraph/aspnet-connect-sample <--DO NOT USE Uses MSAL Preview

    public class ProfileController : Controller
    {
        public void SignIn()
        {
            // Send an OpenID Connect sign-in request.
            if (!Request.IsAuthenticated)
            {
                HttpContext.GetOwinContext().Authentication.Challenge(new AuthenticationProperties { RedirectUri = "/" }, OpenIdConnectAuthenticationDefaults.AuthenticationType);
            }
        }

        // BUGBUG: Ending a session with the v2.0 endpoint is not yet supported.  Here, we just end the session with the web app.  
        public void SignOut()
        {
            if (Request.IsAuthenticated)
            {
                // Get the user's token cache and clear it.
                string userObjectId = ClaimsPrincipal.Current.FindFirst(ClaimTypes.NameIdentifier).Value;

            }

            // Send an OpenID Connect sign-out request. 
            HttpContext.GetOwinContext().Authentication.SignOut(
              CookieAuthenticationDefaults.AuthenticationType);

            // Send an OpenID Connect sign-out request.
            HttpContext.GetOwinContext().Authentication.SignOut(CookieAuthenticationDefaults.AuthenticationType);
            Response.Redirect("/");
        }

        [Authorize]
        //
        // GET: /UserProfile/
        public ActionResult Index()
        {
            UserProfileViewModel userProfile = new UserProfileViewModel();
            userProfile.Id = ClaimsPrincipal.Current.FindFirst(ClaimTypes.NameIdentifier).Value;
            // The object ID claim will only be emitted for work or school accounts at this time.
            Claim oid = ClaimsPrincipal.Current.FindFirst("http://schemas.microsoft.com/identity/claims/objectidentifier");

            // The 'preferred_username' claim can be used for showing the user's primary way of identifying themselves
            userProfile.DisplayName = ClaimsPrincipal.Current.FindFirst("preferred_username").Value;

            //TODO: Get these properties from Graph
            //userProfile.GivenName = ClaimsPrincipal.Current.FindFirst(ClaimTypes.GivenName).Value;
            //userProfile.MobilePhone = ClaimTypes.MobilePhone;
            //userProfile.Surname = ClaimTypes.Surname;
            ////userProfile.BusinessPhones = "";
            //userProfile.DisplayName = "";
            //userProfile.JobTitle = "";
            //userProfile.Mail = "";
            //userProfile.MobilePhone = "";
            //userProfile.OfficeLocation = "";
            //userProfile.PreferredLanguage = "";
            //userProfile.Surname = "";
            // The subject or nameidentifier claim can be used to uniquely identify the user
            //userProfile.UserPrincipalName = ClaimsPrincipal.Current.FindFirst(ClaimTypes.Upn).Value;

            return View(userProfile);
        }

    }
}