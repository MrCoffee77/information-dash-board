package de.paetz.feuerwehr.informationdashboard.security;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import de.paetz.feuerwehr.informationdashboard.security.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {

	@Query("SELECT u FROM User u WHERE u.username = :username")
	User getUserByUsername(@Param("username") String username);
}
