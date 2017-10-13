using System;
using System.Collections.Generic;
using System.Text;

namespace StreetLightSim
{
    public class Simulatedlight
    {
        static Task<MethodResponse> ToggleOnOff(MethodRequest methodRequest, object userContext)
        {
            Console.WriteLine();
            Console.WriteLine("\t{0}", "Toggling");
            Console.WriteLine();

            return Task.FromResult(new MethodResponse(new byte[0], 200));
        }
        static Task<MethodResponse> Blink(MethodRequest methodRequest, object userContext)
        {
            Console.WriteLine();
            Console.WriteLine("\t{0}", "Blinking");
            Console.WriteLine();

            return Task.FromResult(new MethodResponse(new byte[0], 200));
        }

        static Task<MethodResponse> GetDeviceInfo(MethodRequest methodRequest, object userContext)
        {
            MethodResponse retValue;
            if (userContext == null)
            {
                retValue = new MethodResponse(new byte[0], 500);
            }
            else
            {
                string result = "{\"name\":\"" + currentLight.Id + "\"}";
                retValue = new MethodResponse(Encoding.UTF8.GetBytes(result), 200);
            }
            return Task.FromResult(retValue);
        }
        static async Task<string> selfRegisterAndSetConnString(string DeviceId)
        {
            Task<string> deviceConnString = null;
            try
            {
                registryManager = RegistryManager.CreateFromConnectionString(iotHubConnString);
                Device newDevice = new Device(DeviceId);

                await registryManager.AddDeviceAsync(newDevice);
                newDevice = await registryManager.GetDeviceAsync(DeviceId);
                newDevice.Authentication.SymmetricKey.PrimaryKey = CryptoKeyGenerator.GenerateKey(32);
                newDevice.Authentication.SymmetricKey.SecondaryKey = CryptoKeyGenerator.GenerateKey(32);
                newDevice = await registryManager.UpdateDeviceAsync(newDevice);

                string deviceInfo = String.Format("ID={0}\nPrimaryKey={1}\nSecondaryKey={2}", newDevice.Id, newDevice.Authentication.SymmetricKey.PrimaryKey, newDevice.Authentication.SymmetricKey.SecondaryKey);
                deviceConnString = Task.FromResult(string.Format("HostName={0};DeviceId={1};SharedAccessKey={2}", iotHubName, newDevice.Id, newDevice.Authentication.SymmetricKey.PrimaryKey));

            }
            catch (Exception ex)
            {
                Console.WriteLine("An error occured creating device:{0}", ex.ToString());
            }
            return deviceConnString.Result;
        }
    }
}
