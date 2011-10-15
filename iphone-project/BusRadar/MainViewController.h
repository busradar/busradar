//
//  MainViewController.h
//  BusRadar
//
//  Created by Michael J. Choi on 10/13/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import "StopAnnotation.h"
#import "QuadTree.h"

#define METERS_PER_MILE 1609.344

@interface MainViewController : UIViewController <MKMapViewDelegate> {
    
}
@property (weak, nonatomic) IBOutlet MKMapView *mapView;
@property (weak, nonatomic) IBOutlet UIScrollView *scrollView;

@end
