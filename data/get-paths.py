#!/usr/bin/python
# -*- coding: utf-8 -*-

import urllib2
import re
import sys
import json

routes = {}

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

def float2fixed(val):
    return int(round(float(val) * 1E6))


for route in rpc('http://webwatch.cityofmadison.com/tmwebwatch/Arrivals.aspx/getRoutes')['d']:
    full_name = route['name']
    print full_name
    name = re.search('- \w+ ([a-zA-Z0-9_-]+)', full_name).group(1)
    id = route['id']
    routes[id] = {
        'name': name
    }

for routeid in routes:
    route = routes[routeid]
    routename = route['name']
    print 'route', routename
    
    data = rpc('http://webwatch.cityofmadison.com/tmwebwatch/GoogleMap.aspx/getRouteTrace',
        {'routeID': routeid})
    
    polylines = []
    for polyline in data['d']['polylines']:
        lat0 = float2fixed(polyline[0]['lat'])
        lng0 = float2fixed(polyline[0]['lon'])
        lat1 = float2fixed(polyline[1]['lat'])
        lng1 = float2fixed(polyline[1]['lon'])
        polylines.append([lat0, lng0, lat1, lng1])
        
        i = 2
        while i < len(polyline):
            lat0 = lat1
            lng0 = lng1
            lat1 = float2fixed(polyline[i]['lat'])
            lng1 = float2fixed(polyline[i]['lon'])
            i += 1
            polylines.append([lat0, lng0, lat1, lng1])
    
    route['polylines'] = polylines

paths = {}
for routeid in routes:
    route = routes[routeid]
    paths[route['name']] = route['polylines']
print >>open('paths.json', 'w'), json.dumps(paths, sort_keys=True, indent=4)

