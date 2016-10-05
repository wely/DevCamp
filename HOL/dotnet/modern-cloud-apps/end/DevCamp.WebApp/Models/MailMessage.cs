using DevCamp.WebApp.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace DevCamp.WebApp.Models
{
    public class Body
    {
        public string contentType { get; set; }
        public string content { get; set; }
    }

    public class EmailAddress
    {
        public string name { get; set; }
        public string address { get; set; }
    }

    public class Sender
    {
        public Sender()
        {
            emailAddress = new EmailAddress();
        }
        public EmailAddress emailAddress { get; set; }
    }

    public class From
    {
        public From()
        {
            emailAddress = new EmailAddress();
        }
        public EmailAddress emailAddress { get; set; }
    }

    public class ToRecipient
    {
        public ToRecipient()
        {
            emailAddress = new EmailAddress();
        }
        public EmailAddress emailAddress { get; set; }
    }

    public class CcRecipient
    {
        public CcRecipient()
        {
            emailAddress = new EmailAddress();
        }
        public EmailAddress emailAddress { get; set; }
    }

    public class BccRecipient
    {
        public BccRecipient()
        {
            emailAddress = new EmailAddress();
        }
        public EmailAddress emailAddress { get; set; }
    }

    public class ReplyTo
    {
        public ReplyTo()
        {
            emailAddress = new EmailAddress();
        }
        public EmailAddress emailAddress { get; set; }
    }

    public class Message
    {
        public Message()
        {
            DateTime utcNow = DateTime.UtcNow;
            sender = new Sender();
            body = new Body();
            from = new From();
            toRecipients = new List<ToRecipient>();
            ccRecipients = new List<CcRecipient>();
            bccRecipients = new List<BccRecipient>();
            replyTo = new List<ReplyTo>();
            categories = new List<string>();
            createdDateTime = utcNow.ToString();
            receivedDateTime = utcNow.ToString();
            sentDateTime = utcNow.ToString();
        }
        public string receivedDateTime { get; set; }
        public string sentDateTime { get; set; }
        public bool hasAttachments { get; set; }
        public string subject { get; set; }
        public Body body { get; set; }
        public string parentFolderId { get; set; }
        public Sender sender { get; set; }
        public From from { get; set; }
        public List<ToRecipient> toRecipients { get; set; }
        public List<CcRecipient> ccRecipients { get; set; }
        public List<BccRecipient> bccRecipients { get; set; }
        public List<ReplyTo> replyTo { get; set; }
        public string conversationId { get; set; }
        public bool isDeliveryReceiptRequested { get; set; }
        public bool isReadReceiptRequested { get; set; }
        public bool isRead { get; set; }
        public bool isDraft { get; set; }
        public string webLink { get; set; }
        public string createdDateTime { get; set; }
        public string lastModifiedDateTime { get; set; }
        public string changeKey { get; set; }
        public List<string> categories { get; set; }
        public string id { get; set; }
    }
    public class EmailMessage
    {
        public EmailMessage()
        {
            Message = new Message();
        }
        public Message Message { get; set; }
        public bool SaveToSentItems { get; set; }
    }
}