//
//  RootViewController.h
//  RSS-Reader
//
//  Created by Christopher Mills on 2/27/10.
//  Copyright __MyCompanyName__ 2010. All rights reserved.
//

#import "RssParser.h"

@interface RootViewController : UITableViewController {
	NSMutableArray *text_array;
	RSSParser *parser;
}

@property (nonatomic, retain) NSMutableArray *text_array;


@end
