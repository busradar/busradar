#!/usr/bin/evn python
# -*- coding: utf-8 -*-

import json
import sys

stops = json.load(open("../stops-combined.json"))

for stop in stops:
	print "\t\tt.add(new Point(%d, %d));" % (round(float(stops[stop]['lat'])*1E6), round(float(stops[stop]['lon'])*1E6))