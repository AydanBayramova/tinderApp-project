package org.example.tinderapp.domain.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class User {
    private Long id;
    private String name;
    private String surname;
    private String userName;
    private String email;
    private String phoneNumber;
    private String password;

    public User(String name, String surname, String userName, String email, String phoneNumber, String password) {
        this.name = name;
        this.surname = surname;
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }
}
