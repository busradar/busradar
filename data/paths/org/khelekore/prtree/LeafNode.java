package org.khelekore.prtree;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import busradar.madison.RouteTree.Line;

class LeafNode<T> extends NodeBase<T, T> {

    public LeafNode (Object[] data) {
	super (data);
    }
    
    public LeafNode(DataInputStream s) throws IOException {
    	if (s.readBoolean() == true) {
    		mbr = new SimpleMBR(s);
    	}
    	
    	data = new Object[s.readInt()];
    	for (int i = 0; i < data.length; i++) {
    		data[i] = new Line(s);
    	}
    }
    
	public void write(DataOutputStream s) throws IOException {
    	
		if (mbr != null) {
			s.writeBoolean(true);
			((SimpleMBR)mbr).write(s);
		}
		else
			s.writeBoolean(false);
		
    	s.writeInt(data.length);
    	for (int i = 0; i < data.length; i++) {
    		((Line)data[i]).write(s);
    	}
    }

    public MBR getMBR (T t, MBRConverter<T> converter) {
	return new SimpleMBR (converter.getMinX (t), converter.getMinY (t),
			      converter.getMaxX (t), converter.getMaxY (t));
    }
    
    @Override public MBR computeMBR (MBRConverter<T> converter) {
	MBR ret = null;
	for (int i = 0, s = size (); i < s; i++)
	    ret = getUnion (ret, getMBR (get (i), converter));
	return ret;
    }

    public void expand (MBR mbr, MBRConverter<T> converter, 
			List<T> found, List<Node<T>> nodesToExpand) {
	find (mbr, converter, found);
    }

    public void find (MBR mbr, MBRConverter<T> converter, List<T> result) {
	for (int i = 0, s = size (); i < s; i++) {
	    T  t = get (i);
	    if (mbr.intersects (t, converter))
		result.add (t);
	}
    }
}
