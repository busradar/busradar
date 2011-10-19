//
//  DB.m
//  BusRadar
//
//  Created by Michael J. Choi on 10/18/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import "DB.h"

static sqlite3 *database;

@implementation DB

+ (void)initialize {
    if(database == nil) {
        NSString *path = [[NSBundle mainBundle] pathForResource:@"db" ofType:@"sqlite"];
        if(sqlite3_open([path UTF8String], &database) == SQLITE_OK) {
//            NSLog(@"db open success");
        } else {
            NSLog(@"db open failed");
        }
    }
}

+ (NSString *)getStopName:(int)stopId {
    NSString *str = [NSString stringWithFormat:@"SELECT [Stop].[Name] FROM [Stop] WHERE [Stop].[_ID] = %d", stopId];
    const char *query = [str UTF8String];
    sqlite3_stmt *stmt;
    
    if(sqlite3_prepare_v2(database, query, -1, &stmt, nil) == SQLITE_OK) {
        while(sqlite3_step(stmt) == SQLITE_ROW) {
            return [NSString stringWithUTF8String:(char *)sqlite3_column_text(stmt, 0)];
        }
    } else {
        NSLog(@"db getStopName query for _id: %d failed", stopId);
    }
    
    return nil;
}

@end
