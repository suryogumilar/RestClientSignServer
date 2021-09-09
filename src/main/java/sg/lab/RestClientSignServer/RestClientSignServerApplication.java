package sg.lab.RestClientSignServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@SpringBootApplication
@Configuration
@ComponentScan("sg.lab.RestClientSignServer")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class RestClientSignServerApplication extends WebSecurityConfigurerAdapter{
	Logger logger = LoggerFactory.getLogger(RestClientSignServerApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(RestClientSignServerApplication.class, args);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().authenticated().and().x509().subjectPrincipalRegex("CN=(.*?)(?:,|$)")
				.userDetailsService(userDetailsService());
	}
	private String cnStringList = System.getenv("CN_LIST");
	
	@Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
        	
            @Override
            public UserDetails loadUserByUsername(String username) {
            	logger.info("username is "+username+";");
            	String[] cnList = cnStringList.split(":");
            	for (String cn : cnList) {
            		logger.info("cn = "+cn+";");
            		if (username.equals(cn)) {
                        return new User(username, "", 
                          AuthorityUtils
                            .commaSeparatedStringToAuthorityList("ROLE_USER"));
                    }
				}
                
                throw new UsernameNotFoundException("User not found!");
            }
        };
    }

}
