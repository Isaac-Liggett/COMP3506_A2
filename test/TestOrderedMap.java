/**
 * Supplied by the COMP3506/7505 teaching team, Semester 2, 2025.
 */

import uq.comp3506.a2.structures.OrderedMap;

public class TestOrderedMap {

    public static void main(String[] args) {
        System.out.println("Testing the OrderedMap (AVL Tree) Class...");

        // 1. Test basic insertions and rebalancing
        OrderedMap<Integer, String> map = new OrderedMap<>();

        // --- Basic insertion and retrieval ---
        assert map.isEmpty() : "Map should start empty";
        assert map.size() == 0 : "Size should be 0 initially";

        assert map.put(5, "five") == null;
        assert map.put(3, "three") == null;
        assert map.put(7, "seven") == null;
        assert map.size() == 3 : "After three inserts, size should be 3";

        assert map.get(5).equals("five");
        assert map.get(3).equals("three");
        assert map.get(7).equals("seven");
        assert map.get(1) == null : "Nonexistent key should return null";

        // --- Replacement ---
        String oldVal = map.put(3, "THREE");
        assert oldVal.equals("three") : "put() should return old value on update";
        assert map.get(3).equals("THREE") : "Updated value not stored correctly";
        assert map.size() == 3 : "Size should not increase after replacement";

        // --- Deletion ---
        String removed = map.remove(3);
        assert removed.equals("THREE") : "remove() should return the removed value";
        assert map.get(3) == null : "Removed key should not exist anymore";
        assert map.size() == 2 : "Size should decrease after deletion";

        assert map.remove(3) == null : "Removing non-existent key should return null";
        assert map.size() == 2;

        // --- Clear ---
        map.clear();
        assert map.isEmpty() : "Map should be empty after clear()";
        assert map.size() == 0 : "Size should be zero after clear()";

        // --- Re-insertion to test balancing ---
        map.put(10, "ten");
        map.put(20, "twenty");
        map.put(30, "thirty");  // should trigger left rotation
        map.put(25, "twenty-five");
        map.put(5, "five");
        map.put(15, "fifteen"); // likely to trigger balancing on both sides
        assert map.size() == 6 : "Should have 6 nodes after reinsertions";

        // --- Structural consistency ---
        assert map.get(10).equals("ten");
        assert map.get(25).equals("twenty-five");
        assert map.get(15).equals("fifteen");
        assert map.get(100) == null;

        // --- Edge cases ---
        assert map.remove(999) == null : "Removing unknown key should not fail";
        assert map.get(999) == null;
        assert map.put(25, "updated") != null : "Should replace existing value";
        assert map.get(25).equals("updated");

        // --- Optional (for future methods) ---
        // Once you implement nextGeq / nextLeq / keysInRange, uncomment and test:
        /*
        assert map.nextGeq(17).equals("updated") : "nextGeq(17) should be 25 -> 'updated'";
        assert map.nextLeq(17).equals("fifteen") : "nextLeq(17) should be 15 -> 'fifteen'";
        List<Integer> keys = map.keysInRange(10, 25);
        assert keys.equals(List.of(10, 15, 20, 25)) : "keysInRange(10,25) incorrect: " + keys;
        */

        System.out.println("✅ All OrderedMap tests passed!");
    }
}
