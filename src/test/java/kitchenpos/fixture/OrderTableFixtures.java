package kitchenpos.fixture;

import kitchenpos.table.application.dto.request.OrderTableCreateRequest;
import kitchenpos.table.application.dto.request.OrderTableUpdateEmptyRequest;
import kitchenpos.table.application.dto.request.OrderTableUpdateNumberOfGuestsRequest;

public class OrderTableFixtures {

    public static OrderTableCreateRequest createOrderTable(final int numberOfGuests, final boolean empty) {
        return new OrderTableCreateRequest(numberOfGuests, empty);
    }

    public static OrderTableUpdateEmptyRequest createOrderTableUpdateEmptyRequest(final boolean empty) {
        return new OrderTableUpdateEmptyRequest(empty);
    }

    public static OrderTableUpdateNumberOfGuestsRequest createOrderTableUpdateNumberOfGuestsRequest(
            final int numberOfGuests) {
        return new OrderTableUpdateNumberOfGuestsRequest(numberOfGuests);
    }

    public static OrderTableCreateRequest 테이블_1번() {
        return createOrderTable(0, true);
    }

    public static OrderTableCreateRequest 테이블_2번() {
        return createOrderTable(0, true);
    }

    public static OrderTableCreateRequest 테이블_3번() {
        return createOrderTable(0, true);
    }
}
