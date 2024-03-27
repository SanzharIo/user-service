package kz.flurent.model.entity;

import kz.flurent.model.audit.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Role extends AuditModel implements GrantedAuthority {

    @Column(name = "role")
    private String roleName;

    @JoinColumn(name = "role_id")
    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();

    @Override
    public String getAuthority() {
        return roleName;
    }

}
