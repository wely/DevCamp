using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Azure.Devices;
using Microsoft.Azure.Devices.Common.Exceptions;

namespace CreateDevices
{
    class Program
    {
        static RegistryManager registryManager;
        static string connectionString = "<your iot hub connection string here>";
        static string[] deviceName = new string[18] { "k1tx1", "k1tx2", "k1tx3", "k1tx4", "k1tx5", "k1tx6", "k1tx7", "k1tx8", "k1tx9", "sthlk1", "sthlk2", "sthlk3", "sthlk4", "sthlk5", "sthlk6", "sthlk7", "sthlk8", "sthlk9" };
        static string[] latitude = new string[18] { "32.924276", "32.923664", "32.923556", "32.923556", "32.922763", "32.922079", "32.921502", "32.92107", "32.920566", "32.938589", "32.937923", "32.937455", "32.93668", "32.936338", "32.935366", "32.934321", "32.932736", "32.930341" };
        static string[] longitude = new string[18] { "-97.295136", "-97.296081", "-97.29681", "-97.297626", "-97.297668", "-97.297497", "-97.296853", "-97.295866", "-97.294836", "-97.14298", "-97.143431", "-97.143753", "-97.144439", "-97.14474", "-97.144954", "-97.14489", "-97.144654", "-97.142723" };

        private static async Task AddDeviceAsync(string name, string lat, string lon)
        {
            string deviceId = name;
            Device device;
            try
            {
                device = await registryManager.AddDeviceAsync(new Device(deviceId));
            }
            catch (DeviceAlreadyExistsException)
            {
                device = await registryManager.GetDeviceAsync(deviceId);
            }
            Console.Write("'{0}', ", device.Authentication.SymmetricKey.PrimaryKey);
        }

        static void Main(string[] args)
        {
            registryManager = RegistryManager.CreateFromConnectionString(connectionString);
            for (int i = 0; i < deviceName.Length; i++)
            {
                AddDeviceAsync(deviceName[i], latitude[i], longitude[i]).Wait();
            }
            Console.ReadLine();
        }
    }
}
