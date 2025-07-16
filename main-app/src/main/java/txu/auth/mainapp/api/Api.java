package txu.auth.mainapp.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import txu.auth.mainapp.base.AbstractApi;
import txu.auth.mainapp.security.AuthenticationService;
import txu.auth.mainapp.security.JwtRequest;
import txu.auth.mainapp.security.JwtResponse;
import txu.common.exception.BadParameterException;

@Slf4j
@RestController
@RequestMapping()
@RequiredArgsConstructor
public class Api extends AbstractApi {

    private final AuthenticationService authenticateService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest jwtRequest) {
        JwtResponse jwtResponse = authenticateService.authenticateUer(jwtRequest.getUsername(), jwtRequest.getPassword());
        if (jwtResponse == null) {
            throw new BadParameterException("Username or password is incorrect");
        }
        return ResponseEntity.ok(jwtResponse);
    }

}
