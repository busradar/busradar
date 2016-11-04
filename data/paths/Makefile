all: routes.bin

routes.bin: CreateRoutes.class  ../combined-stops.json ../paths.json
	java -cp ../commons-io-2.0.1.jar:. CreateRoutes
	
CreateRoutes.class: CreateRoutes.java
	javac -cp ../commons-io-2.0.1.jar:. CreateRoutes.java
	
TestRoutes.class: TestRoutes.java
	javac -cp ../commons-io-2.0.1.jar:. TestRoutes.java

clean:
	rm -f *.bin *.class busradar/madison/*.class