using Common.Messages;
using Common.Simulators;
using System;
using System.Collections.Generic;
using System.Text;

namespace Common.Devices
{
    public interface IStreetlight
    {
        string Id { get; set; }

        //TODO: Move to the twin
        float InstallLocationLat { get; set; }
        //TODO: Move to the twin
        float InstallLocationLong { get; set; }
        DeviceSimulationPolicy SimulationPolicy { get; set; }
        DeviceCapabilities Capabilities { get; set; }
        bool OnOff { get; set; }
        DateTime LastTurnedOn { get; set; }
        DateTime LastTurnedOff { get; set; }
        LightBulb CurrentBulb { get; set; }
        DateTime BulbLastChanged { get; set; }
        long CurrentPowerConsumptionInWatts { get; set; }
        bool TurnOn();
        bool TurnOff();
        void Blink();
        void ChangeLightBulb(LightBulb newBulb);
        AlertMsg Alert();
        TelemetryMsg Read();
        LogMsg Log();

        EventHandler OnBlink();
        EventHandler OnStateChanged();
    }
}
