all: route_points.bin

route_points.bin: CreateRoutes.class
	java CreateRoutes
	
CreateRoutes.class: CreateRoutes.java
	javac CreateRoutes.java
	
clean:
	rm -f route_points.bin