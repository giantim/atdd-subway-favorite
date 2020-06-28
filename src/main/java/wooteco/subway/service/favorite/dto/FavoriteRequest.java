package wooteco.subway.service.favorite.dto;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

public class FavoriteRequest {
    @NotNull
    private String departureName;
    @NotNull
    private String arrivalName;

    private FavoriteRequest() {
    }

    public FavoriteRequest(String departureName, String arrivalName) {
        this.departureName = departureName;
        this.arrivalName = arrivalName;
    }

    public List<String> stationNames() {
        return Arrays.asList(departureName, arrivalName);
    }

    public String getDepartureName() {
        return departureName;
    }

    public String getArrivalName() {
        return arrivalName;
    }
}
