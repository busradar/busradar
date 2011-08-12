#!/usr/bin/env python
# -*- coding: utf-8 -*-

import urllib
import urllib2
import re
import sys
import json
import BeautifulSoup

def simplify(s):
	return " ".join(s.split())

db = {}

page_number = 1
url = 'http://www.cityofmadison.com/metro/BusStopDepartures/number/%d.cfm'

numre = re.compile("\\d+")
gpsre = re.compile("cbll=(-?\\d+\\.\\d*),(-?\\d+\\.\\d*)")


while True:
	try:
		html = urllib2.urlopen(url % page_number)
	except Exception as e:
		print >>sys.stderr, "Done on page %d with exception %s"%(page_number, e)
		break
		
	if html.url == 'http://www.cityofmadison.com/siteRedirect.cfm?redirect=/metro/':
		print 'done'
		break
		
	print >>sys.stderr, "On page %d" % page_number
	page_number += 1
		
	#html = open("tmp.html").read();
	page = BeautifulSoup.BeautifulSoup(html)
	
	
	td = page.find(name="td", text="StopID")
	tr = td.findParent("tr")
	tr = tr.findNextSibling("tr") # skip past first td which is empty

	for tr in tr.findNextSiblings("tr"):
		tds = tr.findChildren("td")
		
		if tds[0].text == "&nbsp;":
			break;
		
		stopid 		= int(tds[0].text)
		direction 	= simplify(tds[1].text)
		houseno		= simplify(tds[2].text)
		addr1 		= simplify(tds[3].text)
		addr2 		= simplify(tds[4].text)
		
		havegps = False
		
		print >>sys.stderr, "<%s><%s><%s><%s><%s> %d" % \
				(stopid, direction, houseno, addr1, addr2, page_number)
		
		if tds[6].a and re.search("N/A$", tds[6].a['href']) == None:
			gpsurl = tds[6].a['href']
			havegps = True
			
			lat = gpsre.search(gpsurl).group(1)
			lng = gpsre.search(gpsurl).group(2)
			
		db[stopid] = { 
		   'dir': direction,
		   'houseno': houseno,
		   'addr1': addr1,
		   'addr2': addr2,
		}
		
		if havegps:
			db[stopid]['lat'] = lat
			db[stopid]['lon'] = lng

		
print >>open('stops.json', 'w'), json.dumps(db, sort_keys=True, indent=4)
print >>sys.stderr, "count", len(db)
