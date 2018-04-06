package devCamp.WebApp.services;

import org.springframework.stereotype.Service;

import devCamp.WebApp.models.UserProfileBean;
import devCamp.WebApp.models.ContactValueBean;

@Service
public interface GraphService {
    public UserProfileBean getUserProfile() ;

    public ContactValueBean getContacts() ;
    
    public void sendMail(String displayName,String emailAddr);
    
    public void createEvents(String locationDisplayName,
    		String bodyContent,
    		String subject,
    		String startDateTime,
    		String endDateTime,
    		String attendee1EmailAddress,
    		String attendee1name,
    		String attendee2EmailAddress,
    		String attendee2name,
    		String attendee3EmailAddress,
    		String attendee3name);
}