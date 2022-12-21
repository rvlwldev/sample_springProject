package com.shopping.study.Domain.Login.Interface;

import com.shopping.study.Domain.Login.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepo extends JpaRepository<Member, String> {
}
