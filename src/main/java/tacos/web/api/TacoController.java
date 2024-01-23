package tacos.web.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tacos.Taco;
import tacos.data.TacoRepository;

import java.util.List;

@RestController
@RequestMapping(path = "/api/tacos", produces = "application/json")
@CrossOrigin(origins = "http://tacocloud:8080")
@RequiredArgsConstructor
public class TacoController {

    private final TacoRepository tacoRepository;

    @GetMapping(params = "recent")
    public List<Taco> recentTacos() {
        PageRequest pageRequest = PageRequest
                .of(0, 12, Sort.by("createdAt").descending());
        return tacoRepository.findAll(pageRequest).getContent();
    }
}
