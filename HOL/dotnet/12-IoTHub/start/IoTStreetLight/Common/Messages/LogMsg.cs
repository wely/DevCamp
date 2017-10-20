using System;
using System.Collections.Generic;
using System.Text;

namespace Common.Messages
{
    public class LogMsg : BaseMsg
    {
        public string CurrentState { get; set; }
        public string CurrentId { get; set; }
        public override string ToString()
        {
            //TODO: Make this a JSON string
            return "DeviceId={DeviceId}";
        }
    }
}
