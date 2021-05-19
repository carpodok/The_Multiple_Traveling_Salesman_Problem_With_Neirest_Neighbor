/*
import java.util.ArrayList;
import java.util.LinkedList;

public class Main2 {

    public static void travel(int d, int s) {

        final int numberOfCities = TurkishNetwork.cities.length;

        int atLeastNum = d * (s + 1);

        unusedIndexesOfCities = new ArrayList<>();
        indexesOfDepots = new LinkedList<>();

        for (int i = 0; i < numberOfCities; i++) {
            unusedIndexesOfCities.add(i);
        }

        for (int i = 0; i < d; i++) {

            int index1 = produceRandomIndexFromList(unusedIndexesOfCities);

            indexesOfDepots.add(index1);

            atLeastNum--;

            LinkedList<LinkedList<Integer>> routesOfCurrDepot = new LinkedList<>();

            for (int j = 0; j < s; j++) {

                atLeastNum--;

                int randomCityNumForRoute;

                if (i == d - 1 && j == s - 1) {
                    randomCityNumForRoute = unusedIndexesOfCities.size();

                } else {

                    randomCityNumForRoute = 1 + ((int) (Math.random() * (unusedIndexesOfCities.size() - atLeastNum - 1)));

                }

                LinkedList<Integer> citiesOfCurrRoutes = new LinkedList<>();

                for (int k = 0; k < randomCityNumForRoute; k++) {

                    int index2 = produceRandomIndexFromList(unusedIndexesOfCities);

                    citiesOfCurrRoutes.add(index2);

                }

                routesOfCurrDepot.add(citiesOfCurrRoutes);
            }
            all.put(i, routesOfCurrDepot);
        }

    }
}
*/
