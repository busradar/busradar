#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sqlite3
import json
import sys
import os

stops = json.load(open("combined-stops.json"))

try:
	os.remove("./db.sqlite")
except Exception as e:
	pass
	
conn = sqlite3.connect("./db.sqlite")
c = conn.cursor()

c.execute("CREATE TABLE android_metadata (locale TEXT DEFAULT 'en_US')")
c.execute("INSERT INTO android_metadata VALUES ('en_US')")

c.execute("CREATE TABLE db_version (version INT)")
c.execute("INSERT INTO db_version VALUES (3)")

c.execute("create table routestops(stopid integer, route integer, url text)")
c.execute("CREATE TABLE Stop (_ID INTEGER PRIMARY KEY, Name TEXT)")


for stopid in stops:
	c.execute("INSERT INTO Stop(_ID, Name) values (?, ?)", \
			(stopid, stops[stopid]['name']))
			
	for route in stops[stopid]['routes']:
		c.execute("insert into routestops(stopid, route, url) values(?, ?, ?)", \
				(stopid, \
				 route['routeno'], \
				 route['url']))
		
		
conn.commit()

c.close()
