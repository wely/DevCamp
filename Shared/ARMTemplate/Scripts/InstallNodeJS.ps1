# Ensure that current process can run scripts. 
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process -Force 

# Install NodeJS via Chocolatey
Write-Host "Installing package: NodeJS ..."

# Install git via chocolatey.
choco install nodejs.install --x86 --force --yes --acceptlicense --verbose --allow-empty-checksums | Out-Null  
if (-not $?)
{
    Write-Host "Installation of NodeJS failed via Chocolatey."
}
else {
    Write-Host "Successfully installed NodeJS via Chocolatey."
}