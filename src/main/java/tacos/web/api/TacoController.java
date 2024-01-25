package tacos.web.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tacos.Taco;

import java.util.List;

@RestController
@RequestMapping(path = "/api/tacos", produces = "application/json")
@CrossOrigin(origins = "http://tacocloud:8080")
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class TacoController {

    private final TacoService tacoService;

    @GetMapping(params = "recent")
    public List<Taco> recentTacos() {
        return tacoService.recentTacos();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Taco> tacoById(final @PathVariable("id") Long id) {
        return tacoService.getTacoById(id);
    }

    @PostMapping(consumes = "application/json", path = "new")
    @ResponseStatus(HttpStatus.CREATED)
    public Taco postTacos(final @RequestBody Taco userTaco) {
        return tacoService.saveTaco(userTaco);
    }

}
