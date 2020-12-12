# Algorithm_II_Princeton
## Week7 Reflection: WordNet (100/100)

* DataStructure to store the synsets id and noun is essential for both method operation and time efficiency:
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

## Week8 Reflection: Seam Carving (101/100) 
* Data Structure: 
  - The only data struture that we deal with is the energy array. Here, I picked an 1d array instead of 2d one to do all the operation. For an MxN array, it saves memory arround 24*M bytes. 
* Thought of solution:
  - Finding Shortest path in a tological ordered DAG is all about relaxing the edge and add up the minimum distance. If we map the same concept to an 2d grid, what we need to do is to add up the minimum distances of reachable neighboring grids. This concept can be realized by dynamic programming.
* Errors when testing:
  - Time testing-Test 1: Transpose is recommended in the Q&A session. However, I found out that due to my previous implementation(transpose the Picture), I cannot save the number of transposes if I do not know the sequence and number of resize and find. Meawhile, even the picture is tranposed, it still need to wlak through the dynamic programming part. Therefore, I decided to rewrite the vertical part in horizontal ones. This really save memory and time that tranpose a picture takes.
 
* Further optimization can be done:
  - The energy update part is optimized by my own dynamic programming thought, however, the Q&A session also mentioned to only update the pixels after the seam(included). My implementation apparently does not support this kind of optimization.
  - There is still limiation of this algorithm, see the result part where the Temppeliaukion kirkko still get distorted with this algorithm. 

* Results:
<p align="middle">
  <img src="https://github.com/charlesfu4/Algorithm_II_Princeton/blob/master/Week08_SeamCarving/IMG_0894.JPG", height = 300px>
  <img src="https://github.com/charlesfu4/Algorithm_II_Princeton/blob/master/Week08_SeamCarving/output2.jpg", height = 300px>
</p>

<p align="middle">
  <img src="https://github.com/charlesfu4/Algorithm_II_Princeton/blob/master/Week08_SeamCarving/chameleon.png", height = 200px>
  <img src="https://github.com/charlesfu4/Algorithm_II_Princeton/blob/master/Week08_SeamCarving/output.jpg", height = 200px>
</p>


## Week9 Reflection: Baseball Elimination (100/100) 
* Data Structure: 
  - I firstly created Record comparable data type to store the records of each team, this is mainly because I believed that sorting the record beforehand by number of wins and then number of remaining games will help in trivial-elimination. However, this is questionable when records like `(w, l, r) = (0, 0, 63) ,(32, 31, 0), (31, 32 ,0)` appear. The sequence can be problemetic. Therefore, I then simply stored the records in seperated integer arraies and link the team name with the indices by HashMap.  

* Difficulties:
  - The trivial-eliminated teams should not be taken into consideration when examine if a team is non-trivial eliminated. However, the correponding vertices should still be included when checking other teams. Same thought is applicable to non-trivial eliminated teams. Do not drop them out when constructing the FlowNet. It was not clear and I spent one week in this pitfall. But afterward it is quite intuitive that if we remove this corresponding vertices, the impact of the team to the whole net work is not complete. 
  - To construct the FlowNet, I selected to store the remaining record excluding current examined team into new space and then walking through the indices. This is very complex but can be solved by doing some math in some simple examples.

* Errors when testing:
  - `against` should not throw IllegalArgumentException when two team are the same. It will return 0. 

## Week10 Reflection: Boggle (100/100)

* Data Structure: An initial trail by applying normal tries led to memory issue. Therefore, I decided to apply ternany trie set. This lead the score to 86 with low speed. The cause of the low speed is originated from the instance method for algs4 data structures of tries. They search for and retrieve specifc strings from the top root of the tries tree every time. However, in this application, prefix checking can be wisely designed by Node memorizing. In this way, every next prefix search does not need to start from the root. Which saves tremendous amount of time.


* Further Optimization:
  - A non recursive prefix search can be implemented to push the speed higher. Probably need to design a dynamic programming approach.

* Errors when testing:
  - Adjacent list of each character was not designde carefully to address 1d array. 
  - Compliation warning of the data structure chosen to save adjacent list. This problem can be solve by using ArrayList to store the Bag instead of casting Bag object array directly to primitive type array.


## Week11 Reflection: BurrowsWheeler (100/100)

* Move to front decode and encode can be optimized.
