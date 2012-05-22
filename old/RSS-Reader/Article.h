//
//  Article.h
//  Project1Solution
//
//  Created by Michael Griepentrog on 2/3/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface Article : NSObject {
	NSString *title;
	NSString *url;
	NSDate *date;
	NSString *description;
}

@property (nonatomic, retain) NSString *title;
@property (nonatomic, retain) NSString *url;
@property (nonatomic, retain) NSDate *date;
@property (nonatomic, retain) NSString *description;

@end
