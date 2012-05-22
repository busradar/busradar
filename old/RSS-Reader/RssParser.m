//
//  RSSParser.m
//  Project1Solution
//
//  Created by Michael Griepentrog on 2/17/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "RSSParser.h"


@implementation RSSParser

@synthesize articleList, rssURL, currentData, currentArticle;

-(RSSParser*) initWithRSSFeed: (NSString *)anRSSFeed {
    self = [super init];
	
    if ( self ) {
        self.rssURL = anRSSFeed;
		self.articleList = [NSMutableArray array];
		NSData *data = [NSData dataWithContentsOfURL:[NSURL URLWithString:anRSSFeed]];
		
		if(data != nil)
		{
			self.currentData = [NSMutableString string];
			//
			// It's also possible to have NSXMLParser download the data, by passing it a URL, but this is not desirable
			// because it gives less control over the network, particularly in responding to connection errors.
			//
			NSXMLParser *parser = [[NSXMLParser alloc] initWithData:data];
			[parser setDelegate:self];
			[parser parse];
			[parser release];
		}
		else 
		{
			UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error" 
																message:@"Unable to download RSS feed. Please check your network connection" 
															   delegate:self cancelButtonTitle:nil otherButtonTitles:@"Close",nil];
			[alertView show];
			[alertView release];
			[self release];
			return nil;
		}
		
    }
    return self;
}

// Reduce potential parsing errors by using string constants declared in a single place.
static NSString * const kEntryElementName = @"item";
static NSString * const kLinkElementName = @"link";
static NSString * const kTitleElementName = @"title";
static NSString * const kPubDateElementName = @"pubDate";

#pragma mark NSXMLParser delegate methods

- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict {
    if ([elementName isEqualToString:kEntryElementName]) {
        Article *article = [[Article alloc] init];
        self.currentArticle = article;
        [article release];
		inItemTag = YES;
    } else if ([elementName isEqualToString:kTitleElementName] || [elementName isEqualToString:kLinkElementName] || [elementName isEqualToString:kPubDateElementName]) {
		if (inItemTag) {
			accumulatingParsedCharacterData = YES;
			// The mutable string needs to be reset to empty.
			[currentData setString:@""];
		}
    }
}

- (void)parser:(NSXMLParser *)parser didEndElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName {     
    if ([elementName isEqualToString:kEntryElementName]) {
        [self.articleList addObject:self.currentArticle];
		inItemTag = NO;
    } else if ([elementName isEqualToString:kTitleElementName]) {
        
		self.currentArticle.title = [self.currentData copy];
		
		//  } else if ([elementName isEqualToString:@"description"]) {
		//        
		//		self.currentArticle.desc = [self.currentData copy];
		
    }
	
	else if ([elementName isEqualToString:kPubDateElementName]) {
        NSDateFormatter *dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
        [dateFormatter setDateFormat:@"EEE, dd MMM yyyy H:mm:ss ZZZ"];
        self.currentArticle.date = [dateFormatter dateFromString:self.currentData];
		if(self.currentArticle.date == nil)
		{
			// Unable to parse data, attempt to recover
			[dateFormatter setDateFormat:@"EEE, dd MMM yyyy H:mm:ss zzz"];
			self.currentArticle.date = [dateFormatter dateFromString:self.currentData];
			if(self.currentArticle.date == nil)
				NSLog(@"Article date is nil. Check the formatting used in the RSS feed and adjust the NSDateFormatter accordingly.");
		}
    } else if ([elementName isEqualToString:kLinkElementName]) {
        self.currentArticle.url = [self.currentData copy];
    }
    // Stop accumulating parsed character data. We won't start again until specific elements begin.
    accumulatingParsedCharacterData = NO;
}

- (void)parser:(NSXMLParser *)parser parseErrorOccurred:(NSError *)parseError {
	NSLog(@"Parse error occurred: %@", [parseError localizedDescription]);
	[self.articleList release];
	self.articleList = nil;
}

// This method is called by the parser when it find parsed character data ("PCDATA") in an element. The parser is not
// guaranteed to deliver all of the parsed character data for an element in a single invocation, so it is necessary to
// accumulate character data until the end of the element is reached.
- (void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string {
	if (accumulatingParsedCharacterData) {
        // If the current element is one whose content we care about, append 'string'
        // to the property that holds the content of the current element.
        [self.currentData appendString:string];
    }
}

-(void)dealloc
{
	[super dealloc];
	[articleList release];
}

@end
