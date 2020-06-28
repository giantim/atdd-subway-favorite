package wooteco.subway.service.line;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.line.LineStationRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.line.dto.LineStationCreateRequest;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LineServiceTest {
    private static final String STATION_NAME1 = "강남역";
    private static final String STATION_NAME2 = "역삼역";
    private static final String STATION_NAME3 = "선릉역";
    private static final String STATION_NAME4 = "삼성역";

    @Mock
    private LineRepository lineRepository;

    @Mock
    private LineStationService lineStationService;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private LineStationRepository lineStationRepository;

    private LineService lineService;

    private Line line;
    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;

    @BeforeEach
    void setUp() {
        lineService = new LineService(lineStationService, lineRepository, stationRepository, lineStationRepository);

        station1 = new Station(1L, STATION_NAME1);
        station2 = new Station(2L, STATION_NAME2);
        station3 = new Station(3L, STATION_NAME3);
        station4 = new Station(4L, STATION_NAME4);

        line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line.addLineStation(new LineStation(null, station1, 10, 10));
        line.addLineStation(new LineStation(station1, station2, 10, 10));
        line.addLineStation(new LineStation(station2, station3, 10, 10));
    }

    @Test
    void addLineStationAtTheFirstOfLine() {
        when(stationRepository.findById(station4.getId())).thenReturn(Optional.of(station4));
        when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));

        LineStationCreateRequest request = new LineStationCreateRequest(null, station4.getId(), 10, 10);
        lineService.addLineStation(line.getId(), request);

        assertThat(line.getLineStations().getStations()).hasSize(4);

        List<Long> stationIds = line.getStationIds();
        assertThat(stationIds.get(0)).isEqualTo(4L);
        assertThat(stationIds.get(1)).isEqualTo(1L);
        assertThat(stationIds.get(2)).isEqualTo(2L);
        assertThat(stationIds.get(3)).isEqualTo(3L);
    }

    @Test
    void addLineStationBetweenTwo() {
        when(stationRepository.findById(station1.getId())).thenReturn(Optional.of(station1));
        when(stationRepository.findById(station4.getId())).thenReturn(Optional.of(station4));
        when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));

        LineStationCreateRequest request = new LineStationCreateRequest(station1.getId(), station4.getId(), 10, 10);
        lineService.addLineStation(line.getId(), request);

        assertThat(line.getLineStations().getStations()).hasSize(4);

        List<Long> stationIds = line.getStationIds();
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(4L);
        assertThat(stationIds.get(2)).isEqualTo(2L);
        assertThat(stationIds.get(3)).isEqualTo(3L);
    }

    @Test
    void addLineStationAtTheEndOfLine() {
        when(stationRepository.findById(station3.getId())).thenReturn(Optional.of(station3));
        when(stationRepository.findById(station4.getId())).thenReturn(Optional.of(station4));
        when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));

        LineStationCreateRequest request = new LineStationCreateRequest(station3.getId(), station4.getId(), 10, 10);
        lineService.addLineStation(line.getId(), request);

        assertThat(line.getLineStations().getStations()).hasSize(4);

        List<Long> stationIds = line.getStationIds();
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(2L);
        assertThat(stationIds.get(2)).isEqualTo(3L);
        assertThat(stationIds.get(3)).isEqualTo(4L);
    }

    @Test
    void removeLineStationAtTheFirstOfLine() {
        when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));
        lineService.removeLineStation(line.getId(), 1L);

        assertThat(line.getLineStations().getStations()).hasSize(2);

        List<Long> stationIds = line.getStationIds();
        assertThat(stationIds.get(0)).isEqualTo(2L);
        assertThat(stationIds.get(1)).isEqualTo(3L);
    }

    @Test
    void removeLineStationBetweenTwo() {
        when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));
        lineService.removeLineStation(line.getId(), 2L);

        verify(lineRepository).save(any());
    }

    @Test
    void removeLineStationAtTheEndOfLine() {
        when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));
        lineService.removeLineStation(line.getId(), 3L);

        assertThat(line.getLineStations().getStations()).hasSize(2);

        List<Long> stationIds = line.getStationIds();
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(2L);
    }
}
