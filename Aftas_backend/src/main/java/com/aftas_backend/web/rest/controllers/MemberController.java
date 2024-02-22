package com.aftas_backend.web.rest.controllers;

import com.aftas_backend.handlers.response.ResponseMessage;
import com.aftas_backend.models.entities.Member;
import com.aftas_backend.security.common.principal.UserPrincipal;
import com.aftas_backend.security.common.principal.UserPrincipalService;
import com.aftas_backend.services.MemberService;
import com.aftas_backend.web.rest.vms.member.MemberRequestUpdateVM;
import com.aftas_backend.web.rest.vms.member.MemberRequestVM;
import com.aftas_backend.web.rest.vms.member.MemberResponseVM;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberService memberService;
    private final UserPrincipalService userPrincipalService;


    @PreAuthorize(value = "hasRole('MANAGER')")
    @GetMapping
    public ResponseEntity getAllMembers(@ParameterObject Pageable pageable, @RequestParam(required = false, name = "search") String search) {
        List<Member> members = memberService.getAllMembers(pageable, search);
        List<MemberResponseVM> memberResponseVMS = new ArrayList<>();
        for (Member member : members) {
            memberResponseVMS.add(MemberResponseVM.fromMember(member));
        }
        return ResponseMessage.ok(memberResponseVMS, "Members retrieved successfully");
    }

    @PreAuthorize(value = "hasRole('MANAGER')")
    @PostMapping()
    public ResponseEntity createMember(@Valid @RequestBody MemberRequestVM memberVM) {
        Member createdMember = memberService.createMember(memberVM.toMember());
        MemberResponseVM memberResponseVM = MemberResponseVM.fromMember(createdMember);
        return ResponseMessage.created(memberResponseVM, "Member created successfully");
    }

    @PreAuthorize(value = "hasRole('MANAGER')")
    @GetMapping("/number/{number}")
    public ResponseEntity getMemberByNumber(@PathVariable Integer number) {
        Member member = memberService.getMemberByNumber(number);
        MemberResponseVM memberResponseVM = MemberResponseVM.fromMember(member);
        return ResponseMessage.ok(memberResponseVM, "Member retrieved successfully");
    }

    @PreAuthorize(value = "hasRole('MANAGER')")
    @PutMapping("/{number}")
    public ResponseEntity updateMember(@Valid @PathVariable Integer number, @RequestBody MemberRequestUpdateVM memberVM) {
        Member updatedMember = memberService.updateMember(memberVM.toMember(), number);
        MemberResponseVM memberResponseVM = MemberResponseVM.fromMember(updatedMember);
        return ResponseMessage.ok(memberResponseVM, "Member updated successfully");
    }

    @PreAuthorize(value = "hasRole('MANAGER')")
    @DeleteMapping("/{number}")
    public ResponseEntity deleteMember(@PathVariable Integer number) {
        memberService.deleteMember(number);
        return ResponseMessage.ok(null, "Member deleted successfully");
    }

    @PreAuthorize(value = "hasRole('MANAGER')")
    @GetMapping("/count")
    public ResponseEntity countMembers() {
        Long count = memberService.countMembers();
        return ResponseMessage.ok(count, "Members counted successfully");
    }


    @GetMapping("/me")
    public UserPrincipal getMe() {
        return userPrincipalService.getUserPrincipalFromContextHolder();
    }


}
