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
const int numSegments = 15;
const double maxZoom = 0.028; // allowed longtigude span degree

-(void)addAllAnnotations:(MKCoordinateRegion)region {
//    NSLog(@"add: count %d", [[_mapView annotations] count]);    
    if([_mapView.annotations count] > 1) {
        // do nothing; already drawn
    } else {
        int pixel = (region.span.longitudeDelta * 1E6) / _mapView.bounds.size.width;
        //    double minLat = adjustRegion.center.latitude - (adjustRegion.span.latitudeDelta / 2.0);
        //    double maxLat = adjustRegion.center.latitude + (adjustRegion.span.latitudeDelta / 2.0);
        //    double minLong = adjustRegion.center.longitude - (adjustRegion.span.longitudeDelta / 2.0);
        //    double maxLong = adjustRegion.center.longitude + (adjustRegion.span.longitudeDelta / 2.0);
        //    int xboundmin = (int) round(minLong * 1E6);
        //    int yboundmin = (int) round(minLat * 1E6);
        //    int xboundmax = (int) round(maxLong * 1E6);
        //    int yboundmax = (int) round(maxLat * 1E6);
        //    NSMutableArray *geopoints = [stops_tree get:xboundmin-32*pixel :yboundmin-32*pixel 
        //                                              :xboundmax+32*pixel :yboundmax+32*pixel :15*pixel];
        NSMutableArray *geopoints = [stops_tree get:-92*1E6 :41*1E6
                                                   :-87*1E6 :45*1E6 :15*pixel];
        for(Element *e in geopoints) {
            [_mapView addAnnotation:[[StopAnnotation alloc] initWithElement:e]];
        }
    }
}

-(void)removeAllAnnotations
{
//    NSLog(@"remove: count %d", [[_mapView annotations] count]);
    if([[_mapView annotations] count] > 1) {
        [_mapView removeAnnotations:_mapView.annotations];
    }
}

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

- (void)viewDidLoad // Called after the controller’s view is loaded into memory.
{
    [self layoutScrollSegments];
    
    NSString *path = [[NSBundle mainBundle] pathForResource:@"stops" ofType:@"bin"];
    NSInputStream *is = [[NSInputStream alloc] initWithFileAtPath:path];
    [is open]; // you NEED to open the stream dude!
    
    stops_tree = [[QuadTree alloc] initFromStream:is];
    
    [DB initialize];
}

- (void)viewDidUnload // Called when the controller’s view is released from memory.
{
    [self setMapView:nil];
    [self setScrollView:nil];
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated // Notifies the view controller that its view is about to be become visible.
{
    self.navigationController.navigationBarHidden = YES;
    
    CLLocationCoordinate2D zoomLocation;
    zoomLocation.latitude = 43.066667;
    zoomLocation.longitude = -89.4;
    
    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(zoomLocation, METERS_PER_MILE, METERS_PER_MILE);
    
    MKCoordinateRegion adjustRegion = [_mapView regionThatFits:viewRegion];
    [_mapView setRegion:adjustRegion animated:YES];    
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

- (void)mapView:(MKMapView *)mapView annotationView:(MKAnnotationView *)view calloutAccessoryControlTapped:(UIControl *)control
{
    self.navigationController.navigationBarHidden = NO;
    [self performSegueWithIdentifier:@"ShowDetail" sender:self];
}

- (MKAnnotationView *)mapView:(MKMapView *)mapView dequeueForDirection:(char)direction
{
    MKAnnotationView *av;
    switch (direction) {
        case 'N':
            av = [mapView dequeueReusableAnnotationViewWithIdentifier:@"N"];
            break;
            
        case 'S':
            av = [mapView dequeueReusableAnnotationViewWithIdentifier:@"S"];
            break;
            
        case 'E':
            av = [mapView dequeueReusableAnnotationViewWithIdentifier:@"E"];
            break;
            
        case 'W':
            av = [mapView dequeueReusableAnnotationViewWithIdentifier:@"W"];
            break;
            
        default:
            av = [mapView dequeueReusableAnnotationViewWithIdentifier:@"none"];
            break;
    }
    
    return av;
}

- (MKAnnotationView *)annotation:(id<MKAnnotation>)annotation initForDirection:(char)direction
{
    MKAnnotationView *av;
    switch (direction) {
        case 'N':
            av = [[MKAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"N"];
            break;
            
        case 'S':
            av = [[MKAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"S"];
            break;
            
        case 'E':
            av = [[MKAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"E"];
            break;
            
        case 'W':
            av = [[MKAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"W"];
            break;
            
        default:
            av = [[MKAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"none"];
            break;
    }
    
    return av;
}

- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id<MKAnnotation>)annotation
{    
    if([annotation isKindOfClass:[StopAnnotation class]]) {
        StopAnnotation *sa = (StopAnnotation *)annotation; // cast to be used many times below
        
//        MKAnnotationView *av = [mapView dequeueReusableAnnotationViewWithIdentifier:[sa title]];
        MKAnnotationView *av = [self mapView:mapView dequeueForDirection:[sa dir]];
        if(av == nil) {
//            NSLog(@"creating");
//            av = [[MKAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:[sa title]];
            av = [self annotation:annotation initForDirection:[sa dir]];
            [av setImage:[sa img]];
            av.canShowCallout = YES;
            
            UIButton *btn = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
            av.rightCalloutAccessoryView = btn;
        } else {
//            NSLog(@"re-using");
        }
        
//        av.centerOffset = [(StopAnnotation *)annotation offset]; // TODO: make this dynamic
        return av;
    } else {
        return nil;
    }
}

- (void)mapView:(MKMapView *)mapView regionDidChangeAnimated:(BOOL)animated {
    MKCoordinateRegion region = [_mapView regionThatFits:[_mapView region]];
    if(region.span.longitudeDelta > maxZoom) {
        [self removeAllAnnotations];
    } else {
        [self addAllAnnotations:region];
    }
}

@end
