using DevCamp.API.Data;
using DevCamp.API.Models;
using Microsoft.Azure.Documents;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web;
using System.Web.Http;

namespace DevCamp.API.Controllers
{
    public class IncidentController : ApiController
    {
        [HttpGet]
        [Route("incidents/{IncidentId}")]
        public async Task<IHttpActionResult> GetById(string IncidentId)
        {
            Incident incident = await DocumentDBRepository<Incident>.GetItemAsync(IncidentId);

            if (incident == null)
            {
                return NotFound();
            }
            return Ok(incident);
        }

        [HttpGet]
        [Route("incidents")]
        public async Task<IHttpActionResult> GetAllIncidents()
        {
            var incidents = await DocumentDBRepository<Incident>.GetItemsAsync(i => !i.Resolved);
            return Ok(incidents);
        }

        [HttpGet]
        [Route("incidents/count")]
        public int GetIncidentCount()
        {
            var incidentCount = DocumentDBRepository<Incident>.GetItemsCount();
            return incidentCount;
        }

        [HttpGet]
        [Route("incidents/count/includeresolved")]
        public int GetAllIncidentsCount()
        {
            var incidentCount = DocumentDBRepository<Incident>.GetItemsCount(true);
            return incidentCount;
        }

        [HttpPost]
        [Route("incidents/")]
        public async Task<IHttpActionResult> CreateIncident(Incident newIncident)
        {
            var newDocDbIncident = await DocumentDBRepository<Incident>.CreateItemAsync(newIncident);

            if (newDocDbIncident == null)
            {
                var resp = new HttpResponseMessage(HttpStatusCode.NotFound)
                {
                    Content = new StringContent("Unable to create incident"),
                    ReasonPhrase = "An error occurred"
                };
                throw new HttpResponseException(resp);
            }
            else
            {
                newIncident.Id = newDocDbIncident.Id;
                newIncident.Created = newDocDbIncident.Timestamp;
            }
            return Ok(newIncident);
        }
    }
}