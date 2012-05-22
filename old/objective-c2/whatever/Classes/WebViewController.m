//
//  WebViewController.m
//  RSS-Reader
//
//  Created by Christopher Mills on 2/27/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "WebViewController.h"


@implementation WebViewController
@synthesize myAddress;
@synthesize webView;
@synthesize toolbar;
@synthesize email;

//@dynamic webView;

/*
 // The designated initializer.  Override if you create the controller programmatically and want to perform customization that is not appropriate for viewDidLoad.
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    if (self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil]) {
        // Custom initialization
    }
    return self;
}
*/


// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad {
	NSURL *url = [NSURL URLWithString:myAddress];
	NSURLRequest *requestObj = [NSURLRequest requestWithURL:url];
	[webView loadRequest:requestObj];
    [super viewDidLoad];
//	[[self webView] setScalesPageToFit:YES];
}


/*
// Override to allow orientations other than the default portrait orientation.
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}
*/

- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
	// Release any retained subviews of the main view.
	// e.g. self.myOutlet = nil;
}

- (IBAction)sendEmail:(id)sender{
	NSURL *url = [[NSURL alloc] initWithString:
				  [NSString stringWithFormat: @"mailto:%@?subject=Check out this link&body=%@", email.text, myAddress]];
	[[UIApplication sharedApplication] openURL:url];
}

- (void)dealloc {
	[webView release];
	[email release];
	[toolbar release];
	[myAddress release];
    [super dealloc];
}


@end
