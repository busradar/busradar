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
#import "DB.h"

#define METERS_PER_MILE 1609.344

@interface MainViewController : UIViewController <MKMapViewDelegate> {
    QuadTree *stops_tree;
}
@property (weak, nonatomic) IBOutlet MKMapView *mapView;
@property (weak, nonatomic) IBOutlet UIScrollView *scrollView;

- (void)addAllAnnotations:(MKCoordinateRegion)region;
- (void)removeAllAnnotations;
- (void)layoutScrollSegments;

- (MKAnnotationView *)mapView:(MKMapView *)mapView dequeueForDirection:(char)direction;
- (MKAnnotationView *)annotation:(id<MKAnnotation>)annotation initForDirection:(char)direction;

@end
