package devCamp.WebApp.configurations;

import javax.servlet.Filter;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.microsoft.applicationinsights.TelemetryConfiguration;
import com.microsoft.applicationinsights.web.internal.WebRequestTrackingFilter;


@Configuration
public class AppInsightsConfig {

    @Bean
    public String telemetryConfig() {
        String telemetryKey = System.getenv("APPLICATION_INSIGHTS_IKEY");
        if (telemetryKey != null) {
            TelemetryConfiguration.getActive().setInstrumentationKey(telemetryKey);
        }
        return telemetryKey;
    }

    @Bean
    public FilterRegistrationBean aiFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new WebRequestTrackingFilter());
        registration.addUrlPatterns("/**");
        registration.setOrder(1);
        return registration;
    } 

    @Bean(name = "WebRequestTrackingFilter")
    public Filter WebRequestTrackingFilter() {
        return new WebRequestTrackingFilter();
    }	
}