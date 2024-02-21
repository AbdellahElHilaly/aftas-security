package com.aftas_backend.factory.seeders;

import com.aftas_backend.factory.fakers.MemberFaker;
import com.aftas_backend.models.entities.Member;
import com.aftas_backend.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberSeeder {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberFaker memberFaker;

    public void seed(Integer count) {
        List<Member> members = new  ArrayList<>();
        members.add(getAdmin());
        for (int i = 0; i < count; i++) {
            members.add(memberFaker.makeMember());
        }
        memberRepository.saveAll(members);
    }

    private Member getAdmin() {
        return Member.builder()
                .number(1)
                .firstName("Admin")
                .lastName("Admin")
                .password(passwordEncoder.encode("admin"))
                .build();
    }
}
