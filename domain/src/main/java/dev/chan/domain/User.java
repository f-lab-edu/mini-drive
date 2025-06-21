package dev.chan.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;
    private String fileName;
    private String email;
    private String password;
    private String profile;
    private String phoneNumber;

}
