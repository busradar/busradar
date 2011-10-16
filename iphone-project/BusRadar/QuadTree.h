//
//  QuadTree.h
//  BusRadar
//
//  Created by Michael J. Choi on 10/14/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "StreamUtil.h"

@interface Element : NSObject {
    int lat, lon, _id;
    char dir;
    
    int *routes; // array
    int size; // size of array
}

- (id)initFromStream:(NSInputStream *)s;
    
@end

@interface QuadTree : NSObject {
    QuadTree *nw, *ne, *sw, *se;
    int midx, midy;
    NSMutableArray *items;
}

- (id)initFromStream:(NSInputStream *)s;

@end
