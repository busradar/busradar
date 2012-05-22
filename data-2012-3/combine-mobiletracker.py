import json

weekdays = json.load(open("mobiletracker-weekdays.json"))
holidays = json.load(open("mobiletracker-weekendsandholidays.json"))

for stop in holidays:
	for holiday_route in holidays[stop]:
		if stop not in weekdays:
			weekdays[stop] = []
			
		weekday_stop = weekdays[stop]
		has_route = False
		
		
		for weekday_route in weekday_stop:
			if weekday_route['url'] == holiday_route['url']:
				has_route = True
				break
		
				
		if not has_route:
			weekday_stop.append(holiday_route)

print >>open('mobiletracker-combined.json', 'w'), json.dumps(weekdays, sort_keys=True, indent=4)
