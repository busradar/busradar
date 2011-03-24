all: quadtree/stops.bin paths/route_points.bin db.sqlite

quadtree/stops.bin: combined-stops.json
	make -C quadtree

#stops-combined.json: saturday-stops.json stops.json
#	python stops-combined.py > stops-combined.json

paths/route_points.bin: combined-stops.json
	make -C paths
	
paths.json:
	python get-paths.py

db.sqlite: combined-stops.json
	python make-sqlite.py

combined-stops.json: map-weekday.json map-holiday.json stops.json mobiletracker-combined.json
	python combine-stops.py

map-weekday.json:
	python get-map.py

map-holiday.json:
	python get-map.py	

stops.json:
	python get-stops.py

mobiletracker-weekdays.json:
	python get-mobiletracker.py

mobiletracker-weekendsandholidays.json:
	python get-mobiletracker.py
	
mobiletracker-combined.json: mobiletracker-weekdays.json mobiletracker-weekendsandholidays.json
	python combine-mobiletracker.py

clean:
	make -C quadtree clean
	rm *.json
	rm db.sqlite
