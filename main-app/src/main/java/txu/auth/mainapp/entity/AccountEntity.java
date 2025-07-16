package txu.auth.mainapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
@Setter
@Getter
@Table(name = "ACCOUNT")
public class AccountEntity implements Serializable {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emp_seq_gen")
    @SequenceGenerator(name = "emp_seq_gen", sequenceName = "EMP_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Getter
    @Column(name = "USERNAME")
    private String username;

    @Getter
    @Column(name = "PASSWORD")
    private String password;

    //
    @Column(name = "LASTNAME")
    @Getter
    private String lastName;

    @Column(name = "FIRSTNAME")
    @Getter
    private String firstName;


    @Column(name = "PHONENUMBER")
    @Getter
    private String phoneNumber;

    @Column(name = "EMAIL")
    @Getter
    private String email;

    @Column(name = "ROLE")
    @Getter
    private String role;


    @Column(name = "CREATED_AT")
    private Date createdAt;
    public String getCreatedAt() {
//        return createdAt.toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("[dd/MM/yyyy] HH:mm:ss"));
        return createdAt.toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("[dd/MM/yyyy]"));
    }

    @Column(name = "UPDATED_AT")
    private Date updateAt;
    public String getUpdateAt() {
//        return updateAt.toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("[dd/MM/yyyy] HH:mm:ss"));
        return updateAt.toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("[dd/MM/yyyy]"));
    }

}
