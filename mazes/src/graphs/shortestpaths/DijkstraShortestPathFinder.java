package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.Collections;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        // return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        Map<V, E> edgeTo = new HashMap<>();
        Map<V, Double> distTo = new HashMap<>();

        ExtrinsicMinPQ<V> perimeter = createMinPQ();

        perimeter.add(start, 0.0);
        distTo.put(start, 0.0);

        while (!perimeter.isEmpty()) {
            V from = perimeter.removeMin();
            if (Objects.equals(from, end)) {
                return edgeTo;
            }
            for (E edge : graph.outgoingEdgesFrom(from)) {
                V to = edge.to();
                if (!distTo.containsKey(to)) {
                    distTo.put(to, Double.POSITIVE_INFINITY);
                }
                Double oldDist = distTo.get(to);
                Double newDist = distTo.get(from) + edge.weight();

                if (newDist < oldDist) {
                    distTo.put(to, newDist);
                    edgeTo.put(to, edge);
                    if (perimeter.contains(to)) {
                        perimeter.changePriority(to, newDist);
                    } else {
                        perimeter.add(to, newDist);
                    }
                }
            }
        }

        return edgeTo;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }

        E edge = spt.get(end);
        if (edge == null) {
            return new ShortestPath.Failure<>();
        }

        List<E> edgesList = new ArrayList<>();

        V vertexFrom = end;

        while (!Objects.equals(vertexFrom, start)) {
            edge = spt.get(vertexFrom);
            edgesList.add(edge);
            vertexFrom = edge.from();
        }

        Collections.reverse(edgesList);

        return new ShortestPath.Success<>(edgesList);
    }

}
