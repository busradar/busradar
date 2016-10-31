#!/usr/bin/env python
# -*- coding: utf-8 -*-

import urllib
import urllib2
import re
import sys
import json
import BeautifulSoup

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

routes = json.load(open("routes.json"))
stopsdb = {}

for route in routes:
    routeid = route['id']
    routename = route['name']
    print 'route', routename
    
    stops = rpc(
        'http://webwatch.cityofmadison.com/tmwebwatch/GoogleMap.aspx/getStops', {
            'routeID': routeid
        })['d']

    stopsdb[routeid] = stops
        
    
		
print >>open('stops.json', 'w'), json.dumps(stopsdb, sort_keys=True, indent=4)
