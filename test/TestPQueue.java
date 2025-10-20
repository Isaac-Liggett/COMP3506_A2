/**
 * Supplied by the COMP3506/7505 teaching team, Semester 2, 2025.
 */

import uq.comp3506.a2.structures.Heap;

public class TestPQueue {

    public static void main(String[] args) {
        System.out.println("Testing the Heap-based Priority Queue Class...");
     
        Heap<Integer, String> pq = new Heap<>();

        System.out.println("Starting Heap tests...");

        Heap<Integer, String> heap = new Heap<>();

        // --- Test 1: Empty heap ---
        assert heap.isEmpty() : "Heap should start empty";
        assert heap.size() == 0 : "Heap size should start at 0";
        assert heap.peekMin() == null : "peekMin() should return null on empty heap";
        assert heap.removeMin() == null : "removeMin() should return null on empty heap";

        // --- Test 2: Single insert ---
        heap.insert(5, "five");
        assert !heap.isEmpty() : "Heap should not be empty after one insert";
        assert heap.size() == 1 : "Heap size should be 1 after insert";
        assert heap.peekMin().getKey() == 5 : "Min key should be 5";
        assert heap.peekMin().getValue().equals("five") : "Min value should be 'five'";

        // --- Test 3: Multiple inserts, check min tracking ---
        heap.insert(3, "three");
        heap.insert(7, "seven");
        heap.insert(1, "one");
        heap.insert(4, "four");
        assert heap.size() == 5 : "Heap should have 5 elements";
        assert heap.peekMin().getKey() == 1 : "Min key should be 1 after inserting smaller value";

        // --- Test 4: RemoveMin order ---
        assert heap.removeMin().getKey() == 1 : "removeMin() should return smallest key first (1)";
        assert heap.removeMin().getKey() == 3 : "Next smallest should be 3";
        assert heap.removeMin().getKey() == 4 : "Then 4";
        assert heap.removeMin().getKey() == 5 : "Then 5";
        assert heap.removeMin().getKey() == 7 : "Then 7";
        assert heap.isEmpty() : "Heap should be empty after removing all elements";

        // --- Test 5: Interleaved insert/remove ---
        heap.insert(10, "ten");
        heap.insert(2, "two");
        assert heap.peekMin().getKey() == 2 : "Min key should be 2";
        heap.insert(8, "eight");
        assert heap.removeMin().getKey() == 2 : "Removed min should be 2";
        assert heap.peekMin().getKey() == 8 : "Min should now be 8";
        heap.insert(1, "one");
        assert heap.peekMin().getKey() == 1 : "Min should now be 1";
        heap.insert(0, "zero");
        assert heap.removeMin().getKey() == 0 : "Min removal should give 0";
        assert heap.removeMin().getKey() == 1 : "Next removal should give 1";
        assert heap.removeMin().getKey() == 8 : "Then 8";
        assert heap.removeMin().getKey() == 10 : "Then 10";
        assert heap.isEmpty() : "Heap should be empty again";

        // --- Test 6: Clear operation ---
        heap.insert(100, "hundred");
        heap.insert(200, "two hundred");
        heap.clear();
        assert heap.isEmpty() : "Heap should be empty after clear()";
        assert heap.size() == 0 : "Heap size should be 0 after clear()";

        System.out.println("✅ All Heap tests passed!");

    }
}
