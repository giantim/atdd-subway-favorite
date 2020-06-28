package wooteco.subway.domain.station;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import wooteco.subway.domain.BaseEntity;
import wooteco.subway.domain.line.LineStation;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "STATION")
@AttributeOverride(name = "id", column = @Column(name = "station_id"))
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@EqualsAndHashCode(callSuper = false)
public class Station extends BaseEntity {
    @Column(unique = true)
    private String name;

    @CreatedDate
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "preStation", cascade = CascadeType.REMOVE)
    private List<LineStation> preStations = new ArrayList<>();

    @OneToMany(mappedBy = "station", cascade = CascadeType.REMOVE)
    private List<LineStation> stations = new ArrayList<>();

    public Station(String name) {
        this.name = name;
    }

    public Station(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public boolean isSameId(Long stationId) {
        return this.id.equals(stationId);
    }
}
