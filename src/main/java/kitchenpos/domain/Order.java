package kitchenpos.domain;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.domain.OrderStatus.COMPLETION;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.common.BaseEntity;

@Table(name = "orders")
@Entity
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;

    @OneToMany(fetch = FetchType.EAGER, cascade = {PERSIST})
    @JoinColumn(name = "order_id", nullable = false, updatable = false)
    private List<OrderLineItem> orderLineItems;

    public Order(Long orderTableId) {
        this(orderTableId, OrderStatus.COOKING, LocalDateTime.now());
    }

    public Order(Long orderTableId, List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    public Order(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this(null, orderTableId, orderStatus, orderedTime, new ArrayList<>());
    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
                 List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    protected Order() {
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus == COMPLETION) {
            throw new IllegalArgumentException("완료된 주문의 상태는 변경할 수 없습니다.");
        }
        this.orderStatus = orderStatus;
    }

    public void changeOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
