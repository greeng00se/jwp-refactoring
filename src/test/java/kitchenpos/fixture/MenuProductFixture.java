package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductFixture {

    public static MenuProduct 메뉴_상품(Product product, Long quantity) {
        return new MenuProduct(product, quantity);
    }
}
