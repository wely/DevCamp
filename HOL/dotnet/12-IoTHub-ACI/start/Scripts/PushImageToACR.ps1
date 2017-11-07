$resourceGroupName = <your resource group name>
$acrName = <your acr name>

Login-AzureRmAccount
Select-AzureRmSubscription -Subscription <your subscription>

$acr = Get-AzureRmContainerRegistry -ResourceGroupName $resourceGroupName -RegistryName $acrName
$acrImageName = $acr.LoginServer + "/coresimulateddevice:v1"

docker tag coresimulateddevice $acrImage

$acrCreds = Get-AzureRmContainerRegistryCredential -RegistryName $acrName -ResourceGroupName $resourceGroupName

docker login $acr.LoginServer -u $acrCreds.Username -p $acrCreds.Password
docker push $acrImageName