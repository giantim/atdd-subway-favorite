package wooteco.subway.domain.line;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wooteco.subway.domain.BaseEntity;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "LINE")
@AttributeOverride(name = "id", column = @Column(name = "line_id"))
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Line extends BaseEntity {
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;

    @Embedded
    private LineStations lineStations = LineStations.empty();

    public Line(Long id, String name, LocalTime startTime, LocalTime endTime, int intervalTime) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
    }

    public Line(String name, LocalTime startTime, LocalTime endTime, int intervalTime) {
        this(null, name, startTime, endTime, intervalTime);
    }

    public void update(Line line) {
        if (line.getName() != null) {
            this.name = line.getName();
        }
        if (line.getStartTime() != null) {
            this.startTime = line.getStartTime();
        }
        if (line.getEndTime() != null) {
            this.endTime = line.getEndTime();
        }
        if (line.getIntervalTime() != 0) {
            this.intervalTime = line.getIntervalTime();
        }
    }

    public void addLineStation(LineStation lineStation) {
        lineStations.add(lineStation);
    }

    public void removeLineStationById(Long stationId) {
        lineStations.removeById(stationId);
    }

    public List<Long> getStationIds() {
        return lineStations.getStationIds();
    }
}
