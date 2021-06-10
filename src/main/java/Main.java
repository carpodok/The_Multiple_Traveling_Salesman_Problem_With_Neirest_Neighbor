import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@SuppressWarnings({"unchecked"})
public class Main {

    static final int COUNT_OF_MOVE_OPERATIONS = 5;

    static int totalCost;

    static HashMap<Integer, LinkedList<LinkedList<Integer>>> all;
    static HashMap<Integer, LinkedList<LinkedList<Integer>>> copyAll;
    static HashMap<Integer, LinkedList<LinkedList<Integer>>> copyAllOfBestAllOfRandomInSol;
    static HashMap<Integer, LinkedList<LinkedList<Integer>>> copyAllOfBestAllOfNNInSol;

    static String[] cities = TurkishNetwork.cities;
    static int[][] distances = TurkishNetwork.distance;
    static ArrayList<Integer> unusedIndexesOfCities;

    static int randomMoveOperation;

    static int bestCostFromRandomInSol;
    static int bestCostFromNNInSol;

    static LinkedList<Integer> indexesOfDepots;
    static LinkedList<Integer> copyIndexesOfDepotsOfPart2;
    static LinkedList<Integer> copyIndexesOfDepotsOfRandomInSol;
    static LinkedList<Integer> copyIndexesOfDepotsOfNNInSol;

    static int countOfswapNodesInRoute = 0;
    static int countOfswapHubWithNodeInRoute = 0;
    static int countOfswapNodesBetweenRoutes = 0;
    static int countOfinsertNodeInRoute = 0;
    static int countOfinsertNodeBetweenRoutes = 0;

    static Params params;

    static int callCountPrint = 0;

    public static void main(String[] args) {

        try {
            params = CliFactory.parseArguments(Params.class, args);
        } catch (ArgumentValidationException e) {
            System.out.println(e.getMessage());
            return;
        }
        copyAll = new HashMap<>();
        all = new HashMap<>();

        copyIndexesOfDepotsOfPart2 = new LinkedList<>();


        System.out.println("******************** Part 1 With Random Initial ********************");

        randomInSol();
        print(copyAllOfBestAllOfRandomInSol, copyIndexesOfDepotsOfRandomInSol, params.getVerbose());

        int inCostRandomp1 = getTotalCost();
        System.out.println("Random Inıtial Cost :" + inCostRandomp1);


        part2WithRandomInSol();
        System.out.println("\n\n******************** Part 2 With Random Initial ********************");
        print(params.getVerbose());

        System.out.println("\nRandom Inıtial Cost :" + inCostRandomp1);

        int inCosRandomp2 = getTotalCost();
        System.out.println("Best Cost With Random Initial Cost :" + inCosRandomp2);

        System.out.println("*****************************************************************************");
        countOfswapNodesInRoute = 0;
        countOfswapHubWithNodeInRoute = 0;
        countOfswapNodesBetweenRoutes = 0;
        countOfinsertNodeInRoute = 0;
        countOfinsertNodeBetweenRoutes = 0;


        NNInSol();
        System.out.println("\n\n******************** Part 1 With NN Initial ********************");
        print(copyAllOfBestAllOfNNInSol, copyIndexesOfDepotsOfNNInSol, params.getVerbose());

        int inCostNNp1 = getTotalCost();

        System.out.println("NN Initial Cost :" + inCostNNp1);


        part2WithNNInSol();
        System.out.println("\n\n******************** Part 2 With NN Initial ********************");
        print(params.getVerbose());

        System.out.println("\nNN Initial Cost :" + inCostNNp1);

        int inCostNNp2 = getTotalCost();
        System.out.println("Best Cost With NN Initial Cost :" + inCostNNp2);

        setJSON();


        System.out.println("\n\n*********************************************** COMPARING ***********************************************\n");

        System.out.println("Part 1 with Random Initial Solution : " + inCostRandomp1 + "      Part 1 with NN Initial Solution : " + inCostNNp1);
        System.out.println("Part 2 with Random Initial Solution : " + inCosRandomp2 + "      Part 2 with NN Initial Solution : " + inCostNNp2);


    }

    static void randomInSol() {

        all = new HashMap<>();

        bestCostFromRandomInSol = Integer.MAX_VALUE;
        copyAllOfBestAllOfRandomInSol = new HashMap<>();

        for (int i = 0; i < 100000; i++) {

            travel(params.getNumDepots(), params.getNumSalesmen());

            calculateTotalCost();

            if (getTotalCost() < bestCostFromRandomInSol) {
                bestCostFromRandomInSol = getTotalCost();
                copyAllOfBestAllOfRandomInSol = copy(all);
                copyIndexesOfDepotsOfRandomInSol = (LinkedList<Integer>) indexesOfDepots.clone();
            }
        }
    }

