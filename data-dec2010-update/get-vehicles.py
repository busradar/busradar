#!/usr/bin/python
# -*- coding: utf-8 -*-

import urllib
import re
import sys
import json

namelist = json.loads(open('names.json').read().replace("&amp;", "&"))

def print_stops(name, infostring, stopdict, route):
	#print "<<<<<<<<<<", name, ">>>>>>>>>>>>>>"
	stops = infostring.split(';')
	for stop in stops: #lat|lon|name|dir|time time time;  
		if stop == "":
			continue;
		
		parts = stop.split('|')
		lat		= parts[0]
		lon		= parts[1]
		name	= parts[2]
		dir		= parts[3]
		time	= parts[4]
		
		latlong = "%s:%s" % (lat, lon)
		
		
		stopidre = re.search('\\[ID#(.*)\\]', name)
		if stopidre == None:
			stopidre = re.search('\\[(.*)\\]', name)
		if stopidre == None and "School Day Only" in dir:
			stopid = -1
		elif stopidre == None:
			print >>sys.stderr, "no id:", name, "dir:", dir, "route:", route
			for tryname in namelist:
				if '&' in name:
					names = name.split('&')
					if names[0].strip().upper() in tryname.upper() and \
					   names[1].strip().upper() in tryname.upper() and \
					   "route="+str(route) in tryname and \
					   "dir="+dir in tryname:
					   
						stopid = int(re.search('ID#(\\d+)', tryname).group(1))
						print >>sys.stderr, tryname, "dir", dir
						break
				else:
					if name.strip().upper() in tryname.upper() and \
					   "route="+str(route) in tryname and \
					   "dir="+dir in tryname:
					   
						stopid = int(re.search('ID#(\\d+)', tryname).group(1))
						print >>sys.stderr, tryname, "dir:", dir
						break
						
			print >>sys.stderr, stopid
			print >>sys.stderr, ""
		else:
			stopid = int(stopidre.group(1))
		
		item = {}
		item['lat'] = lat
		item['lon'] = lon
		item['name'] = name
		#item['dir'] = dir
		
		if (stopid in stopdict and stopdict[stopid] != item):
			print >> sys.stderr, "already contains id %d: old <%s> new <%s>" % (stopid, stopdict[stopid], item)
		
		stopdict[stopid] = item
		
	
		#print "lat=%s lon=%s name=%s dir=%s time=%s" % (lat, lon, name, dir, time)
		#print

def print_vehicles(infostring):
	#print "<<<<<<<<<< Vehicle >>>>>>>>>>>>>>"
	vehicles = infostring.split(';')
	for vehicle in vehicles:   
		if vehicle == "":
			continue;
		
		parts = vehicle.split('|')
		#print parts
		lat		= parts[0]
		lon		= parts[1]
		vehNumber	= parts[2]
		content		= parts[3]
	
		print "lat=%s lon=%s vehNumber=%s content=%s" % (lat, lon, vehNumber, content)
		print


def get_stops(route, stopdict):
	infostring = urllib.urlopen('http://webwatch.cityofmadison.com/webwatch/UpdateWebMap.aspx?u=%02d' % route).read()
	
	parts = infostring.split('*')
	#RouteAbbr = parts[0]
	#RefreshRate = parts[1]
	#StopDisplayTypeDesc = parts[2]
	
	#print >> sys.stderr, "RouteAbbr:", RouteAbbr
	#print >> sys.stderr, "RefreshRate:", RefreshRate
	#print >> sys.stderr, "StopDisplayTypeDesc:", StopDisplayTypeDesc
	
	MajorStops = parts[1]
	#print_stops('MajorStops', MajorStops, stopdict, route)
	
	Vehicles = parts[2]
	
	
	if len(parts) > 3:
		MinorStops = parts[3]
		#print_stops('MinorStops', MinorStops, stopdict, route)
	else:
		print >> sys.stderr, "no minor stops for this route"
	
	#print Vehicles
	print_vehicles(Vehicles)
	
	
	#print stopdict

stopdict = {}
get_stops(6, stopdict)

print >> sys.stderr
#print >> sys.stderr, stopdict
print >> sys.stderr, len(stopdict)

print json.dumps(stopdict, sort_keys=True, indent=4)

