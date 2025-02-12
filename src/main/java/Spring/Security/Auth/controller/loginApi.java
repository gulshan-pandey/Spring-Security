package Spring.Security.Auth.controller;


import Spring.Security.Auth.model.Userinfo;
import Spring.Security.Auth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



@RestController
public class loginApi {

    @Autowired
    UserService userService;



    @GetMapping("/")
    public String response(HttpServletRequest request) {
//        return "Welcome " + request.getAuthType() + " " + request.getRequestURI() + " " + request.getSession().getId();
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String userAgent = request.getHeader("User-Agent");
        String param = request.getParameter("paramName");
        String sessionId = request.getSession().getId();

        return "Request URI: " + uri + "\n" +
                "HTTP Method: " + method + "\n" +
                "User-Agent: " + userAgent + "\n" +
                "Parameter: " + param + "\n" +
                "Session ID: " + sessionId;
    }



    @GetMapping("/csrf")
    public ResponseEntity<CsrfToken> getCsrfToken(HttpServletRequest request){
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        if (csrfToken == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(csrfToken);
    }



    // would be rejected by the CSRF token
    // by default the CRSF is not needed in the get mappings

    @PostMapping("/register")
    public Userinfo post(@RequestBody Userinfo userinfo) {
        return userService.save(userinfo);
    }


    @GetMapping("all")
    public List<Userinfo> getall(){
        return userService.findAll();
    }


    @PostMapping("/login")
    public String login(@RequestBody Userinfo userinfo) {

        return userService.verify(userinfo);
    }
    @GetMapping("/delete-all")
    public ResponseEntity<?> delete() {
        userService.deleteAllUserinfos();
        return ResponseEntity.ok("All users deleted");
    }
}
