using Microsoft.VisualStudio.TestTools.UnitTesting;
using DevCamp.API.Controllers;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using DevCamp.API.Models;
using DevCamp.API.Tests.Utils;
using System.Web.Http.Results;
using System.Configuration;

namespace DevCamp.API.Controllers.Tests
{
    [TestClass()]
    public class IncidentControllerTests
    {
        [TestMethod()]
        public async Task ShouldGetAllIncidentsAPITest()
        {
            var controller = new IncidentController();
            string methodName = System.Reflection.MethodInfo.GetCurrentMethod().Name;
            Incident testIncident = IncidentGenerator.GetTestIncident(methodName);
            var result = await controller.CreateIncident(testIncident);
            Assert.IsNotNull(result);

            var incidents = await controller.GetAllIncidents() as OkNegotiatedContentResult<List<Incident>>;
            Assert.AreNotEqual(0, incidents.Content.Count);
        }

        [TestMethod()]
        public async Task ShouldGetAllIncidentsCountAPITest()
        {
            var controller = new IncidentController();
            string methodName = System.Reflection.MethodInfo.GetCurrentMethod().Name;
            Incident testIncident = IncidentGenerator.GetTestIncident(methodName);
            var result = await controller.CreateIncident(testIncident);
            Assert.IsNotNull(result);

            var incidentCount = controller.GetIncidentCount();

            Assert.AreNotEqual(0, incidentCount);
            Console.WriteLine($"Incident count is {incidentCount}");
        }


        [TestMethod()]
        public async Task ShouldGetByIdAPITest()
        {
            string methodName = System.Reflection.MethodInfo.GetCurrentMethod().Name;
            Incident testIncident = IncidentGenerator.GetTestIncident(methodName);
            var controller = new IncidentController();
            var result = await controller.CreateIncident(testIncident);
            Assert.IsNotNull(result);

            //var newIncident = await controller.GetById();
        }

        [TestMethod()]
        public async Task ShouldCreateIncidentAPITest()
        {
            string methodName = System.Reflection.MethodInfo.GetCurrentMethod().Name;
            Incident testIncident = IncidentGenerator.GetTestIncident(methodName);
            var controller = new IncidentController();
            var result = await controller.CreateIncident(testIncident);
            Assert.IsNotNull(result);
        }
    }
}