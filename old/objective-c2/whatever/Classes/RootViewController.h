//
//  RootViewController.h
//  whatever
//
//  Created by Aleksandr Dobkin on 2/11/10.
//  Copyright __MyCompanyName__ 2010. All rights reserved.
//

#import "RSSParser.h"

@interface RootViewController : UITableViewController {
	NSMutableArray *text_array;
	RSSParser *parser;
}

@property (nonatomic, retain) NSMutableArray *text_array;
//@property (nonatomic, retain) RSSParser *parser;

@end
