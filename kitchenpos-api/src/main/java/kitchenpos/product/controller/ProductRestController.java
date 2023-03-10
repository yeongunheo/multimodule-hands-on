package kitchenpos.product.controller;

import java.net.URI;
import java.util.List;

import kitchenpos.product.controller.dto.request.ProductCreateRequest;
import kitchenpos.product.controller.dto.response.ProductCreateResponse;
import kitchenpos.product.controller.dto.response.ProductFindAllResponse;
import kitchenpos.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/products")
    public ResponseEntity<ProductCreateResponse> create(@RequestBody final ProductCreateRequest request) {
        final ProductCreateResponse response = productService.create(request);
        final URI uri = URI.create("/api/products/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response);
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductFindAllResponse>> list() {
        final List<ProductFindAllResponse> responses = productService.list();
        return ResponseEntity.ok()
                .body(responses);
    }
}
