package com.shopping.study.Domain.Login.Service;

import com.shopping.study.Domain.Login.Entity.Member;
import com.shopping.study.Domain.Login.Entity.MemberDetails;
import com.shopping.study.Domain.Login.Interface.MemberRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LoginService implements UserDetailsService {

    @Autowired
    MemberRepo memberRepo;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Member member;
        try {
            member = memberRepo.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("아이디 못찾음;");
        }

        return setUserDetails(member);
    }

    private UserDetails setUserDetails(Member member) {
        MemberDetails memberDetails = new MemberDetails();

        List<GrantedAuthority> auth = new ArrayList<>();
        auth.add(new SimpleGrantedAuthority(member.getRole()));

        memberDetails.setUsername(member.getNickname());
        memberDetails.setPassword(member.getPassword());
        memberDetails.setAuthorities(auth);

        // 일단 다 true
        memberDetails.setEnabled(true);
        memberDetails.setAccountNonExpired(true);
        memberDetails.setAccountNonLocked(true);
        memberDetails.setCredentialsNonExpired(true);

        return memberDetails;
    }

}
