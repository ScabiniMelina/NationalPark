package model.utils;

import model.util.UnionFind;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class UnionFindTest {
    private UnionFind<String> uf;

    @BeforeEach
    void setUp(){
        uf = new UnionFind<>();
    }

    @Test
    void testShouldReturnSelfAsRootWhenOnlyOneElementAdded() {
        // Given
        uf.add("A");

        // When
        String root = uf.find("A");

        // Then
        assertEquals("A", root);
    }

    @Test
    void testShouldThrowExceptionWhenFindingUnregisteredElement() {
        // Given
        String item = "X";

        // When
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> uf.find(item));

        // Then
        assertEquals("Elemento no registrado: X", ex.getMessage());
    }

    @Test
    void testShouldUnionTwoElements() {
        // Given
        uf.add("A");
        uf.add("B");

        // When
        uf.union("A", "B");

        // Then
        assertEquals(uf.find("A"), uf.find("B"), "A y B deben tener la misma raíz");
    }

    @Test
    void testShouldDetectIfElementsAreConnected() {
        // Given
        uf.add("A");
        uf.add("B");
        uf.add("C");

        // When
        uf.union("A", "B");

        // Then
        assertTrue(uf.connected("A", "B"), "A y B deben estar conectados");
        assertFalse(uf.connected("A", "C"), "A y C no deben estar conectados");
    }

}
