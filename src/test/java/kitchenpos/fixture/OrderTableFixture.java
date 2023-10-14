package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class OrderTableFixture {

    public static OrderTable 테이블(boolean empty) {
        return new OrderTable(null, 0, empty);
    }

    public static OrderTable 테이블(boolean empty, int numberOfGuests) {
        return new OrderTable(null, numberOfGuests, empty);
    }

    public static OrderTable 테이블(boolean empty, int numberOfGuests, TableGroup tableGroup) {
        return new OrderTable(tableGroup, numberOfGuests, empty);
    }
}
