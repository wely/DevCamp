using System;
using System.Collections.Generic;
using System.Text;

namespace Common.Messages
{
    public class AlertMsg : BaseMsg
    {
        public string Alert { get; set; }
        public bool IsCritical { get; set; }

        public override string ToString()
        {
            //TODO: Make this a JSON string
            return "DeviceId={DeviceId}";
        }
    }
}
