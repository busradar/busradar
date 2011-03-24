#!/usr/bin/env python
import urllib2
import re
import json
import sys

data=	'.s=&'+\
		'FormState=Valid&'+\
		'StartGeo=Stop%2C%3B-89405837%3B43072117&'+\
		'EndGeo=Stop%2C%3B-89475418%3B43053811'+\
		'&Start='+\
		'&SelectStartType=0'+\
		'&End='+\
		'&SelectEndType=0&Date=05-13-2010&Time=+5%3A05&Meridiem=p&TripDirection=DEP&SortBy=1&.a=iTripPlanning'
print urllib2.urlopen('http://trip.cityofmadison.com/', data).read()
