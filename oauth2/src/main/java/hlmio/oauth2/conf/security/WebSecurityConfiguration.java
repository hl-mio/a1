package hlmio.oauth2.conf.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.*;


@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService userDetailsServiceImpl;


	//@Override
	//protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	//	auth.userDetailsService(userDetailsServiceBean()).passwordEncoder(passwordEncoder());
	//}


	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/assets/**", "/css/**", "/images/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin()
				.loginPage("/login")
				.and()
				.authorizeRequests()
				.antMatchers("/login").permitAll()
				.anyRequest()
				.authenticated()
				.and().csrf().disable().cors();
	}


	@Bean
	@Override
	public UserDetailsService userDetailsServiceBean() {
		return userDetailsServiceImpl;

		//Collection<UserDetails> users = buildUsers();
		//return new InMemoryUserDetailsManager(users);
	}

	private Collection<UserDetails> buildUsers() {
		String password = passwordEncoder().encode("123456");

		List<UserDetails> users = new ArrayList<>();

		UserDetails user_admin = User.withUsername("admin").password(password).authorities("ADMIN", "USER").build();
		UserDetails user_user1 = User.withUsername("user1").password(password).authorities("USER").build();

		users.add(user_admin);
		users.add(user_user1);

		return users;
	}


	@Bean
	public PasswordEncoder passwordEncoder() {
		String encodingId = "bcrypt";
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put(encodingId, new BCryptPasswordEncoder());
		encoders.put("ldap", new org.springframework.security.crypto.password.LdapShaPasswordEncoder());
		encoders.put("MD4", new org.springframework.security.crypto.password.Md4PasswordEncoder());
		encoders.put("MD5", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("MD5"));
		encoders.put("noop", org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance());
		encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
		encoders.put("scrypt", new SCryptPasswordEncoder());
		encoders.put("SHA-1", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-1"));
		encoders.put("SHA-256", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-256"));
		encoders.put("sha256", new org.springframework.security.crypto.password.StandardPasswordEncoder());
		encoders.put("raw", new raw_passwordEncoder());

		DelegatingPasswordEncoder delegatingPasswordEncoder = new DelegatingPasswordEncoder(encodingId, encoders);
		delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(new raw_passwordEncoder());

		return delegatingPasswordEncoder;
	}

	class raw_passwordEncoder implements PasswordEncoder{
		@Override
		public String encode(CharSequence rawPassword) {
			return rawPassword.toString();
		}
		@Override
		public boolean matches(CharSequence rawPassword, String encodedPassword) {
			return rawPassword.toString().equals(encodedPassword);
		}
	}


	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}
