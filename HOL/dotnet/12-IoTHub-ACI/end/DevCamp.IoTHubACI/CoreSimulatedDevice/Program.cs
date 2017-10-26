using System;
using System.Text;
using Microsoft.Azure.Devices.Client;
using Newtonsoft.Json;
using System.Threading.Tasks;

namespace CoreSimulatedDevice
{
    class Program
    {

        static DeviceClient deviceClient;
        static string iotHubUri = null;
        static string deviceKey = null;
        static string deviceName = null;
        static string deviceLatitude = null;
        static string deviceLongitude = null;

        private static async Task SendDeviceToCloudMessagesAsync()
        {
            double minTemperature = 20;
            double minHumidity = 60;
            int messageId = 1;
            Random rand = new Random();

            while (true)
            {
                double currentTemperature = minTemperature + rand.NextDouble() * 15;
                double currentHumidity = minHumidity + rand.NextDouble() * 20;

                var telemetryDataPoint = new
                {
                    messageId = messageId++,
                    timestamp = System.DateTime.UtcNow.ToString(),
                    deviceId = deviceName,
                    temperature = currentTemperature,
                    humidity = currentHumidity,
                    longitude = deviceLatitude,
                    latitude = deviceLongitude
                };
                var messageString = JsonConvert.SerializeObject(telemetryDataPoint);
                var message = new Message(Encoding.ASCII.GetBytes(messageString));
                message.Properties.Add("temperatureAlert", (currentTemperature > 30) ? "true" : "false");

                Console.WriteLine("{0} > Sending message: {1}", DateTime.Now, messageString);

                await deviceClient.SendEventAsync(message);
                await Task.Delay(1000);
            }
        }
        static void Main(string[] args)
        {
            iotHubUri = Environment.GetEnvironmentVariable("IOTHUB_URI");
            deviceName = Environment.GetEnvironmentVariable("DEVICE_NAME");
            deviceKey = Environment.GetEnvironmentVariable("DEVICE_KEY");
            deviceLatitude = Environment.GetEnvironmentVariable("DEVICE_LATITUDE");
            deviceLongitude = Environment.GetEnvironmentVariable("DEVICE_LONGITUDE");

            Console.WriteLine("Device Name: " + deviceName);
            Console.WriteLine("Device Key: " + deviceKey);
            Console.WriteLine("Device Latitude: " + deviceLatitude);
            Console.WriteLine("Device Longitude: " + deviceLongitude);

            deviceClient = DeviceClient.Create(iotHubUri, new DeviceAuthenticationWithRegistrySymmetricKey(deviceName, deviceKey), TransportType.Mqtt);
            SendDeviceToCloudMessagesAsync().Wait();

        }
    }
}
