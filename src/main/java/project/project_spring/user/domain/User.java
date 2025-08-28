package project.project_spring.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import project.project_spring.user.domain.common.BaseEntity;
import project.project_spring.user.domain.enums.AccountStatus;
import project.project_spring.user.domain.enums.Gender;
import project.project_spring.user.domain.enums.Role;
import project.project_spring.user.web.dto.SignupRequest;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String email;

    @Column
    private String password;

    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String specAddress;

    @Column(nullable = false)
    @ColumnDefault("'USER'")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    @ColumnDefault("'ACTIVATED'")
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @Builder
    private User(String userName, String email, String password, String gender, Integer age, String address, String specAddress){

        this.userName = userName;
        this.email = email;
        this.password = password;

        if (gender.equals("MALE")) this.gender = Gender.MALE;
        else if (gender.equals("FEMALE")) this.gender = Gender.MALE;

        this.age = age;
        this.address = address;
        this.specAddress = specAddress;
        this.role = Role.USER;
        this.accountStatus = AccountStatus.ACTIVATED;
    }

    public static User of(SignupRequest request, String encryptedPassword){

        return User.builder()
                .userName(request.getUserName())
                .email(request.getEmail())
                .address(request.getAddress())
                .password(encryptedPassword)
                .gender(request.getGender())
                .age(request.getAge())
                .specAddress(request.getSpecAddress())
                .build();
    }

}
