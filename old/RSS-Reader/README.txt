/*
*  CS 638: iPhone Project
*  PROJECT 2: RSS Reader
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

   RSS-Reader is an iPhone application that takes a RSS feed, and

   1) Displays the RSS feed title and dates of publication in a UITableView. 
   2) On tapping on the title, the user is taken to a UIWebView where they can read the article.
   3) User is able to browse other pages by clicking on links, and navigate backwards and forwards using tool buttons
   4) User is able to send a link to the article via email to their friends by tapping on the 'Email to friend' button
   5) In case of network error, an error message box is displayed.

Implementation Details:

   1) RSS Parser:
	We will be using the provided RssParser class to retrieve and
	parse the RSS feed of our choice. The feed url is "http://www.engadget.com/rss.xml".

end
