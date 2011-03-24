#!/usr/bin/env python
# -*- coding: utf-8 -*-

import json

stopdb = json.load(open("stops.json"))

routedb = {}


for stop in stopdb:
	for r in stopdb[stop]['routes']:
		route = int(r)
		
		if route not in routedb:
			routedb[route] = {}
		
		routedb[route][int(stop)] = {
			'name': stopdb[stop]['addr1'] + " " + stopdb[stop]['addr2']
		}
		
print json.dumps(routedb, sort_keys=True, indent=4)