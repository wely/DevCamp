Login-AzureRmAccount
Select-AzureRmSubscription -Subscription "Shadph's Azure"


$rgName = "rg_iot1"
$location = "westus"
$imageName = "acriot2.azurecr.io/coresimulateddevice:v1"
$deviceName = @("k1tx1", "k1tx2", "k1tx3", "k1tx4", "k1tx5", "k1tx6", "k1tx7", "k1tx8", "k1tx9", "sthlk1", "sthlk2", "sthlk3", "sthlk4", "sthlk5", "sthlk6", "sthlk7", "sthlk8", "sthlk9" )
$latitude   = @( "32.924276", "32.923664", "32.923556", "32.923556", "32.922763", "32.922079", "32.921502", "32.92107", "32.920566", "32.938589", "32.937923", "32.937455", "32.93668", "32.936338", "32.935366", "32.934321", "32.932736", "32.930341" )
$longitude  = @( "-97.295136", "-97.296081", "-97.29681", "-97.297626", "-97.297668", "-97.297497", "-97.296853", "-97.295866", "-97.294836", "-97.14298", "-97.143431", "-97.143753", "-97.144439", "-97.14474", "-97.144954", "-97.14489", "-97.144654", "-97.142723" )
$keys = @('zEzZN41Fo/TjodvgBQJBUkX1yIhXmpy1ga8+Z6a8Rys=', 'yGE6H3314QvXY85RHuRfQ5vUZ8Kl6XEuwTZgvTDHJP8=', '/RHiHoAQHPU0+iDGL0OEna1IsL0ONhxJRkXPCKogV8Q=', 'zDoO7yv7j9MDpuXcWh0i/1Wd3WdMDzcxzkxAN1qu5Nw=', 'BLlCxkC2tC5hUAdXZ+G/sEomWzYSw8D+sdvWFaZjLL8=', 'NSq/BzgQN+kHmruvK600DnCLiowGfNVzsXyf1zYuAhY=', 'ehh7pFeKxjtleubAyjyNeBn2W06bgTvyfAHMqe0EXsQ=', 'Knqd4XseBiEbsUNOpwa7gdTlldoPRkqOwcFtDgy1Clk=', 'd4BePx89saKfnspjhEgmN2DuzW5xrk104Hy1WuqLz58=', '6pN0GC1+I3icVX0pstGYyVpjBSMrCTXd6Zmz1As6EEM=', '6xMwIO01z+AMYfXlolow3NSkC4tY/hA8/HbEpiSC3oA=', 'oUUhLAKZoHYanXD2Zdz7IMePfHqa+iYNjejEA9oVrVA=', 'mec8sj9K7XvpbBzLLw3yaywUlwvxpnE+hxCEAt5z4/Y=', '3l3bWxxCMSPbnMGUVUfuWc51ghlDHmiZn4WoEZjKXTs=', 'R7FiB9Sa/7RBw1V45gT9ibZGeaTzncHRvFKxqLqjmGk=', 'zwrbOVr2K5rr+wZPhAMhpEnPfkMdqKlP4Jhg/YekyJU=', 'MpTSnRUhhRVB3WBoO396K7LZW+nKiv60RpA/bQ1dnd0=', '6rEk9RZ150guZ172Qo993xliewwisxjVE57d8IG+lMM=')

$acr = Get-AzureRmContainerRegistry -ResourceGroupName rg_iot1 -Name acriot2
$loginServer = $acr.LoginServer
$creds = Get-AzureRmContainerRegistryCredential -Registry $acr
$secpasswd = ConvertTo-SecureString $creds.Password -AsPlainText -Force
$acrcred = New-Object System.Management.Automation.PSCredential ($creds.Username, $secpasswd)

#Create all the containers
for($i=0; $i -lt 1; $i++)
#for($i=0; $i -lt $deviceName.Length; $i++)
{
    Write-Host "deviceName: " $deviceName[$i] 
    Write-Host "deviceLat: "  $latitude[$i] 
    Write-Host "deviceLong: " $longitude[$i] 
    Write-Host "key: " $keys[$i]
    $EnvironmentHash = @{"DEVICE_NAME"=$deviceName[$i];"DEVICE_KEY"=$keys[$i];"DEVICE_LATITUDE"=$latitude[$i];"DEVICE_LONGITUDE"=$longitude[$i]}

    New-AzureRmContainerGroup -ResourceGroupName $rgName -Name $deviceName[$i] -Image $imageName `
     -Location $location -OsType Linux -Cpu 1 -MemoryInGB 1 -IpAddressType Public -RegistryCredential $acrcred -EnvironmentVariable $EnvironmentHash
}

#Show the Console output from the first container
Get-AzureRmContainerInstanceLog -ContainerGroupName $deviceName[0] -ResourceGroupName $rgName

#Clean up Containers 
for($i=0; $i -lt $deviceName.Length; $i++)
{
    Remove-AzureRmContainerGroup -ResourceGroupName $rgName -Name $deviceName[$i]
}
