package algorithms;

import java.awt.Point;
import java.util.*;

/**
 * @author  CODEBECQ Florian
 * @date    17/02/2025
 * @version 1.0
 *
 * Sorbonne Université / M2-STL-ALT
 *
 * Description :
 *      Projet réalisé dans le cadre du Devoir TME8: Feedback Vertex Set
 *      Implémentation d'une heuristique pour résoudre le problème du FVS dans un graphe géométrique.
 *
 * /!\ Ce code a été amélioré avec l'aide d'une IA pour
 *      - l'écriture des commentaires
 *      - le renommage des fonctions et variables
 *      - l'optimisation du temps d'execution
 *
 **/
public class DefaultTeam {

    public Evaluation evaluation = new Evaluation();

    /**
     * Calcule un ensemble de sommets formant un Feedback Vertex Set (FVS)
     *
     * @param points        Liste des points (sommets du graphe)
     * @param edgeThreshold Seuil de distance pour considérer une arête
     * @return Liste des points formant un FVS
     */
    public ArrayList<Point> calculFVS(ArrayList<Point> points, int edgeThreshold) {
        // Initialisation du FVS avec tous les points
        ArrayList<Point> fvsSet = new ArrayList<>(points);
        HashSet<Point> pointsToRemove = new HashSet<>();

        // Calcul des voisins pour chaque point (optimisation)
        Map<Point, List<Point>> neighborsMap = calculateNeighborsMap(points, edgeThreshold);

        // File de priorité pour les points ayant au moins 2 voisins
        PriorityQueue<Point> priorityQueue = new PriorityQueue<>(
                Comparator.comparingInt(pt -> neighborsMap.get(pt).size())
        );

        // Initialisation de la file de priorité
        for (Point point : points) {
            if (neighborsMap.get(point).size() < 2) {
                pointsToRemove.add(point);
            } else {
                priorityQueue.add(point);
            }
        }

        // Suppression des points en priorisant ceux ayant le moins de voisins
        while (!priorityQueue.isEmpty()) {
            Point minPoint = priorityQueue.poll();

            if (pointsToRemove.contains(minPoint)) {
                continue;
            }

            fvsSet.remove(minPoint);
            if (!evaluation.isValid(points, fvsSet, edgeThreshold)) {
                fvsSet.add(minPoint);
            } else {
                pointsToRemove.add(minPoint);
            }
        }

        // Amélioration itérative du FVS
        ArrayList<Point> tempFVS = new ArrayList<>(fvsSet);
        do {
            fvsSet = tempFVS;
            tempFVS = improveFVS(points, fvsSet, edgeThreshold, neighborsMap);
        } while (tempFVS.size() < fvsSet.size());

        return tempFVS;
    }

    /**
     * Calcule une carte des voisins pour chaque point
     *
     * @param points        Liste des points (sommets du graphe)
     * @param edgeThreshold Seuil de distance pour considérer une arête
     * @return Carte des voisins pour chaque point
     */
    private Map<Point, List<Point>> calculateNeighborsMap(ArrayList<Point> points, int edgeThreshold) {
        Map<Point, List<Point>> neighborsMap = new HashMap<>();
        for (Point point : points) {
            neighborsMap.put(point, evaluation.neighbor(point, points, edgeThreshold));
        }
        return neighborsMap;
    }

    /**
     * Tente d'améliorer le FVS en remplaçant des paires de sommets par un autre sommet
     *
     * @param points        Liste des points (sommets du graphe)
     * @param fvsSet        Liste des points formant un FVS
     * @param edgeThreshold Seuil de distance pour considérer une arête
     * @param neighborsMap  Carte des voisins pour chaque point
     * @return Nouvelle liste de points après amélioration
     */
    private ArrayList<Point> improveFVS(ArrayList<Point> points, ArrayList<Point> fvsSet, int edgeThreshold, Map<Point, List<Point>> neighborsMap) {
        ArrayList<Point> improvedFVS = new ArrayList<>(fvsSet);

        // Parcourt de toutes les paires de sommets du FVS
        for (int i = 0; i < fvsSet.size(); i++) {
            for (int j = i + 1; j < fvsSet.size(); j++) {
                Point vertex1 = fvsSet.get(i);
                Point vertex2 = fvsSet.get(j);

                improvedFVS.remove(vertex1);
                improvedFVS.remove(vertex2);

                // Essai d'ajout d'un autre sommet pour optimiser
                for (Point candidate : points) {
                    if (!improvedFVS.contains(candidate)) {
                        improvedFVS.add(candidate);
                        if (evaluation.isValid(points, new ArrayList<>(improvedFVS), edgeThreshold)) {
                            return improvedFVS;
                        }
                        improvedFVS.remove(candidate);
                    }
                }

                improvedFVS.add(vertex1);
                improvedFVS.add(vertex2);
            }
        }
        return improvedFVS;
    }
}
