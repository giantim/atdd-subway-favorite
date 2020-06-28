package wooteco.subway.domain.station;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {
    @Override
    List<Station> findAllById(Iterable ids);

    List<Station> findAllByNameIn(List<String> stationNames);

    @Override
    List<Station> findAll();

    Optional<Station> findByName(String stationName);
}
