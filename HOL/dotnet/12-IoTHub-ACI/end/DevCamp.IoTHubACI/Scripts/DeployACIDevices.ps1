Login-AzureRmAccount
Select-AzureRmSubscription -Subscription "<your subscription here>"

$rgName = "<your resource group name"
$acrName = "<your acr name"
$location = "westus"
$imageName = "<your acr login server>/coresimulateddevice:v1"

$deviceName = @("k1tx1", "k1tx2", "k1tx3", "k1tx4", "k1tx5", "k1tx6", "k1tx7", "k1tx8", "k1tx9", "sthlk1", "sthlk2", "sthlk3", "sthlk4", "sthlk5", "sthlk6", "sthlk7", "sthlk8", "sthlk9" )

$latitude   = @( "32.924276", "32.923664", "32.923556", "32.923556", "32.922763", "32.922079", "32.921502", "32.92107", "32.920566", "32.938589", "32.937923", "32.937455", "32.93668", "32.936338", "32.935366", "32.934321", "32.932736", "32.930341" )

$longitude  = @( "-97.295136", "-97.296081", "-97.29681", "-97.297626", "-97.297668", "-97.297497", "-97.296853", "-97.295866", "-97.294836", "-97.14298", "-97.143431", "-97.143753", "-97.144439", "-97.14474", "-97.144954", "-97.14489", "-97.144654", "-97.142723" )


$keys = @('<device keys here>', '<device keys here>', '<device keys here>', '<device keys here>', '<device keys here>', '<device keys here>', '<device keys here>', '<device keys here>', '<device keys here>', '<device keys here>', '<device keys here>', '<device keys here>', '<device keys here>', '<device keys here>', '<device keys here>', '<device keys here>', '<device keys here>', '<device keys here>')

$acr = Get-AzureRmContainerRegistry -ResourceGroupName $rgName -Name $acrName
$loginServer = $acr.LoginServer
$acrCreds = Get-AzureRmContainerRegistryCredential -RegistryName $acrName -ResourceGroupName $rgName 
$secpasswd = ConvertTo-SecureString $acrCreds.Password -AsPlainText -Force
$acrcred = New-Object System.Management.Automation.PSCredential ($acrCreds.Username, $secpasswd)

##Create a single container to test
#Write-Host "deviceName: " $deviceName[0] 
#Write-Host "deviceLat: "  $latitude[0] 
#Write-Host "deviceLong: " $longitude[0] 
#Write-Host "key: " $keys[0]
#$EnvironmentHash = @{"IOTHUB_URI"=$loginServer;"DEVICE_NAME"=$deviceName[0];"DEVICE_KEY"=$keys[0];"DEVICE_LATITUDE"=$latitude[0];"DEVICE_LONGITUDE"=$longitude[0]}
#New-AzureRmContainerGroup -ResourceGroupName $rgName -Name $deviceName[$i] -Image $imageName `
#    -Location $location -OsType Linux -Cpu 1 -MemoryInGB 1 -IpAddressType Public -RegistryCredential $acrcred -EnvironmentVariable $EnvironmentHash
##Show the Console output from the first container
#Get-AzureRmContainerInstanceLog -ContainerGroupName $deviceName[0] -ResourceGroupName $rgName
##Remove the test ACI
#Remove-AzureRmContainerGroup -ResourceGroupName $rgName -Name $deviceName[0]


#Create all the containers
for($i=0; $i -lt $deviceName.Length; $i++)
{
    Write-Host "deviceName: " $deviceName[$i] 
    Write-Host "deviceLat: "  $latitude[$i] 
    Write-Host "deviceLong: " $longitude[$i] 
    Write-Host "key: " $keys[$i]
    $EnvironmentHash = @{"IOTHUB_URI"=$loginServer;"DEVICE_NAME"=$deviceName[$i];"DEVICE_KEY"=$keys[$i];"DEVICE_LATITUDE"=$latitude[$i];"DEVICE_LONGITUDE"=$longitude[$i]}
    New-AzureRmContainerGroup -ResourceGroupName $rgName -Name $deviceName[$i] -Image $imageName `
    -Location $location -OsType Linux -Cpu 1 -MemoryInGB 1 -IpAddressType Public -RegistryCredential $acrcred -EnvironmentVariable $EnvironmentHash
}


##Clean up Containers when done testing
for($i=0; $i -lt $deviceName.Length; $i++)
{
    Remove-AzureRmContainerGroup -ResourceGroupName $rgName -Name $deviceName[$i]
}
