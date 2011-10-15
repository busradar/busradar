//
//  QuadTree.m
//  BusRadar
//
//  Created by Michael J. Choi on 10/14/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import "QuadTree.h"

//@implementation Element
//
//@end

@implementation QuadTree

- (id)initFromStream:(NSInputStream *)stream {
    uint8_t buf1[32];
    int len1 = [stream read:buf1 maxLength:32];
    uint8_t buf2[32];
    int len2 = [stream read:buf2 maxLength:32];
    
    NSMutableData *data1 = [[NSMutableData alloc] init];
    NSMutableData *data2 = [[NSMutableData alloc] init];
    NSLog(@"%d,%d bytes read", len1, len2);
    if(len1 > 0) {
        [data1 appendBytes:&buf1 length:len1];
    }
    if(len2 > 0) {
        [data2 appendBytes:&buf2 length:len2];
    }
    
    NSString *first = [[NSString alloc] initWithData:data1 encoding:NSASCIIStringEncoding];
    NSString *second = [[NSString alloc] initWithData:data2 encoding:NSASCIIStringEncoding];
    
    NSLog(@"first 32bytes: %@", first);
    NSLog(@"second 32bytes: %@", second);
    
//    nw = [[QuadTree alloc] initFromStream:stream];
//    ne = [[QuadTree alloc] initFromStream:stream];
//    sw = [[QuadTree alloc] initFromStream:stream];
//    se = [[QuadTree alloc] initFromStream:stream];
    return self;
}

@end
