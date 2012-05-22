//
//  UntitledAppDelegate.h
//  Untitled
//
//  Created by Aleksandr Dobkin on 2/9/10.
//  Copyright __MyCompanyName__ 2010. All rights reserved.
//

#import <UIKit/UIKit.h>

@class UntitledViewController;

@interface UntitledAppDelegate : NSObject <UIApplicationDelegate> {
    UIWindow *window;
    UntitledViewController *viewController;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet UntitledViewController *viewController;

@end

