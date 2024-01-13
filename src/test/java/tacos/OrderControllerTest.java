package tacos;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import tacos.data.IngredientRepository;
import tacos.data.JdbcIngredientRepository;
import tacos.data.JdbcOrderRepository;

import static net.andreinc.mockneat.unit.financial.CreditCards.creditCards;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("unused")
class OrderControllerTest {

    private final String postData = String.format("deliveryName=name&deliveryStreet=street&deliveryCity=city&deliveryState=state&deliveryZip=12345&ccNumber=%s&ccExpiration=01%%2F23&ccCVV=123", creditCards().get());
    private final MockMvc mockMvc;

    @MockBean
    private JdbcIngredientRepository ingredientRepository;

    @MockBean
    private JdbcOrderRepository jdbcOrderRepository;

    @Test
    void testProcessOrder() throws Exception {
        mockMvc.perform(post("/orders")
                        .content(postData)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().stringValues("Location", "/"));
    }

}