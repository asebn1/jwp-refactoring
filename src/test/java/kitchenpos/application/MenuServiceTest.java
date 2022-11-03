package kitchenpos.application;

import kitchenpos.menu.application.dto.request.MenuCreateRequest;
import kitchenpos.menu.application.dto.response.MenuGroupResponse;
import kitchenpos.menu.application.dto.response.MenuResponse;
import kitchenpos.product.application.dto.response.ProductResponse;
import kitchenpos.fixture.ProductFixtures;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.product.application.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixture.MenuFixtures.createMenu;
import static kitchenpos.fixture.MenuFixtures.후라이드치킨;
import static kitchenpos.fixture.MenuGroupFixtures.한마리메뉴;
import static kitchenpos.fixture.MenuProductFixtures.createMenuProduct;
import static kitchenpos.fixture.ProductFixtures.후라이드;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Sql("/truncate.sql")
class MenuServiceTest {

    private final MenuService menuService;
    private final ProductService productService;
    private final MenuGroupService menuGroupService;

    @Autowired
    public MenuServiceTest(final MenuService menuService, final ProductService productService,
                           final MenuGroupService menuGroupService) {
        this.menuService = menuService;
        this.productService = productService;
        this.menuGroupService = menuGroupService;
    }

    @DisplayName("menu를 생성한다.")
    @Test
    void createMenuSuccess() {
        ProductResponse 후라이드 = productService.create(후라이드());
        MenuGroupResponse 한마리메뉴 = menuGroupService.create(한마리메뉴());
        MenuCreateRequest 메뉴_후라이드치킨 = 후라이드치킨(한마리메뉴.getId(),
                List.of(createMenuProduct(후라이드.getId(), 1)));

        MenuResponse actual = menuService.create(메뉴_후라이드치킨);

        assertAll(
                () -> assertThat(actual.getName()).isEqualTo(메뉴_후라이드치킨.getName()),
                () -> assertThat(actual.getPrice()).isEqualByComparingTo(메뉴_후라이드치킨.getPrice()),
                () -> assertThat(actual.getMenuGroupId()).isEqualTo(한마리메뉴.getId()),
                () -> assertThat(actual.getMenuProducts()).hasSize(1)
        );
    }

    @DisplayName("price가 null인 경우 예외를 던진다.")
    @Test
    void createMenuByPriceNull() {
        ProductResponse 후라이드 = productService.create(후라이드());
        MenuGroupResponse 한마리메뉴 = menuGroupService.create(한마리메뉴());
        MenuCreateRequest 메뉴_후라이드치킨 = createMenu("후라이드치킨", null, 한마리메뉴.getId(),
                List.of(createMenuProduct(후라이드.getId(), 1)));

        assertThatThrownBy(() -> menuService.create(메뉴_후라이드치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("price가 0미만인 경우 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -1})
    void createMenuByPriceNegative(final int price) {
        ProductResponse 후라이드 = productService.create(후라이드());
        MenuGroupResponse 한마리메뉴 = menuGroupService.create(한마리메뉴());
        MenuCreateRequest 메뉴_후라이드치킨 = createMenu("후라이드치킨", BigDecimal.valueOf(price), 한마리메뉴.getId(),
                List.of(createMenuProduct(후라이드.getId(), 1)));

        assertThatThrownBy(() -> menuService.create(메뉴_후라이드치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 menuGroupId인 경우 예외를 던진다.")
    @Test
    void createMenuByNonExistMenuGroupId() {
        ProductResponse 후라이드 = productService.create(후라이드());
        MenuCreateRequest 없는그룹_후라이드치킨 = 후라이드치킨(0L,
                List.of(createMenuProduct(후라이드.getId(), 1)));

        assertThatThrownBy(() -> menuService.create(없는그룹_후라이드치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("product의 price 합이 menu price보다 큰 경우 예외를 던진다.")
    @Test
    void createMenuByOverProductPrice() {
        ProductResponse 후라이드 = productService.create(후라이드());
        MenuGroupResponse 한마리메뉴 = menuGroupService.create(한마리메뉴());
        MenuCreateRequest 메뉴_후라이드치킨 = createMenu("후라이드치킨", BigDecimal.valueOf(20000), 한마리메뉴.getId(),
                List.of(createMenuProduct(후라이드.getId(), 1)));

        assertThatThrownBy(() -> menuService.create(메뉴_후라이드치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("menu 전체 조회한다.")
    @Test
    void findAllMenu() {
        ProductResponse 후라이드 = productService.create(후라이드());
        ProductResponse 양념치킨 = productService.create(ProductFixtures.양념치킨());
        MenuGroupResponse 한마리메뉴 = menuGroupService.create(한마리메뉴());
        // 메뉴
        MenuCreateRequest 메뉴_후라이드치킨 = createMenu("후라이드치킨", BigDecimal.valueOf(16000), 한마리메뉴.getId(),
                List.of(createMenuProduct(후라이드.getId(), 1)));
        MenuCreateRequest 메뉴_양념치킨 = createMenu("양념치킨", BigDecimal.valueOf(16000), 한마리메뉴.getId(),
                List.of(createMenuProduct(양념치킨.getId(), 1)));

        menuService.create(메뉴_후라이드치킨);
        menuService.create(메뉴_양념치킨);
        List<MenuResponse> menus = menuService.list();

        assertThat(menus).hasSize(2);
    }
}
