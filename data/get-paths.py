#!/usr/bin/python
# -*- coding: utf-8 -*-

import urllib
import re
import sys
import json
from BeautifulSoup import BeautifulSoup

indexpage = urllib.urlopen("http://webwatch.cityofmadison.com/webwatch/map.aspx").read();
indexsoup = BeautifulSoup(indexpage);
route_names = []

for opt in indexsoup.findAll("option"):
    name = opt["value"]
    if name != "Select a route":
        route_names.append(name)
        print name

routes = {}

def float2fixed(val):
    return int(round(float(val) * 1E6))

for route in route_names:
    print "fetching route %s..." % route
    parms = urllib.urlopen("http://webwatch.cityofmadison.com/webwatch/Scripts/Route%0s_trace.js" % route).read()

    polylines = []
    
    # http://webwatch.cityofmadison.com/webwatch/Scripts/LoadMap.js
    parameters = parms.split("*");
    #unit = parameters[0]
    legs = parameters[1].split("|")
    # penParms = parameters[2].split(",")
    # latLons = legs[0].split(";")
    # coordinates = latLongs[0].split(" ")
    
    for i in range(0, len(legs)-1):
        leg = legs[i]

        points = leg.split(";")
        
        coords = points[0].split(" ")
        prev_lat = float2fixed(coords[1])
        prev_lng = float2fixed(coords[0])
        
        for j in range(1, len(points)):
            point = points[j]
            if point == "":
                continue
            coords = point.split(" ")
            lat = float2fixed(coords[1])
            lng = float2fixed(coords[0])
            
            polylines.append([prev_lat, prev_lng, lat, lng])
            
            prev_lat = lat
            prev_lng = lng

    routes[route] = polylines

print >>open('paths.json', 'w'), json.dumps(routes, sort_keys=True, indent=4)

