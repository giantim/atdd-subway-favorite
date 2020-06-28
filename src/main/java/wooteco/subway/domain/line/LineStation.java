package wooteco.subway.domain.line;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wooteco.subway.domain.station.Station;

import javax.persistence.*;

@Entity
@Table(name = "LINE_STATION")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Getter
public class LineStation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_station_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "line_id", nullable = false)
    private Line line;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pre_station_id")
    private Station preStation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    private int distance;
    private int duration;

    public LineStation(Station preStation, Station station, int distance, int duration) {
        this.preStation = preStation;
        this.station = station;
        this.distance = distance;
        this.duration = duration;
    }

    public void updatePreLineStation(Station preStation) {
        this.preStation = preStation;
    }

    public boolean isLineStationOf(Station preStation, Station station) {
        return this.preStation == preStation && this.station == station
                || this.preStation == station && this.station == preStation;
    }

    public void setLine(Line line) {
        this.line = line;
    }
}
