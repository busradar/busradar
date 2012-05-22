//
//  WebViewController.h
//  RSS-Reader
//
//  Created by Christopher Mills on 2/27/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface WebViewController : UIViewController {
	
	IBOutlet UIWebView *webView;
	IBOutlet UIToolbar *toolbar; 
	IBOutlet UITextField *email;
	NSString *myAddress;
	
}
@property (nonatomic, retain) UIWebView *webView;
@property (nonatomic, retain) UIToolbar *toolbar;
@property (nonatomic, retain) NSString *myAddress;
@property (nonatomic, retain) UITextField *email;
- (IBAction)sendEmail:(id)sender;
- (IBAction)goForward:(id)sender;
- (IBAction)goBack:(id)sender;
@end
