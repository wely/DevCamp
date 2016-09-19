using DevCamp.API.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;

namespace DevCamp.API.Controllers
{
    public class IncidentController : Controller
    {
        // GET: Incident
        public ActionResult Index()
        {
            return View();
        }

        public Incident GetById(int Id)
        {
            return new Incident();
        }
    }
}