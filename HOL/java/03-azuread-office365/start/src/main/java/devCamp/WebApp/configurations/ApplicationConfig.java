package devCamp.WebApp.configurations;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.microsoft.azure.storage.CloudStorageAccount;

import devCamp.WebApp.properties.ApplicationProperties;
import devCamp.WebApp.properties.AzureStorageAccountProperties;

@Configuration
@EnableConfigurationProperties(value = {
        ApplicationProperties.class,
        AzureStorageAccountProperties.class
})    

public class ApplicationConfig {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationConfig.class);
    
    @Autowired
    private ApplicationProperties applicationProperties;
    
    @Autowired
    private AzureStorageAccountProperties azureStorageAccountProperties;

    @PostConstruct
    protected void postConstruct() throws IOException {
        LOG.info(applicationProperties.toString());
        LOG.info(azureStorageAccountProperties.toString());
    }    
    
    @Bean
    RestTemplate getRestTemplate(){
        //create/configure REST template class here and autowire where needed
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        return restTemplate;
    }
    
    @Bean
    public CloudStorageAccount getStorageAccount() throws InvalidKeyException, URISyntaxException {
        String cs = String.format("DefaultEndpointsProtocol=http;AccountName=%s;AccountKey=%s",
                azureStorageAccountProperties.getName(),
                azureStorageAccountProperties.getKey());
        LOG.info("using cloud storage account {}",cs);
        return CloudStorageAccount.parse(cs);
    }
}    