using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace DevCamp.API.Models
{
    public class Incident
    {
        [JsonProperty("id")]
        public string Id { get; internal set; }
        public string Description { get; set; }
        public string Street { get; set; }
        public string City { get; set; }
        public string State { get; set; }
        public string ZipCode { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string PhoneNumber { get; set; }
        public string OutageType { get; set; }
        public bool IsEmergency { get; set; }
        public bool Resolved { get; set; }
        public Uri ImageUri { get; set; }
        public DateTime? Created { get; internal set; }
        public DateTime? LastModified { get; internal set; }

        public Incident()
        {
            DateTime utcNow = DateTime.UtcNow;
            Created = utcNow;
            LastModified = utcNow;
            Id = Guid.NewGuid().ToString();
        }
    }
}