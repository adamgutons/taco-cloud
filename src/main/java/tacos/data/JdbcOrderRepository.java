package tacos.data;

import lombok.AllArgsConstructor;
import org.springframework.asm.Type;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tacos.IngredientRef;
import tacos.Taco;
import tacos.TacoOrder;

import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;

@Repository
@AllArgsConstructor
public class JdbcOrderRepository implements OrderRepository {

    private JdbcOperations jdbcOperations;

    @Override
    @Transactional
    public TacoOrder save(final TacoOrder tacoOrder) {

        tacoOrder.setPlacedAt(new Date());
        final PreparedStatementCreatorFactory tacoOrderStatementCreatorFactory = new PreparedStatementCreatorFactory(
                "insert into Taco_Order "
                        + "(delivery_name, delivery_street, delivery_city, "
                        + "delivery_state, delivery_zip, cc_number, "
                        + "cc_expiration, cc_cvv, placed_at) "
                        + "values (?,?,?,?,?,?,?,?,?)",
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP
        );
        tacoOrderStatementCreatorFactory.setReturnGeneratedKeys(true);

        final PreparedStatementCreator preparedStatementCreator =
                tacoOrderStatementCreatorFactory.newPreparedStatementCreator(
                        List.of(tacoOrder.getDeliveryName(),
                                tacoOrder.getDeliveryStreet(),
                                tacoOrder.getDeliveryCity(),
                                tacoOrder.getDeliveryState(),
                                tacoOrder.getDeliveryZip(),
                                tacoOrder.getCcNumber(),
                                tacoOrder.getCcExpiration(),
                                tacoOrder.getCcCVV(),
                                tacoOrder.getPlacedAt()));

        final GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(preparedStatementCreator, generatedKeyHolder);
        final Long orderId = requireNonNull(generatedKeyHolder.getKey()).longValue();
        tacoOrder.setId(orderId);

        final List<Taco> tacos = tacoOrder.getTacos();
        IntStream.range(0, tacos.size())
                .forEach(key -> saveTaco(orderId, key, tacos.get(key)));

        return tacoOrder;
    }

    private Long saveTaco(final Long orderId, final int orderKey, final Taco taco) {
        taco.setCreatedAt(new Date());

        final PreparedStatementCreatorFactory tacoStatementCreatorFactory = new PreparedStatementCreatorFactory(
                "insert into Taco "
                        + "(name, created_at, taco_order, taco_order_key) "
                        + "values (?, ?, ?, ?)",
                Types.VARCHAR, Types.TIMESTAMP, Type.LONG, Type.LONG
        );
        tacoStatementCreatorFactory.setReturnGeneratedKeys(true);

        final PreparedStatementCreator preparedStatementCreator =
                tacoStatementCreatorFactory.newPreparedStatementCreator(
                        List.of(
                                taco.getName(),
                                taco.getCreatedAt(),
                                orderId,
                                orderKey));
        final GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(preparedStatementCreator, generatedKeyHolder);
        final Long tacoId = requireNonNull(generatedKeyHolder.getKey()).longValue();
        taco.setId(tacoId);

        saveIngredientRefs(tacoId, taco.getIngredients());

        return tacoId;
    }

    private void saveIngredientRefs(final Long tacoId, final List<IngredientRef> ingredientRefs) {
        IntStream.range(0, ingredientRefs.size())
                .forEach(key -> jdbcOperations.update(
                        "insert into Ingredient_Ref (ingredient, taco, taco_key) values (?, ?, ?)",
                        ingredientRefs.get(key).getIngredient(), tacoId, key));
    }
}
