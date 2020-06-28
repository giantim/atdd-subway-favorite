package wooteco.subway.domain.favorite;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wooteco.subway.domain.BaseEntity;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.station.Station;

import javax.persistence.*;

@Entity
@Table(name = "FAVORITE")
@AttributeOverride(name = "id", column = @Column(name = "favorite_id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Favorite extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "departure_id")
    private Station departure;

    @OneToOne
    @JoinColumn(name = "arrival_id")
    private Station arrival;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Favorite(Member member, Station departure, Station arrival) {
        this(null, member, departure, arrival);
    }

    public Favorite(Long id, Member member, Station departure, Station arrival) {
        this.id = id;
        this.member = member;
        this.departure = departure;
        this.arrival = arrival;
    }

    public boolean isDuplicate(Favorite favorite) {
        return this.member.equals(favorite.member)
                && this.departure.equals(favorite.departure)
                && this.arrival.equals(favorite.arrival);
    }
}
