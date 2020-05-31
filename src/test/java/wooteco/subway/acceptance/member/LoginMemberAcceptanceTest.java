package wooteco.subway.acceptance.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.member.dto.ErrorResponse;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class LoginMemberAcceptanceTest extends AcceptanceTest {
    @DisplayName("로그인 멤버 기능")
    @TestFactory
    Stream<DynamicTest> manageMember() {
        // Given 유저 정보가 생성 되어 있다.
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        // When 로그인을 요청한다.
        // Then 토큰을 획득한다.
        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        return Stream.of(
            dynamicTest("로그인", () -> {
                assertThat(tokenResponse.getTokenType()).isEqualTo("bearer");
                assertThat(tokenResponse.getAccessToken()).isNotEmpty();
            }),
            dynamicTest("회원 본인 정보 수정", () -> {
                // When 로그인 후 회원 정보 수정 요청을 한다.
                String updateName = "NEW_" + TEST_USER_NAME;
                String updatePassword = "NEW_" + TEST_USER_PASSWORD;
                modify(tokenResponse, updateName, updatePassword);
                // Then 회원 정보가 수정 된다.
                MemberResponse updatedMember = getMember(TEST_USER_EMAIL);
                assertThat(updatedMember.getName()).isEqualTo(updateName);
            }),
            dynamicTest("회원 본인 탈퇴", () -> {
                // When 로그인 후 회원 탈퇴 요청을 한다.
                delete(tokenResponse);
                // Then 해당 회원이 삭제 된다.
                assertThat(getInvalidMember(TEST_USER_EMAIL)).isInstanceOf(ErrorResponse.class);
            })
        );
    }

    private void modify(TokenResponse tokenResponse, String updateName, String updatePassword) {
        Map<String, String> params = new HashMap<>();
        params.put("name", updateName);
        params.put("password", updatePassword);

        given().
            header("Authorization",
                tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
            body(params).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            put("/me/bearer").
            then().
            log().all().
            statusCode(HttpStatus.OK.value());
    }

    private void delete(TokenResponse tokenResponse) {
        given().
            header("Authorization",
                tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            delete("/me/bearer").
            then().
            log().all().
            statusCode(HttpStatus.OK.value());
    }

    private ErrorResponse getInvalidMember(String email) {
        return given().
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            get("/members?email=" + email).
            then().
            log().all().
            statusCode(HttpStatus.BAD_REQUEST.value()).
            extract().as(ErrorResponse.class);
    }

    @DisplayName("토큰 획득에 실패")
    @Test
    void invalidLoginMember() {
        // when 정보가 없는 회원으로 로그인 시도를 한다
        // then 토큰을 발급 받지 못한다
        assertThat(invalidLoginResponse(TEST_USER_EMAIL, TEST_USER_PASSWORD)).isInstanceOf(ErrorResponse.class);
    }

    private ErrorResponse invalidLoginResponse(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/oauth/token").
                then().
                log().all().
                statusCode(HttpStatus.BAD_REQUEST.value()).
                extract().as(ErrorResponse.class);
    }
}
