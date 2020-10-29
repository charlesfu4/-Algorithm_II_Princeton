# Algorithm_II_Princeton
## Week1 Reflection: WordNet (100/100)

* DataStructer to store the synsets id and noun is essential for both method operation and time efficiency:
  - First, I tried SymbolTable which implements TreeMap to store `<id, SET<noun>>`. This data structure will encounter problems in distance method, since we have to loop through every entry to find the nouns. Therefore, I reversed two elements as `<noun, SET<id>>`. The problem was solved.
  - Second, for the method ancestor, the required return variable is noun instead of integer. Therefore, the data structue above is inconvenient in returning the original string in the synset. I created a SymbolTable data structure to store `<id, nouns>` to solve this problem.
  - Third, I realize though SymbolTable provide logrithmic operation in several method. We do not need comparison-based operation on the data structure storing the synsets at all. Therefore, I decided to replace SymbolTable with LinearProbingHashST and SET to HashSet to push forward the time efficiency to constant.

* Errors when testing:
  + Test 17: check length() & ancestor() with zero-length iterable argument.
    - I accidentally classified this error into IllegalArgumentEception, simply altering it to return -1 can solve the problem.
  + Test 8: check constructor when input is not a rooted DAG
    - Check rooted: Count the number of hypernym and number of synset id. If #synsetid - #hypernym > 1. Then it means there will be more than one root in the Digraph.
    - Check acylic: Make use of `DirectedCycle` class in algs4 to check wheather the Digraph is acylic.
  + Test 2: count number of SAP operations when constructing a WordNet object and calling distance() and sap() three times each
    - I realize that for every distance and ancestor method in WordNet, I created a new SAP constructor for the hypernym graph. Set it to instance variable and assign it after testing rooted DAG can solve the problem.
 
* Further optimization can be done:
  - A better SAP implementation in finding same ancestor 
  - Software Cache to accelerate between calling distance and ancestor

## Week2 Reflection: 
