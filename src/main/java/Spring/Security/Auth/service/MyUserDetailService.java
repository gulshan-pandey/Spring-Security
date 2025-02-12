package Spring.Security.Auth.service;


import Spring.Security.Auth.model.UserPrincipals;
import Spring.Security.Auth.model.Userinfo;
import Spring.Security.Auth.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class MyUserDetailService  implements UserDetailsService {

    // this implementation will be autowired further in the securityConfig file!

    @Autowired
    UserRepo userRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Userinfo userinfo  = userRepo.findByName(username);

        if(userinfo== null){
            throw  new UsernameNotFoundException("userinfo not found");
        }


        return new UserPrincipals(userinfo);                  // will have to return the userdetail's implementation

    }

}
