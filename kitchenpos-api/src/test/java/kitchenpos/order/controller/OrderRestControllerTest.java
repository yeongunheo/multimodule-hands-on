package kitchenpos.order.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import kitchenpos.order.controller.OrderRestController;
import kitchenpos.order.service.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.controller.dto.request.OrderCreateRequest;
import kitchenpos.order.controller.dto.request.OrderLineItemDto;
import kitchenpos.order.controller.dto.request.OrderStatusChangeRequest;
import kitchenpos.order.controller.dto.response.OrderCreateResponse;
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

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext ctx;

    @MockBean
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("????????? ????????????.")
    @Test
    void create() throws Exception {
        // given
        OrderCreateRequest request = new OrderCreateRequest(1L, Arrays.asList(new OrderLineItemDto(1L, 2)));
        given(orderService.create(any())).willReturn(OrderCreateResponse.of(Order.builder()
                .id(1L)
                .orderStatus(OrderStatus.COOKING)
                .orderTableId(1L)
                .orderedTime(LocalDateTime.now())
                .build(), new ArrayList<>()));

        // when
        ResultActions perform = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

        // then
        perform.andExpect(status().isCreated());
    }

    @DisplayName("????????? ????????? ??? ??????????????? ??????????????? ????????? ????????????.")
    @Test
    void create_fail_if_orderLineItems_is_empty() throws Exception {
        // given
        OrderCreateRequest request = new OrderCreateRequest(1L, new ArrayList<>());
        given(orderService.create(any())).willReturn(OrderCreateResponse.of(Order.builder()
                .id(1L)
                .orderStatus(OrderStatus.COOKING)
                .orderTableId(1L)
                .orderedTime(LocalDateTime.now())
                .build(), new ArrayList<>()));

        // when
        ResultActions perform = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

        // then
        perform.andExpect(status().isBadRequest());
    }

    @DisplayName("????????? ????????????.")
    @Test
    void list() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(get("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print());

        // then
        perform.andExpect(status().isOk());
    }

    @DisplayName("?????? ????????? ????????????.")
    @Test
    void changeOrderStatus() throws Exception {
        // given
        OrderStatusChangeRequest request = new OrderStatusChangeRequest(OrderStatus.MEAL.name());

        // when
        ResultActions perform = mockMvc.perform(put("/api/orders/1/order-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

        // then
        perform.andExpect(status().isOk());
    }
}
