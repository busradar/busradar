#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sqlite3
import json
import sys
import os

stops = json.load(open("combined-stops.json"))
route_info = json.load(open("routes.json"))

try:
	os.remove("./db.sqlite")
except Exception as e:
	pass
	
conn = sqlite3.connect("./db.sqlite")
c = conn.cursor()

c.execute("CREATE TABLE android_metadata (locale TEXT DEFAULT 'en_US')")
c.execute("INSERT INTO android_metadata VALUES ('en_US')")

c.execute("CREATE TABLE db_version (version INT)")
c.execute("INSERT INTO db_version VALUES (10)")

c.execute("CREATE TABLE routestops(stopid integer, route integer, url text)")
c.execute("CREATE TABLE Stop (_ID INTEGER PRIMARY KEY, Name TEXT)")


for stopid in stops:
        stopid_int = int(stopid)
        c.execute("INSERT INTO Stop(_ID, Name) values (?, ?)", \
                        (stopid_int, stops[stopid]['name']))
                        
        for route in stops[stopid]['routes']:
                route_name = route['routeno']
                
                id = 0;
                found = False
                
                for rinfo in route_info:
                    if rinfo['name'] == route_name:
                        found = True
                        break
                    id += 1
                    
                if not found:
                    raise "Not found"
                
            
                c.execute("insert into routestops(stopid, route, url) values(?, ?, ?)", \
                                (stopid, \
                                    id, \
                                    route['url']))


conn.commit()

c.close()
