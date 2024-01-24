package tacos;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import tacos.data.IngredientRepository;
import tacos.data.OrderRepository;
import tacos.data.TacoRepository;
import tacos.data.UserRepository;
import tacos.web.api.TacoController;
import tacos.web.api.TacoService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TacoController.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("unused")
class TacoControllerTest {

    private final MockMvc mockMvc;
    private final Taco taco1 = Taco.builder().build();
    private final Taco taco2 = Taco.builder().build();

    @MockBean
    private IngredientRepository ingredientRepository;

    @MockBean
    private OrderRepository jdbcOrderRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private TacoService tacoService;

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void recentTacos() throws Exception {

        final List<Taco> tacos = Arrays.asList(taco1, taco2);

        when(tacoService.recentTacos()).thenReturn(tacos);

        mockMvc.perform(get("/api/tacos?recent"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
