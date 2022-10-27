package kitchenpos.ui;

import static kitchenpos.fixture.MenuFixture.createMenu;
import static kitchenpos.fixture.MenuProductFixture.createMenuProduct;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MenuRestController.class)
public class MenuRestControllerTest extends ControllerTest {

    @MockBean
    private MenuService menuService;

    @DisplayName("메뉴를 생성한다.")
    @Test
    public void create() throws Exception {
        // given
        Menu menu = createMenu("후라이드+후라이드", 19_000L, 1L,
                Collections.singletonList(createMenuProduct(1L, 2)));
        given(menuService.create(any())).willReturn(createMenu(1L));

        // when
        ResultActions perform = mockMvc.perform(post("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(menu)))
                .andDo(print());

        // then
        perform.andExpect(status().isCreated());
    }

    @DisplayName("메뉴를 조회한다.")
    @Test
    public void list() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(get("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print());

        // then
        perform.andExpect(status().isOk());
    }
}