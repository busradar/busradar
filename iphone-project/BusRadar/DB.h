//
//  DB.h
//  BusRadar
//
//  Created by Michael J. Choi on 10/18/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <sqlite3.h>

@interface DB : NSObject

+ (void)initialize;
+ (NSString *)getStopName:(int)stopId;

@end
