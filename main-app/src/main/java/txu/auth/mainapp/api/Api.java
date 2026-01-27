package txu.auth.mainapp.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import txu.auth.mainapp.base.AbstractApi;
import txu.auth.mainapp.dto.RefreshTokenRequest;
import txu.auth.mainapp.dto.RoleDto;
import txu.auth.mainapp.dto.UserDto;
import txu.auth.mainapp.entity.AccountEntity;
import txu.auth.mainapp.security.AuthenticationService;
import txu.auth.mainapp.security.JwtRequest;
import txu.auth.mainapp.security.JwtResponse;
import txu.auth.mainapp.service.AccountService;
import txu.auth.mainapp.util.RestTXUTemplate;
import txu.common.exception.BadParameterException;

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

    @Value("${keycloak.token-url}")
    private String tokenUrl;


    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    private final AuthenticationService authenticateService;
    private final AccountService accountService;
    private  final RestTXUTemplate restTemplate;

    @RequestMapping(value = "/authenticate1", method = RequestMethod.POST)
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
    public ResponseEntity<?> userInfo(HttpServletRequest httpServletRequest) {

        String authHeader = httpServletRequest.getHeader("Authorization");
        log.info("Authorization header: {}", authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        // ----- Header -----
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String basicAuth = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));
        headers.set(HttpHeaders.AUTHORIZATION, "Basic " + basicAuth);

        // ----- Body -----
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("token", authHeader.substring(7));
        body.add("token_type_hint", "access_token");

        HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<>(body, headers);

        try {
            // ----- Call -----
            ResponseEntity<Map> response = restTemplate.exchange(introspectUrl, HttpMethod.POST, req, Map.class
            );

            log.info("StatusCode:" + response.getStatusCode().toString());
            // Trả nguyên status + body
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (HttpStatusCodeException ex) {
            // BẮT các status != 2xx (400, 401, 403, 500...)
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        }

    }

    @PostMapping(value = "/authenticate")
//    public Map<String, Object> authenticate1(@RequestBody UserDto userDto) {
    public ResponseEntity<?> authenticate1(@RequestBody UserDto userDto) {
        // ----- Header -----
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String basicAuth = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));
        headers.set(HttpHeaders.AUTHORIZATION, "Basic " + basicAuth);

        // ----- Body -----
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("client_id", clientId);
//        body.add("client_secret", clientSecret);
        body.add("grant_type", "password");
        body.add("username", userDto.getUsername());
        body.add("password", userDto.getPassword());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            // ----- Call -----
            ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class

            );

            // Trả nguyên status + body
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (HttpStatusCodeException ex) {
            // BẮT các status != 2xx (400, 401, 403, 500...)
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        }
    }

//    @PostMapping(value = "/refresh-token")
//    public Map<String, Object> refresh_token(@RequestBody RefreshTokenRequest refreshTokenRequest) {
//        // ----- Header -----
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        String basicAuth = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));
//        headers.set(HttpHeaders.AUTHORIZATION, "Basic " + basicAuth);
//
//        // ----- Body -----
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("grant_type", "refresh_token");
//        body.add("refresh_token", refreshTokenRequest.getRefresh_token());
//
//        HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<>(body, headers);
//
//        // ----- Call -----
//        ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, req, Map.class);
//
//        return response.getBody();
//    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {

        // ----- Header -----
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String basicAuth = Base64.getEncoder()
                .encodeToString((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));
        headers.set(HttpHeaders.AUTHORIZATION, "Basic " + basicAuth);

        // ----- Body -----
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", refreshTokenRequest.getRefresh_token());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            // ----- Call -----
            ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class
            );

            // Trả nguyên status + body
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (HttpStatusCodeException ex) {
            // BẮT các status != 2xx (400, 401, 403, 500...)
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        }
    }

}
