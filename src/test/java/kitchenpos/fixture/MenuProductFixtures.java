package kitchenpos.fixture;

import kitchenpos.menu.application.dto.request.MenuProductCreateRequest;

public class MenuProductFixtures {

    public static MenuProductCreateRequest createMenuProduct(final Long productId, final long quantity) {
        return new MenuProductCreateRequest(productId, quantity);
    }
}
