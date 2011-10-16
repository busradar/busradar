//
//  QuadTree.m
//  BusRadar
//
//  Created by Michael J. Choi on 10/14/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import "QuadTree.h"

const int MAX_CHILD = 20;

@implementation Element

- (id)initFromStream:(NSInputStream *)s {
    lat = [StreamUtil readInt:s];
    lon = [StreamUtil readInt:s];
    dir = [StreamUtil readChar:s];
    _id = [StreamUtil readInt:s];
    
    size = [StreamUtil readInt:s]; // size of array
    routes = malloc(sizeof(int) * size);
    for(int i = 0; i < size; i++) {
        routes[i] = [StreamUtil readInt:s];
    }
    
    return self;
}

@end

@implementation QuadTree

- (id)initFromStream:(NSInputStream *)s {
    if([StreamUtil readBoolean:s] == YES) {
        int x = [StreamUtil readInt:s];
        items = [[NSMutableArray alloc] initWithCapacity:x];
        for(int i = 0; i < x; i++) {
            [items insertObject:[[Element alloc] initFromStream:s] atIndex:i];
        }
        
    } else {
        
        nw = [[QuadTree alloc] initFromStream:s];
        ne = [[QuadTree alloc] initFromStream:s];
        sw = [[QuadTree alloc] initFromStream:s];
        se = [[QuadTree alloc] initFromStream:s];
        
        midx = [StreamUtil readInt:s];
        midy = [StreamUtil readInt:s];
    }
    
    return self;
}

- (NSMutableArray *)get:(int)xboundmin :(int)yboundmin :(int)xboundmax :(int)yboundmax :(int)span {
    NSMutableArray *l = [[NSMutableArray alloc] init];
    
    if(items != nil) { // we're a leaf
        for(Element *e in items) {
            if(e->lon >= xboundmin && e->lon <= xboundmax &&
               e->lat >= yboundmin && e->lat <= yboundmax) {
                [l addObject:e];
            }
        }
        
    } else {
        if(yboundmin < midy) { // include south
            if(xboundmin < midx) { // include west
                [l addObjectsFromArray:[sw get:xboundmin :yboundmin :xboundmax :yboundmax :span]];
            }
            if(xboundmax >= midx) { // include east
                [l addObjectsFromArray:[se get:xboundmin :yboundmin :xboundmax :yboundmax :span]];
            }
        }
        
        if(yboundmax >= midy) { // include north
            if(xboundmin < midx) { // include west
                [l addObjectsFromArray:[nw get:xboundmin :yboundmin :xboundmax :yboundmax :span]];
            }
            if(xboundmax >= midx) { // include east
                [l addObjectsFromArray:[ne get:xboundmin :yboundmin :xboundmax :yboundmax :span]];
            }
        }

    }
    
    return l;
}

@end
