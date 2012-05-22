package org.khelekore.prtree;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import busradar.madison.RouteTree.Line;

class InternalNode<T> extends NodeBase<Node<T>, T> {
    public InternalNode (Object[] data) {
	super (data);
    }
    
    public InternalNode(DataInputStream s) throws IOException {
    	data = new Object[s.readInt()];
    	for (int i = 0; i < data.length; i++) {
    		if (s.readBoolean() == true) {
    			data[i] = new LeafNode<Line>(s);
    		}
    		else {
    			data[i] = new InternalNode<Line>(s);
    		}
    	}
    }
    
    @SuppressWarnings("unchecked")
	public void write(DataOutputStream s) throws IOException {
    	
    	s.writeInt(data.length);
    	for (int i = 0; i < data.length; i++) {
    		if (data[i] instanceof LeafNode) {
        		s.writeBoolean(true);
        		((LeafNode)data[i]).write(s);
        	}
        	else if (data[i] instanceof InternalNode) {
        		s.writeBoolean(false);
        		((InternalNode)data[i]).write(s);
        	}
        	else
        		throw new RuntimeException();
    	}
    }
    
    @Override public MBR computeMBR (MBRConverter<T> converter) {
	MBR ret = null;
	for (int i = 0, s = size (); i < s; i++)
	    ret = getUnion (ret, get (i).getMBR (converter));
	return ret;
    }
    
    public void expand (MBR mbr, MBRConverter<T> converter, List<T> found, 
			List<Node<T>> nodesToExpand) {
	for (int i = 0, s = size (); i < s; i++) {
	    Node<T> n = get (i);
	    if (mbr.intersects (n.getMBR (converter)))
		nodesToExpand.add (n);
	}
    }

    public void find (MBR mbr, MBRConverter<T> converter, List<T> result) {
	for (int i = 0, s = size (); i < s; i++) {
	    Node<T> n = get (i);
	    if (mbr.intersects (n.getMBR (converter)))
		n.find (mbr, converter, result);
	}
    }
}
