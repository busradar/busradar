//
//  MailComposeViewDelagate.h
//  RSS-Reader
//
//  Created by Christopher Mills on 3/3/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <MessageUI/MessageUI.h>

@interface MailComposeViewDelagate : NSObject {
}
	- (void)mailComposeController:(MFMailComposeViewController*)controller didFinishWithResult:(MFMailComposeResult)result error:(NSError*)error;


@end
