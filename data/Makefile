all: quadtree/stops.bin paths/route_points.bin db.sqlite

quadtree/stops.bin: combined-stops.json
	make -C quadtree

paths/route_points.bin: combined-stops.json paths.json
	make -C paths
	
paths.json:
	python get-paths.py

db.sqlite: combined-stops.json make-sqlite.py
	python make-sqlite.py

combined-stops.json: mobiletracker-combined.json gtf
	python stops-from-gtf.py

mobiletracker-weekdays.json:
	python get-mobiletracker.py

mobiletracker-weekendsandholidays.json:
	python get-mobiletracker.py
	
mobiletracker-combined.json: mobiletracker-weekdays.json mobiletracker-weekendsandholidays.json
	python combine-mobiletracker.py

mmt_gtfs.zip:
	wget http://www.cityofmadison.com/metro/gtfs/mmt_gtfs.zip 

gtf: mmt_gtfs.zip
	unzip -d gtf mmt_gtfs.zip

clean:
	make -C quadtree clean
	make -C paths clean
	rm -f *.json
	rm -f db.sqlite
	rm -rf gtf
