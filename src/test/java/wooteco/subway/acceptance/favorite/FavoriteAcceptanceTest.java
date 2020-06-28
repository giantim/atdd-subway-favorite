package wooteco.subway.acceptance.favorite;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.line.dto.LineResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.station.dto.StationResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    @DisplayName("즐겨찾기 관련 기능")
    @TestFactory
    public Stream<DynamicTest> manageFavorite() {
        // given 노선과 역, 구간이 주어진다
        LineResponse line = createLine(LINE_NAME_2);
        StationResponse kangnam = createStation(STATION_NAME_KANGNAM);
        StationResponse dogok = createStation(STATION_NAME_DOGOK);
        StationResponse hanti = createStation(STATION_NAME_HANTI);
        addLineStation(line.getId(), null, kangnam.getId());
        addLineStation(line.getId(), kangnam.getId(), dogok.getId());
        addLineStation(line.getId(), dogok.getId(), hanti.getId());

        // and 회원이 로그인한 상태이다
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        return Stream.of(dynamicTest("즐겨찾기 추가", () -> {
                // given 경로를 검색한 상태이다
                String departureName = kangnam.getName();
                String arrivalName = hanti.getName();
                // when 즐겨찾기를 저장하는 요청을 보낸다
                String location = addFavorite(tokenResponse, departureName, arrivalName);

                // then 해당 회원에 즐겨찾기가 추가되었다
                assertThat(location).isNotNull();
            }),
            dynamicTest("즐겨찾기 목록 조회", () -> {
                // when 즐겨찾기 목록을 조회한다
                List<FavoriteResponse> favoriteResponses = getFavorites(tokenResponse);

                // then 해당 회원의 즐겨찾기 목록을 반환한다
                assertThat(favoriteResponses.size()).isEqualTo(1);
            }),
            dynamicTest("즐겨찾기 삭제", () -> {
                // when 회원이 즐겨찾기 삭제를 요청한다
                String departureName = kangnam.getName();
                String arrivalName = hanti.getName();
                deleteFavorite(tokenResponse, departureName, arrivalName);

                // then 해당 회원의 즐겨찾기가 삭제되었다
                List<FavoriteResponse> favoriteResponses = getFavorites(tokenResponse);
                assertThat(favoriteResponses).isEmpty();
            }));
    }

    private String addFavorite(TokenResponse tokenResponse, String departureName, String arrivalName) {
        Map<String, String> params = new HashMap<>();
        params.put("departureName", departureName);
        params.put("arrivalName", arrivalName);

        return given().
            header("Authorization",
                tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
            body(params).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            when().
            post("/favorites").
            then().
            log().all().
            statusCode(HttpStatus.CREATED.value()).
            extract().header("Location");
    }

    private List<FavoriteResponse> getFavorites(TokenResponse tokenResponse) {
        return given().
            header("Authorization",
                tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            get("/favorites").
            then().
            log().all().
            statusCode(HttpStatus.OK.value()).
            extract().jsonPath().getList(".", FavoriteResponse.class);
    }

    private void deleteFavorite(TokenResponse tokenResponse, String departureName, String arrivalName) {
        Map<String, String> params = new HashMap<>();
        params.put("departureName", departureName);
        params.put("arrivalName", arrivalName);

        given().
            header("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
            body(params).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            when().
            delete("/favorites").
            then().
            log().all().
            statusCode(HttpStatus.OK.value());
    }
}
