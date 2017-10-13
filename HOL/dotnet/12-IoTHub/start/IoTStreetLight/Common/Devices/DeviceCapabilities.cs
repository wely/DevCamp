using System;
using System.Collections.Generic;
using System.Text;

namespace Common.Devices
{
    public class DeviceCapabilities
    {
        public bool SupportsAutoSwitching { get; set; }

        public bool SupportsIntensityControl { get; set; }

        public bool SupportsRemoteShutoff { get; set; }

        public bool CanGenerateAlerts { get; set; }
    }
}
