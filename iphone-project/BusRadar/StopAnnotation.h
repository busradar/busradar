//
//  StopAnnotation.h
//  BusRadar
//
//  Created by Michael J. Choi on 10/14/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <MapKit/MapKit.h>
#import "QuadTree.h"

@interface StopAnnotation : NSObject <MKAnnotation> {
    CLLocationCoordinate2D coordinate;
    NSString *title;
    NSString *subtitle;
    
    Element *element; // holds data (lat/long, routes[], etc)
    UIImage *img;
    CGPoint offset;
}
- (id)initWithElement:(Element *)e;
- (UIImage *)img;
- (CGPoint)offset;

@end
