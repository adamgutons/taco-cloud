package tacos.web.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tacos.Taco;
import tacos.data.TacoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TacoService {

    private final TacoRepository tacoRepository;

    public List<Taco> recentTacos() {
        PageRequest pageRequest = PageRequest
                .of(0, 12, Sort.by("createdAt").descending());
        return tacoRepository.findAll(pageRequest).getContent();
    }

    public Taco saveTaco(final Taco userTaco) {
        tacoRepository.save(userTaco);
        return userTaco;
    }
}