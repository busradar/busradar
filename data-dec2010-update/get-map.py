#!/usr/bin/python
# -*- coding: utf-8 -*-

import urllib
import re
import sys
import json
import datetime

#namelist = json.loads(open('names-saturday.json').read().replace("&amp;", "&"))

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
		
		#print lat
		#print lon
		#print name
		#print dir
		#print time
		
		#print
		#print name
		#stopidre = re.search('\\[ID#(.*)\\]', name)
		#if stopidre == None:
			#stopidre = re.search('\\[(.*)\\]', name)
		#if stopidre == None and "School Day Only" in dir:
			#stopid = -1
		#elif stopidre == None:
			#print >>sys.stderr, "no id:", name, "dir:", dir, "route:", route
			#for tryname in namelist:
				#if '&' in name:
					#names = name.split('&')
					#if names[0].strip().upper() in tryname.upper() and \
					   #names[1].strip().upper() in tryname.upper() and \
					   #"route="+str(route) in tryname and \
					   #"dir="+dir in tryname:
					   
						#stopid = int(re.search('ID#(\\d+)', tryname).group(1))
						#print >>sys.stderr, tryname, "dir", dir
						#break
				#else:
					#if name.strip().upper() in tryname.upper() and \
					   #"route="+str(route) in tryname and \
					   #"dir="+dir in tryname:
					   
						#stopid = int(re.search('ID#(\\d+)', tryname).group(1))
						#print >>sys.stderr, tryname, "dir:", dir
						#break
						
			#print >>sys.stderr, stopid
			#print >>sys.stderr, ""
		#else:
			#stopid = int(stopidre.group(1))
		
		item = {}
		item['lat'] = lat
		item['lon'] = lon
		item['dir'] = dir
		item['time'] = time
		
		#if (stopid in stopdict and stopdict[stopid] != item):
			#print >> sys.stderr, "already contains id %d: old <%s> new <%s>" % (stopid, stopdict[stopid], item)
		
		stopdict[name] = item
		
	
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
	
		#print "lat=%s lon=%s vehNumber=%s content=%s" % (lat, lon, vehNumber, content)
		#print


