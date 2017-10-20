using Common.Enums;
using System;
using System.Collections.Generic;
using System.Text;

namespace Common.Messages
{
    public abstract class BaseMsg
    {
        public string DeviceId { get; set; }
        public string MessageId { get; set; }
        public DateTime MessageTimestamp { get; set; }

        public DeviceMessageType MessageType { get; set; }

       public BaseMsg()
        {
            MessageId = Guid.NewGuid().ToString();
            MessageTimestamp = DateTime.UtcNow;
        }
        public BaseMsg(string deviceId, DeviceMessageType msgType = DeviceMessageType.Log)
        {
            MessageId = Guid.NewGuid().ToString();
            DeviceId = DeviceId;
            MessageTimestamp = DateTime.UtcNow;
            MessageType = msgType;
        }

        public string Message { get; set; }
        public override string ToString()
        {
            //TODO: Make this a JSON string
            return "DeviceId={DeviceId}";
        }
    }
}
