package kitchenpos.menu.controller;

import java.net.URI;
import java.util.List;
import kitchenpos.menu.service.MenuGroupService;
import kitchenpos.menu.controller.dto.response.MenuGroupCreateResponse;
import kitchenpos.menu.controller.dto.response.MenuGroupFindAllResponse;
import kitchenpos.menu.controller.dto.request.MenuGroupCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuGroupRestController {

    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupCreateResponse> create(@RequestBody final MenuGroupCreateRequest request) {
        final MenuGroupCreateResponse response = menuGroupService.create(request);
        final URI uri = URI.create("/api/menu-groups/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response);
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupFindAllResponse>> list() {
        final List<MenuGroupFindAllResponse> responses = menuGroupService.list();
        return ResponseEntity.ok()
                .body(responses);
    }
}
