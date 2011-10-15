//
//  QuadTree.h
//  BusRadar
//
//  Created by Michael J. Choi on 10/14/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

//@interface Element : NSObject {
//    NSInteger lat, lon, stopId;
//    char dir;
//    NSArray *routes;
//}
//
//- (id)initFromStream:(NSInputStream *)stream;
//    
//@end

@interface QuadTree : NSObject {
    QuadTree *nw, *ne, *sw, *se;
    NSInteger midx, midy;
    NSArray *items;
}

- (id)initFromStream:(NSInputStream *)stream;

@end
