//
//  DetailViewController.h
//  RSS-Reader
//
//  Created by Christopher Mills on 2/27/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface DetailViewController : UIViewController {
	NSString *myTitle;	
	IBOutlet UILabel *myLabel;

}

@property (nonatomic, retain) NSString *myTitle;
@property (nonatomic, retain) IBOutlet UILabel *myLabel;

@end
