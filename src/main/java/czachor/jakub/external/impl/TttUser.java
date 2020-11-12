package czachor.jakub.external.impl;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "TTT_USERS")
public class TttUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "USERNAME", nullable = false)
    private String username;
    @Column(name = "PASSWORD", nullable = false)
    private String password;
}
