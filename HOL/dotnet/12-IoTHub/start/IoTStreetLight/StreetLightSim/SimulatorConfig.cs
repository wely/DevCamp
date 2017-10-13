using System;
using System.Collections.Generic;
using System.Text;

namespace StreetLightSim
{
    public class SimulatorConfig
    {
        const int DEFAULT_INTENSITY_IN_WATTS = 40;
        const string DEFAULT_LIGHT_TYPE = "SmartLight";
        private Dictionary<string, string> argDictionary;

        public SimulatorConfig(Dictionary<string, string> argDictionary)
        {
            this.argDictionary = argDictionary;
            LightType = argDictionary["lighttype"];
            IntensityInWatts = int.Parse(argDictionary["intensity"]);
            DeviceId = argDictionary["deviceid"];
            Validate();
        }

        public string DeviceId { get; set; }
        /// <summary>
        /// Specifies the light type to create. There are 3 kinds:
        /// Legacy
        /// SmartLight
        /// CloudLight
        /// </summary>
        public string LightType { get; set; }
        public int IntensityInWatts { get; set; }

        public void Validate()
        {
            if (LightType == null)
            {
                LightType = DEFAULT_LIGHT_TYPE;
            }

            if (LightType != "Legacy" || LightType != "SmartLight" || LightType != "CloudLight")
            {
                throw new ArgumentException($"Invalid light type. Supported types are 'Legacy', 'SmartLight' or 'CloudLight'. Conifig value was $LightType");
            }

            
            if (IntensityInWatts > 1000)
            {
                throw new ArgumentException($"Thats a bright light. Please us a value less than 1000. Config value is $IntensityInWatts");
            }

            if (IntensityInWatts < 0)
            {
                IntensityInWatts = DEFAULT_INTENSITY_IN_WATTS;
            }

            if (DeviceId == null)
            {
                DeviceId = Guid.NewGuid().ToString();
            }
        }
    }
}
