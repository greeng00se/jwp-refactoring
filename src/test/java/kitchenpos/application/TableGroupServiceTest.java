package kitchenpos.application;

import static kitchenpos.fixture.OrderFixture.주문;
import static kitchenpos.fixture.OrderTableFixture.테이블;
import static kitchenpos.fixture.TableGroupFixture.단체_지정;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.test.ServiceTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService sut;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Nested
    class 단체_지정을_할_때 {

        @Test
        void 단체_지정하는_테이블이_2개_미만인_경우_예외를_던진다() {
            // given
            OrderTable orderTable = orderTableDao.save(테이블(true));
            TableGroup tableGroup = 단체_지정(List.of(orderTable));

            // expect
            assertThatThrownBy(() -> sut.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("단체 지정하려는 테이블은 2개 이상이어야 합니다.");
        }

        @Test
        void 올바른_테이블을_입력하지_않은_경우_예외를_던진다() {
            // given
            OrderTable orderTable = orderTableDao.save(테이블(true));
            OrderTable unsavedOrderTable = 테이블(true);
            TableGroup tableGroup = 단체_지정(List.of(orderTable, unsavedOrderTable));

            // expect
            assertThatThrownBy(() -> sut.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("올바른 테이블을 입력해주세요.");
        }

        @Test
        void 단체_지정하려는_테이블이_비어있지_않은_경우_예외를_던진다() {
            // given
            OrderTable orderTable1 = orderTableDao.save(테이블(true));
            OrderTable orderTable2 = orderTableDao.save(테이블(false));
            TableGroup tableGroup = 단체_지정(List.of(orderTable1, orderTable2));

            // expect
            assertThatThrownBy(() -> sut.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("비어있지 않거나, 이미 단체 지정이 된 테이블은 단체 지정을 할 수 없습니다.");
        }

        @Test
        void 이미_단체_지정이_되어있는_테이블을_단체_지정하려는_경우_예외를_던진다() {
            // given
            TableGroup tableGroup = tableGroupDao.save(단체_지정());
            OrderTable orderTable1 = orderTableDao.save(테이블(true, 0, tableGroup.getId()));
            OrderTable orderTable2 = orderTableDao.save(테이블(true, 0, tableGroup.getId()));
            OrderTable orderTable3 = orderTableDao.save(테이블(true));
            TableGroup newTableGroup = 단체_지정(List.of(orderTable2, orderTable3));

            // expect
            assertThatThrownBy(() -> sut.create(newTableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("비어있지 않거나, 이미 단체 지정이 된 테이블은 단체 지정을 할 수 없습니다.");
        }

        @Test
        void 단체_지정이_성공하는_경우_테이블의_상태가_주문_테이블로_변경된다() {
            // given
            OrderTable orderTable1 = orderTableDao.save(테이블(true));
            OrderTable orderTable2 = orderTableDao.save(테이블(true));
            TableGroup tableGroup = 단체_지정(List.of(orderTable1, orderTable2));

            // when
            TableGroup savedTableGroup = sut.create(tableGroup);

            // then
            List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(savedTableGroup.getId());
            assertThat(orderTables)
                    .extracting(OrderTable::isEmpty)
                    .containsExactly(false, false);
        }
    }

    @Nested
    class 단체_지정을_해제_할_때 {

        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        @ParameterizedTest(name = "테이블에 해당하는 주문 상태가 {0}인 경우 예외를 던진다")
        void 테이블에_해당하는_주문_상태가_조리중이거나_식사중인_경우_예외를_던진다(OrderStatus orderStatus) {
            // given
            TableGroup tableGroup = tableGroupDao.save(단체_지정());
            OrderTable orderTable1 = orderTableDao.save(테이블(true, 0, tableGroup.getId()));
            OrderTable orderTable2 = orderTableDao.save(테이블(true, 0, tableGroup.getId()));
            orderDao.save(주문(orderTable1.getId(), orderStatus));

            // expect
            assertThatThrownBy(() -> sut.ungroup(tableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테이블의 주문 상태가 조리중이거나 식사중인 경우 단체 지정 해제를 할 수 없습니다.");
        }

        @Test
        void 단체_지정_해제를_성공하는_경우_테이블의_단체_지정_번호가_제거된다() {
            // given
            TableGroup tableGroup = tableGroupDao.save(단체_지정());
            orderTableDao.save(테이블(true, 0, tableGroup.getId()));
            orderTableDao.save(테이블(true, 0, tableGroup.getId()));

            // when
            sut.ungroup(tableGroup.getId());

            // then
            assertThat(orderTableDao.findAllByTableGroupId(tableGroup.getId())).isEmpty();
        }
    }
}
