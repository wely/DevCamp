using System;
using System.Collections.Generic;
using System.Text;
using Common.Simulators;
using Common.Messages;

namespace Common.Devices
{
    public abstract class BaseStreetLight : IStreetlight
    {
        public string Id { get; set; }
        public float InstallLocationLat { get; set; }
        public float InstallLocationLong { get; set; }
        public DeviceCapabilities Capabilities { get; set; }
        public DeviceSimulationPolicy SimulationPolicy { get; set; }
        public bool OnOff { get; set; }
        public DateTime LastTurnedOn {get; set; }
        public DateTime LastTurnedOff { get; set; }
        public LightBulb CurrentBulb { get; set; }
        public DateTime BulbLastChanged { get; set; }
        public long CurrentPowerConsumptionInWatts { get; set; }

        public abstract bool TurnOn();

        public abstract bool TurnOff();

        public abstract void Blink();
        public abstract AlertMsg GenerateAlert();

        public abstract TelemetryMsg GenerateTelemetry();

        public abstract LogMsg GenerateLog();

        public BaseStreetLight()
        {
            string newId= Guid.NewGuid().ToString();
            InitCapabilities();
        }

        public BaseStreetLight(string LightId)
        {
            Id = LightId;
            InitCapabilities();  
        }

        public BaseStreetLight(string LightId, DeviceCapabilities deviceCapabilities)
        {
            Id = LightId;
           Capabilities = deviceCapabilities;
        }

        protected abstract void LogEvent();

        protected virtual void InitCapabilities()
        {
            Capabilities = new DeviceCapabilities()
            {
                SupportsAutoSwitching = false,
                SupportsIntensityControl = false,
                SupportsRemoteShutoff = false
            };
        }

        public abstract void ChangeLightBulb(LightBulb newBulb);

        public abstract EventHandler OnBlink();

        public abstract EventHandler OnStateChanged();

        public abstract AlertMsg Alert();
        public abstract TelemetryMsg Read();

        public abstract LogMsg Log();
    }
}
