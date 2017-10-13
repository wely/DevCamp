using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Common.Devices;

namespace StreetLightTests.LightTests
{
    [TestClass]
    public class LegacyLightTest
    {
        [TestMethod]
        public void CanCreateLegacyLight()
        {
            LegacyLight currentLight = new LegacyLight();
            
        }
    }
}
