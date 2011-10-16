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
    NSMutableString *sub = [[NSMutableString alloc] init];
    for(int i = 0; i < element->size; i++) {
        [sub appendFormat:@"%d", element->routes[i]];
        
        if(i != element->size - 1) { // if not last
            [sub appendString:@", "];
        }
    }

    return sub;
}

- (NSString *)title {
    return @"title";
}

- (id)initWithElement:(Element *)e {
    CLLocationCoordinate2D point;
    point.latitude = e->lat / 1E6;
    point.longitude = e->lon / 1E6;
    
    element = e;
    coordinate = point;
    
    // query sqlite for the title
    
    return self;
}

@end
