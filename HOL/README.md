# Microsoft Azure and Office 365 DevCamp

These hands on labs (HOLs) are intended to introduce developers to Azure and Office 365 API development. The HOLs are development language specific so you get to pick.

----
## HOL 1: Setting up your developer environment
Setting up your developer environment for your specific language
In this lab you will create the environment that is needed for your language preference.

* Create O365 Developer Tenant
* Connect an Azure subscription (Trial or other)
* Take prepared image, walk through the tools that are available for your platform (.NET, Node.JS, Java | Windows, MacOSX)
* Run a custom ARM Template to scaffold out resources used during the training

#### View instructions for [.NET](../HOL/dotnet/01-developer-environment) | [Node.JS](../HOL/node/01-developer-environment) | [Java](../HOL/java/01-developer-environment)

----
## HOL 2: Building modern cloud apps
This lab will introduce you to building modern cloud apps with Azure. You will perform the following tasks:

* Connect to deployed API
* Add blob storage for the images
* Add queueing for image processing
* Add Redis cache for the dashboard
* Stretch: Image resizing with Azure Functions

#### View instructions for [.NET](../HOL/dotnet/02-modern-cloud-apps) | [Node.JS](../HOL/node/02-modern-cloud-apps) | [Java](../HOL/java/02-modern-cloud-apps)

----
## HOL 3: Identity with Azure AD and Office 365 APIs
This lab will introduce you to identity in Azure AD and the Microsoft Graph. You will perform the following tasks:

* Create AAD Application
* Add authentication to app
* Populate first & last name of the new incident form with from the Graph/token
* Add a user profile page with a graph API call to get user data
* Send an email via Graph on new incident creation
* Stretch: Add a calendar event on new incident creation

#### View instructions for [.NET](../HOL/dotnet/03-azuread-office365) | [Node.JS](../HOL/node/03-azuread-office365) | [Java](../HOL/java/03-azuread-office365)

----
## HOL 4: DevOps with Azure and VSTS
This lab will introduce you to DevOps with Visual Studio Team Services. You will perform the following tasks:

* Create Visual Studio Team Services (VSTS) Online account
* Create Git repository
* Clone Git repo locally
* Push code into VSTS
* Create CI pipeline for build. Ends with published artifacts

#### View instructions for [.NET](../HOL/dotnet/04-devops-ci) | [Node.JS](../HOL/node/04-devops-ci) | [Java](../HOL/java/04-devops-ci)

----
## HOL 5: Infrastructure as code with (ARM)
This lab will introduce you to ARM templates and deployments to Azure. You will perform the following tasks:

* Create ARM template for web app in VS
* Deploy using VSTS
* Create 1 Production environment
* Configure Continuous deployment

#### View instructions for [.NET](../HOL/dotnet/05-arm-cd) | [Node.JS](../HOL/node/05-arm-cd) | [Java](../HOL/java/05-arm-cd)

----
## HOL 6: Monitoring applications with App Insights
This lab will introduce you to Azure Application Insights. You will perform the following tasks:

* Add App Insights resource to Azure
* Add App Insights to application server side
* Monitor the API call
* Add client side library
* Dashboard custom event to capture dashboard views
* Create availability test that test the dashboard
* Stretch - Create custom metric around the API call

#### View instructions for [.NET](../HOL/dotnet/06-appinsights) | [Node.JS](../HOL/node/06-appinsights) | [Java](../HOL/java/06-appinsights)
