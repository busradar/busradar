all: routes.bin

routes.bin: CreatePaths.class  ../combined-stops.json ../paths.json
	java -cp ../commons-io-2.0.1.jar:. CreatePaths
	
CreatePaths.class: CreatePaths.java
	javac -cp ../commons-io-2.0.1.jar:. CreatePaths.java
	
TestRoutes.class: TestRoutes.java
	javac -cp ../commons-io-2.0.1.jar:. TestRoutes.java

clean:
	rm -f *.bin *.class busradar/madison/*.class