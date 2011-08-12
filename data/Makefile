all: quadtree/stops.bin paths/route_points.bin db.sqlite

quadtree/stops.bin: combined-stops.json
	make -C quadtree

paths/route_points.bin: combined-stops.json paths.json
	make -C paths
	
paths.json:
	python get-paths.py

db.sqlite: combined-stops.json
	python make-sqlite.py

combined-stops.json: mobiletracker-combined.json
	python stops-from-gtf.py

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
