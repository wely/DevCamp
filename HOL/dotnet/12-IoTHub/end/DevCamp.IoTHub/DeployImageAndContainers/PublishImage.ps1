#
# PublishImage.ps1
#
$rgName = "rg_iot1"

Login-AzureRmAccount
Select-AzureRmSubscription -Subscription "Shadph's Azure"


$acr = Get-AzureRmContainerRegistry -ResourceGroupName rg_iot1 -Name acriot2
$loginServer = $acr.LoginServer
$creds = Get-AzureRmContainerRegistryCredential -RegistryName acriot2 -ResourceGroupName rg_iot1
docker login $acr.LoginServer -u $creds.Username -p $creds.Password
$image = $acr.LoginServer + "/coresimulateddevice:v1"
Docker push $acr.LoginServer + "/coresimulateddevice:v1"
#docker build -t coresimulateddevices . : to build the image
#docker tag coresimulateddevice acriot2.azurecr.io/coresimulateddevice:v1 : to tag the image for the ACR 
#docker run -e "DEVICE_NAME=k1tx1" -e "DEVICE_KEY=zEzZN41Fo/TjodvgBQJBUkX1yIhXmpy1ga8+Z6a8Rys=" -e "DEVICE_LATITUDE=32.924276" -e "DEVICE_LONGITUDE=-97.295136" coresimulateddevice k1tx1
#docker rmi to remove image and tagged images
#az acr repository list --name acriot2 : to list out the repositories
#az acr repository delete --name acriot2 --repository coresimulateddevice : if you need to remove and replace the image in the registry