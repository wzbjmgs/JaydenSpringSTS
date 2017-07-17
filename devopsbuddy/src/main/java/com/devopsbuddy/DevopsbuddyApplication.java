package com.devopsbuddy;

import com.devopsbuddy.backend.persistence.domain.backend.Role;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.domain.backend.UserRole;
import com.devopsbuddy.backend.service.UserService;
import com.devopsbuddy.enums.PlanEnum;
import com.devopsbuddy.enums.RolesEnums;
import com.devopsbuddy.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.HashSet;
import java.util.Set;


@SpringBootApplication
public class DevopsbuddyApplication implements CommandLineRunner{

	/**The application logger**/
	private static final Logger LOG = LoggerFactory.getLogger(DevopsbuddyApplication.class);

	@Autowired
	private UserService userService;

	@Value("${Webmaster.username}")
	private String webmasterUsername;

	@Value("${Webmaster.password}")
	private String webmasterPassword;

	@Value("${Webmaster.email}")
	private String webmasterEmail;

	public static void main(String[] args) {
		SpringApplication.run(DevopsbuddyApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User user = UserUtils.createBasicUser(webmasterUsername, webmasterEmail);
		user.setPassword(webmasterPassword);
		Set<UserRole> userRoles = new HashSet<UserRole>();
		userRoles.add(new UserRole(user, new Role(RolesEnums.ADMIN)));
		LOG.debug("Creating user with username {}", user.getUsername());
		userService.createUser(user, PlanEnum.BASIC,userRoles);
		LOG.info("User {} created.", user.getUsername());
	}
}
