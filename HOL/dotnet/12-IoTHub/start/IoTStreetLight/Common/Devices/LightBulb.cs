using System;
using System.Collections.Generic;
using System.Text;

namespace Common.Devices
{
    public class LightBulb
    {
        const long InitialOutputInWatts = 100;
        const long InitialLifetimeInMinutes = 30;
        bool isBulbOn = false;
        public long RuntimeInMinutes { get; set; }
        public long RemainingLifetimeInMinutes { get; set; }
        DateTime LastTurnedOn { get; set; }
        DateTime LastTurnedOff { get; set; }
        public long EstimatedLifetimeInMinutes { get; set; }
        public long CalculateRemainingLifetime()
        {
            return RemainingLifetimeInMinutes - RuntimeInMinutes;
        }

        public bool ToggleOnOff()
        {
            if (isBulbOn)
            {
                //Already on, turn it off
                isBulbOn = false;
                LastTurnedOff = DateTime.UtcNow;
            }
            else
            {
                isBulbOn = true;
                LastTurnedOn = DateTime.UtcNow;
                startUsage();
            }
            return isBulbOn;
        }

        private void startUsage()
        {
            
        }

        public long OutputInWatts { get; set; }

        public LightBulb()
        {
            RemainingLifetimeInMinutes = InitialLifetimeInMinutes;
            EstimatedLifetimeInMinutes = RuntimeInMinutes;

        }
    }
}
