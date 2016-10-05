using DevCamp.WebApp.Models;
using IncidentAPI.Models;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;
using DevCamp.WebApp.Mappers;
using DevCamp.WebApp.Utils;
using DevCamp.WebApp.ViewModels;
using IncidentAPI;
using System.Security.Claims;
using System.Web.Mvc;
using Microsoft.Owin.Security;
using Microsoft.Owin.Security.Cookies;
using Microsoft.Owin.Security.OpenIdConnect;
using System.Web;
using Microsoft.Graph;
using Microsoft.Identity.Client;
using Microsoft.IdentityModel.Protocols;
using System.Configuration;
using System.Runtime.Serialization.Formatters.Binary;
using System.IO;

namespace DevCamp.WebApp.Utils
{
    public class EmailHelper
    {
        const string emailContent = @"
        <html>
        <head>
        <meta http-equiv='Content-Type' content='text/html; charset=us-ascii\'>
        <title></title>
        </head>
        <body style='font-family:Calibri' >
        <div style='width:50%;background-color:#CCC;padding:10px;margin:0 auto;text-align:center;'>
        <h1>City Power &amp; Light</h1>
        <h2>New Incident Reported by {0}</h2>
        <p>A new incident has been reported to the City Power &amp; Light outage system.</p>   
        <br />
        </div>
        </body>
        </html>";
        // Send an email message.
        // This snippet sends a message to the current user on behalf of the current user.
        public static async void SendIncidentEmail(Incident incidentData, string AuthRedirectUrl)
        {
            string userObjId = System.Security.Claims.ClaimsPrincipal.Current.FindFirst("http://schemas.microsoft.com/identity/claims/objectidentifier").Value;

            SessionTokenCache tokenCache = new SessionTokenCache(userObjId, HttpContext.Current.Request.RequestContext.HttpContext);
            string tenantId = System.Security.Claims.ClaimsPrincipal.Current.FindFirst("http://schemas.microsoft.com/identity/claims/tenantid").Value;
            string authority = string.Format(ProfileHelper.AADInstance, tenantId, "");
            AuthHelper authHelper = new AuthHelper(authority, ProfileHelper.AppId, ProfileHelper.AppSecret, tokenCache);

            string accessToken = await authHelper.GetUserAccessToken(AuthRedirectUrl);

            EmailMessage msg = getEmailContent(incidentData);

            using (var client = new HttpClient())
            {
                client.DefaultRequestHeaders.Accept.Clear();
                client.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", accessToken);
                client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));

                // New code:
                StringContent msgContent = new StringContent(JsonConvert.SerializeObject(msg.Message));
                HttpResponseMessage response = await client.PostAsync("https://graph.microsoft.com/v1.0/me/sendMail", msgContent);
                if (response.IsSuccessStatusCode)
                {
                    string resultString = await response.Content.ReadAsStringAsync();
                }
            }
        }

        private static EmailMessage getEmailContent(Incident incidentData)
        {
            EmailMessage msg = new EmailMessage();
            msg.Message.body.contentType = "HTML";
            msg.Message.body.content = string.Format(emailContent, incidentData.FirstName);
            msg.Message.subject = "New Incident Reported";
            Models.EmailAddress emailTo = new Models.EmailAddress() { name = "self", address = @"ivega@microsoft.com" };
            ToRecipient sendTo = new ToRecipient();
            sendTo.emailAddress = emailTo;
            msg.Message.toRecipients.Add(sendTo);
            msg.SaveToSentItems = true;
            return msg;
        }
    }
}
