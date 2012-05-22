//
//  whateverAppDelegate.h
//  whatever
//
//  Created by Aleksandr Dobkin on 2/11/10.
//  Copyright __MyCompanyName__ 2010. All rights reserved.
//

@interface whateverAppDelegate : NSObject <UIApplicationDelegate> {
    
    UIWindow *window;
    UINavigationController *navigationController;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet UINavigationController *navigationController;

@end

