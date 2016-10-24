#!/usr/bin/env python
# -*- coding: utf-8 -*-

import json
import sys
import re
import urllib2

#mapdata_wk = json.load(open("map-weekday.json"))
#mapdata_hl = json.load(open("map-holiday.json"))
#stopsdata = json.load(open("stops.json"))
mobiletracker_data = json.load(open("mobiletracker-combined.json"))
#mbdata_holidays = json.load(open("mobiletracker-weekendsandholidays.json"))

stops = {}

def rpc(url, data=None):
    print 'rpc', url
    headers = {
        'Content-Type': 'application/json; charset=utf-8'
    }
    if data is None:
        data = ''
    else:
        data = json.dumps(data)
    req = urllib2.Request(url, data=data, headers=headers)
    result = urllib2.urlopen(req).read();
    return json.loads(result)

for route in rpc('http://webwatch.cityofmadison.com/tmwebwatch/Arrivals.aspx/getRoutes')['d']:
    full_name = route['name']
    print full_name
    name = re.search('- \w+ ([a-zA-Z0-9_-]+)', full_name).group(1)
    routeid = route['id']
    
    for stop in rpc(
        'http://webwatch.cityofmadison.com/tmwebwatch/GoogleMap.aspx/getStops', {
            'routeID': routeid
        })['d']:
        id = stop['stopID']
        lat = stop['lat']
        lon = stop['lon']
        name = stop['stopName']
        dir = '?'
        
        match = re.search(r'\[([NESW])B#', name)
        if match:
            dir = match.group(1)
        
        strid = str(id)
        if strid in mobiletracker_data:
            routes = mobiletracker_data[strid]
        else:
            routes = {}
            print "mobile tracker data not found for", id, name
            sys.exit(1)
        
        stops[id] = {
            'lat': lat,
            'lon': lon,
            'name': name,
            'dir': dir,
            'routes': routes
        }



print >>open('combined-stops.json', 'w'), json.dumps(stops, sort_keys=True, indent=4)
