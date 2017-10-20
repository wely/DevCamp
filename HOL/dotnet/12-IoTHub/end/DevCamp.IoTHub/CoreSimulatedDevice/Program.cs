using System;
using System.Text;
using Microsoft.Azure.Devices.Client;
using Newtonsoft.Json;
using System.Threading.Tasks;

namespace CoreSimulatedDevice
{
    //https://docs.microsoft.com/en-us/dotnet/framework/docker/console#building-the-application
    //To publish .net core first https://stackoverflow.com/questions/43387693/build-docker-in-asp-net-core-no-such-file-or-directory-error 
    class Program
    {
        static DeviceClient deviceClient;
        static string iotHubUri = "shadphiothub2.azure-devices.net";
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

        private static async Task RecieveCloudMessage()
        {
            while (true)
            {
                Message receivedMessage = await deviceClient.ReceiveAsync();
                if (receivedMessage == null) continue;

                Console.ForegroundColor = ConsoleColor.Yellow;
                Console.WriteLine("Received message: {0}", Encoding.ASCII.GetString(receivedMessage.GetBytes()));
                Console.ResetColor();

                await deviceClient.CompleteAsync(receivedMessage);
            }
        }

        static void Main(string[] args)
        {
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
            RecieveCloudMessage().Wait();
        }
    }
}
