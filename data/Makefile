all: quadtree/stops.bin paths/route_points.bin db.sqlite config.java

quadtree/stops.bin: combined-stops.json UPDATE
	make -C quadtree

paths/route_points.bin: combined-stops.json paths.json UPDATE
	make -C paths
	
paths.json: UPDATE
	python get-paths.py

db.sqlite: combined-stops.json make-sqlite.py UPDATE
	python make-sqlite.py

combined-stops.json: mobiletracker-combined.json gtf UPDATE
	python stops-from-gtf.py

mobiletracker-weekdays.json: UPDATE
	python get-mobiletracker.py

mobiletracker-weekendsandholidays.json: UPDATE
	python get-mobiletracker.py
	
mobiletracker-combined.json: mobiletracker-weekdays.json mobiletracker-weekendsandholidays.json UPDATE
	python combine-mobiletracker.py

mmt_gtfs.zip: UPDATE
	curl http://www.cityofmadison.com/metro/gtfs/mmt_gtfs.zip > mmt_gtfs.zip

config.java: config.java.tmpl UPDATE
	cpp -P -DDATABASE_VERSION=`stat --format=%Y UPDATE` config.java.tmpl > config.java
	
gtf: mmt_gtfs.zip UPDATE
	rm -rf gtf
	unzip -d gtf mmt_gtfs.zip

clean:
	make -C quadtree clean
	make -C paths clean
	rm -f combined-stops.json mobiletracker-weekendsandholidays.json \
		mobiletracker-combined.json  \
		paths.json mobiletracker-weekdays.json config.java
	rm -f db.sqlite
	rm -rf gtf
	rm -f mmt_gtfs.zip
