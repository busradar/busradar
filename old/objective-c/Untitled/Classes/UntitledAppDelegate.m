//
//  UntitledAppDelegate.m
//  Untitled
//
//  Created by Aleksandr Dobkin on 2/9/10.
//  Copyright __MyCompanyName__ 2010. All rights reserved.
//

#import "UntitledAppDelegate.h"
#import "UntitledViewController.h"

@implementation UntitledAppDelegate

@synthesize window;
@synthesize viewController;


- (void)applicationDidFinishLaunching:(UIApplication *)application {    
    
    // Override point for customization after app launch    
    [window addSubview:viewController.view];
    [window makeKeyAndVisible];
}


- (void)dealloc {
    [viewController release];
    [window release];
    [super dealloc];
}


@end
