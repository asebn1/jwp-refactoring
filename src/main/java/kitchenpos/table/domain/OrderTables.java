package kitchenpos.table.domain;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderTables {

    private final List<OrderTable> orderTables;

    public OrderTables(final List<OrderTable> orderTables) {
        validateSize(orderTables);
        this.orderTables = orderTables;
    }

    public static OrderTables from(List<OrderTable> orderTables, OrderTables checkOrderTables) {
        validateTableGroup(orderTables, checkOrderTables);
        return new OrderTables(orderTables);
    }

    private static void validateTableGroup(final List<OrderTable> orderTables, final OrderTables savedOrderTables) {
        validateOrderTablesSize(orderTables, savedOrderTables);
        validatePossibleGrouping(savedOrderTables);
    }

    private static void validateOrderTablesSize(List<OrderTable> orderTables, OrderTables savedOrderTables) {
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    private static void validatePossibleGrouping(OrderTables savedOrderTables) {
        if (!savedOrderTables.canGrouping()) {
            throw new IllegalArgumentException();
        }
    }

    public int size() {
        return orderTables.size();
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public List<Long> getIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public boolean canGrouping() {
        for (final OrderTable orderTable : orderTables) {
            validatePossibleGrouping(orderTable);
        }

        return true;
    }

    private void validatePossibleGrouping(final OrderTable orderTable) {
        if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    private void validateSize(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }
}
