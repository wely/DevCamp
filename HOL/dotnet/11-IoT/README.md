# IoT (.NET)

## Overview
City Power & Light is a sample application that allows citizens to report "incidents" that have occurred in their community. It includes a landing screen, a dashboard, and a form for reporting new incidents with an optional photo. The application is implemented with several components:

* Front end web application contains the user interface and business logic. This component has been implemented three times in .NET, NodeJS, and Java.
* WebAPI is shared across the front ends and exposes the backend DocumentDB.
* DocumentDB is used as the data persistence layer.

In this lab, you will combine the web app with an IoT device based on an Arduino board that will query the app for the number of incidents and display the refreshed number every minute.

## Objectives
In this hands-on lab, you will learn how to:
* Set up the developing environment to support the programming of Arduino chips.
* Create your own IoT software from scratch.

## Prerequisites
* The source for the starter app is located in the [start](start) folder. 
* The finished project is located in the [end](end) folder. 
* Deployed the starter ARM Template [HOL 1](../01-developer-environment).
* Completion of the [HOL 5](../05-arm-cd).

## Exercises
This hands-on-lab has the following exercises:
* [Exercise 1: Set up your environment](#ex1)
* [Exercise 2: Create output that will be consumed by the device](#ex2)
* [Exercise 3: Program the device](#ex3)

---
## Exercise 1: Set up your environment<a name="ex1"></a>

To program an Arduino device on your machine you need ..., Visual Studio and ...

1. The easiest way to install ...

You have now installed all the necessary components to start programming an Arduino device on your machine.

---

## Exercise 2: Create output that will be consumed by the device<a name="ex2"></a>

The device will regularly call an URL to fetch the current incident count. We will add a page to our existing web application as an easy way to provide this data.

1. Create a new controller. Right-click on `Controllers` and select `Add` -> `Controller...`.

    ![image](./media/2017-09-11_10_14_00.png)

1. In the `Add Scaffold` dialog select `MVC 5 Controller - Empty` and click `Add`.

    ![image](./media/2017-09-11_10_19_00.png)

1. Name the new controller `IoTController` and click `Add`.

    ![image](./media/2017-09-11_10_20_00.png)

1. The controller will just emulate the behavior of the `DashboardController`. Add the following code to the newly created file:

    ```csharp
    using DevCamp.WebApp.Utils;
    using IncidentAPI;
    using IncidentAPI.Models;
    using Newtonsoft.Json;
    using System.Collections.Generic;
    using System.Threading.Tasks;
    using System.Web.Mvc;
    
    namespace DevCamp.WebApp.Controllers
    {
        public class IoTController : Controller
        {
            // GET: IoT
            public async Task<ActionResult> Index()
            {
                List<Incident> incidents;
                using (var client = IncidentApiHelper.GetIncidentAPIClient())
                {
                    int CACHE_EXPIRATION_SECONDS = 60;
    
                    //Check Cache
                    string cachedData = string.Empty;
                    if (RedisCacheHelper.UseCachedDataSet(Settings.REDISCCACHE_KEY_INCIDENTDATA, out cachedData))
                    {
                        incidents = JsonConvert.DeserializeObject<List<Incident>>(cachedData);
                    }
                    else
                    {
                        //If stale refresh
                        var results = await client.IncidentOperations.GetAllIncidentsAsync();
                        Newtonsoft.Json.Linq.JArray ja = (Newtonsoft.Json.Linq.JArray)results;
                        incidents = ja.ToObject<List<Incident>>();
                        RedisCacheHelper.AddtoCache(Settings.REDISCCACHE_KEY_INCIDENTDATA, incidents, CACHE_EXPIRATION_SECONDS);
                    }
                }
                return View(incidents);
            }
        }
    }

1. Now create a view for the new controller by right-clicking on `Views` -> `IoT` and selecting `Add` -> `View`.

    ![image](./media/2017-09-11_10_22_00.png)

1. In the `Add View` dialog set the name to `Index` and make sure that no layout page will be created before you click `Add`.

    ![image](./media/2017-09-11_10_31_00.png)

1. Since we don't need a whole HTML page remove the content of the `Views` -> `IoT` -> `Index.cshtml` file and replace it with:

    ```csharp
    @{
        Layout = null;
    }
    @Model.Count

1. Start the debugger and add `/IoT` to the URL to test the new view. It will contain just the number of incidents.

    ![image](./media/2017-09-11_10_54_00.png)

You have now created the data feed for your device.

---
## Exercise 3: Program the device<a name="ex3"></a>

Introduction.
	
1. Exercise.

Summary.

---
## Summary

In this hands-on lab, you learned how to:
* Set up the developing environment to support the programming of Arduino chips.
* Create your own IoT software from scratch.

---
Copyright 2017 Microsoft Corporation. All rights reserved. Except where otherwise noted, these materials are licensed under the terms of the MIT License. You may use them according to the license as is most appropriate for your project. The terms of this license can be found at https://opensource.org/licenses/MIT.