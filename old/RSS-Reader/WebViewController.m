//
//  WebViewController.m
//  RSS-Reader
//
//  Created by Christopher Mills on 2/27/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "WebViewController.h"
#import <MessageUI/MessageUI.h>
#import "MailComposeViewDelagate.h"


@implementation WebViewController
@synthesize myAddress;
@synthesize webView;
@synthesize toolbar;
@synthesize email;
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
	//[url release];
	//[requestObj release];
}


/*
// Override to allow orientations other than the default portrait orientation.
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}
*/
- (IBAction)sendEmail:(id)sender{
	[email resignFirstResponder];
	//NSLog(@"mailto:%@?subject=Check out this link&body=%@", email.text, myAddress);
		if ([MFMailComposeViewController canSendMail]) {
			// create mail composer object
			MFMailComposeViewController *mailer = [[MFMailComposeViewController alloc] init];
			
			mailer.mailComposeDelegate = [[MailComposeViewDelagate alloc] init];
			
			// generate message body
			NSString *body = myAddress;
			
			// add to users signature
			[mailer setMessageBody:body isHTML:NO];
			
			// present user with composer screen
			[self presentModalViewController:mailer animated:YES];
			
			// release composer object
			[mailer release];
		} else {
			// alert to user there is no email support
		}
}

- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
	// Release any retained subviews of the main view.
	// e.g. self.myOutlet = nil;
}


- (IBAction)goBack:(id)sender{
	[webView goBack];
}

- (IBAction)goForward:(id)sender{
	[webView goForward];
}

- (void)dealloc {
	[webView release];
	[email release];
	[toolbar release];
	[myAddress release];
    [super dealloc];
}


@end
