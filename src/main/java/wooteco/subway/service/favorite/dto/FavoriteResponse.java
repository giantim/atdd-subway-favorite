package wooteco.subway.service.favorite.dto;

import wooteco.subway.domain.favorite.Favorite;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class FavoriteResponse {
    private String departureName;
    private String arrivalName;

    private FavoriteResponse() {
    }

    public FavoriteResponse(String departureName, String arrivalName) {
        this.departureName = departureName;
        this.arrivalName = arrivalName;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getDeparture().getName(), favorite.getArrival().getName());
    }

    public static List<FavoriteResponse> listOf(List<Favorite> favorites) {
        return favorites.stream()
            .map(FavoriteResponse::of)
            .collect(toList());
    }

    public String getDepartureName() {
        return departureName;
    }

    public String getArrivalName() {
        return arrivalName;
    }
}
