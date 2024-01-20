package tacos.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Taco;
import tacos.TacoOrder;
import tacos.data.IngredientRepository;

import java.util.Arrays;
import java.util.stream.StreamSupport;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
public class DesignTacoController {

    private final IngredientRepository ingredientRepository;

    @ModelAttribute
    @SuppressWarnings("unused")
    public void addIngredientsToModel(final Model model) {
        final Iterable<Ingredient> ingredients = ingredientRepository.findAll();
        final Type[] types = Ingredient.Type.values();
        Arrays.stream(types).forEach(type ->
                model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type)));
    }

    @ModelAttribute(name = "tacoOrder")
    @SuppressWarnings("unused")
    public TacoOrder order() {
        return new TacoOrder();
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    @GetMapping
    @SuppressWarnings("unused")
    public String showDesignForm() {
        return "design";
    }

    @PostMapping
    @SuppressWarnings("unused")
    public String processTaco(final @Valid Taco taco,
                              final Errors errors,
                              final @ModelAttribute TacoOrder tacoOrder) {

        if (errors.hasErrors()) {
            return "design";
        }

        tacoOrder.addTaco(taco);
        log.info("Processing taco: {}", taco);

        return "redirect:/orders/current";
    }

    private Iterable<Ingredient> filterByType(final Iterable<Ingredient> ingredients, final Type type) {
        return StreamSupport.stream(ingredients.spliterator(), false)
                .filter(i -> i.getType().equals(type))
                .toList();
    }

}
