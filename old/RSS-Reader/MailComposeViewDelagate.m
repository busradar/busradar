//
//  MailComposeViewDelagate.m
//  RSS-Reader
//
//  Created by Christopher Mills on 3/3/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "MailComposeViewDelagate.h"


@implementation MailComposeViewDelagate
- (void)mailComposeController:(MFMailComposeViewController*)controller didFinishWithResult:(MFMailComposeResult)result error:(NSError*)error
{
	[controller dismissModalViewControllerAnimated:YES];
}
@end
