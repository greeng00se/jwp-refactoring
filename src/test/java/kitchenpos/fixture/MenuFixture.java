package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.support.money.Money;

public class MenuFixture {

    public static Menu 메뉴(String name, Long price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(null, name, Money.valueOf(price), menuGroupId, menuProducts);
    }

    public static MenuRequest 메뉴_생성_요청(String name, Long price, Long menuGroupId, List<MenuProduct> menuProducts) {
        List<MenuProductRequest> menuProductRequests = menuProducts.stream()
                .map(menuProduct -> new MenuProductRequest(menuProduct.getProduct().getId(), menuProduct.getQuantity()))
                .collect(Collectors.toList());
        return new MenuRequest(null, name, BigDecimal.valueOf(price), menuGroupId, menuProductRequests);
    }
}
