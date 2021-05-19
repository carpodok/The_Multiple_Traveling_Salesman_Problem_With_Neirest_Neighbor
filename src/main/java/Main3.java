import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Main3 {

    static ArrayList<Integer> unusedIndexesOfCities;
    static ArrayList<Integer> usedIndexesOfCities;
    static LinkedList<Integer> indexesOfDepots;

    public static void main(String[] args) {


        unusedIndexesOfCities = new ArrayList<>();
        usedIndexesOfCities = new ArrayList<>();
        indexesOfDepots = new LinkedList<>();


        NN(5,2,5);
        System.out.println(unusedIndexesOfCities.size());
        System.out.println(indexesOfDepots.size());

    }

    static void NN(int d, int s, int initialCity) {
        final int numberOfCities = TurkishNetwork.cities.length;

        int countOfEachRoute = (numberOfCities - d) / (d * s);


        for (int i = 0; i < numberOfCities; i++) {
           // unusedIndexesOfCities.add(distancesOfCities[initialCity][i]);
            unusedIndexesOfCities.add(i);
        }
        int preCity = 0;

        for (int i = 0; i < d; i++) {

            if (i == 0) {
                indexesOfDepots.add(initialCity);
                unusedIndexesOfCities.remove(Integer.valueOf(initialCity));
                preCity = initialCity;
            } else {
                //  int index1 = produceRandomIndexFromList(unusedIndexesOfCities);
                // indexesOfDepots.add(index1);

                int nearestCurr = nearestNeighbor(preCity);
                indexesOfDepots.add(nearestCurr);
                preCity = nearestCurr;
                unusedIndexesOfCities.remove(Integer.valueOf(nearestCurr));

            }

            LinkedList<LinkedList<Integer>> routesOfCurrDepot = new LinkedList<>();

            for (int j = 0; j < s; j++) {

                int cityNumForRoute;

                if (i == d - 1 && j == s - 1) {
                    cityNumForRoute = unusedIndexesOfCities.size() ;

                } else {

                    cityNumForRoute = countOfEachRoute;

                }

                LinkedList<Integer> citiesOfCurrRoutes = new LinkedList<>();

                for (int k = 0; k < cityNumForRoute; k++) {

                    int index2 = nearestNeighbor(preCity);
                    preCity = index2;
                    unusedIndexesOfCities.remove(Integer.valueOf(index2));

                    citiesOfCurrRoutes.add(index2);

                }

                routesOfCurrDepot.add(citiesOfCurrRoutes);
            }
           // all.put(i, routesOfCurrDepot);
        }
    }

    static int nearestNeighbor(int city) {

        int minDistance = Integer.MAX_VALUE;
        int nearestCity = city;

        for (int i = 0; i < TurkishNetwork.cities.length; i++) {
            if (i != city) {

                int currDistance = TurkishNetwork.distance[city][i];

                if (currDistance < minDistance && unusedIndexesOfCities.contains(i)) {
                    minDistance = currDistance;

                    nearestCity = i;
                }
            }
        }

      //  usedIndexesOfCities.add(nearestCity);

        return nearestCity;
    }
}
