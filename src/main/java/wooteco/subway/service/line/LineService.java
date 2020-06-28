package wooteco.subway.service.line;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.line.LineStationRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.service.line.dto.LineRequest;
import wooteco.subway.service.line.dto.LineStationCreateRequest;
import wooteco.subway.service.line.dto.WholeSubwayResponse;
import wooteco.subway.service.station.exception.InvalidStationNameException;

import java.util.List;
import java.util.Objects;

@Service
public class LineService {
    private LineStationService lineStationService;
    private LineRepository lineRepository;
    private StationRepository stationRepository;
    private LineStationRepository lineStationRepository;

    public LineService(LineStationService lineStationService, LineRepository lineRepository,
                       StationRepository stationRepository, LineStationRepository lineStationRepository) {
        this.lineStationService = lineStationService;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.lineStationRepository = lineStationRepository;
    }

    public Line save(Line line) {
        return lineRepository.save(line);
    }

    public List<Line> findLines() {
        return lineRepository.findAll();
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public void updateLine(Long id, LineRequest request) {
        Line persistLine = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        persistLine.update(request.toLine());
        lineRepository.save(persistLine);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long id, LineStationCreateRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        Station preStation = findPreStation(request);
        Station station = stationRepository.findById(request.getStationId())
                .orElseThrow(InvalidStationNameException::new);
        LineStation lineStation = new LineStation(preStation, station, request.getDistance(), request.getDuration());
        lineStation.setLine(line);
        line.addLineStation(lineStation);
        lineStationRepository.save(lineStation);
        lineRepository.save(line);
    }

    private Station findPreStation(LineStationCreateRequest request) {
        Long preStationId = request.getPreStationId();
        if (Objects.isNull(preStationId)) {
            return null;
        }
        return stationRepository.findById(preStationId).orElseThrow(InvalidStationNameException::new);
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(RuntimeException::new);
        line.removeLineStationById(stationId);
        lineRepository.save(line);
    }

    public LineDetailResponse retrieveLine(Long id) {
        return lineStationService.findLineWithStationsById(id);
    }

    public WholeSubwayResponse findLinesWithStations() {
        return lineStationService.findLinesWithStations();
    }
}
