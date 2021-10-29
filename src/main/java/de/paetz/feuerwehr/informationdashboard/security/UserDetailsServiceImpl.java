package de.paetz.feuerwehr.informationdashboard.security;

import de.paetz.feuerwehr.informationdashboard.security.entities.User;
import de.paetz.feuerwehr.informationdashboard.security.password.PasswordGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserDetailsServiceImpl implements UserDetailsService {

	private static Log LOG= LogFactory.getLog(UserDetailsServiceImpl.class);
	@Autowired
	private UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@PostConstruct
	@Transactional
	void init() {
		if (!userRepository.findAll().iterator().hasNext()) {
			LOG.error("Keine User vorhanden!");
			PasswordGenerator gen=new PasswordGenerator();
			User user=new User();
			user.setUsername("admin");
			String password = gen.getPassword(10);
			user.setPassword(passwordEncoder.encode(password));
			user.setRole("ADMIN");
			userRepository.save(user);
			LOG.error("User admin mit Password '"+password+"' erzeugt.");
		}
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.getUserByUsername(username);
		
		if (user == null) {
			throw new UsernameNotFoundException("Could not find user");
		}
		
		return new MyUserDetails(user);
	}

}
