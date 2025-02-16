package algorithms;

import java.awt.Point;
import java.util.*;

public class DefaultTeam {

    public Evaluation evaluation = new Evaluation();

    public ArrayList<Point> calculFVS(ArrayList<Point> points, int edgeThreshold) {
        ArrayList<Point> fvs = new ArrayList<>(points);
        HashSet<Point> aRetirer = new HashSet<>();
        PriorityQueue<Point> queue = new PriorityQueue<>();
        int temp_delete_size = -1;

        ArrayList<Point> fvsTemp = new ArrayList<>();
        int iteration = 0;
        int max_iteration = 1;

        while (iteration < max_iteration) {
            iteration++;

            queue = new PriorityQueue<>(Comparator.comparingInt(p -> evaluation.neighbor(p, fvs, edgeThreshold).size()));
            fvsTemp = new ArrayList<>(fvs);
            fvsTemp.removeAll(aRetirer);

            for (Point p : points) {
                if (evaluation.neighbor(p, points, edgeThreshold).size() < 2) {
                    aRetirer.add(p);
                } else {
                    queue.add(p);
                }
            }

            while (!queue.isEmpty()) {
                Point minPoint = queue.poll();

                if (aRetirer.contains(minPoint)) {
                    continue;
                }

                fvs.remove(minPoint);
                if (!evaluation.isValid(points, fvs, edgeThreshold)) {
                    fvs.add(minPoint);
                } else {
                    aRetirer.add(minPoint);
                }
            }

        }



//        while (aRetirer.size() > temp_delete_size) {
//            temp_delete_size = aRetirer.size();
//            aRetirer = new HashSet<>(upgrade(points, fvs, edgeThreshold, new ArrayList<>(aRetirer)));
//        }

        fvs.removeAll(aRetirer);
        return fvs;
    }

//    private ArrayList<Point> upgrade(ArrayList<Point> points, ArrayList<Point> fvs, int edgeThreshold, ArrayList<Point> removeList) {
//        ArrayList<Point> upgr_fvs = new ArrayList<Point>(fvs);
//
//        for (int p = 0; p < fvs.size(); p++) {
//            for (int q = p + 1; q < fvs.size(); q++) {
//                Point p1 = fvs.get(p);
//                Point p2 = fvs.get(q);
//
//                upgr_fvs.remove(p1);
//                upgr_fvs.remove(p2);
//
//                //int vl = upgr_fvs.size();
//
//                for (Point x : points) {
//                    if (!upgr_fvs.contains(x)) {
//                        upgr_fvs.add(x);
//                        if (evaluation.isValid(points, new ArrayList<>(upgr_fvs), edgeThreshold)) {
//                            //removeList.add(x);
//                            return removeList;
//                        }
//                        upgr_fvs.remove(x);
//                    }
//                }
//
//                upgr_fvs.add(p1);
//                upgr_fvs.add(p2);
//            }
//        }
//        return removeList;
//    }
}
