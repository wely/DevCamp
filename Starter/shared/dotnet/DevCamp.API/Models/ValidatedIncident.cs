using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DevCamp.API.Models
{
    public class ValidatedIncident : Incident
    {
        public string AssignedTo { get; set; }
        public DateTime? DateAssigned { get; set; }
    }
}
