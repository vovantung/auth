package txu.auth.mainapp.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import txu.auth.mainapp.dao.AccountDao;
import txu.auth.mainapp.dto.UserDto;
import txu.auth.mainapp.entity.AccountEntity;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountDao accountDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AccountEntity user = accountDao.findByUsername(username);

        if (user == null) {
            log.error("User not found");
            return null;
        }

        String[] roles = user.getRole().split(",");

        return User.withUsername(user.getUsername()).password(user.getPassword()).roles(roles).build();
    }


    public UserDto loadUserByUsernameTXU(String username)  {
        AccountEntity user = accountDao.findByUsername(username);
        if (user == null) {
            log.error("User not found");
            return null;
        }
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        String[] roles = user.getRole().split(",");
        userDto.setRole(String.join(",", roles));
        return userDto;
    }

}
