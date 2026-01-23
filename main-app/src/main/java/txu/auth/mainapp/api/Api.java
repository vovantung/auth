package txu.auth.mainapp.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import txu.auth.mainapp.base.AbstractApi;
import txu.auth.mainapp.dto.RoleDto;
import txu.auth.mainapp.dto.UserExDto;
import txu.auth.mainapp.dto.UserInfoRequest;
import txu.auth.mainapp.entity.AccountEntity;
import txu.auth.mainapp.security.AuthenticationService;
import txu.auth.mainapp.security.JwtRequest;
import txu.auth.mainapp.security.JwtResponse;
import txu.auth.mainapp.service.AccountService;
import txu.auth.mainapp.util.RestTXUTemplate;
import txu.common.exception.BadParameterException;
import txu.common.exception.NotFoundException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class Api extends AbstractApi {


    @Value("${keycloak.introspect-url}")
    private String introspectUrl;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    private final AuthenticationService authenticateService;
    private final AccountService accountService;
    private  final RestTXUTemplate restTemplate;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest jwtRequest) {
        JwtResponse jwtResponse = authenticateService.authenticateUerTXU(jwtRequest.getUsername(), jwtRequest.getPassword());
        if (jwtResponse == null) {
            throw new BadParameterException("Username or password is incorrect");
        }
        return ResponseEntity.ok(jwtResponse);
    }

    @GetMapping(value = "get-current-user")
    public AccountEntity getCurrentUser() {
        return accountService.getCurrentUser();
    }

    @GetMapping(value = "get-role")
    public RoleDto getRole() {
        return accountService.getRole();
    }

    @GetMapping(value = "/test")
    public String test() {
        return "Vo Van Tung";
    }

    @PostMapping(value = "/user-info")
//    public Map<String, Object> userInfo(@RequestBody UserInfoRequest request, HttpServletRequest httpServletRequest) {
    public Map<String, Object> userInfo(HttpServletRequest httpServletRequest) {
        HttpRequest httpRequest;

        String authHeader = httpServletRequest.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

//        return authHeader.substring(7); // bỏ "Bearer "

        // ----- Header -----
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String basicAuth = Base64.getEncoder()
                .encodeToString((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));
        headers.set(HttpHeaders.AUTHORIZATION, "Basic " + basicAuth);

        // ----- Body -----
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("token", authHeader.substring(7));
        body.add("token_type_hint", "access_token");

        HttpEntity<MultiValueMap<String, String>> req =
                new HttpEntity<>(body, headers);

        // ----- Call -----
        ResponseEntity<Map> response = restTemplate.exchange(
                introspectUrl,
                HttpMethod.POST,
                req,
                Map.class
        );

        return response.getBody();

    }

    @PostMapping(value = "/authenticate1")
    public ResponseEntity<?> authenticate1(@RequestBody JwtRequest jwtRequest) {
        JwtResponse jwtResponse = authenticateService.authenticateUerTXU(jwtRequest.getUsername(), jwtRequest.getPassword());
        if (jwtResponse == null) {
            throw new BadParameterException("Username or password is incorrect");
        }
        return ResponseEntity.ok(jwtResponse);
    }

}
