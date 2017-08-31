using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace DevCamp.WebApp.Models
{
	public class Time
	{
		public String dateTime { get; set; }
		public String timeZone { get; set; }
	}

	public class Location
	{
		public String displayName { get; set; } = String.Empty;
	}

	public class Attendee
	{
		public EmailAddress emailAddress { get; set; } = new EmailAddress();
		public String type { get; set; }
	}

	public class Event
	{
		public String subject { get; set; }

		public Body body { get; set; } = new Body();

		public Time start { get; set; } = new Time();

		public Time end { get; set; } = new Time();

		public Location location { get; set; } = new Location();

		public Attendee[] attendees { get; set; }
	}
}