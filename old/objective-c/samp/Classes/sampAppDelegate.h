//
//  sampAppDelegate.h
//  samp
//
//  Created by Aleksandr Dobkin on 2/9/10.
//  Copyright __MyCompanyName__ 2010. All rights reserved.
//

@interface sampAppDelegate : NSObject <UIApplicationDelegate> {
    
    UIWindow *window;
    UINavigationController *navigationController;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet UINavigationController *navigationController;

@end

