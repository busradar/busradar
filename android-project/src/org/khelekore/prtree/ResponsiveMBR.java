package org.khelekore.prtree;

public class ResponsiveMBR extends SimpleMBR {
    double span;
    
    public ResponsiveMBR(double xmin, double ymin, double xmax, double ymax,
            double span) {
        super(xmin, ymin, xmax, ymax);
        this.span = span;
    }
    @Override public boolean intersects(MBR other) {
        double xSpan = other.getMaxX() - other.getMinX();
        double ySpan = other.getMaxY() - other.getMinY();
        System.out.printf("BusRadar: intersect %s\n", other);
        if (xSpan < span && ySpan < span) {
            return false;
        }
        return super.intersects(other);
    }
    
    @Override public <T> boolean intersects (T t, MBRConverter<T> converter) {
        double xSpan = converter.getMaxX(t) - converter.getMinX(t);
        double ySpan = converter.getMaxY(t) - converter.getMinY(t);
        if (xSpan < span && ySpan < span) {
            return false;
        }
        return super.intersects(t, converter);
    }
}