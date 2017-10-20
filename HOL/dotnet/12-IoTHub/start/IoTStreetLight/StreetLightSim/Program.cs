using Common.Devices;
using Common.Messages;
using Microsoft.Azure;
using Microsoft.Azure.Devices.Client;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace StreetLightSim
{
    class Program
    {
        private static readonly char SPLIT_CHAR = '=';
        private static IStreetlight currentLight = null;


        static void Main(string[] args)
        {
            Dictionary<string, string> argDictionary = null;
            try
            {
                //Create the device.
                if (args.Length == 0)
                {
                    System.Console.WriteLine("No device information passed as arguments. Using default legacy device.");
                    currentLight = new LegacyLight();
                }
                else
                {
                    //Parse the args passed in for the type of device
                    argDictionary = parseArgs(args);
                }

                SimulatorConfig lightConfig = new SimulatorConfig(argDictionary);

                StartSimulatorServer(lightConfig);

                Console.WriteLine("Exited!\n");
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error in sample: {0}", ex.Message);
            }
        }

        static Dictionary<string, string> parseArgs(string[] args)
        {
            var arguments = new Dictionary<string, string>();

            foreach (string argument in args)
            {
                string[] param = argument.Split(SPLIT_CHAR);

                if (param.Length == 2)
                {
                    arguments[param[0]] = param[1];
                }
            }
            return arguments;
        }


        static async Task SendLogEvent(DeviceClient deviceClient, LogMsg msg)
        {
            Message eventMessage = new Message(Encoding.UTF8.GetBytes(msg.ToString()));
            await deviceClient.SendEventAsync(eventMessage);

            Console.WriteLine($"SendLogMessage::{msg.ToString()}");
        }

        static async Task SendAlertEvent(DeviceClient deviceClient, AlertMsg msg)
        {
            Message eventMessage = new Message(Encoding.UTF8.GetBytes(msg.ToString()));
            await deviceClient.SendEventAsync(eventMessage);

            Console.WriteLine($"SendLogMessage::{msg.ToString()}");
        }

        static async Task SendTelemetryEvent(DeviceClient deviceClient, TelemetryMsg msg)
        {
            Message eventMessage = new Message(Encoding.UTF8.GetBytes(msg.ToString()));
            await deviceClient.SendEventAsync(eventMessage);

            Console.WriteLine($"SendLogMessage::{msg.ToString()}");
        }

        static async Task ReceiveCommands(DeviceClient deviceClient)
        {
            Console.WriteLine("\nDevice waiting for commands from IoTHub...\n");
            Message receivedMessage;
            string messageData;

            while (true)
            {
                receivedMessage = await deviceClient.ReceiveAsync(TimeSpan.FromSeconds(1));

                if (receivedMessage != null)
                {
                    messageData = Encoding.ASCII.GetString(receivedMessage.GetBytes());
                    Console.WriteLine("\t{0}> Received message: {1}", DateTime.Now.ToLocalTime(), messageData);

                    int propCount = 0;
                    foreach (var prop in receivedMessage.Properties)
                    {
                        Console.WriteLine("\t\tProperty[{0}> Key={1} : Value={2}", propCount++, prop.Key, prop.Value);
                    }

                    await deviceClient.CompleteAsync(receivedMessage);
                }
            }
        }

        static void ConnectionStatusChangeHandler(ConnectionStatus status, ConnectionStatusChangeReason reason)
        {
            Console.WriteLine();
            Console.WriteLine("Connection Status Changed to {0}", status);
            Console.WriteLine("Connection Status Changed Reason is {0}", reason);
            Console.WriteLine();
        }

        private static void StartSimulatorServer(SimulatorConfig config)
        {
            //create the device client
            DeviceClient deviceClient = DeviceClient.CreateFromConnectionString(CloudConfigurationManager.GetSetting("IOT_HUB_CONNECTION_STRING"));

            deviceClient.SetConnectionStatusChangesHandler(ConnectionStatusChangeHandler);

            // Method Call processing will be enabled when the first method handler is added.
            // setup a callback for the 'WriteToConsole' method
            deviceClient.SetMethodHandlerAsync("WriteToConsole", ToggleOnOff, null).Wait();
            deviceClient.SetMethodHandlerAsync("GetDeviceName", Blink, null).Wait();
            deviceClient.SetMethodHandlerAsync("GetDeviceName", GetDeviceInfo, null).Wait();

            Console.WriteLine("SimulatorServer started. Press any ESC to exit.");

            try
            {
                do
                {
                    switch (config.LightType)
                    {
                        case "SmartLight":
                            {
                                break;
                            }
                        case "CloudLight":
                            {
                                break;
                            }
                        case "LegacyLight":
                        default:
                            {
                                break;
                            }
                    }
                }
                while(Console.ReadKey(true).Key != ConsoleKey.Escape);

                Console.ReadLine();
                Console.WriteLine("Exiting...");

                // remove the 'WriteToConsole' handler
                deviceClient?.SetMethodHandlerAsync("ToggleOnOff", null, null).Wait();
                deviceClient?.SetMethodHandlerAsync("GetDeviceInfo", null, null).Wait();

                // remove the 'GetDeviceName' handler
                // Method Call processing will be disabled when the last method handler has been removed .
                deviceClient?.SetMethodHandlerAsync("Blink", null, null).Wait();

                deviceClient?.CloseAsync().Wait();
            }
            catch
            {
                // wait forever if there is no console
                Thread.Sleep(Timeout.Infinite);
            }
        }

    }
}