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
	html = urllib.urlopen('http://webwatch.cityofmadison.com/webwatch/map.aspx?mode=g', '__EVENTTARGET=ddlMaps&__EVENTARGUMENT=&__LASTFOCUS=&__VIEWSTATE=%2FwEPDwUKMTk2NjU5NjE5Nw9kFgICAQ9kFgYCAQ8QZBAVQg5TZWxlY3QgYSByb3V0ZQwwMSAtIFJvdXRlIDEMMDIgLSBSb3V0ZSAyDDAzIC0gUm91dGUgMwwwNCAtIFJvdXRlIDQMMDUgLSBSb3V0ZSA1DDA2IC0gUm91dGUgNgwwNyAtIFJvdXRlIDcMMDggLSBSb3V0ZSA4DDA5IC0gUm91dGUgOQ0xMCAtIFJvdXRlIDEwDTExIC0gUm91dGUgMTENMTIgLSBSb3V0ZSAxMg0xMyAtIFJvdXRlIDEzDTE0IC0gUm91dGUgMTQNMTUgLSBSb3V0ZSAxNQ0xNiAtIFJvdXRlIDE2DTE3IC0gUm91dGUgMTcNMTggLSBSb3V0ZSAxOA0xOSAtIFJvdXRlIDE5DTIwIC0gUm91dGUgMjANMjEgLSBSb3V0ZSAyMQ0yMiAtIFJvdXRlIDIyDTI1IC0gUm91dGUgMjUNMjYgLSBSb3V0ZSAyNg0yNyAtIFJvdXRlIDI3DTI4IC0gUm91dGUgMjgNMjkgLSBSb3V0ZSAyOQ0zMCAtIFJvdXRlIDMwDTMyIC0gUm91dGUgMzINMzMgLSBSb3V0ZSAzMw0zNCAtIFJvdXRlIDM0DTM2IC0gUm91dGUgMzYNMzcgLSBSb3V0ZSAzNw0zOCAtIFJvdXRlIDM4DTM5IC0gUm91dGUgMzkNNDAgLSBSb3V0ZSA0MA00NCAtIFJvdXRlIDQ0DTQ3IC0gUm91dGUgNDcNNDggLSBSb3V0ZSA0OA01MCAtIFJvdXRlIDUwDTUxIC0gUm91dGUgNTENNTIgLSBSb3V0ZSA1Mg01NSAtIFJvdXRlIDU1DTU2IC0gUm91dGUgNTYNNTcgLSBSb3V0ZSA1Nw01OCAtIFJvdXRlIDU4DTU5IC0gUm91dGUgNTkNNjMgLSBSb3V0ZSA2Mw02NyAtIFJvdXRlIDY3DTY4IC0gUm91dGUgNjgNNzAgLSBSb3V0ZSA3MA03MSAtIFJvdXRlIDcxDTcyIC0gUm91dGUgNzINNzMgLSBSb3V0ZSA3Mw03NCAtIFJvdXRlIDc0DTc4IC0gUm91dGUgNzgNODAgLSBSb3V0ZSA4MA04MSAtIFJvdXRlIDgxDTgyIC0gUm91dGUgODINODQgLSBSb3V0ZSA4NA04NSAtIFJvdXRlIDg1DTkwIC0gUm91dGUgOTANOTEgLSBSb3V0ZSA5MQ05MiAtIFJvdXRlIDkyDTkzIC0gUm91dGUgOTMVQg5TZWxlY3QgYSByb3V0ZQIwMQIwMgIwMwIwNAIwNQIwNgIwNwIwOAIwOQIxMAIxMQIxMgIxMwIxNAIxNQIxNgIxNwIxOAIxOQIyMAIyMQIyMgIyNQIyNgIyNwIyOAIyOQIzMAIzMgIzMwIzNAIzNgIzNwIzOAIzOQI0MAI0NAI0NwI0OAI1MAI1MQI1MgI1NQI1NgI1NwI1OAI1OQI2MwI2NwI2OAI3MAI3MQI3MgI3MwI3NAI3OAI4MAI4MQI4MgI4NAI4NQI5MAI5MQI5MgI5MxQrA0JnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2cWAQI5ZAIFDw8WAh4EVGV4dAVYRG93bmxvYWRpbmcgdGhlIG1hcCBtYXkgdGFrZSBhIGZldyBtaW51dGVzLCBkZXBlbmRpbmcgb24gdGhlIHNpemUgYW5kIGRldGFpbCBvZiB0aGUgbWFwLmRkAgcPFgIfAAW7RjwhRE9DVFlQRSBodG1sIFBVQkxJQyAiLS8vVzNDLy9EVEQgWEhUTUwgMS4wIFN0cmljdC8vRU4iICAgIA0KICAgICAiaHR0cDovL3d3dy53My5vcmcvVFIveGh0bWwxL0RURC94aHRtbDEtc3RyaWN0LmR0ZCI%2BDQo8aHRtbCB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMTk5OS94aHRtbCIgeG1sbnM6dj0idXJuOnNjaGVtYXMtbWljcm9zb2Z0LWNvbTp2bWwiPg0KICAgPGhlYWQ%2BICAgICAgDQogICAgICA8bWV0YSBodHRwLWVxdWl2PSJDb250ZW50LVR5cGUiIGNvbnRlbnQ9InRleHQvaHRtbDsgY2hhcnNldD11dGYtOCI%2BDQogICAgICA8c3R5bGUgdHlwZT0idGV4dC9jc3MiPiAgICANCiAgICB2XDoqIHsgICAgIA0KICAgICAgICBiZWhhdmlvcjp1cmwoI2RlZmF1bHQjVk1MKTsgDQogICAgICAgICB9ICAgIA0KICAgICA8L3N0eWxlPiAgICAgDQogICAgICA8c2NyaXB0IHNyYz0iaHR0cDovL21hcHMuZ29vZ2xlLmNvbS9tYXBzP2ZpbGU9YXBpJmFtcDt2PTImYW1wO3NlbnNvcj1mYWxzZSZhbXA7a2V5PUFCUUlBQUFBNHlIcWpJNHRLN3haaUQ5VEtQQ291UlNETDBtSWtDVUFFd1dXbGZYc2dJRkM4LVMydmhUZnBSV2otVnZPbzItV2drc3FBUE94THZSZWRnIiB0eXBlPSJ0ZXh0L2phdmFzY3JpcHQiPjwvc2NyaXB0Pg0KICAgICAgPHNjcmlwdCBzcmM9IlNjcmlwdHMvTG9hZE1hcC5qcyI%2BPC9zY3JpcHQ%2BDQogICAgICA8c2NyaXB0IHNyYz0iU2NyaXB0cy9Hb29nbGVNYXBGdW5jcy5qcyI%2BPC9zY3JpcHQ%2BDQogICAgICA8c2NyaXB0IHNyYz0iU2NyaXB0cy9Sb3V0ZTgwX3RyYWNlLmpzIj48L3NjcmlwdD4NCiAgICAgIDxzY3JpcHQ%2BdmFyIGluaXRJbmZvU3RyaW5nPSI4MCozMDAwMCpEZXBhcnR1cmUocykqNS8yOS8yMDExIDk6MTY6MzkgUE0qNDMuMDc2MTQ5MXwtODkuNDAwNjkxNXxNZW1vcmlhbCBVbmlvbnxFYWdsZSBIdHN8OTo0MCBQTSAgVE8gRUFHTEUgSFQ6VVcgSE9TUDxicj4xMDoyNSBQTSAgVE8gRUFHTEUgSFQ6VVcgSE9TUDxicj4xMToxMCBQTSAgVE8gRUFHTEUgSFQ6VVcgSE9TUDxicj58Mjk7NDMuMDc2NDg0fC04OS40MTYxOTl8T2JzZXJ2YXRvcnkgJiBFbG18RWFnbGUgSHRzfDk6NDYgUE0gIFRPIEVBR0xFIEhUOlVXIEhPU1A8YnI%2BMTA6MzEgUE0gIFRPIEVBR0xFIEhUOlVXIEhPU1A8YnI%2BMTE6MTYgUE0gIFRPIEVBR0xFIEhUOlVXIEhPU1A8YnI%2BfDI5OzQzLjA4MDI4ODd8LTg5LjQyODEzNTZ8VW5pdmVyc2l0eSBCYXkgTG90IDYwIE9CfEVhZ2xlIEh0c3w5OjUxIFBNICBUTyBFQUdMRSBIVDpVVyBIT1NQPGJyPjEwOjM2IFBNICBUTyBFQUdMRSBIVDpVVyBIT1NQPGJyPjExOjIxIFBNICBUTyBFQUdMRSBIVDpVVyBIT1NQPGJyPnwyOTs0My4wODYzMjQ4fC04OS40Mzg1OTgzfEVhZ2xlIEhlaWdodHMgU2hlbHRlcnxFYWdsZSBIdHN8fDI5OzQzLjA4NjMyNDh8LTg5LjQzODU5ODN8RWFnbGUgSGVpZ2h0cyBTaGVsdGVyfFVXIFVuaW9ufDk6NTYgUE0gIFRPIFVXIFVOSU9OOlVXIEhPU1A8YnI%2BMTA6NDEgUE0gIFRPIFVXIFVOSU9OOlVXIEhPU1A8YnI%2BMTE6MjYgUE0gIFRPIFVXIFVOSU9OOlVXIEhPU1A8YnI%2BfDI5OzQzLjA3ODk2ODZ8LTg5LjQyOTI2Nzh8TWFyc2ggJiBMb3QgNzZ8VVcgVW5pb258MTA6MDUgUE0gIFRPIFVXIFVOSU9OOlVXIEhPU1A8YnI%2BMTA6NTAgUE0gIFRPIFVXIFVOSU9OOlVXIEhPU1A8YnI%2BMTE6MzUgUE0gIFRPIFVXIFVOSU9OOlVXIEhPU1A8YnI%2BfDI5OzQzLjA3NjQwODZ8LTg5LjQxNjIxODZ8T2JzZXJ2YXRvcnkgJiBFbG18VVcgVW5pb258MTA6MTAgUE0gIFRPIFVXIFVOSU9OOlVXIEhPU1A8YnI%2BMTA6NTUgUE0gIFRPIFVXIFVOSU9OOlVXIEhPU1A8YnI%2BMTE6NDAgUE0gIFRPIFVXIFVOSU9OOlVXIEhPU1A8YnI%2BfDI5OzQzLjA3NjE0OTF8LTg5LjQwMDY5MTV8TWVtb3JpYWwgVW5pb258VVcgVW5pb258fDI5Oyo0My4wNzY0NjUzfC04OS40MTUzMzM0fDN8PGI%2BVVcgVW5pb248L2I%2BPGJyPlZlaGljbGUgTm8uOiAwMTM8YnI%2BTmV4dCBUaW1lcG9pbnQ6IE1lbW9yaWFsIFVuaW9uOyo0My4wODYyNTcyfC04OS40MzQ4Nzc3fEVBR0xFIEhUUyAmIExPVCBFIFtJRCMyMDE4XXxFYWdsZSBIdHN8OTo1NCBQTSAgVE8gRUFHTEUgSFQ6VVcgSE9TUDxicj4xMDozOSBQTSAgVE8gRUFHTEUgSFQ6VVcgSE9TUDxicj4xMToyNCBQTSAgVE8gRUFHTEUgSFQ6VVcgSE9TUDxicj58Mjk7NDMuMDg2MTkwNnwtODkuNDM2NDM0M3xFQUdMRSBIVFMgJiBMT1QgRiBbSUQjMjAyNl18RWFnbGUgSHRzfDk6NTUgUE0gIFRPIEVBR0xFIEhUOlVXIEhPU1A8YnI%2BMTA6NDAgUE0gIFRPIEVBR0xFIEhUOlVXIEhPU1A8YnI%2BMTE6MjUgUE0gIFRPIEVBR0xFIEhUOlVXIEhPU1A8YnI%2BfDI5OzQzLjA3NzQ5NjZ8LTg5LjQyODk3ODN8SElHSExBTkQgJiBPQlNFUlZBVE9SWSBbSUQjMjEyNV18RWFnbGUgSHRzfDk6NDkgUE0gIFRPIEVBR0xFIEhUOlVXIEhPU1A8YnI%2BMTA6MzQgUE0gIFRPIEVBR0xFIEhUOlVXIEhPU1A8YnI%2BMTE6MTkgUE0gIFRPIEVBR0xFIEhUOlVXIEhPU1A8YnI%2BfDI5OzQzLjA4NDI3NDZ8LTg5LjQzMzU2Nzl8TEFLRSBNRU5ET1RBICYgVSBCQVkgW0lEIzIwNDFdfEVhZ2xlIEh0c3w5OjUzIFBNICBUTyBFQUdMRSBIVDpVVyBIT1NQPGJyPjEwOjM4IFBNICBUTyBFQUdMRSBIVDpVVyBIT1NQPGJyPjExOjIzIFBNICBUTyBFQUdMRSBIVDpVVyBIT1NQPGJyPnwyOTs0My4wNzUyMDF8LTg5LjQxMjMwOXxMSU5ERU4gJiBCQUJDT0NLIFtJRCMwNTMyXXxFYWdsZSBIdHN8OTo0NCBQTSAgVE8gRUFHTEUgSFQ6VVcgSE9TUDxicj4xMDoyOSBQTSAgVE8gRUFHTEUgSFQ6VVcgSE9TUDxicj4xMToxNCBQTSAgVE8gRUFHTEUgSFQ6VVcgSE9TUDxicj58Mjk7NDMuMDc1MTQ0NnwtODkuNDA3NTQwOHxMSU5ERU4gJiBDSEFSVEVSIFtJRCMwNDg4XXxFYWdsZSBIdHN8OTo0MiBQTSAgVE8gRUFHTEUgSFQ6VVcgSE9TUDxicj4xMDoyNyBQTSAgVE8gRUFHTEUgSFQ6VVcgSE9TUDxicj4xMToxMiBQTSAgVE8gRUFHTEUgSFQ6VVcgSE9TUDxicj58Mjk7NDMuMDc1MTgxfC04OS40MTA0MDV8TElOREVOICYgSEVOUlkgTUFMTCBbSUQjMDE4NF18RWFnbGUgSHRzfDk6NDMgUE0gIFRPIEVBR0xFIEhUOlVXIEhPU1A8YnI%2BMTA6MjggUE0gIFRPIEVBR0xFIEhUOlVXIEhPU1A8YnI%2BMTE6MTMgUE0gIFRPIEVBR0xFIEhUOlVXIEhPU1A8YnI%2BfDI5OzQzLjA3ODg0ODh8LTg5LjQyOTgxNjZ8TUFSU0ggJiBISUdITEFORCBbSUQjMjAyM118RWFnbGUgSHRzfDk6NTAgUE0gIFRPIEVBR0xFIEhUOlVXIEhPU1A8YnI%2BMTA6MzUgUE0gIFRPIEVBR0xFIEhUOlVXIEhPU1A8YnI%2BMTE6MjAgUE0gIFRPIEVBR0xFIEhUOlVXIEhPU1A8YnI%2BfDI5OzQzLjA3NjQ2M3wtODkuNDEyOTQ3fE9CU0VSVkFUT1JZICYgQkFCQ09DSyBbSUQjMjk5Nl18RWFnbGUgSHRzfDk6NDQgUE0gIFRPIEVBR0xFIEhUOlVXIEhPU1A8YnI%2BMTA6MjkgUE0gIFRPIEVBR0xFIEhUOlVXIEhPU1A8YnI%2BMTE6MTQgUE0gIFRPIEVBR0xFIEhUOlVXIEhPU1A8YnI%2BfDI5OzQzLjA3NTkxMzV8LTg5LjQwMzYxODV8T0JTRVJWQVRPUlkgJiBCQVNDT00gW0lEIzAwNjBdfEVhZ2xlIEh0c3w5OjQxIFBNICBUTyBFQUdMRSBIVDpVVyBIT1NQPGJyPjEwOjI2IFBNICBUTyBFQUdMRSBIVDpVVyBIT1NQPGJyPjExOjExIFBNICBUTyBFQUdMRSBIVDpVVyBIT1NQPGJyPnwyOTs0My4wNzYwNjk5fC04OS40MDU2MjUxfE9CU0VSVkFUT1JZICYgQ0hBUlRFUiBbSUQjMDA1Ml18RWFnbGUgSHRzfDk6NDEgUE0gIFRPIEVBR0xFIEhUOlVXIEhPU1A8YnI%2BMTA6MjYgUE0gIFRPIEVBR0xFIEhUOlVXIEhPU1A8YnI%2BMTE6MTEgUE0gIFRPIEVBR0xFIEhUOlVXIEhPU1A8YnI%2BfDI5OzQzLjA3NjU2OTZ8LTg5LjQyMDY3NTF8T0JTRVJWQVRPUlkgJiBOQVRBVE9SSVVNIFtJRCMyNDQyXXxFYWdsZSBIdHN8OTo0NiBQTSAgVE8gRUFHTEUgSFQ6VVcgSE9TUDxicj4xMDozMSBQTSAgVE8gRUFHTEUgSFQ6VVcgSE9TUDxicj4xMToxNiBQTSAgVE8gRUFHTEUgSFQ6VVcgSE9TUDxicj58Mjk7NDMuMDc2NzU4NnwtODkuNDI2MDU5MXxPQlNFUlZBVE9SWSAmIFdBTE5VVCBbSUQjMjA4OF18RWFnbGUgSHRzfDk6NDggUE0gIFRPIEVBR0xFIEhUOlVXIEhPU1A8YnI%2BMTA6MzMgUE0gIFRPIEVBR0xFIEhUOlVXIEhPU1A8YnI%2BMTE6MTggUE0gIFRPIEVBR0xFIEhUOlVXIEhPU1A8YnI%2BfDI5OzQzLjA4NDE5MTl8LTg5LjQyODgzNTF8VSBCQVkgJiBQSUNOSUMgUFQgW0lEIzI5MzhdfEVhZ2xlIEh0c3w5OjUyIFBNICBUTyBFQUdMRSBIVDpVVyBIT1NQPGJyPjEwOjM3IFBNICBUTyBFQUdMRSBIVDpVVyBIT1NQPGJyPjExOjIyIFBNICBUTyBFQUdMRSBIVDpVVyBIT1NQPGJyPnwyOTs0My4wNzU0NTN8LTg5LjQxMjcxNHxCQUJDT0NLICYgTElOREVOIFtJRCMwMjk4XXxVVyBVbmlvbnw5OjE2IFBNICBUTyBVVyBVTklPTjpVVyBIT1NQPGJyPjEwOjExIFBNICBUTyBVVyBVTklPTjpVVyBIT1NQPGJyPjEwOjU2IFBNICBUTyBVVyBVTklPTjpVVyBIT1NQPGJyPnwyOTs0My4wNzQwNzk4fC04OS40MDYyNTMyfENIQVJURVIgJiBVTklWIEFWRSBbSUQjMDcwNl18VVcgVW5pb258OToxOSBQTSAgVE8gVVcgVU5JT046VVcgSE9TUDxicj4xMDoxNCBQTSAgVE8gVVcgVU5JT046VVcgSE9TUDxicj4xMDo1OSBQTSAgVE8gVVcgVU5JT046VVcgSE9TUDxicj58Mjk7NDMuMDcyMzA0OHwtODkuNDA1ODIyfENIQVJURVIgJiBXIEpPSE5TT04gW0lEIzA0OTBdfFVXIFVuaW9ufDk6MjAgUE0gIFRPIFVXIFVOSU9OOlVXIEhPU1A8YnI%2BMTA6MTUgUE0gIFRPIFVXIFVOSU9OOlVXIEhPU1A8YnI%2BMTE6MDAgUE0gIFRPIFVXIFVOSU9OOlVXIEhPU1A8YnI%2BfDI5OzQzLjA4NjYzNjV8LTg5LjQ0MTc2MTJ8RUFHTEUgSFRTICYgTE9UIE0gW0lEIzIwNTRdfFVXIFVuaW9ufDk6NTggUE0gIFRPIFVXIFVOSU9OOlVXIEhPU1A8YnI%2BMTA6NDMgUE0gIFRPIFVXIFVOSU9OOlVXIEhPU1A8YnI%2BMTE6MjggUE0gIFRPIFVXIFVOSU9OOlVXIEhPU1A8YnI%2BfDI5OzQzLjA3ODM1ODl8LTg5LjQzMDA2NTh8SElHSExBTkQgJiBNQVJTSCBbSUQjMjM0OV18VVcgVW5pb258MTA6MDUgUE0gIFRPIFVXIFVOSU9OOlVXIEhPU1A8YnI%2BMTA6NTAgUE0gIFRPIFVXIFVOSU9OOlVXIEhPU1A8YnI%2BMTE6MzUgUE0gIFRPIFVXIFVOSU9OOlVXIEhPU1A8YnI%2BfDI5OzQzLjA4NjM0NzZ8LTg5LjQzMzcwNHxMQUtFIE1FTkRPVEEgJiBFQUdMRSBIVFMgW0lEIzIwMzRdfFVXIFVuaW9ufDEwOjAxIFBNICBUTyBVVyBVTklPTjpVVyBIT1NQPGJyPjEwOjQ2IFBNICBUTyBVVyBVTklPTjpVVyBIT1NQPGJyPjExOjMxIFBNICBUTyBVVyBVTklPTjpVVyBIT1NQPGJyPnwyOTs0My4wODkwODc0fC04OS40MzgyNjU1fExBS0UgTUVORE9UQSAmIExPVCBOIFtJRCMyMDM5XXxVVyBVbmlvbnw5OjU5IFBNICBUTyBVVyBVTklPTjpVVyBIT1NQPGJyPjEwOjQ0IFBNICBUTyBVVyBVTklPTjpVVyBIT1NQPGJyPjExOjI5IFBNICBUTyBVVyBVTklPTjpVVyBIT1NQPGJyPnwyOTs0My4wODk3MjEyfC04OS40MzczMjIzfExBS0UgTUVORE9UQSAmIExPVCBQIFtJRCMyMDYxXXxVVyBVbmlvbnwxMDowMCBQTSAgVE8gVVcgVU5JT046VVcgSE9TUDxicj4xMDo0NSBQTSAgVE8gVVcgVU5JT046VVcgSE9TUDxicj4xMTozMCBQTSAgVE8gVVcgVU5JT046VVcgSE9TUDxicj58Mjk7NDMuMDkwNTE4OHwtODkuNDM1NjY3OXxMQUtFIE1FTkRPVEEgJiBMT1QgUSBbSUQjMjA3MV18VVcgVW5pb258MTA6MDAgUE0gIFRPIFVXIFVOSU9OOlVXIEhPU1A8YnI%2BMTA6NDUgUE0gIFRPIFVXIFVOSU9OOlVXIEhPU1A8YnI%2BMTE6MzAgUE0gIFRPIFVXIFVOSU9OOlVXIEhPU1A8YnI%2BfDI5OzQzLjA4OTMzMzl8LTg5LjQzMzY5MzR8TEFLRSBNRU5ET1RBICYgTE9UIFIgW0lEIzIwMTRdfFVXIFVuaW9ufDEwOjAxIFBNICBUTyBVVyBVTklPTjpVVyBIT1NQPGJyPjEwOjQ2IFBNICBUTyBVVyBVTklPTjpVVyBIT1NQPGJyPjExOjMxIFBNICBUTyBVVyBVTklPTjpVVyBIT1NQPGJyPnwyOTs0My4wODQzMjgyfC04OS40MzM2NjN8TEFLRSBNRU5ET1RBICYgVSBCQVkgW0lEIzIwNDhdfFVXIFVuaW9ufDEwOjAyIFBNICBUTyBVVyBVTklPTjpVVyBIT1NQPGJyPjEwOjQ3IFBNICBUTyBVVyBVTklPTjpVVyBIT1NQPGJyPjExOjMyIFBNICBUTyBVVyBVTklPTjpVVyBIT1NQPGJyPnwyOTs0My4wNzUwNDY1fC04OS40MDY0Nzg1fExJTkRFTiAmIENIQVJURVIgW0lEIzA0MzhdfFVXIFVuaW9ufDk6MTggUE0gIFRPIFVXIFVOSU9OOlVXIEhPU1A8YnI%2BMTA6MTMgUE0gIFRPIFVXIFVOSU9OOlVXIEhPU1A8YnI%2BMTA6NTggUE0gIFRPIFVXIFVOSU9OOlVXIEhPU1A8YnI%2BfDI5OzQzLjA3NTE1NzF8LTg5LjQxMDM3NjR8TElOREVOICYgSEVOUlkgTUFMTCBbSUQjMDU3M118VVcgVW5pb258OToxNyBQTSAgVE8gVVcgVU5JT046VVcgSE9TUDxicj4xMDoxMiBQTSAgVE8gVVcgVU5JT046VVcgSE9TUDxicj4xMDo1NyBQTSAgVE8gVVcgVU5JT046VVcgSE9TUDxicj58Mjk7NDMuMDc0NTU3OHwtODkuNDAwNTI4NnxOIFBBUksgJiBTVEFURSBTVCBNQUxMIFtJRCMwNzk1XXxVVyBVbmlvbnw5OjIzIFBNICBUTyBVVyBVTklPTjpVVyBIT1NQPGJyPjEwOjE4IFBNICBUTyBVVyBVTklPTjpVVyBIT1NQPGJyPjExOjAzIFBNICBUTyBVVyBVTklPTjpVVyBIT1NQPGJyPnwyOTs0My4wNzMwNjg1fC04OS40MDA0ODE3fE4gUEFSSyAmIFVOSVYgQVZFIFtJRCMwMzQxXXxVVyBVbmlvbnw5OjIyIFBNICBUTyBVVyBVTklPTjpVVyBIT1NQPGJyPjEwOjE3IFBNICBUTyBVVyBVTklPTjpVVyBIT1NQPGJyPjExOjAyIFBNICBUTyBVVyBVTklPTjpVVyBIT1NQPGJyPnwyOTs0My4wNzY0MDl8LTg5LjQxMzAyM3xPQlNFUlZBVE9SWSAmIEJBQkNPQ0sgW0lEIzIxNDVdfFVXIFVuaW9ufDEwOjExIFBNICBUTyBVVyBVTklPTjpVVyBIT1NQPGJyPjEwOjU2IFBNICBUTyBVVyBVTklPTjpVVyBIT1NQPGJyPjExOjQxIFBNICBUTyBVVyBVTklPTjpVVyBIT1NQPGJyPnwyOTs0My4wNzcwMjk2fC04OS40MjgzMDA2fE9CU0VSVkFUT1JZICYgSElHSExBTkQgW0lEIzIwOTFdfFVXIFVuaW9ufDEwOjA2IFBNICBUTyBVVyBVTklPTjpVVyBIT1NQPGJyPjEwOjUxIFBNICBUTyBVVyBVTklPTjpVVyBIT1NQPGJyPjExOjM2IFBNICBUTyBVVyBVTklPTjpVVyBIT1NQPGJyPnwyOTs0My4wNzY0MzQxfC04OS40MjAzNjQzfE9CU0VSVkFUT1JZICYgTkFUQVRPUklVTSBbSUQjMjI2N118VVcgVW5pb258MTA6MDggUE0gIFRPIFVXIFVOSU9OOlVXIEhPU1A8YnI%2BMTA6NTMgUE0gIFRPIFVXIFVOSU9OOlVXIEhPU1A8YnI%2BMTE6MzggUE0gIFRPIFVXIFVOSU9OOlVXIEhPU1A8YnI%2BfDI5OzQzLjA3NjQ1NjZ8LTg5LjQyMzk3NjR8T0JTRVJWQVRPUlkgJiBXQUxOVVQgW0lEIzIwMDddfFVXIFVuaW9ufDEwOjA3IFBNICBUTyBVVyBVTklPTjpVVyBIT1NQPGJyPjEwOjUyIFBNICBUTyBVVyBVTklPTjpVVyBIT1NQPGJyPjExOjM3IFBNICBUTyBVVyBVTklPTjpVVyBIT1NQPGJyPnwyOTs0My4wODA4OTgzfC04OS40MjgyOTY2fFUgQkFZICYgTE9UIDc2IFtJRCMyMDUwXXxVVyBVbmlvbnwxMDowNCBQTSAgVE8gVVcgVU5JT046VVcgSE9TUDxicj4xMDo0OSBQTSAgVE8gVVcgVU5JT046VVcgSE9TUDxicj4xMTozNCBQTSAgVE8gVVcgVU5JT046VVcgSE9TUDxicj58Mjk7NDMuMDg0MjM5fC04OS40Mjg5NzE2fFUgQkFZICYgUElDTklDIFBUIFtJRCMyODgxXXxVVyBVbmlvbnwxMDowMyBQTSAgVE8gVVcgVU5JT046VVcgSE9TUDxicj4xMDo0OCBQTSAgVE8gVVcgVU5JT046VVcgSE9TUDxicj4xMTozMyBQTSAgVE8gVVcgVU5JT046VVcgSE9TUDxicj58Mjk7NDMuMDcyMTU4MnwtODkuNDA0MzExOHxXIEpPSE5TT04gJiBNSUxMUyBbSUQjMDc0MV18VVcgVW5pb258OToyMCBQTSAgVE8gVVcgVU5JT046VVcgSE9TUDxicj4xMDoxNSBQTSAgVE8gVVcgVU5JT046VVcgSE9TUDxicj4xMTowMCBQTSAgVE8gVVcgVU5JT046VVcgSE9TUDxicj58Mjk7Ijs8L3NjcmlwdD4gICAgICAgICAgIA0KICA8L2hlYWQ%2BDQogDQogICA8Ym9keSBvbmxvYWQ9IkxvYWQoKTsiPg0KICAgICAgIDx0YWJsZT4NCiAgICAgICAgICAgPHRyPg0KICAgICAgICAgICAgICAgPHRkPg0KICAgICAgICAgICAgICAgICAgPGRpdiBpZD0nbXlNYXAnIHN0eWxlPSJwb3NpdGlvbjpyZWxhdGl2ZTsgd2lkdGg6NjAwcHg7IGhlaWdodDo1MTBweDsiPjwvZGl2PiAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgIDxkaXYgSUQ9TXlUZXh0IEFMSUdOPSJjZW50ZXIiIHN0eWxlPSJmb250LXNpemUgOnNtYWxsZXIiID48L2Rpdj4NCiAgICAgICAgICAgICAgIDwvdGQ%2BDQogICAgICAgICAgICAgICA8dGQgIHZhbGlnbj0idG9wIj4NCiAgICAgICAgICAgICAgICAgIDxkaXYgaWQ9J2xheWVycyc%2BDQogICAgICAgICAgICAgICAgICA8aW5wdXQgaWQ9IkNoZWNrYm94MSIgdHlwZT0iY2hlY2tib3giIG5hbWU9IlZlaGljbGVzIiBvbmNsaWNrPSJ0aGlzLmNoZWNrZWQgPyBTaG93U2hhcGVMYXllcigyKSA6IEhpZGVTaGFwZUxheWVyKDIpOyJjaGVja2VkPSJjaGVja2VkIiAvPlNob3cgQnVzZXM8YnI%2BDQogICAgICAgICAgICAgICAgICA8aW5wdXQgaWQ9IkNoZWNrYm94MiIgdHlwZT0iY2hlY2tib3giIG5hbWU9Ik1ham9yIFN0b3BzIiBvbmNsaWNrPSJ0aGlzLmNoZWNrZWQgPyBTaG93U2hhcGVMYXllcigxKSA6IEhpZGVTaGFwZUxheWVyKDEpOyIgY2hlY2tlZD0iY2hlY2tlZCIgLz5TaG93IFRpbWUgUG9pbnRzPGJyPiAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgPGlucHV0IGlkPSJDaGVja2JveDMiIHR5cGU9ImNoZWNrYm94IiBuYW1lPSJNaW5vciBTdG9wcyIgb25jbGljaz0idGhpcy5jaGVja2VkID8gU2hvd1NoYXBlTGF5ZXIoMCkgOiBIaWRlU2hhcGVMYXllcigwKTsiIC8%2BU2hvdyBTdG9wcyAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgPC9kaXY%2BDQogICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgPC90ZD4gICAgICAgICAgICAgICANCiAgICAgICAgICAgPC90cj4NCiAgICAgICA8L3RhYmxlPg0KICAgIDwvYm9keT4NCjwvaHRtbD5kZBjff%2BVrgsTsvCXg77kCAR30e1xp&__EVENTVALIDATION=%2FwEWRAKslafNDwLSv%2FPlBwKc8cP0BALC0JWICwLC0JGICwLC0K2ICwLC0KmICwLC0KWICwLC0KGICwLC0L2ICwLC0PmLCwLC0PWLCwLd0JmICwLd0JWICwLd0JGICwLd0K2ICwLd0KmICwLd0KWICwLd0KGICwLd0L2ICwLd0PmLCwLd0PWLCwLc0JmICwLc0JWICwLc0JGICwLc0KWICwLc0KGICwLc0L2ICwLc0PmLCwLc0PWLCwLf0JmICwLf0JGICwLf0K2ICwLf0KmICwLf0KGICwLf0L2ICwLf0PmLCwLf0PWLCwLe0JmICwLe0KmICwLe0L2ICwLe0PmLCwLZ0JmICwLZ0JWICwLZ0JGICwLZ0KWICwLZ0KGICwLZ0L2ICwLZ0PmLCwLZ0PWLCwLY0K2ICwLY0L2ICwLY0PmLCwLb0JmICwLb0JWICwLb0JGICwLb0K2ICwLb0KmICwLb0PmLCwLK0JmICwLK0JWICwLK0JGICwLK0KmICwLK0KWICwLF0JmICwLF0JWICwLF0JGICwLF0K2IC6PFb6yKmCZgi5zmVPOPYt1sNmk4&ddlMaps='+('%02d'%route)+'&Vehicles=on&Major+Stops=on').read()
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
