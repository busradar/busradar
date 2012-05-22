//
//  UntitledViewController.h
//  Untitled
//
//  Created by Aleksandr Dobkin on 2/9/10.
//  Copyright __MyCompanyName__ 2010. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UntitledViewController : UIViewController {
	UILabel *myLabel;
	UIButton *myButton;
	
}

@property (nonatomic, retain) IBOutlet UILabel *myLabel;
@property (nonatomic, retain) IBOutlet UIButton *myButton;

-(IBAction)buttonPressed:(id)sender;


@end

