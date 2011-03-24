#!/usr/bin/env python
# -*- coding: utf-8 -*-

import json
import sys

sat_stops = json.load(open("saturday-stops.json"))
#sun_stops = json.load(open("sunday-stops.json"))
wk_stops = json.load(open("weekday-stops.json"))

stopdir = json.load(open("stops.json"))

count = 0
maxlen = 0

dellist = []

for stop in stopdir:
	
	if stop in wk_stops.keys():
		lat = wk_stops[stop]['lat']
		lon = wk_stops[stop]['lon']
		name = wk_stops[stop]['name']
	elif stop in sat_stops.keys():
		lat = sat_stops[stop]['lat']
		lon = sat_stops[stop]['lon']
		name = sat_stops[stop]['name']
	#elif stop in sun_stops.keys():
	#	lat = sun_stops[stop]['lat']
	#	lon = sun_stops[stop]['lon']
	#	name = sun_stops[stop]['name']
	else:
		if 'lat' not in stopdir[stop]:
			dellist.append(stop)
			print >>sys.stderr, "no gps for stop:", stop
			count += 1
		
		continue
		
	stopdir[stop]['lat'] = lat
	stopdir[stop]['lon'] = lon
	stopdir[stop]['name'] = name
	
for stop in dellist:
	del stopdir[stop]

for stop in wk_stops:
	maxlen = maxlen if maxlen >= len(wk_stops[stop]['name']) else len(wk_stops[stop]['name'])
	if stop not in stopdir:
		print >>sys.stderr, "not in main stops list", stop
		stopdir[stop] = wk_stops[stop]
		stopdir[stop]['addr1'] = wk_stops[stop]['name']
		stopdir[stop]['addr2'] = ""
		stopdir[stop]['dir'] = "?"

print json.dumps(stopdir, sort_keys=True, indent=4)
print >>sys.stderr, "missing gps for", count
print >>sys.stderr, "total:", len(stopdir)
print >>sys.stderr, "maxlen:", maxlen
