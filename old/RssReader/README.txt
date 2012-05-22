/*
*  CS 638: Android Mini Project
*  PROJECT 1: Headline Reader
*
*  Partner 1: Michael Choi
*  Logon 1  : mchoi
*  ID 1     : 9031802664
*
*  Partner 2: Aleksandr Dobkin
*  Logon 2  : aleksand
*  ID 2     : 9030843503
*
*  Partner 3: Chris Mills
*  Logon 3  : mills
*  ID 3     : 9026910977
*/

Project Description:

   Headline Reader is an Android application that takes a RSS feed, and

   1) Display the headlines in a ListView. 
   2) Read button will read the headlines in the ListView one by one
      using TTS (text-to-speech).
   3) Stop button will stop the TTS reading.
   4) On a long click of an item in the ListView, only that headline will
      be read using TTS.
   5) On a click on an item, the image and description will appear
      corresponding to that headline.

Implementation Details:

   1) RSS Parser:
	We will be using the com.vikrant RssParser class to retrieve and
	parse the RSS feed of our choice.
	"http://feeds.nytimes.com/nyt/rss/HomePage"

   2) Headline ListView:
	We will NOT be making a use of the TxnDbAdapter class. We will be
	using an ArrayAdapter of Strings that will hold the titles of the
        headlines.

   3) Text-to-Speech (TTS):
	In lieu of the tts_1.4_market.apk, we will be using the TTS library
	native to Android (android.speech.tts.TextToSpeech). For this reason,
	we have decided to use the Android 2.1 - API Level 7 for this project.

   4) Read/Stop Buttons:
	The Read button will TTS the title for each title in the feed. Note
	that this does not read from the ArrayAdapter. Rather, the button
	directly iterates through an RssFeed object. We've found this method
	to be more efficient. The Stop button will stop the TTS at once.

	We've designed the layout so these buttons appear on the top of the
	ListView of headlines.

   5) Headline Image & Description:
	When a list item is clicked, we will launch a new WebView Activity
	to display the image, the title, and description associated with
	that headline. This is all done in the HtmlViewer class.We have
	modified the RssParser class slightly so it may store the URL of
	the image.

end
