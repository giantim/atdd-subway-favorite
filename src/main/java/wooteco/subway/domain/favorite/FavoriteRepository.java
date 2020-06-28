package wooteco.subway.domain.favorite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.member.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findAllByMember(Member member);

    List<Favorite> findAllByMemberId(Long memberId);

    Optional<Favorite> findByMemberIdAndDepartureIdAndArrivalId(Long memberId, Long departureId, Long arrivalId);

    Optional<Favorite> findByMemberIdAndDepartureNameAndArrivalName(Long memberId, String departureName,
                                                                    String arrivalName);

    void deleteByMemberId(Long memberId);
}