def get_stops(route):
	stopdict = {}
	html = urllib.urlopen('http://webwatch.cityofmadison.com/webwatch/map.aspx?mode=g', '__EVENTTARGET=ddlMaps&__EVENTARGUMENT=&__LASTFOCUS=&__VIEWSTATE=/wEPDwUKMTk2NjU5NjE5Nw9kFgICAQ9kFgQCAQ8QZBAVQg5TZWxlY3QgYSByb3V0ZQwwMSAtIFJvdXRlIDEMMDIgLSBSb3V0ZSAyDDAzIC0gUm91dGUgMwwwNCAtIFJvdXRlIDQMMDUgLSBSb3V0ZSA1DDA2IC0gUm91dGUgNgwwNyAtIFJvdXRlIDcMMDggLSBSb3V0ZSA4DDA5IC0gUm91dGUgOQ0xMCAtIFJvdXRlIDEwDTExIC0gUm91dGUgMTENMTIgLSBSb3V0ZSAxMg0xMyAtIFJvdXRlIDEzDTE0IC0gUm91dGUgMTQNMTUgLSBSb3V0ZSAxNQ0xNiAtIFJvdXRlIDE2DTE3IC0gUm91dGUgMTcNMTggLSBSb3V0ZSAxOA0xOSAtIFJvdXRlIDE5DTIwIC0gUm91dGUgMjANMjEgLSBSb3V0ZSAyMQ0yMiAtIFJvdXRlIDIyDTI1IC0gUm91dGUgMjUNMjYgLSBSb3V0ZSAyNg0yNyAtIFJvdXRlIDI3DTI4IC0gUm91dGUgMjgNMjkgLSBSb3V0ZSAyOQ0zMCAtIFJvdXRlIDMwDTMyIC0gUm91dGUgMzINMzMgLSBSb3V0ZSAzMw0zNCAtIFJvdXRlIDM0DTM2IC0gUm91dGUgMzYNMzcgLSBSb3V0ZSAzNw0zOCAtIFJvdXRlIDM4DTM5IC0gUm91dGUgMzkNNDAgLSBSb3V0ZSA0MA00NCAtIFJvdXRlIDQ0DTQ3IC0gUm91dGUgNDcNNDggLSBSb3V0ZSA0OA01MCAtIFJvdXRlIDUwDTUxIC0gUm91dGUgNTENNTIgLSBSb3V0ZSA1Mg01NSAtIFJvdXRlIDU1DTU2IC0gUm91dGUgNTYNNTcgLSBSb3V0ZSA1Nw01OCAtIFJvdXRlIDU4DTU5IC0gUm91dGUgNTkNNjMgLSBSb3V0ZSA2Mw02NyAtIFJvdXRlIDY3DTY4IC0gUm91dGUgNjgNNzAgLSBSb3V0ZSA3MA03MSAtIFJvdXRlIDcxDTcyIC0gUm91dGUgNzINNzMgLSBSb3V0ZSA3Mw03NCAtIFJvdXRlIDc0DTc4IC0gUm91dGUgNzgNODAgLSBSb3V0ZSA4MA04MSAtIFJvdXRlIDgxDTgyIC0gUm91dGUgODINODQgLSBSb3V0ZSA4NA04NSAtIFJvdXRlIDg1DTkwIC0gUm91dGUgOTANOTEgLSBSb3V0ZSA5MQ05MiAtIFJvdXRlIDkyDTkzIC0gUm91dGUgOTMVQg5TZWxlY3QgYSByb3V0ZQIwMQIwMgIwMwIwNAIwNQIwNgIwNwIwOAIwOQIxMAIxMQIxMgIxMwIxNAIxNQIxNgIxNwIxOAIxOQIyMAIyMQIyMgIyNQIyNgIyNwIyOAIyOQIzMAIzMgIzMwIzNAIzNgIzNwIzOAIzOQI0MAI0NAI0NwI0OAI1MAI1MQI1MgI1NQI1NgI1NwI1OAI1OQI2MwI2NwI2OAI3MAI3MQI3MgI3MwI3NAI3OAI4MAI4MQI4MgI4NAI4NQI5MAI5MQI5MgI5MxQrA0JnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2cWAWZkAgUPDxYCHgRUZXh0BVhEb3dubG9hZGluZyB0aGUgbWFwIG1heSB0YWtlIGEgZmV3IG1pbnV0ZXMsIGRlcGVuZGluZyBvbiB0aGUgc2l6ZSBhbmQgZGV0YWlsIG9mIHRoZSBtYXAuZGRkrXDUWMqxtytbuSOdxapGClN0FRI=&__EVENTVALIDATION=%2FwEWRAKasqUnAtK%2F8%2BUHApzxw%2FQEAsLQlYgLAsLQkYgLAsLQrYgLAsLQqYgLAsLQpYgLAsLQoYgLAsLQvYgLAsLQ%2BYsLAsLQ9YsLAt3QmYgLAt3QlYgLAt3QkYgLAt3QrYgLAt3QqYgLAt3QpYgLAt3QoYgLAt3QvYgLAt3Q%2BYsLAt3Q9YsLAtzQmYgLAtzQlYgLAtzQkYgLAtzQpYgLAtzQoYgLAtzQvYgLAtzQ%2BYsLAtzQ9YsLAt%2FQmYgLAt%2FQkYgLAt%2FQrYgLAt%2FQqYgLAt%2FQoYgLAt%2FQvYgLAt%2FQ%2BYsLAt%2FQ9YsLAt7QmYgLAt7QqYgLAt7QvYgLAt7Q%2BYsLAtnQmYgLAtnQlYgLAtnQkYgLAtnQpYgLAtnQoYgLAtnQvYgLAtnQ%2BYsLAtnQ9YsLAtjQrYgLAtjQvYgLAtjQ%2BYsLAtvQmYgLAtvQlYgLAtvQkYgLAtvQrYgLAtvQqYgLAtvQ%2BYsLAsrQmYgLAsrQlYgLAsrQkYgLAsrQqYgLAsrQpYgLAsXQmYgLAsXQlYgLAsXQkYgLAsXQrYgLgz0BINRDVjnMA7p4K7lhCAH1paM%3D&ddlMaps='+('%02d'%route)+'&Vehicles=on&Major+Stops=on').read()
	
	#html = open('tmp.html').read()
	
	regexp = re.search('initInfoString="(.*)"', html)
	if regexp == None:
		print >> sys.stderr, "No stops found for route", route
		return stopdict
	
	infostring = regexp.group(1);
	
	parts = infostring.split('*')
	RouteAbbr = parts[0]
	RefreshRate = parts[1]
	StopDisplayTypeDesc = parts[2]
	
	print >> sys.stderr, "RouteAbbr:", RouteAbbr
	print >> sys.stderr, "RefreshRate:", RefreshRate
	print >> sys.stderr, "StopDisplayTypeDesc:", StopDisplayTypeDesc
	
	MajorStops = parts[4]
	print_stops('MajorStops', MajorStops, stopdict, route)
	
	Vehicles = parts[5]
	
	
	if len(parts) > 6:
		MinorStops = parts[6]
		print_stops('MinorStops', MinorStops, stopdict, route)
	else:
		print >> sys.stderr, "no minor stops for this route"
	
	print_vehicles(Vehicles)
	print >> sys.stderr, len(stopdict)
	return stopdict

routes = {}

for i in range(1, 100):
	stops = get_stops(i)	
	if len(stops) != 0:
		routes[i] = stops

print >> sys.stderr

addname = "-weekday"
if datetime.date.today().strftime('%A') == 'Saturday' or \
   datetime.date.today().strftime('%A') == 'Sunday':
	addname = '-holiday'
	

print >> open('map'+addname+'.json', 'w'), json.dumps(routes, sort_keys=True, indent=4)

