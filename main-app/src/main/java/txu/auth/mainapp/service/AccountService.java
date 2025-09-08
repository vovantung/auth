package txu.auth.mainapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import txu.auth.mainapp.dao.AccountDao;
import txu.auth.mainapp.dto.RoleDto;
import txu.auth.mainapp.entity.AccountEntity;
import txu.auth.mainapp.security.CustomUserDetails;
import txu.common.exception.NotFoundException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountDao accountDao;

    public AccountEntity getByUsername(String username) {
        AccountEntity user = accountDao.getByUsername(username);
        if (user == null) {
            throw new NotFoundException("User is not found");
        }
        return user;
    }

    public AccountEntity getCurrentUser() {
        // Lấy thông tin người dùng gửi request thông qua token, mà lớp filter đã thực hiện qua lưu vào Security context holder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        AccountEntity account;
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails userDetails) {
                account = getByUsername(userDetails.getUsername());
            } else {
                account = null;
            }
        } else {
            account = null;
        }
        return account;
    }

    public RoleDto getRole() {
        // Lấy thông tin người dùng gửi request thông qua token, mà lớp filter đã thực hiện qua lưu vào Security context holder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        RoleDto role = new RoleDto();
        AccountEntity account;
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails userDetails) {
                account = getByUsername(userDetails.getUsername());
                List<GrantedAuthority> rs = (List<GrantedAuthority>)userDetails.getAuthorities();

                String r = rs.isEmpty() ? null : rs.get(0).getAuthority();
                role.setRole(r);
            } else {
                account = null;
            }
        } else {
            account = null;
        }
        return role;
    }
}
