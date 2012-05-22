//
//  RSS_ReaderAppDelegate.h
//  RSS-Reader
//
//  Created by Christopher Mills on 2/27/10.
//  Copyright __MyCompanyName__ 2010. All rights reserved.
//
#import <UIKit/UIKit.h>
@class MyViewController;

@interface RSS_ReaderAppDelegate : NSObject <UIApplicationDelegate> {
    
    UIWindow *window;
    UINavigationController *navigationController;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet UINavigationController *navigationController;

@end

