#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sqlite3
import json
import sys

#stops = json.load(open("stops-combined.json"))
mtracker = json.load(open("mobile-tracker.json"))
mtracker_wkend = json.load(open("mobile-tracker-saturday.json")) 
#stops_combined = json.load(open("stops_combined.json")) 

conn = sqlite3.connect("./db.sqlite")
c = conn.cursor()

#c.execute('CREATE TABLE "android_metadata" ("locale" TEXT DEFAULT \'en_US\')')
#c.execute('INSERT INTO "android_metadata" VALUES (\'en_US\')')


#c.execute("create table stops(stopid integer, name text)")
c.execute("create table routestops(stopid integer, route integer, dir text, url text)")

#for stop in stops:
	#if stops[stop]['dir'] == "WB":
		#dir = "W"
	#elif stops[stop]['dir'] == "EB":
		#dir = "E"
	#elif stops[stop]['dir'] == "SB":
		#dir = "S"
	#elif stops[stop]['dir'] == "NB":
		#dir = "N"
	#elif stops[stop]['dir'] == "":
		#dir = "?"
	#else:
		#raise Exception("unknown direction: "+stops[stop]['dir'])
		
	#c.execute("insert into routes values(?, ?, ?, ?, ?, ?)", \
		#(stop, stops[stop]['addr1'], stops[stop]['addr1'], dir, 
		#round(float(stops[stop]['lat'])*1E6), round(float(stops[stop]['lon'])*1E6 )))

#for stop in stops_combined:
#	name = stops_combined[stop]['name']
#	c.execute('INSERT INTO stops(stopid, name) VALUES(

for stop in mtracker:
	for routeinfo in mtracker[stop]:
		route = int(routeinfo['routeno'])
		dir = routeinfo['direction']
		url = routeinfo['url']
		
		#print stop, route, dir, url
		c.execute("insert into routestops(stopid, route, dir, url) values(?, ?, ?, ?)", \
			(stop, route, dir, url))

for stop in mtracker_wkend:
	for routeinfo in mtracker_wkend[stop]:
		if (stop in mtracker) and (routeinfo in mtracker[stop]):
			continue
	
		route = int(routeinfo['routeno'])
		dir = routeinfo['direction']
		url = routeinfo['url']
		
		#print stop, route, dir, url
		c.execute("insert into routestops(stopid, route, dir, url) values(?, ?, ?, ?)", \
			(stop, route, dir, url))
		

conn.commit()

c.close()
