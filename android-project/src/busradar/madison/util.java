// Copyright (C) 2016 Aleksandr Dobkin.
// 
// This file is part of BusRadar <https://github.com/orgs/busradar/>.
// 
// BusRadar is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 3 of the License, or
// (at your option) any later version.
// 
// BusRadar is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

package busradar.madison;

import java.net.*;
import java.io.*;

public class util {
    static String http_get(String url) throws IOException {
        InputStream in = new URL(url).openStream();
        return read_fully(in);
    }
    
    static String read_fully(InputStream in) throws IOException {
        String str = "";
        int nbytes;
        byte[] b = new byte[1024*4];
        while ( (nbytes = in.read(b)) != -1 ) {
            str += new String(b, 0, nbytes, "UTF-8");
        }
        //System.out.printf("BusRadar: read %d %s\n", str.length(), str);
        return str;
    }
}