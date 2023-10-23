package kitchenpos.domain.tablegroup;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.support.domain.BaseRootEntity;
import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup extends BaseRootEntity<TableGroup> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private LocalDateTime createdDate;

    @OneToMany
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public TableGroup(LocalDateTime createdDate) {
        this(null, createdDate);
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public static TableGroup create() {
        return new TableGroup(LocalDateTime.now());
    }

    public void group(List<OrderTable> orderTables) {
        validate(orderTables);
        for (OrderTable orderTable : orderTables) {
            orderTable.group();
        }
        this.orderTables.addAll(orderTables);
    }

    private void validate(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("단체 지정하려는 테이블은 2개 이상이어야 합니다.");
        }
    }

    public void ungroup() {
        orderTables.clear();
        registerEvent(new TableGroupUngroupedEvent(id));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
