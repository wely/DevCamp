/*
package devCamp.WebApp;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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

		RequestMatcher resourcesMatcher = new AntPathRequestMatcher("/resources/**");
		RequestMatcher postLoginMatcher = new AntPathRequestMatcher("/login", "POST");
		RequestMatcher dashboardMatcher = new AntPathRequestMatcher("/dashboard/**");
		RequestMatcher indexMatcher = new AntPathRequestMatcher("/");
		RequestMatcher ignored = new OrRequestMatcher(resourcesMatcher, postLoginMatcher,dashboardMatcher,indexMatcher);

		AzureADAuthenticationFilter delegateFilter = new AzureADAuthenticationFilter();
		delegateFilter.setAADAuthenticationMatcher(ignored);

		http.addFilterBefore(new AzureADResponseFilter(), CsrfFilter.class);
		http.addFilterAfter(delegateFilter, CsrfFilter.class);

		http.authorizeRequests().antMatchers("/dashboard**").permitAll()
		.antMatchers("/").permitAll()
		.antMatchers("/static/**").permitAll()
		.antMatchers("/images/**").permitAll()
		.antMatchers("/details/**").permitAll()
		.antMatchers("/user.**").permitAll()
		.antMatchers("/new/**").authenticated();

	}

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

*/
