using DevCamp.API.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace DevCamp.WebApp.Controllers
{
    public class NewController : Controller
    {
        public ActionResult Index()
        {
            return View();
        }

        [HttpPost]
        //public ActionResult PostIncident([FromBody] Incident newIncident)
        //{

        //}

    }
}