

# Developer Environment (Java)

## Overview
In this lab, you will set up an Office365 trial subscription, an Azure trial subscription, configure your Azure subscription for the DevCamp, and provision 
a virtual machine in the subscription to use for development of further labs.

## Objectives
In this hands-on lab, you will set up an Office365 developer subscription, and Azure trial subscription, and an Azure-based virutal machine for the development environment for subsequent labs in the DevCamp.  To expedite the process, we've prepared Windows and Linux images that you will copy into your own environment, start the virtual machine and connect to it.  You will then configure the components for Azure development.
* Set up an Office365 trial subscription
* Set up an Azure trial subscription
* Configure your Azure subscription for DevCamp
* Create an Azure Virtual Machine for development
* Connect to the Azure Virtual Machine and configure it for development.

## Prerequisites

The source for the starter app is located in the [TODO:ADD REF](#) folder. 

## Exercises
This hands-on-lab has the following exercises:
* Exercise 1: Set up Office 365 trial subscription
* Exercise 2: Set up Azure trial subscription
* Exercise 3: Configure your Azure subscription for DevCamp
* Exercise 4: Create an Azure Virtual Machine for development
* Exercise 5: Connect to the Azure Virtual Machine and configure it for development

### Exercise 1: Set up Office 365 trial subscription

1. In your browser, go to `https://products.office.com/en-us/business/office-365-enterprise-e3-business-software` 
and click the link that says "Free Trial", which will take you to this page:

    ![image](./media/2016-10-14_18-59-56.png)

1. Enter the information requested, and click `Just one more step` which 
    will take you to the following page:
    
    ![image](./media/2016-10-14_19-06-31.png)

1. Choose a userid and a `tenant name` for your trial.  In this case I chose `devcampross`, but you 
    can choose anything you'd like, as long as it is unique.  You'll also have to choose 
    a password, and click `Create my account`.  Next you will see this page, which asks
    for a phone verification:

    ![image](./media/2016-10-14_19-07-30.png)

1. After this process is complete, your O365 trial will be set up, and you'll see this page:
    ![image](./media/2016-10-18_12-39-13.png)

    Make note of your user id, which will be used to sign onto the Azure portal later.
    Click `You're ready to go`, which will take you to the following screen:
    ![image](./media/2016-10-18_12-42-47.png)

    Click on the `admin` app icon, which will open the Office365 admin center page
    in your broser.  It will look like this:

    ![image](./media/2016-10-14_18-38-17.png)

     If you'd like to see the welcome tour click `Next`, 
    or simply close the dialog box.   
    
    >Stay on this page until the next exercise.


### Exercise 2: Set up Azure trial subscription

1.  Next, we want to set up an Azure subscription, which
    can be accessed via the Azure Active Directory.  At the bottom of the screen, 
    click `Admin centers`, and then click `Azure AD`. This will open up a new browser tab
    showing this page:

    ![image](./media/2016-10-14_21-41-33.png)

1. Click on `Azure subscription`, that will take you to the page for creating a new 
    trial subscription:
    ![image](./media/2016-10-18_12-48-52.png)

    Enter the requested information and click `next`.

1. Enter the information about you, and verify your identity by phone.  Also you'll 
    need to verify via credit card.  Your credit card will not be charged unless you
    remove the spending cap from your subscription.
    
    ![image](./media/2016-10-14_21-42-46.png)

1. Finally after the verification process, you'll have to agree to the terms of the trial
    subscription: 

    ![image](./media/2016-10-14_21-44-53.png)

1. it will take a few moments to set up your azure subscription.  

    ![image](./media/2016-10-14_21-45-32.png)

1. When the subscription set up process is done, you can click on `Start managing my service`
to open the Azure portal, which will look like this:

    ![image](./media/2016-10-14_18-00-54.png)

### Exersize 3: Start your VSTS trial subscription

1. In your browser, go to `http://www.visualstudio.com/team-services`, and click on `sign in` in the upper right corner.  This should take you to a screen that looks like this:

    ![image](./media/2016-10-18_17-30-18.png)

    click on `Use your benefits`, and this window will pop up:

    ![image](./media/2016-10-18_17-42-01.png)

    Click Accept to access your Visual Studio Dev Essentials benefits.

### Exercise 3: Configure your Azure subscription for DevCamp

1. We have created an Azure Resource Group template that will configure the resources you need in Azure for the DevCamp.  To deploy 
these resources in your Azure subscription, do `control-click` on this button:

<a href="https://portal.azure.com/#create/Microsoft.Template/uri/https%3A%2F%2Fraw.githubusercontent.com%2FAzureCAT-GSI%2FDevCamp%2Fmaster%2FShared%2FARMTemplate%2FAzureDeploy.json" target="_blank">
    <img src="http://azuredeploy.net/deploybutton.png"/>
</a>

1. You should see a new tab open in your browser and open the Azure portal, with a blade that looks like this:

    ![image](./media/2016-10-18_13-13-25.png)

    Name your resource group `DevCamp`, choose a location for your deployment, check the box that indicates you agree to the terms and conditions, and click the `Purchase` button.

    The Resource Group template will start deploying, and the portal will pin a tile to the dashbord showing the status:

    ![image](./media/2016-10-18_13-19-01.png)

1. This will take approximately 20 minutes, and when done, go visit the resource group by clicking `resource groups on the left:

    ![image](./media/2016-10-18_13-36-29.png) 

    Then click your Resource group

    ![image](./media/2016-10-18_13-39-10.png)

### Exercise 4: Create an Azure Virtual Machine for development

1. The Azure resource group template will have created two virtual machines - one for 
Windows development, and the other for Linux development.  In these instructions, we
will focus on the Windows machine setup.  Feel free to use the Linux machine instead,
or to stop or delete it.

    Find the Windows virtual machine in your resource group, it's name will start with `windev`:

    ![image](./media/2016-10-18_15-32-30.png)



### Exercise 5: Connect to the Azure Virtual Machine and configure it for development

1. click the virtual machine, and then click `Connect` to connect to the machine using 
Remote Desktop:

    ![image](./media/2016-10-18_16-37-57.png)

    Use the credentials l-admin with password Devc@mp2016! to log onto the machine.  It
    would be wise to change the password in the virtual machine.

    When remote desktop is connected, you will see server manager initally.  We will
    want to turn of IE enhanced security.  First click local server;

    ![image](./media/2016-10-18_16-46-33.png)

    then click the `on` next to 

    ![image](./media/2016-10-18_16-50-50.png)

    A dialog box pops up, turn enhanced security off for administrators.

1. We are going to use git to clone the DevCamp github repository to this development machine.  Open `cmd`, change directory to the root, and do `git clone https://github.com/AzureCAT-GSI/DevCamp.git` :

    ![image](./media/2016-10-18_17-03-51.png)

    All of the content for this DevCamp will now be located in `c:\DevCamp\`.

    Start `visual studio 2015`, and in the sign in screen, click `sign in` and use the credentials you used earlier for Office 365.

    ![image](./media/2016-10-18_17-59-21.png)
    
    You can create a VSTS repository now, choose an appropriate repository name and click continue:

    ![image](./media/2016-10-18_18-07-34.png)

    Create your first team project, and name it `DevCamp`:

    ![image](./media/2016-10-18_18-10-39.png)

    Finally click close, and you are done with the Visual Studio setup.

1. We are now going to deploy our .NET API to an Azure App Service.  In the command window change the directory too the root with `cd \`, and clone the github repository for the API with `git clone https://github.com/AzureCAT-GSI/DevCampSharedDotNetAPI.git`.  

1. Switch back to Visual Studio and open the API solution with File/Open/Project/Solution:

    ![image](./media/2016-10-18_18-34-01.png)

    and open the API solution located at `C:\DevCampSharedDotNetAPI\src>`.  

    ![image](./media/2016-10-18_18-44-50.png)

    right click on the DevCamp API project, and choose `publish`:

    ![image](./media/2016-10-18_18-47-26.png)

    In the publish wizard, click `Microsoft Azure App Service` for the publish target:

    ![image](./media/2016-10-18_18-49-13.png)

    Next select `DevCamp` and then the `incidentapi...` app service, and click `OK`

    ![image](./media/2016-10-18_18-55-41.png)

    In the next step, leave all the defaults and click `Publish`:

    ![image](./media/2016-10-18_19-03-10.png)

    After a short time, you should see the `publish succeeded` message in the output window:

    ![image](./media/2016-10-18_19-05-52.png)
    
     

 npm install azure-cli -g


## Summary

In this hands-on lab, you learned how to:
* Set up an Office365 developer subscription
* Set up an Azure trial subscription
* Configure your Azure subscription for DevCamp
* Create an Azure Virtual Machine for development
* Connect to the Azure Virtual Machine and configure it for development.

Copyright 2016 Microsoft Corporation. All rights reserved. Except where otherwise noted, these materials are licensed under the terms of the MIT License. You may use them according to the license as is most appropriate for your project. The terms of this license can be found at https://opensource.org/licenses/MIT.
