#!/usr/bin/python
# -*- coding: utf-8 -*-

# Copyright (C) 2016 Aleksandr Dobkin.
# 
# This file is part of BusRadar <https:#github.com/orgs/busradar/>.
# 
# BusRadar is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 3 of the License, or
# (at your option) any later version.
# 
# BusRadar is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.

import json
import re
import urllib2

routes_json = json.load(open('routes.json'))

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

route_info = {}

for route in rpc('http://webwatch.cityofmadison.com/tmwebwatch/Arrivals.aspx/getRoutes')['d']:
    full_name = route['name']
    name = re.search('- \w+ ([a-zA-Z0-9_-]+)', full_name).group(1)
    id = route['id']
    route_info[name] = {
        'name': name,
        'id': id
    }
    
index = 0
for route in routes_json:
    name = route['name']
    if name in route_info:
        route['id'] = route_info[name]['id']
        
    if not route['inactive']:
        route['index'] = index
        index += 1
        
for route_name in route_info:
    found = False
    for json_route in routes_json:
        if json_route['name'] == route_name:
            found = True
            break
    if not found:
        raise Exception("Not found route %s" % route_name)

print >>open('routes.json', 'w'), json.dumps(routes_json, sort_keys=True, indent=4)