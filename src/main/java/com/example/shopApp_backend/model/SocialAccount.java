package com.example.shopApp_backend.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "social_accounts")
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "provider", nullable = false, length = 20)
    private String provider;

    @Column(name = "provider_id", nullable = false, length = 50)
    private String providerId;

    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
