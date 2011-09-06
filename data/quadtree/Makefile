stops.bin: CreateQuadTree.class ../combined-stops.json
	java -cp ../commons-io-2.0.1.jar:. CreateQuadTree
	
CreateQuadTree.class: CreateQuadTree.java
	javac -cp ../commons-io-2.0.1.jar:. CreateQuadTree.java
	
clean:
	rm -f *.class stops.bin busradar/madison/*.class

