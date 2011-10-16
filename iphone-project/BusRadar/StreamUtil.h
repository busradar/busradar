//
//  DataInputStream.h
//  BusRadar
//
//  Created by Michael J. Choi on 10/15/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface StreamUtil : NSObject

+ (BOOL)readBoolean:(NSInputStream *)s;
+ (int)readInt:(NSInputStream *)s;
+ (char)readChar:(NSInputStream *)s;

@end
