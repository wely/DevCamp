using System;
using System.Collections.Generic;
using System.Text;
using Common.Messages;

namespace Common.Devices
{
    public class SmartLight : BaseStreetLight
    {
        bool lastAlarmCCritical;
        List<AlertMsg> alerts = new List<AlertMsg>();

        public SmartLight() : base()
        {
            InitCapabilities();
        }

        public SmartLight(string LightId) : base(LightId)
        {
            Id = LightId;
            InitCapabilities();
        }

        public override AlertMsg Alert()
        {
            AlertMsg msg = getLastAlert();;
            return msg;
        }

        private AlertMsg getLastAlert()
        {
            return alerts[alerts.Count];
        }

        public override void Blink()
        {
            throw new NotImplementedException();
        }

        public override void ChangeLightBulb(LightBulb newBulb)
        {
            throw new NotImplementedException();
        }

        public override AlertMsg GenerateAlert()
        {
            throw new NotImplementedException();
        }

        public override LogMsg GenerateLog()
        {
            throw new NotImplementedException();
        }

        public override TelemetryMsg GenerateTelemetry()
        {
            throw new NotImplementedException();
        }

        public override LogMsg Log()
        {
            throw new NotImplementedException();
        }

        public override EventHandler OnBlink()
        {
            throw new NotImplementedException();
        }

        public override EventHandler OnStateChanged()
        {
            throw new NotImplementedException();
        }

        public override TelemetryMsg Read()
        {
            throw new NotImplementedException();
        }

        public override bool TurnOff()
        {
            throw new NotImplementedException();
        }

        public override bool TurnOn()
        {
            throw new NotImplementedException();
        }

        protected override void InitCapabilities()
        {
            base.Capabilities = new DeviceCapabilities()
            {
                SupportsAutoSwitching = true,
                SupportsIntensityControl = false,
                SupportsRemoteShutoff = false
            };
        }

        protected override void LogEvent()
        {
            throw new NotImplementedException();
        }
    }
}
