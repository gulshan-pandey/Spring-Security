package Spring.Security.Auth.service;

import Spring.Security.Auth.model.Userinfo;
import Spring.Security.Auth.repo.UserRepo;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    JWTService jwtService;


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private EntityManager entityManager;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public List<Userinfo> findAll() {
        return userRepo.findAll();
    }

    @Transactional
    public Userinfo save(Userinfo userinfo) {
        userinfo.setPassword(bCryptPasswordEncoder.encode(userinfo.getPassword()));
        return userRepo.save(userinfo);
    }

    @Transactional
    public void resetAutoIncrement() {
        String query = "ALTER TABLE userinfo AUTO_INCREMENT = 1";
        entityManager.createNativeQuery(query).executeUpdate();
    }

    @Transactional
    public void deleteAllUserinfos() {
        userRepo.deleteAll();
        resetAutoIncrement();
    }

    public String verify(Userinfo userinfo) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userinfo.getName(), userinfo.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(userinfo.getName());
        } else {
            return "fail";
        }
    }
}