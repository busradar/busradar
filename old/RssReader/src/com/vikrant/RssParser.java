package com.vikrant;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RssParser extends DefaultHandler
{  
    private String        urlString;
    private RssFeed       rssFeed;
    private StringBuilder text;
    private Item          item;
    private boolean       imgStatus;
   
    public RssParser(String url)
    {
        this.urlString = url;
        this.text = new StringBuilder();
    }
   
    public void parse() throws SAXException, IOException, ParserConfigurationException
    {
        InputStream urlInputStream = null;
        SAXParserFactory spf = null;
        SAXParser sp = null;
       
        try
        {
            URL url = new URL(this.urlString);
            _setProxy(); // Set the proxy if needed
            urlInputStream = url.openConnection().getInputStream();           
            spf = SAXParserFactory.newInstance();
            if (spf != null)
            {
                sp = spf.newSAXParser();
                sp.parse(urlInputStream, this);
            }
        }

        /*
         * Exceptions need to be handled
         * MalformedURLException
         * ParserConfigurationException
         * IOException
         * SAXException
         */
        finally
        {
            try
            {
                if (urlInputStream != null) urlInputStream.close();
            }
            catch (Exception e) {}
        }
    }

    public RssFeed getFeed()
    {
        return (this.rssFeed);
    }
   
    public void startElement(String uri, String qName, String localName,
            Attributes attributes)
    {
        if (qName.equalsIgnoreCase("channel"))
            this.rssFeed = new RssFeed();
        else if (qName.equalsIgnoreCase("item") && (this.rssFeed != null))
        {
            this.item = new Item();
            this.rssFeed.addItem(this.item);
        }
        else if (qName.equalsIgnoreCase("image") && (this.rssFeed != null))
            this.imgStatus = true;
        else if(qName.equals("content") && uri.equals("http://search.yahoo.com/mrss/")) {
        	this.item.media_content = attributes.getValue("url").toString();
        }
    }
   
    public void endElement(String uri, String qName, String localName)
    {
        if (this.rssFeed == null)
            return;
       
        if (qName.equalsIgnoreCase("item"))
            this.item = null;
       
        else if (qName.equalsIgnoreCase("image"))
            this.imgStatus = false;
       
        else if (qName.equalsIgnoreCase("title"))
        {
            if (this.item != null) this.item.title = this.text.toString().trim();
            else if (this.imgStatus) this.rssFeed.imageTitle = this.text.toString().trim();
            else this.rssFeed.title = this.text.toString().trim();
        }       
       
        else if (qName.equalsIgnoreCase("link"))
        {
            if (this.item != null) this.item.link = this.text.toString().trim();
            else if (this.imgStatus) this.rssFeed.imageLink = this.text.toString().trim();
            else this.rssFeed.link = this.text.toString().trim();
        }       
       
        else if (qName.equalsIgnoreCase("description"))
        {
            if (this.item != null) this.item.description = this.text.toString().trim();
            else this.rssFeed.description = this.text.toString().trim();
        }
       
        else if (qName.equalsIgnoreCase("url") && this.imgStatus)
            this.rssFeed.imageUrl = this.text.toString().trim();
       
        else if (qName.equalsIgnoreCase("language"))
            this.rssFeed.language = this.text.toString().trim();
       
        else if (qName.equalsIgnoreCase("generator"))
            this.rssFeed.generator = this.text.toString().trim();
       
        else if (qName.equalsIgnoreCase("copyright"))
            this.rssFeed.copyright = this.text.toString().trim();
       
        else if (qName.equalsIgnoreCase("pubDate") && (this.item != null))
            this.item.pubDate = this.text.toString().trim();
       
        else if (qName.equalsIgnoreCase("category") && (this.item != null))
            this.rssFeed.addItem(this.text.toString().trim(), this.item);

       
        this.text.setLength(0);
    }
   
    public void characters(char[] ch, int start, int length)
    {
        this.text.append(ch, start, length);
    }
   
    public static void _setProxy()
    throws IOException
    {
        Properties sysProperties = System.getProperties();
        sysProperties.put("proxyHost", "<Proxy IP Address>");
        sysProperties.put("proxyPort", "<Proxy Port Number>");
        System.setProperties(sysProperties);
    }
  
    public static class RssFeed
    {
        public  String title;
        public  String description;
        public  String link;
        public  String language;
        public  String generator;
        public  String copyright;
        public  String imageUrl;
        public  String imageTitle;
        public  String imageLink;
       
        public ArrayList <Item> items;
        public HashMap <String, ArrayList <Item>> category;
       
        public void addItem(Item item)
        {
            if (this.items == null)
                this.items = new ArrayList<Item>();
            this.items.add(item);
        }
       
        public void addItem(String category, Item item)
        {
            if (this.category == null)
                this.category = new HashMap<String, ArrayList<Item>>();
            if (!this.category.containsKey(category))
                this.category.put(category, new ArrayList<Item>());
            this.category.get(category).add(item);
        }
    }
   
   
    public static class Item
    {
        public  String title;
        public  String description;
        public  String link;
        public  String pubDate;
        public  String media_content;
       
        public String toString()
        {
            return (this.title + ": " + this.pubDate + "n" + this.description);
        }
    }
   
}
