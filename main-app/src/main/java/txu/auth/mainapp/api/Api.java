package txu.auth.mainapp.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import txu.auth.mainapp.base.AbstractApi;
import txu.auth.mainapp.dto.RoleDto;
import txu.auth.mainapp.entity.AccountEntity;
import txu.auth.mainapp.security.AuthenticationService;
import txu.auth.mainapp.security.JwtRequest;
import txu.auth.mainapp.security.JwtResponse;
import txu.auth.mainapp.service.AccountService;
import txu.common.exception.BadParameterException;

@Slf4j
@RestController
@RequestMapping()
@RequiredArgsConstructor
public class Api extends AbstractApi {

    private final AuthenticationService authenticateService;
    private final AccountService accountService;

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

    @GetMapping(value = "test")
    public String test() {
        return "Vo Van Tung";
    }

    @GetMapping(value = "/test")
    public String test1() {
        return "Vo Van Tung";
    }

}
