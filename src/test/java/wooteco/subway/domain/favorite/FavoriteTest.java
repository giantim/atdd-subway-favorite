package wooteco.subway.domain.favorite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.station.Station;

import static org.assertj.core.api.Assertions.assertThat;

class FavoriteTest {
    private Member member;
    private Station departure;
    private Station arrival;

    @BeforeEach
    private void setUp() {
        member = new Member(1L, "a@a.com", "pobi", "1234");
        departure = new Station(1L, "강남");
        arrival = new Station(2L, "도곡");
    }

    @Test
    void isDuplicate() {
        Favorite favorite = new Favorite(member, departure, arrival);
        Favorite duplicated = new Favorite(member, departure, arrival);

        assertThat(favorite.isDuplicate(duplicated)).isTrue();
    }

    @Test
    void isDuplicateWhenFail() {
        Favorite favorite = new Favorite(member, departure, arrival);
        Station nonDuplicateDeparture = new Station(3L, "잠실");
        Station nonDuplicateArrival = new Station(4L, "석촌");
        Member nonDuplicateMember = new Member(2L, "b@b.com", "jason", "123456");
        Favorite duplicated = new Favorite(nonDuplicateMember, nonDuplicateDeparture, nonDuplicateArrival);

        assertThat(favorite.isDuplicate(duplicated)).isFalse();
    }
}