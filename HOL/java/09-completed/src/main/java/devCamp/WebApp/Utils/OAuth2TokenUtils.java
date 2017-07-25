package devCamp.WebApp.Utils;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class OAuth2TokenUtils {
    private static final Logger LOG = LoggerFactory.getLogger(OAuth2TokenUtils.class);
    //businessPhones
    //mail
    //officeLocation
    //displayName
    //givenName
    //userPrincipalName

    public static String getMail() {
        return getAttr("mail");
    }

    public static String getGivenName() {
        return getAttr("givenName");
    }
    
    public static String getMail(Authentication auth) {
        return getAttr(auth, "mail");
    }

    public static String getGivenName(Authentication auth) {
        return getAttr(auth,"givenName");
    }    

    public static String getAttr(String attr) {
        HashMap hm = getMap();
        if (hm != null){
            String m = (String)hm.get(attr);
            LOG.info("{} = {}",attr,m);
            return m;
        }
        return null;
    }

    public static String getAttr(Authentication auth, String attr) {
        HashMap hm = getMap(auth);
        if (hm != null){
            String m = (String)hm.get(attr);
            LOG.info("{} = {}",attr,m);
            return m;
        }
        return null;
    }

    private static HashMap getMap(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext != null) {
            Authentication sco = securityContext.getAuthentication();
        	return getMap(sco);
        }
        return null;
    }
    
    private static HashMap getMap(Authentication sco){
            if (sco instanceof OAuth2Authentication){
                OAuth2Authentication au = (OAuth2Authentication)sco;
                Authentication auth = au.getUserAuthentication();
                Object deets = auth.getDetails();
                if (deets instanceof HashMap){
                    return (HashMap)deets;
                }
            }
        return null;		
    }
}