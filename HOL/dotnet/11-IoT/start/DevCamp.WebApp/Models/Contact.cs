using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace DevCamp.WebApp.Models
{
	public class Contact
	{
		public String displayName { get; set; } = String.Empty;

		public EmailAddress[] emailAddresses { get; set; }
	}
}