    public static void NNInSol() {
        all = new HashMap<>();
        bestCostFromNNInSol = Integer.MAX_VALUE;
        copyAllOfBestAllOfNNInSol = new HashMap<>();

        NN(params.getNumDepots(), params.getNumSalesmen(), 65);
        calculateTotalCost();

        bestCostFromNNInSol = getTotalCost();
        copyAllOfBestAllOfNNInSol = copy(all);
        copyIndexesOfDepotsOfNNInSol = (LinkedList<Integer>) indexesOfDepots.clone();

    }

    static void part2WithRandomInSol() {

        setAll(copyAllOfBestAllOfRandomInSol);
        setIndexesOfDepots(copyIndexesOfDepotsOfRandomInSol);

        for (int i = 0; i < 5000000; i++) {

            calculateTotalCost();

            int preCost = getTotalCost();

            copyAll = copy(all);
            copyIndexesOfDepotsOfPart2 = (LinkedList<Integer>) indexesOfDepots.clone();

            getRandomMoveOperations();

            calculateTotalCost();
            int currCost = getTotalCost();

            if (currCost < preCost) {

                switch (randomMoveOperation) {
                    case 0:
                        countOfswapNodesInRoute++;
                        break;
                    case 1:
                        countOfswapHubWithNodeInRoute++;
                        break;
                    case 2:
                        countOfswapNodesBetweenRoutes++;
                        break;
                    case 3:
                        countOfinsertNodeInRoute++;
                        break;
                    case 4:
                        countOfinsertNodeBetweenRoutes++;
                        break;
                }
            } else {

                setAll(copyAll);
                setIndexesOfDepots(copyIndexesOfDepotsOfPart2);

            }
        }
    }

    public static void part2WithNNInSol() {
        setAll(copyAllOfBestAllOfNNInSol);
        setIndexesOfDepots(copyIndexesOfDepotsOfNNInSol);

        for (int i = 0; i < 5000000; i++) {

            calculateTotalCost();

            int preCost = getTotalCost();

            copyAll = copy(all);
            copyIndexesOfDepotsOfPart2 = (LinkedList<Integer>) indexesOfDepots.clone();

            getRandomMoveOperations();

            calculateTotalCost();
            int currCost = getTotalCost();

            if (currCost < preCost) {

                switch (randomMoveOperation) {
                    case 0:
                        countOfswapNodesInRoute++;
                        break;
                    case 1:
                        countOfswapHubWithNodeInRoute++;
                        break;
                    case 2:
                        countOfswapNodesBetweenRoutes++;
                        break;
                    case 3:
                        countOfinsertNodeInRoute++;
                        break;
                    case 4:
                        countOfinsertNodeBetweenRoutes++;
                        break;
                }
            } else {

                setAll(copyAll);
                setIndexesOfDepots(copyIndexesOfDepotsOfPart2);

            }
        }
    }

    public static HashMap<Integer, LinkedList<LinkedList<Integer>>> copy(
            HashMap<Integer, LinkedList<LinkedList<Integer>>> original) {
        HashMap<Integer, LinkedList<LinkedList<Integer>>> copy = new HashMap<>();

        for (int i = 0; i < original.size(); i++) {
            LinkedList<LinkedList<Integer>> linkedListLinkedList = new LinkedList<>();

            for (int j = 0; j < original.get(i).size(); j++) {
                LinkedList<Integer> linkedList = new LinkedList<>(original.get(i).get(j));
                linkedListLinkedList.add(linkedList);

            }
            copy.put(i, linkedListLinkedList);

        }

        return copy;
    }

    public static void travel(int d, int s) {

        final int numberOfCities = TurkishNetwork.cities.length;

        unusedIndexesOfCities = new ArrayList<>();
        indexesOfDepots = new LinkedList<>();

        for (int i = 0; i < numberOfCities; i++) {
            unusedIndexesOfCities.add(i);
        }

        for (int i = 0; i < d; i++) {
            indexesOfDepots.add(produceRandomIndexFromList(unusedIndexesOfCities));
        }

        for (int i = 0; i < d; i++) {

            LinkedList<LinkedList<Integer>> list = new LinkedList<>();

            for (int j = 0; j < s; j++) {

                LinkedList<Integer> linkedList = new LinkedList<>();

                list.add(linkedList);

            }
            all.put(i, list);

        }
        for (int i = 0; i < numberOfCities - d; i++) {

            all.get(produceRandomNum(d)).get(produceRandomNum(s)).add(produceRandomIndexFromList(unusedIndexesOfCities));

        }
    }


