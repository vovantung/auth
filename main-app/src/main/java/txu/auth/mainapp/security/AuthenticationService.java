package txu.auth.mainapp.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import txu.auth.mainapp.dto.UserDto;
import txu.common.exception.BadParameterException;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {


    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    public JwtResponse authenticateUer(String username, String password) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            return null;
        }

        // Xác thực người dùng đã được load vào UserDetails, tuy nhiên trong trường này không cần
        // authenticate người dùng vì chỉ cần kiểm tra có phải người dùng hợp lệ hay không để tạo
        // token đăng nhập cho người dùng này dùng trong các request tiếp sau đó.
        Authentication authentication = authenticate(username, password);

        return new JwtResponse(jwtTokenUtil.generateToken(userDetails));
    }

    public JwtResponse authenticateUerTXU(String username, String password) {

        UserDto user = customUserDetailsService.loadUserByUsernameTXU(username);
        if (user == null) {
            return null;
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        passwordEncoder.encode(password);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadParameterException("Password is not correct!");
        }

        return new JwtResponse(jwtTokenUtil.generateTokenTXU(user));
    }

    public Authentication authenticate(String username, String password) throws AuthenticationException {
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authentication);
    }

}