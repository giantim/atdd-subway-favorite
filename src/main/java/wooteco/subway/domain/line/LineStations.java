package wooteco.subway.domain.line;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wooteco.subway.domain.station.Station;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LineStations {
    @OneToMany(mappedBy = "line", fetch = FetchType.EAGER)
    private Set<LineStation> stations = new HashSet<>();

    public LineStations(Set<LineStation> stations) {
        this.stations = stations;
    }

    public static LineStations empty() {
        return new LineStations(new HashSet<>());
    }

    public void add(LineStation targetLineStation) {
        updatePreStationOfNextLineStation(targetLineStation.getPreStation(), targetLineStation.getStation());
        stations.add(targetLineStation);
    }

    private void remove(LineStation targetLineStation) {
        updatePreStationOfNextLineStation(targetLineStation.getStation(), targetLineStation.getPreStation());
        stations.remove(targetLineStation);
    }

    public void removeById(Long targetStationId) {
        extractByStation(targetStationId)
                .ifPresent(this::remove);
    }

    public List<Long> getStationIds() {
        List<Long> result = new ArrayList<>();
        extractNext(null, result);
        return result;
    }

    private void extractNext(Station preStation, List<Long> ids) {
        stations.stream()
                .filter(it -> Objects.equals(it.getPreStation(), preStation))
                .findFirst()
                .ifPresent(it -> {
                    Station nextStationId = it.getStation();
                    ids.add(nextStationId.getId());
                    extractNext(nextStationId, ids);
                });
    }

    private void updatePreStationOfNextLineStation(Station targetStation, Station newPreStation) {
        extractByPreStation(targetStation)
                .ifPresent(it -> it.updatePreLineStation(newPreStation));
    }

    private Optional<LineStation> extractByStation(Long stationId) {
        return stations.stream()
                .filter(it -> Objects.equals(it.getStation().getId(), stationId))
                .findFirst();
    }

    private Optional<LineStation> extractByPreStation(Station preStation) {
        return stations.stream()
                .filter(it -> Objects.equals(it.getPreStation(), preStation))
                .findFirst();
    }

    public int getTotalDistance() {
        return stations.stream().mapToInt(it -> it.getDistance()).sum();
    }

    public int getTotalDuration() {
        return stations.stream().mapToInt(it -> it.getDuration()).sum();
    }

    public boolean isContain(LineStation lineStation) {
        return stations.contains(lineStation);
    }

    public void removeLineStation(LineStation lineStation) {
        stations.remove(lineStation);
    }
}
