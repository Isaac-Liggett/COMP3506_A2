// @edu:student-assignment

package uq.comp3506.a2;

// You may wish to import more/other structures too
import uq.comp3506.a2.structures.UnorderedMap;
import uq.comp3506.a2.structures.Vertex;
import uq.comp3506.a2.structures.Entry;
import uq.comp3506.a2.structures.TopologyType;
import uq.comp3506.a2.structures.Edge;
import uq.comp3506.a2.structures.Heap;
import uq.comp3506.a2.structures.Tunnel;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

// This is part of COMP3506 Assignment 2. Students must implement their own solutions.

/**
 * Supplied by the COMP3506/7505 teaching team, Semester 2, 2025.
 * No bounds are provided. You should maximize efficiency where possible.
 * Below we use `S` and `U` to represent the generic data types that a Vertex
 * and an Edge can have, respectively, to avoid confusion between V and E in
 * typical graph nomenclature. That is, Vertex objects store data of type `S`
 * and Edge objects store data of type `U`.
 */
public class Problems {

    /**
     * Return a double representing the minimum radius of illumination required
     * to light the entire tunnel. Your answer will be accepted if
     * |your_ans - true_ans| is less than or equal to 0.000001
     * @param tunnelLength The length of the tunnel in question
     * @param lightIntervals The list of light intervals in [0, tunnelLength];
     * that is, all light interval values are >= 0 and <= tunnelLength
     * @return The minimum radius value required to illuminate the tunnel
     * or -1 if no light fittings are provided
     * Note: We promise that the input List will be an ArrayList.
     */
    public static double tunnelLighting(int tunnelLength, ArrayList<Integer> lightIntervals) {
        if (lightIntervals == null || lightIntervals.isEmpty()) {
            return -1; // No lights provided
        }

        lightIntervals.sort(null);

        double maxRadius = 0;

        for(int i = 0; i <= lightIntervals.size(); i++){
            double radius;
            if(i == 0){
                radius = lightIntervals.get(i);
            }else if(i == lightIntervals.size()){
                radius = tunnelLength - lightIntervals.get(i-1);
            }else{
                radius = (double) (lightIntervals.get(i) - lightIntervals.get(i-1)) / 2.0;
            }

            if(radius > maxRadius){
                maxRadius = radius;
            }
        }

        return maxRadius;
    }

    /**
     * Compute the TopologyType of the graph as represented by the given edgeList.
     * @param edgeList The list of edges making up the graph G; each is of type
     *              Edge, which stores two vertices and a value. Vertex identifiers
     *              are NOT GUARANTEED to be contiguous or in a given range.
     * @return The corresponding TopologyType.
     * Note: We promise not to provide any self loops, double edges, or isolated
     * vertices.
     */
    public static <S, U> TopologyType topologyDetection(List<Edge<S, U>> edgeList) {
        // Convert EdgeList into Adjacency List
        UnorderedMap<Vertex<S>, List<Vertex<S>>> nodes = new UnorderedMap<>();

        for(Edge<S, U> edge : edgeList){
            if(nodes.get(edge.getVertex1()) == null){
                nodes.put(edge.getVertex1(), new ArrayList<>());
            }
            if(nodes.get(edge.getVertex2()) == null){
                nodes.put(edge.getVertex2(), new ArrayList<>());
            }
            nodes.get(edge.getVertex1()).add(edge.getVertex2());
            nodes.get(edge.getVertex2()).add(edge.getVertex1());
        }

        // Classification
        boolean anyTree = false;
        boolean anyGraph = false;
        int componentCount = 0;

        // Perform a BFS on the graph
        UnorderedMap<Vertex<S>, Integer> visited = new UnorderedMap<>();
        List<Vertex<S>> queue = new ArrayList<>();
        queue.add(edgeList.getFirst().getVertex1());

        while (true) {
            if (!queue.isEmpty()) {
                componentCount += 1;
            }

            while (!queue.isEmpty()) {
                Vertex<S> curr = queue.removeFirst();
                if (visited.get(curr) != null) {
                    anyGraph = true;
                    continue;
                }
                visited.put(curr, 1);
                queue.addAll(nodes.get(curr));
            }

            // Check for connectedness
            Vertex<S> unvisitedNode = null;
            for (Vertex<S> node : nodes.keys()) {
                if (visited.get(node) == null) {
                    unvisitedNode = node;
                    break;
                }
            }

            if (unvisitedNode == null) {
                if (componentCount == 1 && anyTree && !anyGraph) {
                    return TopologyType.CONNECTED_TREE;
                } else if (componentCount == 1 && !anyTree && anyGraph) {
                    return TopologyType.CONNECTED_GRAPH;
                } else if (componentCount > 1 && anyTree && !anyGraph) {
                    return TopologyType.FOREST;
                } else if (componentCount > 1 && !anyTree && anyGraph) {
                    return TopologyType.DISCONNECTED_GRAPH;
                } else if (componentCount > 1 && anyTree && anyGraph) {
                    return TopologyType.HYBRID;
                } else {
                    return TopologyType.UNKNOWN;
                }
            } else {
                // Not connected
                queue.add(unvisitedNode);
            }
        }
    }
 
