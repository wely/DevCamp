using System;
using System.Collections.Generic;
using System.Text;
using Common.Messages;

namespace Common.Devices
{
    public class LegacyLight : BaseStreetLight
    {
        public LegacyLight() : base()
        {
            InitCapabilities();
        }

        public LegacyLight(string LightId) : base(LightId)
        {
            Id = LightId;
            InitCapabilities();
        }

        public override AlertMsg Alert()
        {
            throw new NotImplementedException();
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
                SupportsAutoSwitching = false,
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
