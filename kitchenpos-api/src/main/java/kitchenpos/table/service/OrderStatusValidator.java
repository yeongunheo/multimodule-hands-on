package kitchenpos.table.service;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public interface OrderStatusValidator {

    boolean existsByOrderTableIdInAndOrderStatusNotCompletion(List<Long> orderTableIds);

    boolean existsByOrderTableIdAndOrderStatusNotCompletion(Long orderTableId);
}
