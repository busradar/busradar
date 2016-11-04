#!/usr/bin/python
# -*- coding: utf-8 -*-

import json

pathsdb = json.load(open('paths-joined.json'))
routes = json.load(open('routes.json'))
paths_out = []

def get_route_by_index(i):
    for route in routes:
        if route['index'] == i:
            return route
    raise Exception('route not found')

for i in xrange(len(routes)):
    route = get_route_by_index(i)
    paths = pathsdb[route['name']]
    paths_out.append(paths)
    
print '''package busradar.madison;

public class RouteData {
    static int[][][] polylines = new int[][][] {'''

for routei in xrange(len(paths_out)):
    print ' '*8 + 'new int[][] {'

    polylines = paths_out[routei]
    for polylinei in xrange(len(polylines)):
        print ' '*12 + 'new int[] {'
        polyline = polylines[polylinei]
        for pointi in xrange(len(polyline)):
            point = polyline[pointi]
            print ' '*16 + str(point[0]) + ','
            if pointi != len(polyline) - 1:
                print ' '*16 + str(point[1]) + ','
            else:
                print ' '*16 + str(point[1])
    
        if polylinei != len(polylines) -1:
            print ' '*12 + '},'
        else:
            print ' '*12 + '}'

    if routei != len(paths_out) - 1:
        print ' '*8 + '},'
    else:
        print ' '*8 + '}'

print ' '*4 + '};'
print '}'

