package wooteco.subway.service.favorite;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.favorite.exception.DuplicateFavoriteException;
import wooteco.subway.service.favorite.exception.NoExistFavoriteException;
import wooteco.subway.service.member.exception.InvalidMemberIdException;
import wooteco.subway.service.station.exception.InvalidStationNameException;

import java.util.List;

@Service
public class FavoriteService {
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository,
                           FavoriteRepository favoriteRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public Long create(Long memberId, FavoriteRequest favoriteRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(InvalidMemberIdException::new);
        List<String> stationNames = favoriteRequest.stationNames();
        List<Station> stations = stationRepository.findAllByNameIn(stationNames);
        if (stations.size() != 2) {
            throw new InvalidStationNameException();
        }

        Favorite favorite = new Favorite(member, stations.get(0), stations.get(1));

        if (isDuplicateFavorite(member, favorite)) {
            throw new DuplicateFavoriteException();
        }

        return favoriteRepository.save(favorite).getId();
    }

    private boolean isDuplicateFavorite(Member member, Favorite favorite) {
        return favoriteRepository.findAllByMember(member)
                .stream()
                .anyMatch(f -> f.isDuplicate(favorite));
    }

    @Transactional
    public void delete(Long memberId, FavoriteRequest favoriteRequest) {
        Favorite favorite = favoriteRepository.findByMemberIdAndDepartureNameAndArrivalName(memberId,
                favoriteRequest.getDepartureName(), favoriteRequest.getArrivalName())
                .orElseThrow(NoExistFavoriteException::new);

        favoriteRepository.delete(favorite);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findAll(Long memberId) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(memberId);
        return FavoriteResponse.listOf(favorites);
    }
}
