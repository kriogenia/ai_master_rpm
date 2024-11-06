package uimp.muia.rpm.ea.phub;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodesPropertiesTest {

    public static final double DELTA = 0.001;

    @Test
    void costs() throws Exception {
        var fcost = NodesProperties.costs(NodesProperties.Type.LOOSE, 10);
        assertEquals(NodesProperties.Type.LOOSE, fcost.type());
        assertEquals(10, fcost.values().length);
        assertEquals(28766.736921, fcost.values()[0], DELTA);
    }

    @Test
    void capacities() throws Exception {
        var caps = NodesProperties.capacities(NodesProperties.Type.LOOSE, 20);
        assertEquals(NodesProperties.Type.LOOSE, caps.type());
        assertEquals(20, caps.values().length);
        assertEquals(1869.755130, caps.values()[0], DELTA);
    }

}