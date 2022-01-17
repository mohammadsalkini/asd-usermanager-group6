package model;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User {
    private String username;
    private String firstName;
    private String lastName;
    private String password;
}
