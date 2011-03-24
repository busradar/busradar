#!/usr/bin/env python
# -*- coding: utf-8 -*-

import json
import sys
import re

mapdata_wk = json.load(open("map-weekday.json"))
mapdata_hl = json.load(open("map-holiday.json"))
stopsdata = json.load(open("stops.json"))
mbdata_week = json.load(open("mobiletracker-combined.json"))
#mbdata_holidays = json.load(open("mobiletracker-weekendsandholidays.json"))

def find_in_map(sid, stopname2):
	sid = int(sid)
	for route in mapdata_wk:
		for stopname in mapdata_wk[route]:
			if ("[ID#%04d]"%sid) in stopname:
				#print "found", stopname
				return mapdata_wk[route][stopname]
				
	for route in mapdata_hl:
		for stopname in mapdata_hl[route]:
			if ("[ID#%04d]"%sid) in stopname:
				#print "found", stopname
				return mapdata_hl[route][stopname]
	
	xstr = ("[ID#%04d]"%sid)
	#print "not found %s" % xstr
	#print sid, stopname2

	stopname = re.search('\\((.*)\\)', stopname2).group(1).replace("&amp;", "&").replace(' and ', '&')
	
	for route in mapdata_wk:
		for s in mapdata_wk[route]:
			if s.lower().replace('&amp;', '&').replace(' and ', '&') == stopname.lower():
				#print "found", stopname
				return mapdata_wk[route][s]
				
				
	for route in mapdata_hl:
		for s in mapdata_hl[route]:
			if s.lower().replace('&amp;', '&').replace(' and ', '&') == stopname.lower():
				#print "found", stopname
				return mapdata_hl[route][s]
				
	print "not found for", stopname
	

def get_data(stopid, mbdata):
	out = {}
	
	if 'stopname' not in mbdata[stopid][0]:
		print "stopname missing for stop ", stopid
		
	result = find_in_map(stopid, mbdata[stopid][0]['stopname'])
	
	out['routes'] = mbdata[stopid]
	out['lat'] = result['lat']
	out['lon'] = result['lon']
	
	if stopid in stopsdata:
		#if "lat" in stopsdata[stopid]:
			#out[stopid]['lat'] = stopsdata[stopid]['lat']
			#out[stopid]['lon'] = stopsdata[stopid]['lon']
			
		out['dir'] = stopsdata[stopid]['dir'][0]	
			
		name = "%s %s & %s" % ( stopsdata[stopid]['houseno'],
								stopsdata[stopid]['addr1'].title(),
								stopsdata[stopid]['addr2'].title())
		out['name'] = name
		
	else:
		name = mbdata[stopid][0]['stopname'].replace('&amp;', '&').title()
		name = re.match('([^\\[]*)', name).group(1).strip()
		
		out['name'] = name
		out['dir'] = '?'
		
	return out


combined_stops = {}

for stopid in mbdata_week:
	combined_stops[stopid] = get_data(stopid, mbdata_week)
	
		
#for stopid in mbdata_holidays:
#	if stopid not in combined_stops:
#		print stopid
#		combined_stops[stopid] = get_data(stopid, mbdata_holidays)
#	else:
#		if len(combined_stops[stopid]['routes']) != len(mbdata_week[stopid]):
#			print "mismatch"

print >>open('combined-stops.json', 'w'), json.dumps(combined_stops, sort_keys=True, indent=4)
