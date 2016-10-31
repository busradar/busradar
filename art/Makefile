all:    ../android-project/res/drawable/user_location_compass.png \
	../android-project/res/drawable-hdpi/user_location_compass.png \
	../android-project/res/drawable-xhdpi/user_location_compass.png

../android-project/res/drawable/user_location_compass.png: user-location-compass.svg
	inkscape --export-png="$@" --export-width=34 $<
	
../android-project/res/drawable-hdpi/user_location_compass.png: user-location-compass.svg
	inkscape --export-png="$@" --export-width=51 $<
	
../android-project/res/drawable-xhdpi/user_location_compass.png: user-location-compass.svg
	inkscape --export-png="$@" --export-width=68 $<
	
clean:
	rm ../android-project/res/drawable/user_location_compass.png \
	   ../android-project/res/drawable-hdpi/user_location_compass.png \
	   ../android-project/res/drawable-xhdpi/user_location_compass.png