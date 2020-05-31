package wooteco.subway.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<Void> createMember(@RequestBody @Valid MemberRequest memberRequest) {
        MemberResponse memberResponse = memberService.createMember(memberRequest);
        return ResponseEntity
                .created(URI.create("/members/" + memberResponse.getId()))
                .build();
    }

    @GetMapping("/members")
    public ResponseEntity<MemberResponse> getMemberByEmail(@RequestParam String email) {
        MemberResponse memberResponse = memberService.findMemberByEmail(email);
        return ResponseEntity.ok().body(memberResponse);
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long id,
                                                       @RequestBody @Valid UpdateMemberRequest param) {
        memberService.updateMember(id, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<MemberResponse> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}
