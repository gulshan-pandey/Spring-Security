package Spring.Security.Auth.repo;

import Spring.Security.Auth.model.Userinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepo extends JpaRepository<Userinfo, Integer> {

    Userinfo findByName(String name);

}
