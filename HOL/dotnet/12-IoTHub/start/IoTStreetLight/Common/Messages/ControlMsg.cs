using System;
using System.Collections.Generic;
using System.Text;

namespace Common.Messages
{
    public class ControlMsg : BaseMsg
    {
        public string ControlType { get; set; }
        public bool MessageRecieved { get; set; }
        public bool MessageProcessed { get; set; }
        public override string ToString()
        {
            //TODO: Make this a JSON string
            return "DeviceId={DeviceId}";
        }
    }
}
