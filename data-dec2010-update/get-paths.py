#!/usr/bin/python
# -*- coding: utf-8 -*-

import urllib
import re
import sys
import json

routes = {}
for route in range(0, 100):
	parms= urllib.urlopen("http://webwatch.cityofmadison.com/webwatch/Scripts/Route%02d_trace.js" % route).read()

	legslist = []

	try:
		legs = parms.split("*")[1].split("|")
	except:
		continue
	del legs[-1]
	for leg in legs:
		ends = leg.split(";")

		lat0 = ends[0].split(' ')[1];
		lon0 = ends[0].split(' ')[0];
	
		lat1 = ends[1].split(' ')[1];
		lon1 = ends[1].split(' ')[0];
	
		new = [int(round(float(lat0)*1E6)), int(round(float(lon0)*1E6)), int(round(float(lat1)*1E6)), int(round(float(lon1)*1E6))]
		if new[1] > new[3]:
			tmp1 = new[0]
			tmp2 = new[1]
			new[0] = new[2]
			new[1] = new[3]
			new[2] = tmp1
			new[3] = tmp2
		
		legslist.append(new)
	

	legslist.sort(key=lambda x: x[1])	
	print >>sys.stderr, route
	routes[route] = legslist
	
print >>sys.stderr, "done"
print >>open('paths.json', 'w'), json.dumps(routes, sort_keys=True, indent=4)

