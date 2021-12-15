package com.cookingwebsite.crud.security.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "User", schema = "cookingwebsite")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @NotNull
    @Column(unique = true)
    private String username;
    @NotNull
    private String email;
    @NotNull
    private String password;
    
    @NotNull
    private String firstname;
    
    @NotNull
    private String lastname;
    
    @Column(name = "au_creation_user", nullable = false)
	private Integer auCreationUser;

	@Column(name = "au_creation_date", nullable = false)
	private Timestamp auCreationDate;

	@Column(name = "au_modification_user", nullable = true)
	private Integer auModificationUser;

	@Column(name = "au_modification_date", nullable = true)
	private Timestamp auModificationDate;

	@Column(name = "au_active", nullable = false)
	private Boolean auActive;
    
    @NotNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "Membership", joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    public User(@NotNull String username, @NotNull String firstname, @NotNull String lastname, @NotNull String email, @NotNull String password) {
    	this.firstname = firstname;
    	this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.password = password;
    }

}