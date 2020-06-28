package wooteco.subway.domain.line;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import wooteco.subway.domain.station.Station;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class LineRepositoryTest {
    private static final String STATION_NAME1 = "강남역";
    private static final String STATION_NAME2 = "역삼역";

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private LineStationRepository lineStationRepository;

    @Test
    void addLineStation() {
        // given
        Line line = new Line("2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        Line persistLine = lineRepository.save(line);
        Station station1 = new Station(1L, STATION_NAME1);
        Station station2 = new Station(2L, STATION_NAME2);
        LineStation lineStation1 = new LineStation(null, station1, 10, 10);
        LineStation lineStation2 = new LineStation(station1, station2, 10, 10);
        lineStation1.setLine(persistLine);
        lineStation2.setLine(persistLine);
        persistLine.addLineStation(lineStationRepository.save(lineStation1));
        persistLine.addLineStation(lineStationRepository.save(lineStation2));

        // when
        Line resultLine = lineRepository.save(persistLine);

        // then
        assertThat(resultLine.getLineStations().getStations()).hasSize(2);
    }
}
