package devCamp.WebApp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.addFilterBefore(new OncePerRequestFilter() {
//            @Override
//            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//                    FilterChain filterChain) throws ServletException, IOException {
//
//                log.info("check csrf token {}", request.getHeader("X-CSRF-HEADER"));
//                log.info("check csrf token {}", request.getParameter("_csrf"));
//
//                filterChain.doFilter(request, response);
//            }
//        }, CsrfFilter.class);
        
        // Add AzureAD filter before and after CsrfFilter
//        http.addFilterBefore(new AntPathRequestMatcher(""),AzureADAuthenticationFilter()), CsrfFilter.class);
//
//    	
//RWS    	http.addFilterBefore(new AzureADResponseFilter(), CsrfFilter.class);
//RWS        http.addFilterAfter(new AzureADAuthenticationFilter(), CsrfFilter.class);
 
//        http.authorizeRequests()
//        .antMatchers("/dashboard**").permitAll()
//        .antMatchers("/static/**").permitAll()
//        .antMatchers("/").permitAll()
//        .and()
//            .exceptionHandling().accessDeniedPage("/403")
//        .and()
//            .csrf();
//        

      RequestMatcher resourcesMatcher = new AntPathRequestMatcher("/resources/**");
      RequestMatcher postLoginMatcher = new AntPathRequestMatcher("/login", "POST");
      RequestMatcher dashboardMatcher = new AntPathRequestMatcher("/dashboard/**");
      RequestMatcher indexMatcher = new AntPathRequestMatcher("/");
//    RequestMatcher postLoginMatcher = new AntPathRequestMatcher("/login", "POST");
    RequestMatcher ignored = new OrRequestMatcher(resourcesMatcher, postLoginMatcher,dashboardMatcher,indexMatcher);
//      Filter delegateFilter = new DelegateRequestMatchingFilter(ignored, new AzureADAuthenticationFilter());
    	AzureADAuthenticationFilter delegateFilter = new AzureADAuthenticationFilter();
    	delegateFilter.setAADAuthenticationMatcher(ignored);

      http.addFilterBefore(new AzureADResponseFilter(), CsrfFilter.class);
      http.addFilterAfter(delegateFilter, CsrfFilter.class);
		
	  
		
//		RequestMatcher matcher = new RequestMatcher() {
//		private AntPathRequestMatcher[] requestMatchers = {
//				new AntPathRequestMatcher("/new"),
//				new AntPathRequestMatcher("/details/**")
//		};
//		@Override
//		public boolean matches(HttpServletRequest request) {
//			for (AntPathRequestMatcher rm : requestMatchers) {
//				if (rm.matches(request)) {return true;}
//			}
//			return false;
//		}
//		};
		
    	
//        http.addFilterBefore(new AzureADResponseFilter(), CsrfFilter.class);
//        AzureADAuthenticationFilter filter = new AzureADAuthenticationFilter();
//        filter.setAADAuthenticationMatcher(matcher);
//        http.addFilterAfter(filter, CsrfFilter.class);
        
		http.authorizeRequests().antMatchers("/dashboard**").permitAll()
		.antMatchers("/").permitAll()
		.antMatchers("/static/**").permitAll()
		.antMatchers("/images/**").permitAll()
		.antMatchers("/details/**").permitAll()
		.antMatchers("/new/**").authenticated();
//        
//        
//        new AntPathRequestMatcher("/login**")));
//		http.authorizeRequests()
//			.antMatchers("/**").permitAll()
//			.antMatchers("/dashboard**").permitAll()
//			.antMatchers("/static/**").permitAll()
//			.antMatchers("/new/**").authenticated()
//			.antMatchers("/details/**").authenticated()
//			;
//        http.authorizeRequests().antMatchers("/new").authenticated();

    }
    
//    @Bean(name="springSecurityFilterChain")
//    public FilterChainProxy getFilterChainProxy() throws ServletException, Exception {
//    	List listOfFilterChains = new ArrayList();
//    	listOfFilterChains.add(new DefaultSecurityFilterChain(
//    			new AntPathRequestMatcher("/")));
//    	listOfFilterChains.add(new DefaultSecurityFilterChain(
//    			new AntPathRequestMatcher("/dashboard**")));
//    	listOfFilterChains.add(new DefaultSecurityFilterChain(
//    			new AntPathRequestMatcher("/new"),
//    			new AzureADAuthenticationFilter()));
//    	return new FilterChainProxy(listOfFilterChains);
//    }

    
//    @Bean
//    public TokenFilterSecurityInterceptor<TokenInfo> tokenInfoTokenFilterSecurityInterceptor() throws Exception
//    {
//        TokenService<TokenInfo> tokenService = new TokenServiceImpl(userInfoCache);
//        return new TokenFilterSecurityInterceptor<TokenInfo>(tokenService, serverStatusService, "RUN_ROLE");
//    }    
//    		http.authorizeRequests().antMatchers("static/**").permitAll();

//    
//    @Bean
//    public TokenFilterSecurityInterceptor<TokenInfo> tokenInfoTokenFilterSecurityInterceptor() throws Exception
//    {
//        TokenService<TokenInfo> tokenService = new TokenServiceImpl(userInfoCache);
//        TokenFilterSecurityInterceptor tokenFilter new TokenFilterSecurityInterceptor<TokenInfo>(tokenService, serverStatusService, "RUN_ROLE");
//
//
//        RequestMatcher resourcesMatcher = new AntPathRequestMatcher("/resources/**");
//        RequestMatcher postLoginMatcher = new AntPathRequestMatcher("/login", "POST");
//        RequestMatcher ignored = new OrRequestMatcher(resourcesMatcher, postLoginMatcher);
//        return new DelegateRequestMatchingFilter(ignored, tokenService);
//    }
    
     class DelegateRequestMatchingFilter extends OncePerRequestFilter {
        private Filter delegate;
        private RequestMatcher ignoredRequests;

        public DelegateRequestMatchingFilter(RequestMatcher matcher, Filter delegate) {
            this.ignoredRequests = matcher;
            this.delegate = delegate;
        }
        
        public void setIgnoredMatcher(RequestMatcher matcher) {
        	this.ignoredRequests = matcher;
        }

		@Override
		protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
				FilterChain filterChain) throws ServletException, IOException {
            if( ignoredRequests.matches(request)) {
                filterChain.doFilter(request,response);
            } else {
                delegate.doFilter(request,response,filterChain);
            }
			
		}

    } 
}