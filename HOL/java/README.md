# DevCamp
Contains Java content for the Azure EDU partner DevCamp world tour 2016-2017

 The current status is:

   * **Developer-environment**  in progress
   * **Modern-cloud-apps** Complete
   * **devops-ci** Complete
   * **Azuread-office365** In Progress
   * **appinsights** completed up to custom event handler.  need to finish up after azuread portion is complete
   * **Arm-CD** duplicate of others

 Scheduled for release in November 2016

These labs and content will kickstart your Azure knowledge with a combination of lectures and hands on labs. It is intended to be delivered in a classroom environment but feel free to reuse this content.

---
### Module 0 - Introduction, roadmap and course overview ####
In this session, we will provide a brief history of Azure, a quick overview of the capabilities available and introduction to the 2-day interactive workshop.

[View PowerPoint](../../Presentation/Module00-Overview.pptx?raw=true)

---
## [Hands on Labs overview](../../HOL/README.md) ####
This provides and overview of each hands on lab exercise.

[View PowerPoint](../../Presentation/Module00-Hols.pptx?raw=true)

---

## Module 1 - Tools and Developer Environment Setup Overview ####
We will provide an overview of the developer tools available for developing on your platform.

[View PowerPoint](../../Presentation/Module01-DevTools.pptx?raw=true)

### HOL 1: Setting up your developer environment ###
Setting up your developer environment for your specific language
In this lab you will create the environment that is needed for your language preference.

* Create O365 Developer Tenant
* Connect an Azure subscription (Trial or other)
* Take prepared image, walk through the tools that are available for your platform (.NET, Node.JS, Java | Windows, MacOSx)
* Run a custom ARM Template to scaffold out resources used during the training

#### View instructions for [.NET](../HOL/dotnet/developer-environment) | [Node.JS](../HOL/node/developer-environment/) | [Java](../HOL/java/developer-environment/)

----
##  Module 2 - Modern Cloud Apps Overview ####
We will provide an overview of some common cloud technologies, patterns and Azure features (Polyglot, scalability, app insights, Redis, patterns, traffic manager, global scale, blob, CDNs) and introduce you to the sample application. It is written 3-ways (.NET, Node.js and Java) so you can pick your platform of choice.

[View PowerPoint](../../Presentation/Module02-ModernCloudApps.pptx?raw=true)

### HOL 2: Building modern cloud apps ###
This lab will introduce you to building modern cloud apps with Azure. You will perform the following tasks:

* Connect to deployed API
* Add blob storage for the images
* Add queueing for image processing
* Add Redis cache for the dashboard
* Stretch: Image resizing with Azure Functions

#### View instructions for [.NET](../HOL/dotnet/modern-cloud-apps) | [Node.JS](../HOL/node/modern-cloud-apps) | [Java](../HOL/java/modern-cloud-apps)

---
##  Module 3 - Identity and Office365 APIs Overview ##
We will provide an overview of Azure AD, and discuss areas for integration with the Office 365 APIs.

[View PowerPoint](../../Presentation/Module03-Identity-0365Apis.pptxx?raw=true)

----
### HOL 3: Identity with Azure AD and Office 365 APIs ###
This lab will introduce you to identity in Azure AD and the Microsoft Graph. You will perform the following tasks:

* Create AAD Application
* Add authentication to app
* Populate first & last name of the new incident form with from the Graph/token
* Add a user profile page with a graph API call to get user data
* Send an email via Graph on new incident creation
* Stretch: Add a calendar event on new incident creation

#### View instructions for [.NET](../HOL/dotnet/azuread-office365) | [Node.JS](../HOL/node/azuread-office365) | [Java](../HOL/java/azuread-office365)

---
## Module 4 - DevOps Overview ####
We will provide an overview of Visual Studio Team Services (VSTS), DevOps concepts, build tasks, release environments, integration with Azure and Git/GitHub and Azure to create a cross-platform build, integration and release pipelines.

[View PowerPoint](../../Presentation/Module04-DevOps.pptx?raw=true)

----
### HOL 4: DevOps with Azure and VSTS ###
This lab will introduce you to DevOps with Visual Studio Team Services. You will perform the following tasks:

* Create Visual Studio Team Services (VSTS) Online account
* Create Git repository
* Clone Git repo locally
* Push code into VSTS
* Create CI pipeline for build. Ends with published artifacts

#### View instructions for [.NET](../HOL/dotnet/devops-ci) | [Node.JS](../HOL/node/devops-ci) | [Java](../HOL/java/devops-ci)

---
## Module 5 - Infrastructure as code ####
Intro to Azure Resource manager and infrastructure as code.

[View PowerPoint](../../Presentation/Module05-ARM-IAC.pptx?raw=true)

### HOL 5: Infrastructure as code with (ARM) ###
This lab will introduce you to ARM templates and deployments to Azure. You will perform the following tasks:

* Create ARM template for web app in VS
* Deploy using VSTS
* Create 1 Production environment
* Configure Continuous deployment

#### View instructions for [.NET](../HOL/dotnet/arm-cd) | [Node.JS](../HOL/node/arm-cd) | [Java](../HOL/java/arm-cd)

---
## Module 6 - Monitoring ####
We will introduce you to the monitoring capabilities in Azure and show you how you can use them in your application.

[View PowerPoint](../../Presentation/Module06-Monitoring.pptxx?raw=true)

### HOL 6: Monitoring applications with App Insights ###
This lab will introduce you to Azure Application Insights. You will perform the following tasks:

* Add App Insights resource to Azure
* Add App Insights to application server side
* Monitor the API call
* Add client side library
* Dashboard custom event to capture dashboard views
* Create availability test that test the dashboard
* Stretch - Create custom metric around the API call

#### View instructions for [.NET](../HOL/dotnet/appinsights) | [Node.JS](../HOL/node/appinsights) | [Java](../HOL/java/appinsights)

---
## Module 7 - Docker ####
In this module, we will provide an overview of Docker and introduce you to the Azure capabilities available for developers.

[View PowerPoint](../../Presentation/Module07-Containers.pptx?raw=true)

---
## Module 8 - Azure features and APIs ####
We will provide a quick lap around the various APIs, features and services available for developers.

[View PowerPoint](../../Presentation/Module08-Azure Features.pptx?raw=true)
