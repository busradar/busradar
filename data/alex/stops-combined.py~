#!/usr/bin/env python
# -*- coding: utf-8 -*-

import json
import sys

#sat_stops = json.load(open("saturday-stops.json"))
#sun_stops = json.load(open("sunday-stops.json"))
#wk_stops = json.load(open("weekday-stops.json"))
stops = json.load(open("saturday-stops.json"))
stopdir = json.load(open("stops.json"))

count = 0
maxlen = 0

dellist = []

for stop in stops:
	stops[stop]['dir'] = stopdir[stop]['dir']
	stops[stop]['routes'] = stopdir[stop]['routes']

print json.dumps(stopdir, sort_keys=True, indent=4)
print >>sys.stderr, "total:", len(stops)
