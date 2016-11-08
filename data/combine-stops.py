#!/usr/bin/env python
# -*- coding: utf-8 -*-

import json
import sys
import re
import urllib2
import csv

#mapdata_wk = json.load(open("map-weekday.json"))
#mapdata_hl = json.load(open("map-holiday.json"))
#stopsdata = json.load(open("stops.json"))
#mobiletracker_data = json.load(open("mobiletracker-combined.json"))
#mbdata_holidays = json.load(open("mobiletracker-weekendsandholidays.json"))
stopsdb = json.load(open('stops.json'))

stops = {}

def setval(id, prop, value):
    if id not in stops:
        stops[id] = {}
        stops[id]['routes'] = []
        
    d = stops[id]
    
    if prop not in d:
        d[prop] = value
    elif d[prop] != value:
        print '%s property mismatch: %s != %s' % (prop, d[prop], value)

for routeid in stopsdb:
    for stop in stopsdb[routeid]:
        stopname = stop['stopName']
        stopid = stop['stopID']
        lat = stop['lat']
        lon = stop['lon']
        dir = '?'
        stopno = '?'
        
        match = re.search(r'\[([NESW])B#', stopname)
        if match:
            dir = match.group(1)
        match = re.search(r'\[[a-zA-Z#]*(\d+)', stopname)
        if match:
            stopno = match.group(1)
            
        setval(stopid, 'lat', lat)
        setval(stopid, 'lon', lon)
        
        if 'name' not in stops[stopid] or stopno != '?':
                stops[stopid]['name'] = stopname
                stops[stopid]['dir'] = dir
                stops[stopid]['stopno'] = stopno
        
        route = {
            'direction': stop['directionName'],
            'routeid': int(routeid),
            'url': '?r=%s&d=%d&s=%d' % (routeid, stop['directionID'], stopid)
        }
        
        stops[stopid]['routes'].append(route)

mmt_stops = {}
for row in csv.DictReader(open('MMT_WebWatch_Crawldata_Truncated.txt')):
    stopid = int(row['(s=)'])
    dir = row['DIR']
    stopname = row['(Stop name) TEXT']
    stopno = row['(Name) #STOPID']
    mmt_stops[stopid] = {
        'name': stopname,
        'dir': dir,
        'stopno': stopno
    }

missing_stops = []

for stopid in stops:
    stop = stops[stopid]
    if stop['dir'] == '?':
        if stopid not in mmt_stops:
            s = '%s (id=%s)' % (stop['name'], stopid)
            if s not in missing_stops: missing_stops.append(s)
        else:
            stopname = mmt_stops[stopid]['name']
            dir = '?'
            match = re.search(r'\[([NESW])B#', stopname)
            if match:
                dir = match.group(1)
            stop['dir'] = dir
            stop['stopno'] = mmt_stops[stopid]['stopno']
            stop['name'] = mmt_stops[stopid]['name']


print 'total number of stops:', len(stops)
print 'missing stops:'
for s in missing_stops:
    print s

print >>open('combined-stops.json', 'w'), json.dumps(stops, sort_keys=True, indent=4)
