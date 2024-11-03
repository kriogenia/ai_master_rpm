package uimp.muia.rpm.phub;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public record FCost(
        Type type,
        Double[] costs
) {

    public static FCost of(Type type, int size) throws URISyntaxException, IOException {
        var name = "or-library/phub1/Fcost%s.%d".formatted(type.code(), size);
        System.out.println(name);
        var uri = Objects.requireNonNull(FCost.class.getClassLoader().getResource(name)).toURI();
        var costs = parseCosts(Path.of(uri));
        assert costs.length == size;
        return new FCost(type, costs);
    }

    static Double[] parseCosts(Path path) throws IOException {
        return Files.readAllLines(path).stream()
                .filter(l -> !l.isEmpty())
                .map(Double::parseDouble)
                .toArray(Double[]::new);
    }

    public enum Type {
        LOOSE,
        TIGHT;

        String code() {
            return switch (this) {
                case LOOSE -> "L";
                case TIGHT -> "T";
            };
        }
    }

}
