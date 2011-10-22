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
    return [DB getStopName:element->_id];
}

- (UIImage *)img {
    return img;
}

- (CGPoint)offset {
    return offset;
}

- (char)dir {
    return element->dir;
}

- (id)initWithElement:(Element *)e {
    CLLocationCoordinate2D point;
    point.latitude = e->lat / 1E6;
    point.longitude = e->lon / 1E6;
    
    element = e;
    coordinate = point;
    
    switch (e->dir) {
        case 'N':
            img = [UIImage imageNamed:@"bus_north_small.png"];
            offset = CGPointMake(0, -[img size].height/2);
            break;
            
        case 'S':
            img = [UIImage imageNamed:@"bus_south_small.png"];
            offset = CGPointMake(-[img size].width, -[img size].height/2);
            break;
            
        case 'E':
            img = [UIImage imageNamed:@"bus_east_small.png"];
            offset = CGPointMake(-[img size].width/2, 0);
            break;
            
        case 'W':
            img = [UIImage imageNamed:@"bus_west_small.png"];
            offset = CGPointMake(-[img size].width/2, -[img size].height);
            break;
            
        default:
            img = [UIImage imageNamed:@"bus_nodir_small.png"];
            offset = CGPointMake(-[img size].width/2, -[img size].height);
            break;
    }
        
    return self;
}

@end