    static void NN(int d, int s, int initialCity) {

        unusedIndexesOfCities = new ArrayList<>();

        indexesOfDepots = new LinkedList<>();
        final int numberOfCities = TurkishNetwork.cities.length;

        int countOfEachRoute = (numberOfCities - d) / (d * s);

        for (int i = 0; i < numberOfCities; i++) {
            unusedIndexesOfCities.add(i);
        }
        int preCity = 0;

        for (int i = 0; i < d; i++) {

            if (i == 0) {
                indexesOfDepots.add(initialCity);
                unusedIndexesOfCities.remove(Integer.valueOf(initialCity));
                preCity = initialCity;
            } else {
                int nearestCurr = nearestNeighbor(preCity);
                indexesOfDepots.add(nearestCurr);
                preCity = nearestCurr;
                unusedIndexesOfCities.remove(Integer.valueOf(nearestCurr));

            }

            LinkedList<LinkedList<Integer>> routesOfCurrDepot = new LinkedList<>();

            for (int j = 0; j < s; j++) {

                int cityNumForRoute;

                if (i == d - 1 && j == s - 1) {
                    cityNumForRoute = unusedIndexesOfCities.size();

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
            all.put(i, routesOfCurrDepot);
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

        return nearestCity;
    }

    public static int produceRandomNum(int upperBond) {
        return (int) (Math.random() * upperBond);
    }

    static void calculateTotalCost() {
        totalCost = 0;

        for (int i = 0; i < indexesOfDepots.size(); i++) {

            for (int j = 0; j < all.get(i).size(); j++) {

                for (int k = 0; k < all.get(i).get(j).size(); k++) {

                    if (k == 0 || k == all.get(i).get(j).size() - 1) {
                        totalCost += distances[indexesOfDepots.get(i)][all.get(i).get(j).get(k)];
                    } else {
                        totalCost += distances[all.get(i).get(j).get(k - 1)][all.get(i).get(j).get(k)];
                    }

                }

            }

        }

    }

    static int getTotalCost() {
        return totalCost;
    }

    public static void getRandomMoveOperations() {
        randomMoveOperation = (int) (Math.random() * COUNT_OF_MOVE_OPERATIONS);

        switch (randomMoveOperation) {
            case 0:
                swapNodesInRoute();
                break;
            case 1:
                swapHubWithNodeInRoute();
                break;
            case 2:
                swapNodesBetweenRoutes();
                break;
            case 3:
                insertNodeInRoute();
                break;
            case 4:
                insertNodeBetweenRoutes();
                break;
        }
    }

    static void print(boolean v) {
        callCountPrint++;

        for (Integer integer : all.keySet()) {

            if (v) {
                System.out.println("Depot" + (integer + 1) + " :" + cities[integer]);
            } else {
                System.out.println("Depot" + (integer + 1) + " :" + indexesOfDepots.get(integer));
            }

            for (LinkedList<Integer> i : all.get(integer)) {
                System.out.print("     Route :");
                for (int indexes : i) {

                    if (v) {
                        System.out.print(cities[indexes] + " ");
                    } else {
                        System.out.print(indexes + " ");

                    }

                }
                System.out.println();
            }
            System.out.println();

        }

        calculateTotalCost();

        /*if (callCountPrint == 1){
            System.out.println("Total Cost From Part 1 Random In Sol: " + bestCostFromRandomInSol);
        }else {
            System.out.println("Total Cost From Part 1 NN In Sol: " + bestCostFromNNInSol);

        }*/

        // System.out.println("Total Cost From Part 2 : " + getTotalCost());
        System.out.println("countOfswapNodesInRoute :" + countOfswapNodesInRoute);
        System.out.println("countOfswapHubWithNodeInRoute :" + countOfswapHubWithNodeInRoute);
        System.out.println("countOfswapNodesBetweenRoutes :" + countOfswapNodesBetweenRoutes);
        System.out.println("countOfinsertNodeInRoute :" + countOfinsertNodeInRoute);
        System.out.println("countOfinsertNodeBetweenRoutes :" + countOfinsertNodeBetweenRoutes);

    }

    static void print(HashMap<Integer, LinkedList<LinkedList<Integer>>> all, LinkedList<Integer> indexesOfDepots, boolean v) {

        for (Integer integer : all.keySet()) {

            if (v) {
                System.out.println("Depot" + (integer + 1) + " :" + cities[integer]);
            } else {
                System.out.println("Depot" + (integer + 1) + " :" + indexesOfDepots.get(integer));
            }

            for (LinkedList<Integer> i : all.get(integer)) {
                System.out.print("     Route :");
                for (int indexes : i) {

                    if (v) {
                        System.out.print(cities[indexes] + " ");
                    } else {
                        System.out.print(indexes + " ");

                    }

                }
                System.out.println();
            }
            System.out.println();

        }

       /* calculateTotalCost();

        System.out.println("Total Cost Random: " + bestCostFromRandomInSol);
        System.out.println("Total Cost NN: " + bestCostFromNNInSol);*/

    }

    public static int produceRandomIndexFromList(ArrayList<Integer> list) {

        Random random = new Random();

        int randomNum = list.get(random.nextInt(list.size()));

        list.remove(Integer.valueOf(randomNum));

        return randomNum;
    }



    // Move Operations

    public static void swapNodesInRoute() {

        int randomIndexOfDepots = (int) (Math.random() * indexesOfDepots.size());

        int randomIndexOfRouteOfDepot = (int) (Math.random() * all.get(randomIndexOfDepots).size());

        if (all.get(randomIndexOfDepots).get(randomIndexOfRouteOfDepot).size() > 1) {
            int randomFirstIndexOfCityOfRouteOfDepot = (int) (Math.random() * all.get(randomIndexOfDepots).get(randomIndexOfRouteOfDepot).size());

            int randomSecondIndexOfCityOfRouteOfDepot = (int) (Math.random() * all.get(randomIndexOfDepots).get(randomIndexOfRouteOfDepot).size());

            int firstRandomNode = all.get(randomIndexOfDepots).get(randomIndexOfRouteOfDepot).get(randomFirstIndexOfCityOfRouteOfDepot);
            int secondRandomNode = all.get(randomIndexOfDepots).get(randomIndexOfRouteOfDepot).get(randomSecondIndexOfCityOfRouteOfDepot);

            if (firstRandomNode != secondRandomNode) {

                all.get(randomIndexOfDepots).get(randomIndexOfRouteOfDepot).set(randomFirstIndexOfCityOfRouteOfDepot, secondRandomNode);
                all.get(randomIndexOfDepots).get(randomIndexOfRouteOfDepot).set(randomSecondIndexOfCityOfRouteOfDepot, firstRandomNode);

            }
        } else {
            getRandomMoveOperations();
        }
    }

    public static void swapHubWithNodeInRoute() {

        int randomIndexOfDepots = (int) (Math.random() * (indexesOfDepots.size()));

        int randomIndexOfRouteOfDepot = (int) (Math.random() * (all.get(randomIndexOfDepots).size()));

        int randomIndexOfCityOfRouteOfDepot = (int) (Math.random() * (all.get(randomIndexOfDepots).get(randomIndexOfRouteOfDepot).size()));

        int randomNode = all.get(randomIndexOfDepots).get(randomIndexOfRouteOfDepot).get(randomIndexOfCityOfRouteOfDepot);

        all.get(randomIndexOfDepots).get(randomIndexOfRouteOfDepot).set(randomIndexOfCityOfRouteOfDepot, indexesOfDepots.get(randomIndexOfDepots));

        indexesOfDepots.set(randomIndexOfDepots, randomNode);

    }

    public static void swapNodesBetweenRoutes() {

        int randomFirstIndexOfDepots = (int) (Math.random() * indexesOfDepots.size());

        int randomFirstIndexOfRouteOfDepot = (int) (Math.random() * all.get(randomFirstIndexOfDepots).size());

        int randomFirstIndexOfCityOfRouteOfDepot = (int) (Math.random() * all.get(randomFirstIndexOfDepots).get(randomFirstIndexOfRouteOfDepot).size());

        int firstRandomNode = all.get(randomFirstIndexOfDepots).get(randomFirstIndexOfRouteOfDepot).get(randomFirstIndexOfCityOfRouteOfDepot);

        int randomSecondIndexOfDepots = (int) (Math.random() * indexesOfDepots.size());

        int randomSecondIndexOfRouteOfDepot = (int) (Math.random() * all.get(randomSecondIndexOfDepots).size());

        int randomSecondIndexOfCityOfRouteOfDepot = (int) (Math.random() * all.get(randomSecondIndexOfDepots).get(randomSecondIndexOfRouteOfDepot).size());

        int secondRandomNode = all.get(randomSecondIndexOfDepots).get(randomSecondIndexOfRouteOfDepot).get(randomSecondIndexOfCityOfRouteOfDepot);

        if (randomFirstIndexOfDepots != randomSecondIndexOfDepots) {


            all.get(randomFirstIndexOfDepots).get(randomFirstIndexOfRouteOfDepot).set(randomFirstIndexOfCityOfRouteOfDepot,
                    secondRandomNode);

            all.get(randomSecondIndexOfDepots).get(randomSecondIndexOfRouteOfDepot).set(randomSecondIndexOfCityOfRouteOfDepot, firstRandomNode);
        } else {
            getRandomMoveOperations();
        }
    }

    public static void insertNodeInRoute() {

        int randomIndexOfDepots = (int) (Math.random() * indexesOfDepots.size());

        int randomIndexOfRouteOfDepot = (int) (Math.random() * all.get(randomIndexOfDepots).size());

        if (all.get(randomIndexOfDepots).get(randomIndexOfRouteOfDepot).size() > 1) {
            int randomFirstIndexOfCityOfRouteOfDepot = (int) (Math.random() * all.get(randomIndexOfDepots).get(randomIndexOfRouteOfDepot).size()); // 2

            int firstRandomNode = all.get(randomIndexOfDepots).get(randomIndexOfRouteOfDepot).get(randomFirstIndexOfCityOfRouteOfDepot); // 58
            all.get(randomIndexOfDepots).get(randomIndexOfRouteOfDepot).remove(randomFirstIndexOfCityOfRouteOfDepot);


            int randomSecondIndexOfCityOfRouteOfDepot = (int) (Math.random() * all.get(randomIndexOfDepots).get(randomIndexOfRouteOfDepot).size());


            all.get(randomIndexOfDepots).get(randomIndexOfRouteOfDepot).add(randomSecondIndexOfCityOfRouteOfDepot + 1, firstRandomNode);


        } else {
            getRandomMoveOperations();
        }


    }

    public static void insertNodeBetweenRoutes() {


        int randomFirstIndexOfDepots = (int) (Math.random() * indexesOfDepots.size());

        int randomFirstIndexOfRouteOfDepot = (int) (Math.random() * all.get(randomFirstIndexOfDepots).size());

        int randomSecondIndexOfDepots = (int) (Math.random() * indexesOfDepots.size());

        if (all.get(randomFirstIndexOfDepots).get(randomFirstIndexOfRouteOfDepot).size() > 1 && randomSecondIndexOfDepots != randomFirstIndexOfDepots) {

            int randomFirstIndexOfCityOfRouteOfDepot = (int) (Math.random() * all.get(randomFirstIndexOfDepots).get(randomFirstIndexOfRouteOfDepot).size());

            int firstRandomNode = all.get(randomFirstIndexOfDepots).get(randomFirstIndexOfRouteOfDepot).get(randomFirstIndexOfCityOfRouteOfDepot);


            int randomSecondIndexOfRouteOfDepot = (int) (Math.random() * all.get(randomSecondIndexOfDepots).size());

            int randomSecondIndexOfCityOfRouteOfDepot = (int) (Math.random() * all.get(randomSecondIndexOfDepots).get(randomSecondIndexOfRouteOfDepot).size());


            all.get(randomFirstIndexOfDepots).get(randomFirstIndexOfRouteOfDepot).remove(randomFirstIndexOfCityOfRouteOfDepot);

            all.get(randomSecondIndexOfDepots).get(randomSecondIndexOfRouteOfDepot).add(randomSecondIndexOfCityOfRouteOfDepot + 1, firstRandomNode);

        } else {
            getRandomMoveOperations();
        }
    }



    // Set methods
    public static void setAll(HashMap<Integer, LinkedList<LinkedList<Integer>>> all) {
        Main.all = all;
    }

    public static void setIndexesOfDepots(LinkedList<Integer> indexesOfDepots) {
        Main.indexesOfDepots = indexesOfDepots;
    }


    static void setJSON() {
        JSONArray trying = new JSONArray();
        JSONObject sol = new JSONObject();

        for (int i = 0; i < all.size(); i++) {
            JSONArray depotsDetails = new JSONArray();
            JSONObject depots = new JSONObject();

            for (int j = 0; j < all.get(i).size(); j++) {

                StringBuilder route = new StringBuilder();
                for (int k = 0; k < all.get(i).get(j).size(); k++) {
                    if (k == all.get(i).get(j).size() - 1) {
                        route.append(all.get(i).get(j).get(k));
                    } else {
                        route.append(all.get(i).get(j).get(k)).append(" ");

                    }
                }
                depotsDetails.add(String.valueOf(route));
            }

            String depotIndex = String.valueOf(indexesOfDepots.get(i));
            depots.put("depot", depotIndex);
            depots.put("Route", depotsDetails);
            trying.add(depots);
        }

        sol.put("Solution", trying);

        try (FileWriter file = new FileWriter("solution.json")) {
            file.write(sol.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
