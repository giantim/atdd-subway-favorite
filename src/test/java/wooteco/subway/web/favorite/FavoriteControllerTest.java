package wooteco.subway.web.favorite;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

@SpringBootTest
@AutoConfigureMockMvc
class FavoriteControllerTest {
    private final String uri = "/favorites";

    @MockBean
    private FavoriteService favoriteService;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void createFavorite() throws Exception {
        given(favoriteService.create(any(), any())).willReturn(1L);

        String inputJson = "{\"departure\" : \"잠실역\", \"arrival\" : \"석촌역\"}";
        mockMvc.perform(post(uri)
            .header("Authorization", "bearer tokenValues")
            .content(inputJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andReturn();
    }

    @Test
    void showFavorites() throws Exception {
        FavoriteResponse favoriteResponse = new FavoriteResponse("잠실", "석촌");
        List<FavoriteResponse> favoriteResponses = Collections.singletonList(favoriteResponse);
        given(favoriteService.findAll(any())).willReturn(favoriteResponses);

        MvcResult mvcResult = mockMvc.perform(get(uri)
            .header("Authorization", "bearer tokenValues"))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

        assertThat(mvcResult.getResponse().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    void deleteFavorite() throws Exception {
        String inputJson = "{\"departure\" : \"잠실역\", \"arrival\" : \"석촌역\"}";

        mockMvc.perform(delete(uri)
            .header("Authorization", "bearer tokenValues")
            .content(inputJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
    }
}