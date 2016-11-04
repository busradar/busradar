#!/usr/bin/python
# -*- coding: utf-8 -*-

import json

pathsdb = json.load(open('paths.json'))
pathdsdb_out = {}

for route in pathsdb:
    print "route", route
    polylines = []
    
    for line in pathsdb[route]:
        
        found = False
        for polyline in polylines:
            if line[2] == polyline[0][0] and line[3] == polyline[0][1]:
                polyline.insert(0, [line[0], line[1]])
                found = True
            elif polyline[len(polyline)-1][0] == line[0] and polyline[len(polyline)-1][1] == line[1]:
                polyline.append([line[2], line[3]])
                found = True
        
        if not found:
            polylines.append([[line[0], line[1]], [line[2], line[3]]])
    
    pathdsdb_out[route] = polylines
    print "  len(lines)     =", len(pathsdb[route])
    print "  len(polylines) =", len(polylines)

print >>open("paths-joined.json", "w"), json.dumps(pathdsdb_out, sort_keys=True, indent=4)
