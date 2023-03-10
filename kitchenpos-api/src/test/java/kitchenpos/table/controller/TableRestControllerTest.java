package kitchenpos.table.controller;

import static kitchenpos.table.fixture.OrderTableFixture.createOrderTable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.table.controller.TableRestController;
import kitchenpos.table.service.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.controller.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.table.controller.dto.response.TableCreateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext ctx;

    @MockBean
    private TableService tableService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("???????????? ????????????.")
    @Test
    void create() throws Exception {
        // given
        OrderTable orderTable = createOrderTable(0, true);
        given(tableService.create(any())).willReturn(TableCreateResponse.from(createOrderTable(1L)));

        // when
        ResultActions perform = mockMvc.perform(post("/api/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(orderTable)))
                .andDo(print());

        // then
        perform.andExpect(status().isCreated());
    }

    @DisplayName("???????????? ????????????.")
    @Test
    void list() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(get("/api/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print());

        // then
        perform.andExpect(status().isOk());
    }

    @DisplayName("??? ???????????? ????????????.")
    @Test
    void changeEmpty() throws Exception {
        // given
        OrderTable orderTable = createOrderTable(true);

        // when
        ResultActions perform = mockMvc.perform(put("/api/tables/1/empty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(orderTable)))
                .andDo(print());

        // then
        perform.andExpect(status().isOk());
    }

    @DisplayName("????????? ?????? ?????? ????????????.")
    @Test
    void changeNumberOfGuests() throws Exception {
        // given
        OrderTable orderTable = createOrderTable(4);

        // when
        ResultActions perform = mockMvc.perform(put("/api/tables/1/number-of-guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(orderTable)))
                .andDo(print());

        // then
        perform.andExpect(status().isOk());
    }

    @DisplayName("????????? ?????? ?????? ????????? ??? ???????????? ????????? ????????????.")
    @Test
    void changeNumberOfGuests_fail_if_numberOfGuests_is_negative() throws Exception {
        // given
        OrderTableChangeNumberOfGuestsRequest request = new OrderTableChangeNumberOfGuestsRequest(-1);

        // when
        ResultActions perform = mockMvc.perform(put("/api/tables/1/number-of-guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

        // then
        perform.andExpect(status().isBadRequest());
    }
}
