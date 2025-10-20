/**
 * Supplied by the COMP3506/7505 teaching team, Semester 2, 2025.
 */

import uq.comp3506.a2.structures.UnorderedMap;

public class TestUnorderedMap {

    public static void main(String[] args) {
        System.out.println("Testing the UnorderedMap Class...");

        UnorderedMap<Integer, String> map = new UnorderedMap<>();

        // --- Test 1: Initial state ---
        assert map.isEmpty() : "Map should be empty initially";
        assert map.size() == 0 : "Size should be 0 initially";

        // --- Test 2: Simple insert ---
        assert map.put(1, "one") == "one" : "First insert should return the value inserted";
        assert map.size() == 1 : "Size should be 1 after first insert";
        assert !map.isEmpty() : "Map should not be empty after insert";

        // --- Test 3: Retrieve existing key ---
        assert map.get(1).equals("one") : "get(1) should return 'one'";

        // --- Test 4: Update existing key ---
        assert map.put(1, "uno").equals("uno") : "put on existing key should return new value";
        assert map.get(1).equals("uno") : "Updated value should be 'uno'";
        assert map.size() == 1 : "Size should not increase on update";

        // --- Test 5: Add multiple keys ---
        map.put(2, "two");
        map.put(3, "three");
        map.put(4, "four");
        map.put(5, "five");
        assert map.size() == 5 : "Size should be 5 after adding multiple keys";

        // --- Test 6: Test collisions / linear probing ---
        // Force collision if your capacity is 16: keys with same hashCode % 16
        int capacity = 16;
        for (int i = 16; i < 20; i++) {
            map.put(i, "val" + i);
        }
        assert map.size() == 9 : "Size should correctly reflect all inserts with collisions";
        for (int i = 16; i < 20; i++) {
            assert map.get(i).equals("val" + i) : "Value for key " + i + " should be correct after collision";
        }

        // --- Test 7: Remove existing key ---
        String removed = map.remove(3);
        assert removed.equals("three") : "Removed value should be 'three'";
        assert map.get(3) == null : "Value for removed key should be null";
        assert map.size() == 8 : "Size should decrease after removal";

        // --- Test 8: Remove non-existing key ---
        removed = map.remove(999);
        assert removed == null : "Removing non-existing key should return null";

        // --- Test 9: Clear map ---
        map.clear();
        assert map.size() == 0 : "Size should be 0 after clear";
        assert map.isEmpty() : "Map should be empty after clear";

        System.out.println("All tests passed successfully!");
    }
}
