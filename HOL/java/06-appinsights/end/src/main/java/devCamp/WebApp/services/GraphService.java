package devCamp.WebApp.services;

import org.springframework.stereotype.Service;

import devCamp.WebApp.models.UserProfileBean;

@Service
public interface GraphService {
    public UserProfileBean getUserProfile() ;
    
    public void sendMail(String displayName,String emailAddr);
    
}