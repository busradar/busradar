#!/usr/bin/python
# -*- coding: utf-8 -*-

# this script downloads data from the mobile tracker
# this data includes the url for each (stop, route) pair
#
# This script must be run separately on weekdays and again on 
# weekends/holiday because the website contains different data
# depending on the day

import urllib
import re
import json
import sys
from BeautifulSoup import BeautifulSoup
import datetime


html = urllib.urlopen('http://webwatch.cityofmadison.com/webwatch/mobileada.aspx').read()
soup = BeautifulSoup(html)

stops = {}
namedb = []

for a in soup.findAll('a'):
	if 'Route ' in a.string:
		routeno = re.search('Route ([a-zA-Z0-9_-]+)', a.string).group(1)
		print "route", routeno
		# for each rote
		
		html = urllib.urlopen('http://webwatch.cityofmadison.com/webwatch/mobileada.aspx'+re.search('(\\?r=.*)', a['href']).group(1)).read()
		
		for dirs in re.findall('\\"MobileAda.aspx(\\?r=.*)\\">(.*?)<', html):
			dirurl = dirs[0]
			dirstr = dirs[1]
			
			html = urllib.urlopen('http://webwatch.cityofmadison.com/webwatch/mobileada.aspx'+dirurl).read()
			
			for stop in re.findall('\\"MobileAda.aspx\\?(r=.*)\\">(.*?)<', html):
				url = stop[0]
				stopname = stop[1]
				
				try:
					stopid = re.search('\\[(.)B#(\\w+)\\]', stop[1]).group(2)
				except Exception as e:
					print >>sys.stderr, "stopname:", stopname, "routeno:", routeno, "url:", url, "exception", e 
					continue
				 
				if stopid not in stops:
					stops[stopid] = []
				
				
				stops[stopid].append({
					'routeno': routeno,
					'direction': dirstr,
					'url': url,
					'stopname': stopname
				})
				
				namedb.append(stopname + " route="+str(routeno) + " dir=" +dirstr)
				
			#break;
	#break	

addname = "-weekdays"
if datetime.date.today().strftime('%A') == 'Saturday' or \
   datetime.date.today().strftime('%A') == 'Sunday':
	addname = '-weekendsandholidays'
	

print >>open("mobiletracker"+addname+".json", "w"), json.dumps(stops, sort_keys=True, indent=4)
#print >>open("names"+addname+".json", "w"), json.dumps(namedb, sort_keys=True, indent=4)
