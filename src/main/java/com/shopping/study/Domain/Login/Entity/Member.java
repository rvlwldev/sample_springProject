package com.shopping.study.Domain.Login.Entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "MEMBER", schema = "shopping")
public class Member {

    @Id
    @Column(name = "MEMBER_ID")
    private String id;

    @Column(name = "MEMBER_PASSWORD")
    private String password;

    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "SIGNED_UP_DATE")
    private Date signedUpDate;

    @Column(name = "ROLE_CODE")
    private String role;

    // TODO: 추후 userDetails 의 필드들 매핑필요
}
