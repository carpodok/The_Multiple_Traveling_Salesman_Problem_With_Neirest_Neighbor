# THE MULTIPLE TRAVELING SALESMAN PROBLEM

The Multiple Traveling Salesman Problem (mTSP) in which more than one salesman is allowed is a generalization of the Traveling Salesman Problem (TSP). Given a set of cities, one depot (where m salesmen are located), and a cost metric, the objective of the mTSP is to determine a set of routes for m salesmen so as to minimize the total cost of the m routes.
The cost metric can represent cost, distance, or time. The requirements on the set of routes are:

-All of the routes start and end at the (same) depot.
-There is at least one city (except depot) in each route.
-Each city visited exactly once by only one salesman.
Multiple depots: Instead of one depot, the multi-depot mTSP has a set of depots, with mj salesmen at each depot j. In the fixed destination version, a salesman returns to the same depot from which he started.

Please keep in mind that this project is based on the 81 cities of Turkey while examining sample solutions given below.

## Part-I

In this part, we will generate 100,000 random solutions to the *fixed destination* version of the multi-depot *m*TSP.
The number of depots and salesman per depot will be our parameters. The cost metric will be total distance in kilometers.
At the end, we will print the best solution that has the minimum cost among 100,000 random solutions.

Our project is a valid maven project. `mvn clean package` produce an executable jar file under the target directory.
This can be done via maven plugins such as [shade](https://maven.apache.org/plugins/maven-shade-plugin) or [assembly](https://maven.apache.org/plugins/maven-assembly-plugin) plugin.
Optional parameter [finalName](https://maven.apache.org/plugins/maven-shade-plugin/shade-mojo.html#finalName) can be used to change the name of the shaded artifactId.
To parse command line arguments, we can use [JewelCLI](http://jewelcli.lexicalscope.com) library.

For example, `java -jar target/mTSP.jar -d 5 -s 2 -v` would produce something like below.
Notice that the last line includes the cost metric: the total distance travelled by all salesmen.

Depot1: İÇEL
  Route1: ZONGULDAK,GİRESUN,VAN,OSMANİYE,BİNGÖL,ELAZIĞ,ŞIRNAK,BAYBURT,IĞDIR
  Route2: BURDUR,AYDIN,MANİSA,TUNCELİ,ANKARA,ÇANKIRI,KIRIKKALE
Depot2: DİYARBAKIR
  Route1: KIRŞEHİR,KAYSERİ,KÜTAHYA,ARTVİN,İZMİR,HATAY,UŞAK,ISPARTA,KAHRAMANMARAŞ,İSTANBUL
  Route2: KONYA,ŞANLIURFA,ADIYAMAN,MALATYA,SİVAS,BATMAN,MUŞ,SİİRT
Depot3: ERZURUM
  Route1: AĞRI,KARAMAN,BOLU,ANTALYA,KASTAMONU,ÇORUM,ÇANAKKALE,SAKARYA,GÜMÜŞHANE,BİTLİS
  Route2: ERZİNCAN,GAZİANTEP,BURSA,HAKKARİ
Depot4: ESKİŞEHİR
  Route1: MUĞLA,BARTIN,NİĞDE,RİZE,NEVŞEHİR
  Route2: YOZGAT,KARABÜK,BALIKESİR,TEKİRDAĞ,AFYON,YALOVA
Depot5: TOKAT
  Route1: DÜZCE,TRABZON,MARDİN,ARDAHAN,KARS,ORDU,KOCAELİ,DENİZLİ,KIRKLARELİ,EDİRNE
  Route2: AKSARAY,BİLECİK,ADANA,SİNOP,AMASYA,KİLİS,SAMSUN
**Total cost is 52308
```

Non-verbose example `java -jar target/mTSP.jar -d 2 -s 5` will print city indices instead of city names:

Depot1: 18
  Route1: 32,67,27,7,54,6,38,53,73
  Route2: 56,9,72,55,1,12
  Route3: 8,16,19,26,3,29,47,11,24
  Route4: 49,42,25,58,4,22
  Route5: 0,43,77,36,70
Depot2: 59
  Route1: 51,35,62,57,50
  Route2: 13,80,31,71,75,14,78
  Route3: 30,41,79,48,64,28,39,45,46
  Route4: 61,76,5,68,74,60,33,21,10,65,23
  Route5: 44,40,15,66,63,34,52,37,17,2,20,69
**Total cost is 51631
```


## Part-II

In the second part, we will apply a heuristic algorithm to our *fixed destination* version of the multi-depot mTSP.

The term heuristic is used for algorithms which find solutions among all possible ones, but they do not guarantee that the optimal will be found.
[Heuristic algorithms](https://optimization.mccormick.northwestern.edu/index.php/Heuristic_algorithms) often times used to solve NP-complete problems.

The heuristic will iteratively work on the solution (best of the 100,000 random solutions) obtained from the [Part-I]. 
In Part-II, we will define five different move operations, which will be detailed in the following subsections.
In each iteration, one move operation will be selected (among five) based on a random manner, and then it will be applied to the current solution.
If the move improves the solution (the total distance travelled) then, we will update the best solution at hand. If not, next iteration will be continued.
To implement this logic, we need to devise a strategy to somehow backup the current solution. 
So that if the subsequent move operation does not improve the solution, it should be possible to rollback to a previous state.

### Move operations
Some of the the move operation will involve a process where we need to generate two different random numbers from a given interval.
We write a method to generate two random numbers that are different from each other.
Here comes the five move operations that the heuristic will be using.

#### swapNodesInRoute
Swap two nodes in a route. Here, both the route and the two nodes are randomly chosen.
In this move we select a random route among all routes and then we swap two nodes.
Remember to avoid no-operation, we need to select two nodes that are different from each other.
Example of the move: random node indices are 1 and 7, which are shown in bold.

Before: hub: 24	nodes: 64,**29**,72,55,71,12,48,**11**
After:  hub: 24	nodes: 64,**11**,72,55,71,12,48,**29**


#### swapHubWithNodeInRoute
Swap hub with a randomly chosen node in a route. Here, both the route and the node are randomly chosen.
In this move we select a random route among all routes and then we replace the hub with a random node.
Here it is crucial to update the hub in the remaining routes of the initial hub.

Example of the move: random node index is 10, which is shown in bold.

Before:
hub : **49**	
  hub: 49 nodes: 11,20,26,78,30,0,41,63,44,34,**8**,47,14,31,2,69,50
  hub: 49 nodes: 18,54,51,27,37
After:
hub : **8**
  hub: 8 nodes: 11,20,26,78,30,0,41,63,44,34,**49**,47,14,31,2,69,50
  hub: 8 nodes: 18,54,51,27,37
  

#### swapNodesBetweenRoutes
This is similar to [swapNodesInRoute](#swapnodesinroute), but this time we will be using two different routes.
In this move we select two random routes (that are different) among all routes. Then, we select a random node in each route and then swap them.
Here it is important to select two routes that are different from each other, otherwise this move will be identical to swapNodesInRoute.

Example of the move: random node indices are 6 and 7, which are shown in bold.

Before:
  hub: 0 nodes: 22,61,23,28,68,24,**11**,20,1,26,45
  hub: 3 nodes: 35,74,7,51,59,37,50,**30**,78,62,71,55
After:
  hub: 0 nodes: 22,61,23,28,68,24,**30**,20,1,26,45
  hub: 3 nodes: 35,74,7,51,59,37,50,**11**,78,62,71,55


#### insertNodeInRoute
This is similar to [swapNodesInRoute](#swapnodesinroute): instead of swapping, we delete the source node, and then insert it to right of the destination node.
Note that this operation is only valid on a route having more than two nodes.

Example of the move: random node indices are 2 and 6, which are shown in bold.

Before:
  hub: 35 nodes: 17,21,**58**,33,23,34,**28**
After:
  hub: 35 nodes: 17,21,33,23,34,28,**58**


### insertNodeBetweenRoutes
This is similar to [swapNodesBetweenRoutes](#swapnodesbetweenroutes): instead of swapping, we delete the source node, and then insert it to right of the destination node.

Example of the move: random node indices are 11 and 4, which are shown in bold.

Before:
  hub: 4 nodes: 3,75,35,74,7,52,27,51,54,56,63,**19**,8,47,14,31,6,41,70,18
  hub: 50 nodes: 72,29,64,48,**12**,55,71,1
After:
  hub: 4 nodes: 3,75,35,74,7,52,27,51,54,56,63,8,47,14,31,6,41,70,18
  hub: 50 nodes: 72,29,64,48,12,**19**,55,71,1

Notice that this is a cross-route operation.
The number of nodes of the first route is decreased by one. The number of nodes of the second route is increased by one. 
Thus, first node must have more than two nodes. Otherwise, solution will be invalid after the deletion.

### The result

Do 5,000,000 iterations. At the end we will obtain a much better solution than that those of [Part-I].
Here is one of the solutions that We obtained.

Depot1: NİĞDE
  Route1: NEVŞEHİR,KAYSERİ
  Route2: GÜMÜŞHANE,RİZE,ARTVİN,ARDAHAN,KARS,ERZURUM,BAYBURT,ERZİNCAN,TUNCELİ,BİNGÖL,DİYARBAKIR,ŞANLIURFA,ADIYAMAN,KAHRAMANMARAŞ,GAZİANTEP,KİLİS,HATAY,OSMANİYE,ADANA,İÇEL
Depot2: SAKARYA
  Route1: KÜTAHYA,AFYON,UŞAK,İZMİR,MANİSA,BALIKESİR,BURSA,YALOVA
  Route2: KARABÜK,BARTIN,ZONGULDAK,İSTANBUL,KIRKLARELİ,EDİRNE,ÇANAKKALE,TEKİRDAĞ,KOCAELİ
Depot3: ŞIRNAK
  Route1: HAKKARİ,VAN,IĞDIR,AĞRI,MUŞ,BİTLİS,BATMAN,SİİRT
  Route2: KIRŞEHİR,KIRIKKALE,ANKARA,ESKİŞEHİR,BİLECİK,DÜZCE,BOLU,ÇANKIRI,KASTAMONU,SİNOP,AMASYA,SİVAS,MALATYA,ELAZIĞ,MARDİN
Depot4: KONYA
  Route1: KARAMAN,ANTALYA,DENİZLİ,AYDIN,MUĞLA,BURDUR,ISPARTA
  Route2: AKSARAY
Depot5: GİRESUN
  Route1: TRABZON
  Route2: TOKAT,YOZGAT,ÇORUM,SAMSUN,ORDU
**Total cost is 14399

Notice that 14,399km is less than 51,631km. Also print counts of the moves that caused gains:
```json
{
  "swapHubWithNodeInRoute": 30,
  "insertNodeBetweenRoutes": 74,
  "swapNodesInRoute": 39,
  "swapNodesBetweenRoutes": 54,
  "insertNodeInRoute": 42
}
```

Which move does the heuristic algorithm benefit the most?

Solution -d 4 -s 2 :new:
We aved our best solution (for numDepots=4 and numSalesmen=2) in a file named `solution.json` and save it at the top-level directory
(near the pom.xml and the README.md files). Commit and push your `solution.json` file to your repository.
Here it does not matter how you obtain best solution. It can be be obtained from any heuristic algorithm (random or hill climbing).
Or you can use commercial solvers if you want to: [GAMS](https://www.gams.com), [Gurobi](http://www.gurobi.com), [CPLEX](https://www.ibm.com/analytics/data-science/prescriptive-analytics/cplex-optimizer) etc.
You can even construct it manually!!
An example of a solution rendered in [JSON](https://github.com/google/gson) format is as follows:

Json
{
  "Solution": [
    {
      "depot": "70",
      "Route": [
        "18 4 59 57 37 50 49 67 39 5",
        "17 36 77 73 66 13 80 53 65"
      ]
    },
    {
      "depot": "76",
      "Route": [
        "15 10 25 42 2 63 19 8 47 40",
        "9 44 34 16 58 38 21 33"
      ]
    },
    {
      "depot": "79",
      "Route": [
        "6 14 31 41 69 32 0 45",
        "78 26 1 62 46 20 71 55 30"
      ]
    },
    {
      "depot": "12",
      "Route": [
        "11 22 43 61 23 28 68 24 48",
        "75 35 74 7 52 60 27 51 54 56 3 64 29 72"
      ]
    }
  ]
}

# NEAREST NEIGHBOR

As the initial solution construction/generation strategy, we will create a giant tour with the Nearest Neighbor (NN) algorithm and divide this tour by the number of routes.
Equal amount of cities in each route. Of course, increasing cities will be added to the last route.
This is an alternative to the random initial solution to generate the mentioned initial solution.
After this step Hill Climbing will be run on it.
And the results will be compared. initial solution Starting with Random or NN Does Hill Climbing provide an improvement at the end of iterations?

It is necessary to enter a starting city for NN. This must be parametric. Generally, the city in the center is used.
For example, Kayseri. The most central cities can be found by calculating the sum of a city's distance from all other cities.

For example, when the starting point of a 2 by 4 problem is entered in Ankara, a giant route is created with NN and divided into (81-4)/(4*2)=9.8=9 city slices,
the initial solution we get is as follows.The final route includes more than 9 cities. Now we add the cities to the final route.
So that there is no idle city. Of course, the program should be able to work with different values such as 1.1 1.x.
Entering 1.1 will be plain TSP, entering 1.x will be multi-TSP, and entering y.x will be multi-depo-multi-tsp.
