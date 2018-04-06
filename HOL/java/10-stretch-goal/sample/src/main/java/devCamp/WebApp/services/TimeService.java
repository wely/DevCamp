package devCamp.WebApp.services;

import java.util.Date;

import org.springframework.stereotype.Service;



@Service
public interface TimeService {

    public String startDateToString(Date date);
    
    public String endDateToString(Date date);
    
    public Date getTime(); 
    
}