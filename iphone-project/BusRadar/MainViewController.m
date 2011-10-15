//
//  MainViewController.m
//  BusRadar
//
//  Created by Michael J. Choi on 10/13/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import "MainViewController.h"

@implementation MainViewController
@synthesize mapView=_mapView;
@synthesize scrollView = _scrollView;

const CGFloat scrollObjectHeight = 46.0;
const CGFloat scrollObjectWidth = 46.0;
const NSUInteger numSegments = 15;

- (void)layoutScrollSegments {
    UISegmentedControl *sc = [[UISegmentedControl alloc] init];
    
    for(int i = 0; i < numSegments; i++) {
        [sc insertSegmentWithTitle:[NSString stringWithFormat:@"%d", i] atIndex:i animated:NO];
    }
    CGRect dimensions = CGRectMake(0, 0, 46*numSegments, 46);
    sc.frame = dimensions;
    sc.selectedSegmentIndex = 0;
    sc.alpha = 0.9; // transparency
    
    [self.scrollView addSubview:sc];
    
    // set it scrollable
    [self.scrollView setContentSize:CGSizeMake(numSegments * scrollObjectWidth, [self.scrollView bounds].size.height)];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [self layoutScrollSegments];
    
    NSString *path = [[NSBundle mainBundle] pathForResource:@"stops" ofType:@"bin"];
    NSInputStream *is = [[NSInputStream alloc] initWithFileAtPath:path];
    [is open]; // you NEED to open the stream dude!
    [[QuadTree alloc] initFromStream:is];
}

- (void)viewDidUnload
{
    [self setMapView:nil];
    [self setScrollView:nil];
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
    CLLocationCoordinate2D zoomLocation;
    zoomLocation.latitude = 43.066667;
    zoomLocation.longitude = -89.4;
    
    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(zoomLocation, METERS_PER_MILE, METERS_PER_MILE);
    
    MKCoordinateRegion adjustRegion = [_mapView regionThatFits:viewRegion];
    
    [_mapView setRegion:adjustRegion animated:YES];
    [_mapView addAnnotation:[[StopAnnotation alloc] initWithCoordinate:zoomLocation]];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
}

- (void)viewWillDisappear:(BOOL)animated
{
	[super viewWillDisappear:animated];
}

- (void)viewDidDisappear:(BOOL)animated
{
	[super viewDidDisappear:animated];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
}

- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id<MKAnnotation>)annotation {
    MKPinAnnotationView *pav = [[MKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:nil];
    pav.pinColor = MKPinAnnotationColorPurple;
    pav.animatesDrop = YES;
    pav.canShowCallout = YES;
    return pav;
}

@end