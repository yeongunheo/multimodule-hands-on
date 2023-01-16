package kitchenpos.product.service;

import static kitchenpos.product.fixture.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import kitchenpos.product.controller.dto.request.ProductCreateRequest;
import kitchenpos.product.controller.dto.response.ProductCreateResponse;
import kitchenpos.product.controller.dto.response.ProductFindAllResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;

//@SpringBootTest
//@Transactional
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @DisplayName("상품을 생성한다.")
    @Test
    void create_success() {
        // given
        ProductCreateRequest request = new ProductCreateRequest("후라이드", BigDecimal.valueOf(10_000L));

        // when
        when(productRepository.save(any())).thenReturn(Product.builder()
                .id(1L)
                .name("후라이드")
                .price(BigDecimal.valueOf(10_000L))
                .build());
        var response = productService.create(request);

        // then
        verify(productRepository, times(1)).save(any());
        assertThat(response.getName()).isEqualTo(request.getName());
    }

    @DisplayName("상품을 생성할 때 가격이 음수이면 예외를 반환한다.")
    @Test
    void create_fail_if_price_is_negative() {
        // given
        ProductCreateRequest request = new ProductCreateRequest("후라이드", BigDecimal.valueOf(-1L));

        // when, then
        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품을 조회한다.")
    @Test
    void list_success() {
        // given
        var productName = "강정치킨";

        // when
        when(productRepository.findAll()).thenReturn(Arrays.asList(Product.builder()
                .id(1L)
                .name("강정치킨")
                .price(BigDecimal.valueOf(17_000L))
                .build()));
        List<ProductFindAllResponse> responses = productService.list();

        // then
        List<String> productNames = responses.stream()
                .map(ProductFindAllResponse::getName)
                .collect(Collectors.toList());
        assertThat(productNames).contains(productName);
    }
}
