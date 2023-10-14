package kitchenpos.dto;

import java.util.List;

public class TableGroupRequest {

    private List<OrderTableIdRequest> orderTables;

    public TableGroupRequest(List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }

    public static class OrderTableIdRequest {
        private Long id;

        public OrderTableIdRequest(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
