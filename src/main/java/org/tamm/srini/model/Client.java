package org.tamm.srini.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.tamm.srini.service.dto.ClientDTO;

import java.util.Date;
import java.util.Locale;

@Entity
@Getter
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "firstname", nullable = false, length = 100)
    private String firstname;

    @Column(name = "lastname", nullable = false, length = 100)
    private String lastname;

    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "address", nullable = false, length = 300)
    private String address;

    @Column(name = "created_by", nullable = false, length = 50, updatable = false)
    private String createdBy;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private Date createdDate;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private Date updatedDate;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "auth_user_id")
    private AuthUser authUser;

    private Client(ClientDTO clientDTO, Country country, AuthUser authUser) {
        firstname = clientDTO.getFirstname();
        lastname = clientDTO.getLastname();
        username = clientDTO.getUsername().toLowerCase(Locale.ROOT);
        email = clientDTO.getEmail();
        address = clientDTO.getAddress();
        createdBy = String.valueOf(authUser.getId());
        this.country = country;
        this.authUser = authUser;
    }

    public static Client create(ClientDTO clientDTO, Country country, AuthUser authUser) {
        return new Client(clientDTO, country, authUser);
    }

    public void update(ClientDTO clientDTO, Country country, AuthUser authUser) {
        firstname = clientDTO.getFirstname();
        lastname = clientDTO.getLastname();
        username = clientDTO.getUsername().toLowerCase(Locale.ROOT);
        email = clientDTO.getEmail();
        address = clientDTO.getAddress();
        this.country = country;
        updatedBy = String.valueOf(authUser.getId());
    }

}
