#!/usr/bin/env python
# -*- coding: utf-8 -*-

import urllib
import re
import sys
import json
import BeautifulSoup

db = {}

url = 'http://www.cityofmadison.com/metro/BusStopDepartures/%04d-%04d.html'
numre = re.compile("\\d+")
gpsre = re.compile("cbll=(-?\\d+\\.\\d*),(-?\\d+\\.\\d*)")


for ran in [[5, 877],
		    [883, 1695],
			[1704, 2325],
			[2326, 3196],
			[3200, 5173],
			[5178, 6318],
			[6319, 6910],
			[6915, 7764],
			[7766, 8919],
			[8920, 9993]]:

	html = urllib.urlopen(url % (ran[0], ran[1])).read()
	#html = open("html").read();
	page = BeautifulSoup.BeautifulSoup(html)
	
	#for tr in page.findAll("tr"):
	td = page.find(name="td", text="StopID")
	tr = td.findParent("tr")
	#print type(tr)
	for tr in tr.findNextSiblings("tr"):
		stoptd = tr.td.find(text=numre)
		stop = int(stoptd)
		stoptd = stoptd if isinstance(stoptd, BeautifulSoup.Tag)and stoptd.name == "tr" else stoptd.findParent("td")
		
		dirtd = stoptd.findNextSibling("td")
		dir = str(dirtd.text)
		
		housenotd = dirtd.findNextSibling("td")
		houseno = str(housenotd.text)
		
		addr1td = housenotd.findNextSibling("td")
		addr1 = str(addr1td.text)
		
		addr2td = addr1td.findNextSibling("td")
		addr2 = str(addr2td.text)
		
		routestd = addr2td.findNextSibling("td")
		routesstr = str(routestd.text)
		
		pdftd = routestd.findNextSibling("td")
		
		gpstd = pdftd.findNextSibling("td")
		gpsa = gpstd.findChild("a")
		havegps = False
		if gpsa != None:
			havegps = True
			gpsurl = gpsa['href']
			gpsmatch = gpsre.search(gpsurl)
		
			lat = gpsre.search(gpsurl).group(1)
			lon = gpsre.search(gpsurl).group(2)
		
			#print lat, lon
		
		print >>sys.stderr, "<%s><%s><%s><%s><%s><%s>" % (stop, dir, houseno, addr1, addr2, routesstr)
		
		routes = []
		for r in routesstr.split(","):
			try:
				routes.append(int(r))
			except Exception as e:
				print >>sys.stderr, e
		
		
		db[stop] = { 'dir': dir,
		   'houseno': houseno,
		   'addr1': addr1,
		   'addr2': addr2,
		   'routes': routes
		}
		
		if havegps:
			db[stop]['lat'] = lat
			db[stop]['lon'] = lon
	

print json.dumps(db, sort_keys=True, indent=4)
print >>sys.stderr, "count", len(db)