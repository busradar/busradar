#/usr/bin/python
import csv
import sys
import json

stops_csv_data = csv.DictReader(open("gtf/stops.txt"))
mobiletracker_data = json.load(open("mobiletracker-combined.json"));

stops_dict = {};

for row in stops_csv_data:
    dic = {}
    stop_id = row['stop_id']
    
    if row["direction"] == "northbound":
        dic['dir'] = 'N'
    elif row["direction"] == "eastbound":
        dic['dir'] = 'E'
    elif row["direction"] == "southbound":
        dic['dir'] = 'S'
    elif row["direction"] == "westbound":
        dic['dir'] = 'W'
    elif row["direction"] == '':
        dic['dir'] = '?'
    else:
        raise Exception( "Uknown direction: '%s'" % row["direction"] );
        
    

    dic['lat'] = row['stop_lat']
    dic['lon'] = row['stop_lon']
    dic['name'] = row['stop_name']
    
    if stop_id in mobiletracker_data:
        dic['routes'] = mobiletracker_data[ stop_id ]
    else:
        print "mobile tracker data not found for", stop_id
        dic['routes'] = {}
    
    
    
    stops_dict[stop_id] = dic;

print >>open('combined-stops.json', 'w'), json.dumps(stops_dict, sort_keys=True, indent=4)
    

