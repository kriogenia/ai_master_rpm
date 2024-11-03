package uimp.muia.rpm.phub;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FCostTest {

    @Test
    void of() throws Exception {
        var fcost = FCost.of(FCost.Type.LOOSE, 10);
        assertEquals(FCost.Type.LOOSE, fcost.type());
        assertEquals(10, fcost.costs().length);
    }
}