# This is used to install Chocolatey packages that have checksum issues. 

# Ensure that current process can run scripts. 
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process -Force 

# Install Chocolatey
iwr https://chocolatey.org/install.ps1 -UseBasicParsing | iex

# Install NodeJS via Chocolatey
Write-Host "Installing package: Microsoft Azure Storage Explorer..."

# Install git via chocolatey.
choco install microsoftazurestorageexplorer --x86 --force --yes --acceptlicense --verbose --allow-empty-checksums --ignore-checksums | Out-Null  
if (-not $?)
{
    Write-Host "Installation of Microsoft Azure Storage Explorer failed via Chocolatey."
}
else {
    Write-Host "Successfully installed Microsoft Azure Storage Explorervia Chocolatey."
}