    /**
     * Compute the list of reachable destinations and their minimum costs.
     * @param edgeList The list of edges making up the graph G; each is of type
     *              Edge, which stores two vertices and a value. Vertex identifiers
     *              are NOT GUARANTEED to be contiguous or in a given range.
     * @param origin The origin vertex object.
     * @param threshold The total time the driver can drive before a break is required.
     * @return an ArrayList of Entry types, where the first element is the identifier
     *         of a reachable station (within the time threshold), and the second
     *         element is the minimum cost of reaching that given station. The
     *         order of the list is not important.
     * Note: We promise that S will be of Integer type.
     * Note: You should return the origin in your result with a cost of zero.
     */
    public static <S, U> List<Entry<Integer, Integer>> routeManagement(
        List<Edge<S, U>> edgeList, Vertex<S> origin, int threshold) {

        // Convert EdgeList into Adjacency List
        UnorderedMap<Integer, List<Entry<Integer, Integer>>> nodes = new UnorderedMap<>();

        for (Edge<S, U> edge : edgeList) {
            int id1 = edge.getVertex1().getId();
            int id2 = edge.getVertex2().getId();
            int weight = ((Number) edge.getData()).intValue();

            if (nodes.get(id1) == null) {
                nodes.put(id1, new ArrayList<>());
            }
            if (nodes.get(id2) == null) {
                nodes.put(id2, new ArrayList<>());
            }

            nodes.get(id1).add(new Entry<>(id2, weight));
            nodes.get(id2).add(new Entry<>(id1, weight));
        }

        // Perform Dikstra's Algorithm using a heap.
        Heap<Integer, Integer> queue = new Heap<>();
        queue.insert(0, origin.getId());

        UnorderedMap<Integer, Integer> visited = new UnorderedMap<>();

        while (!queue.isEmpty()) {
            Entry<Integer, Integer> curr = queue.removeMin();
            int dist = curr.getKey();
            int node = curr.getValue();

            if (dist > threshold){
                continue;
            }


            Integer prev = visited.get(node);
            if (prev != null && dist >= prev) continue; // shorter path exists
            visited.put(node, dist);

            List<Entry<Integer, Integer>> neighbours = nodes.get(node);
            if (neighbours == null) continue;

            for (Entry<Integer, Integer> next : neighbours) {
                int newDist = dist + next.getValue();
                if (newDist <= threshold) {
                    queue.insert(newDist, next.getKey());
                }
            }
        }

        return visited.entries();
    }


    /**
     * Compute the tunnel that if flooded will cause the maximal flooding of 
     * the network
     * @param tunnels A list of the tunnels to consider; see Tunnel.java
     * @return The identifier of the Tunnel that would cause maximal flooding.
     * Note that for Tunnel A to drain into some other tunnel B, the distance
     * from A to B must be strictly less than the radius of A plus an epsilon
     * allowance of 0.000001. 
     * Note also that all identifiers in tunnels are GUARANTEED to be in the
     * range [0, n-1] for n unique tunnels.
     */
    public static int totallyFlooded(List<Tunnel> tunnels) {
        if(tunnels.isEmpty()){
            return -1;
        }

        ArrayList<Integer>[] adj = new ArrayList[tunnels.size()];

        for(int i = 0; i < tunnels.size(); i++){
            adj[i] = new ArrayList<>();
        }

        for(int i = 0; i < tunnels.size(); i++) {
            Tunnel a = tunnels.get(i);
            for(int j = 0; j < tunnels.size(); j++) {
                if(i == j) {
                    continue;
                };
                Tunnel b = tunnels.get(j);
                double dx = a.getX() - b.getX();
                double dy = a.getY() - b.getY();
                double dist = Math.sqrt(dx*dx + dy*dy);
                if(dist < a.getRadius()+0.000001) {
                    adj[a.getId()].add(b.getId()); // i can flood j
                }
            }
        }

        // Perform the DFS
        UnorderedMap<Integer, List<Integer>> dp = new UnorderedMap<>();

        for(Tunnel a : tunnels){
            List<Integer> queue = new LinkedList<>();
            queue.add(a.getId());
            UnorderedMap<Integer, Integer> visited = new UnorderedMap<>();

            while(!queue.isEmpty()){
                Integer curr_id = queue.removeLast();

                if(visited.get(curr_id) == null){
                    visited.put(curr_id, 1);
                    if(dp.get(curr_id) != null){
                        for(Integer v : dp.get(curr_id)){
                            visited.put(v, 1);
                        }
                    }else{
                        queue.addAll(adj[curr_id]);
                    }
                }
            }

            dp.put(a.getId(), visited.keys());
        }

        int max_id = tunnels.size()+1;
        int max_affect = 0;

        for(Entry<Integer, List<Integer>> entry : dp.entries()){
            if(entry.getValue().size() > max_affect ||
                (entry.getValue().size() == max_affect && max_id > entry.getKey())){
                max_affect = entry.getValue().size();
                max_id = entry.getKey();
            }
        }


        if(max_id == tunnels.size()+1){
            return -1;
        }
        return tunnels.get(max_id).getId();
    }

    /**
     * Compute the number of sites that cannot be infiltrated from the given starting sites.
     * @param sites The list of unique site identifiers. A site identifier is GUARANTEED to be
     *              non-negative, starting from 0 and counting upwards to n-1.
     * @param rules The infiltration rule. The right-hand side of a rule is represented by a list
     *             of lists of site identifiers (as is done in the assignment specification). The
     *             left-hand side of a rule is given by the rule's index in the parameter `rules`
     *             (i.e. the rule whose left-hand side is 4 will be at index 4 in the parameter
     *              `rules` and can be accessed with `rules.get(4)`).
     * @param startingSites The list of site identifiers to begin your infiltration from.
     * @return The number of sites which cannot be infiltrated.
     */
    public static int susDomination(List<Integer> sites, List<List<List<Integer>>> rules,
                                     List<Integer> startingSites) {
        return -1;
    }
}
