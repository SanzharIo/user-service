package kz.flurent.model.entity;

import kz.flurent.model.audit.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "opt_request")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OtpRequest extends AuditModel {

    @Column(name = "destination")
    private String destination;

    @Column(name = "code")
    private String code;

    @Column(name = "valid")
    private boolean valid = true;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private User user;


}
