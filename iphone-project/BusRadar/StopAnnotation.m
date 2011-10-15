//
//  StopAnnotation.m
//  BusRadar
//
//  Created by Michael J. Choi on 10/14/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import "StopAnnotation.h"

@implementation StopAnnotation
@synthesize coordinate;

- (NSString *)subtitle {
    return @"subtitle";
}

- (NSString *)title {
    return @"title";
}

- (id)initWithCoordinate:(CLLocationCoordinate2D)c {
    coordinate = c;
    NSLog(@"%f,%f", c.latitude, c.longitude);
    return self;
}

@end
