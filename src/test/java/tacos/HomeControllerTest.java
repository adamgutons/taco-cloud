package tacos;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import tacos.data.IngredientRepository;
import tacos.data.OrderRepository;
import tacos.data.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("unused")
class HomeControllerTest {
    private final MockMvc mockMvc;

    @MockBean
    private IngredientRepository ingredientRepository;

    @MockBean
    private OrderRepository jdbcOrderRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testHomePage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"));
    }

}