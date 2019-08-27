#!/usr/bin/python
# -*- coding: utf-8 -*-

# this scri2pt downloads data from the mobile tracker
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

URL = 'http://webwatch.cityofmadison.com/tmwebwatch/LiveADAArrivalTimes'
html = urllib.urlopen(URL).read()
soup = BeautifulSoup(html)

stops = {}
namedb = []

for a in BeautifulSoup(html).findAll('a', {'class': 'adalink'}):
    res = re.search('- \w+ ([a-zA-Z0-9_-]+)', a.string)
    if not res:
        continue
    routename = res.group(1)
    url = re.search(r'\?.*', a['href']).group(0)
    routeid = re.search(r'r=(\d+)', url).group(1)
    print "routename:", routename, "routeid:", routeid

    url = URL + "?r=%s" % routeid
    html = urllib.urlopen(url).read()
    for a in BeautifulSoup(html).findAll('a', {'class': 'adalink'}):
        url = re.search(r'\?.*', a['href']).group(0)
        dirid = re.search(r'd=(\d+)', url).group(1)
        dirname = a.string
        
        url = URL + '?r=%s&d=%s' % (routeid, dirid)
        print '  dirname:', dirname, 'dirid:', dirid
        html = urllib.urlopen(url).read()
        for a in BeautifulSoup(html).findAll('a', {'class': 'adalink'}):
            if 'href' not in a.attrMap:
                print "skipping", a
                continue
            #print "A", a 
            url = re.search(r'\?.*', a['href']).group(0)
            stopname = a.string
            stopid = re.search(r's=(\d+)', url).group(1)
            print '    url:', url, 'stopname:', stopname, 'stopid:', stopid                
            if stopid not in stops:
                stops[stopid] = []
            
            
            stops[stopid].append({
                'routeno': routename,
                'direction': dirname,
                'url': url,
                'stopname': stopname
            })

addname = "-weekdays"
if datetime.date.today().strftime('%A') == 'Saturday' or \
   datetime.date.today().strftime('%A') == 'Sunday':
    addname = '-weekendsandholidays'

print >>open("mobiletracker"+addname+".json", "w"), json.dumps(stops, sort_keys=True, indent=4)
