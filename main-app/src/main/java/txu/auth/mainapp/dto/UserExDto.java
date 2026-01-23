package txu.auth.mainapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserExDto {
    private Long exp;
    private Long iat;
    private String jti;
    private String iss;
    private String sub;
    private String typ;
    private String azp;
    private String session_state;
    private String acr;
    private RolesDto realm_access;
    private String scope;
    private String sid;
    private boolean email_verified;
    private String name;
    private String preferred_username;
    private String given_name;
    private String family_name;
    private String email;
    private String client_id;
    private String token_type;
    private boolean active;
}
