using System;
using System.Collections.Generic;
using System.Text;

namespace Common.Messages
{
    public class TelemetryMsg : BaseMsg
    {
       public long CurrentPowerConsumptionInWatts { get; set; }

        public override string ToString()
        {
            //TODO: Make this a JSON string
            return "DeviceId={DeviceId}";
        }
    }
}
