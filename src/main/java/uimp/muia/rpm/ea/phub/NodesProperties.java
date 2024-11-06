package uimp.muia.rpm.ea.phub;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public record NodesProperties(
        Type type,
        Double[] values
) {

    private static final String FCOST = "Fcost";
    private static final String CAP = "Cap";

    public double get(int index) {
        return values[index];
    }

    public static NodesProperties costs(Type type, int size) throws URISyntaxException, IOException {
        return load(FCOST, type, size);
    }

    public static NodesProperties capacities(Type type, int size) throws URISyntaxException, IOException {
        return load(CAP, type, size);
    }

    static NodesProperties load(String template, Type type, int size) throws URISyntaxException, IOException {
        var name = "or-library/phub1/%s%s.%d".formatted(template, type.code(), size);
        var uri = Objects.requireNonNull(NodesProperties.class.getClassLoader().getResource(name)).toURI();
        var costs = parseFile(Path.of(uri));
        assert costs.length == size;
        return new NodesProperties(type, costs);
    }

    static Double[] parseFile(Path path) throws IOException {
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
