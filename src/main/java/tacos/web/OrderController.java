package tacos.web;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import lombok.extern.slf4j.Slf4j;
import tacos.TacoOrder;
import tacos.User;
import tacos.data.OrderRepository;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
public class OrderController {

    private static final String VIEW_NAME = "orderForm";
    private OrderRepository orderRepository;

    @GetMapping("/current")
    @SuppressWarnings("unused")
    public String orderForm() {
        return VIEW_NAME;
    }

    @PostMapping
    @SuppressWarnings("unused")
    public String processOrder(final @Valid TacoOrder order,
                               final Errors errors,
                               final SessionStatus sessionStatus,
                               final @AuthenticationPrincipal User user) {

        if (errors.hasErrors()) {
            return VIEW_NAME;
        }

        order.setUser(user);

        orderRepository.save(order);
        sessionStatus.setComplete();
        log.info("Order submitted: {}", order);

        return "redirect:/";
    }
}