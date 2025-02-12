package Spring.Security.Auth.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfigs {


    @Autowired
    UserDetailsService userDetailsService;


    @Autowired
    private JWTFilter jwtFilter;

    @Bean
    public SecurityFilterChain doget(HttpSecurity http) throws Exception {

       return http
                .csrf(customizer  -> customizer.disable())
                .authorizeHttpRequests( req   ->  req
                        .requestMatchers("/csrf","/register","/all","login").permitAll()
                        .anyRequest().authenticated()
                        )
//        http.formLogin(Customizer.withDefaults());
               .httpBasic(Customizer.withDefaults())
                .sessionManagement( session  ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }




    /**
     *
     * used for hardcoded values of users

    @Bean
    public UserDetailsService userDetailService(){

        UserDetails user1 = User
                .withDefaultPasswordEncoder()
                .username("k")
                .password("k")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user1);
    }

     */



    // for using the database based auth we need to make our own AuthenticationProvider

   @Bean
    public AuthenticationProvider authenticationProvider(){
//  DaoAuthenticationProvider is used for database auth, and it implements the AuthenticationProvider interface, in which we need to set the password encoder and set the  userDetailsService

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);


        return provider;

   }

// to implement the JWT in spring boot

    @Bean
    public AuthenticationManager authenticationMaganger(AuthenticationConfiguration config) throws Exception {

        return  config.getAuthenticationManager();
    }


    // By default the UPAF is activated for the regular login, but for JWT auth on requests we need to change this by configuring the JWTfilter first(prior) than the UPAF



}