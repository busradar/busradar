//
//  DataInputStream.m
//  BusRadar
//
//  Created by Michael J. Choi on 10/15/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import "StreamUtil.h"

const int INTEGER = 4;
const int BOOLEAN = 1;
const int CHAR = 2; // binary was written in Java

@implementation StreamUtil

+ (int)readInt:(NSInputStream *)s {
    uint32_t buf;
    [s read:(uint8_t *)&buf maxLength:INTEGER];
//    NSLog(@"readInt: %d", ntohl(buf));
    return ntohl(buf);
}

+ (BOOL)readBoolean:(NSInputStream *)s {
    uint8_t buf;
    [s read:&buf maxLength:BOOLEAN];
//    NSLog(@"readBoolean: %s", buf != 0 ? "true" : "false");
    return buf != 0;
}

+ (char)readChar:(NSInputStream *)s {
    uint16_t buf;
    [s read:(uint8_t *)&buf maxLength:CHAR];
//    NSLog(@"readChar: %c", (char) ntohs(buf));
    return (char) ntohs(buf);
}

@end
