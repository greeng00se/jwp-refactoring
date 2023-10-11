package kitchenpos.application;

import static java.lang.Long.MAX_VALUE;
import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.fixture.OrderFixture.주문;
import static kitchenpos.fixture.OrderTableFixture.테이블;
import static kitchenpos.fixture.TableGroupFixture.단체_지정;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
class TableServiceTest {

    @Autowired
    private TableService sut;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Test
    void 테이블을_생성한다() {
        // given
        OrderTable orderTable = 테이블(true);

        // when
        OrderTable savedOrderTable = sut.create(orderTable);

        // then
        assertThat(orderTableDao.findById(savedOrderTable.getId())).isPresent();
    }

    @Nested
    class 테이블의_상태를_변경할때 {

        @Test
        void 존재하지_않는_테이블인_경우_예외를_던진다() {
            // expect
            assertThatThrownBy(() -> sut.changeEmpty(MAX_VALUE, 테이블(true)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 테이블입니다.");
        }

        @Test
        void 단체_지정이_되어있는_테이블인_경우_예외를_던진다() {
            // given
            TableGroup tableGroup = tableGroupDao.save(단체_지정());
            OrderTable orderTable = orderTableDao.save(테이블(false, 0, tableGroup.getId()));

            // expect
            assertThatThrownBy(() -> sut.changeEmpty(orderTable.getId(), orderTable))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("단체 지정이 되어있는 경우 테이블의 상태를 변경할 수 없습니다.");
        }

        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        @ParameterizedTest(name = "주문 상태가 {0}인 경우 예외를 던진다")
        void 테이블의_주문_상태가_조리중이거나_식사중인_경우_예외를_던진다(OrderStatus orderStatus) {
            // given
            OrderTable orderTable = orderTableDao.save(테이블(false));
            orderDao.save(주문(orderTable.getId(), orderStatus));

            // expect
            assertThatThrownBy(() -> sut.changeEmpty(orderTable.getId(), 테이블(true)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테이블의 주문 상태가 조리중이거나 식사중인 경우 테이블의 상태를 변경할 수 없습니다.");
        }

        @Test
        void 테이블의_주문_상태가_완료인_경우_테이블의_상태를_변경한다() {
            // given
            OrderTable orderTable = orderTableDao.save(테이블(false));
            orderDao.save(주문(orderTable.getId(), COMPLETION));

            // when
            OrderTable changedOrderTable = sut.changeEmpty(orderTable.getId(), 테이블(true));

            // then
            assertThat(changedOrderTable.isEmpty()).isTrue();
        }
    }

    @Nested
    class 테이블의_손님수를_지정할_때 {

        @Test
        void 손님수가_0명보다_적으면_예외를_던진다() {
            // given
            OrderTable orderTable = orderTableDao.save(테이블(false));

            // expect
            assertThatThrownBy(() -> sut.changeNumberOfGuests(orderTable.getId(), 테이블(false, -1)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("방문한 손님 수는 음수가 될 수 없습니다.");
        }

        @Test
        void 변경하려는_테이블이_없는_경우_예외를_던진다() {
            // expect
            assertThatThrownBy(() -> sut.changeNumberOfGuests(MAX_VALUE, 테이블(false, 0)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블을 찾을 수 없습니다.");
        }

        @Test
        void 빈_테이블_경우_예외를_던진다() {
            // given
            OrderTable orderTable = orderTableDao.save(테이블(true));

            // expect
            assertThatThrownBy(() -> sut.changeNumberOfGuests(orderTable.getId(), 테이블(false, 0)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("빈 테이블에는 손님을 지정할 수 없습니다.");
        }

        @Test
        void 빈_테이블이_아닌_경우_인원이_정상적으로_변경된다() {
            // given
            OrderTable orderTable = orderTableDao.save(테이블(false, 1));

            // when
            OrderTable changedOrderTable = sut.changeNumberOfGuests(orderTable.getId(), 테이블(false, 0));

            // then
            assertThat(changedOrderTable.getNumberOfGuests()).isZero();
        }
    }
}
