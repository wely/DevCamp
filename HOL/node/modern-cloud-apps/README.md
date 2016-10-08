# NodeJS Hands on Lab - Modern Applications

## Overview

City Power & Light is a sample application that allows citizens to to report "incidents" that have occured in their community.  It includes a landing screen, a dashboard, and a form for reporting new incidents with an optional photo.  The application is implemented with several components:

* Front end web application contains the user interface and business logic.  This component has been implemented three times in .NET, NodeJS, and Java.
* WebAPI is shared across the front ends and exposes the backend DocumentDB
* DocumentDB is used as the data persistence layer 

In this lab, you will work with an existing API to connect to the web application front end. This will allow you perform CRUD operations for incidents. You will also configure additional Azure features for Redis Cache, Azure Storage Queues, and Azure Blob Storage.

> This guide use [Visual Studio Code](https://code.visualstudio.com/) for editing, however please feel free to use your editor of choice.  If you are interested in using full Visual Studio + [Node.js Tools for Visual Studio Extension (NTVS)](https://www.visualstudio.com/vs/node-js/), please see [here](https://github.com/Microsoft/nodejstools/wiki/Projects#create-project-from-existing-files) for instructions on wrapping existing code into a VS Project.

## Objectives

In this hands-on lab, you will learn how to:

* Use Visual Studio to connect to an API
* Provision an Azure Web App to host the Web site
* Modify a view to add caching
* Modify code to add queuing and blob storage

## Prerequisites

The source for the starter app is located in the HOL\node\modern-cloud-apps\src folder. 

The finished project is located in the HOL\node\modern-cloud-apps\end folder. 

## Exercises

This hands-on-lab has the following exercises:

* Exercise 1: Integrate with the WebAPI
* Exercise 2: Add a caching layer
* Exercise 3: Write images to Azure Blob storage

## Exercise 1: Integrate with the WebAPI
