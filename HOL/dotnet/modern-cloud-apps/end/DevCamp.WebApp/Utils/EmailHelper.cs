using DevCamp.WebApp.Models;
using IncidentAPI.Models;
using Newtonsoft.Json;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Net.Mime;
using System.Threading.Tasks;
using System.Web;

namespace DevCamp.WebApp.Utils
{
    public class EmailHelper
    {

        // Send an email message.
        // This snippet sends a message to the current user on behalf of the current user.
        public static async Task SendIncidentEmail(Incident incidentData, string AuthRedirectUrl)
        {
            string userObjId = System.Security.Claims.ClaimsPrincipal.Current.FindFirst("http://schemas.microsoft.com/identity/claims/objectidentifier").Value;

            SessionTokenCache tokenCache = new SessionTokenCache(userObjId, HttpContext.Current.Request.RequestContext.HttpContext);
            string tenantId = System.Security.Claims.ClaimsPrincipal.Current.FindFirst("http://schemas.microsoft.com/identity/claims/tenantid").Value;
            string authority = string.Format(Settings.AAD_INSTANCE, tenantId, "");
            AuthHelper authHelper = new AuthHelper(authority, Settings.AAD_APP_ID, Settings.AAD_APP_SECRET, tokenCache);

            string accessToken = await authHelper.GetUserAccessToken(AuthRedirectUrl);

            EmailMessage msg = getEmailBodyContent(incidentData, userObjId);

            using (var client = new HttpClient())
            {
                client.DefaultRequestHeaders.Accept.Clear();
                client.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", accessToken);
                client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));

                // New code:
                StringContent msgContent = new StringContent(JsonConvert.SerializeObject(msg), System.Text.Encoding.UTF8, "application/json");
                msgContent.Headers.ContentType = new MediaTypeWithQualityHeaderValue("application/json");
                HttpResponseMessage response = await client.PostAsync(Settings.GRAPH_SENDMESSAGE_URL, msgContent);
                if (response.IsSuccessStatusCode)
                {
                    string resultString = await response.Content.ReadAsStringAsync();
                }
            }
        }

        private static EmailMessage getEmailBodyContent(Incident incidentData, string EmailFromAddress)
        {
            EmailMessage msg = new EmailMessage();
            msg.Message.body.contentType = Settings.EMAIL_MESSAGE_TYPE;
            msg.Message.body.content = string.Format(Settings.EMAIL_MESSAGE_BODY, incidentData.FirstName);
            msg.Message.subject = Settings.EMAIL_MESSAGE_SUBJECT;
            Models.EmailAddress emailTo = new Models.EmailAddress() { name = EmailFromAddress, address = EmailFromAddress };
            ToRecipient sendTo = new ToRecipient();
            sendTo.emailAddress = emailTo;
            msg.Message.toRecipients.Add(sendTo);
            msg.SaveToSentItems = true;
            return msg;
        }
    }
}
