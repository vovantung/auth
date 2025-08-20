package txu.auth.mainapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import txu.auth.mainapp.dao.AccountDao;
import txu.auth.mainapp.entity.AccountEntity;
import txu.auth.mainapp.security.CustomUserDetails;
import txu.common.exception.NotFoundException;

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
}
