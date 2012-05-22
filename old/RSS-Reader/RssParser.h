//
//  RSSParser.h
//  Project1Solution
//
//  Created by Michael Griepentrog on 2/17/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Article.h"


@interface RSSParser : NSObject {
@public
	NSMutableArray *articleList;
@private
	NSString *rssURL;
	NSMutableString *currentData;
	Article *currentArticle;
	BOOL accumulatingParsedCharacterData;
	BOOL inItemTag;
}

-(RSSParser*) initWithRSSFeed: (NSString *)anRSSFeed;

@property (nonatomic, retain) NSMutableArray *articleList;
@property (nonatomic, retain) NSString *rssURL;
@property (nonatomic, retain) NSMutableString *currentData;
@property (nonatomic, retain) Article *currentArticle;

@end
