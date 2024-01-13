package tacos.data;

import tacos.TacoOrder;

public interface OrderRepository {

    TacoOrder save(final TacoOrder order);

}