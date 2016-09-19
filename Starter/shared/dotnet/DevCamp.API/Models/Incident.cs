using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace DevCamp.API.Models
{
    public class Incident
    {
        public int Id { get; set; }
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
        public Uri ImageUri { get; set; }
        public DateTime? Created { get; set; }
        public DateTime? LastModified { get; set; }
    }
}