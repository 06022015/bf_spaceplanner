package com.spaceplanner.java.dxf;

import org.kabeja.dxf.DXFLWPolyline;
import org.kabeja.dxf.DXFPoint;
import org.kabeja.dxf.DXFVertex;
import org.kabeja.dxf.helpers.Point;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ashifqureshi
 * Date: 13/08/15
 * Time: 1:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class DesignPolyLine implements Comparator<DesignPolyLine> {

    private String id;
    private List<Integer> x=new ArrayList<Integer>();
    private List<Integer> y = new ArrayList<Integer>();
    private String text;
    private List<Point> pointList = new ArrayList<Point>();
    private List<DesignMText> mTexts;
    private boolean hasPolyLine = false;

    public DesignPolyLine(DXFLWPolyline dxflwPolyline){
        this.id = dxflwPolyline.getID();
        Iterator textIterator = dxflwPolyline.getVertexIterator();
        while (textIterator.hasNext()) {
            DXFVertex dxfVertex = (DXFVertex) textIterator.next();
            this.pointList.add(dxfVertex.getPoint());
            if (!this.x.contains(dxfVertex.getPoint().getX()))
                this.x.add(((int)dxfVertex.getPoint().getX()));
            if (!this.y.contains((int)dxfVertex.getPoint().getY()))
                this.y.add((int)dxfVertex.getPoint().getY());

        }
        Collections.sort(this.x);
        Collections.sort(this.y);
    }

    public boolean validatePoint(DXFPoint p){
        return ((p.getX()>this.x.get(0)&&p.getX()<this.x.get(x.size()-1)) && (p.getY()>this.y.get(0)&& p.getY()<this.y.get(y.size()-1)));
    }

    public boolean validatePoint(DXFPoint p1, DXFPoint p2){
        return ((p1.getX()>this.x.get(0)&&p1.getX()<this.x.get(x.size()-1)) && (p1.getY()>this.y.get(0)&& p1.getY()<this.y.get(y.size()-1)))
                &&((p2.getX()>this.x.get(0)&&p2.getX()<this.x.get(x.size()-1)) && (p2.getY()>this.y.get(0)&& p2.getY()<this.y.get(y.size()-1)));
    }

    public boolean isValid(DXFPoint p){
        int j= getPointList().size()-1;
        boolean valid=false;
        for (int i=0; i<getPointList().size(); i++) {
            if (getPointList().get(i).getY()<p.getY() && getPointList().get(j).getY()>=p.getY()
                    ||  getPointList().get(j).getY()<p.getY() && getPointList().get(i).getY()>=p.getY()) {
                if (getPointList().get(i).getX()+(p.getY()-getPointList().get(i).getY())/(getPointList().get(j).getY()-getPointList().get(i).getY())*(getPointList().get(j).getX()-getPointList().get(i).getX())<p.getX()) {
                    valid=!valid; }}
            j=i;
        }
        return valid;
    }

    public boolean isValid(List<Point> points){
        for(Point point : points){
            if(!isValid(point))
                return false;
        }
        return true;
    }

    public boolean isValid(Point p){
        int j= getPointList().size()-1;
        boolean valid=false;
        for (int i=0; i<getPointList().size(); i++) {
            if (getPointList().get(i).getY()<=p.getY() && getPointList().get(j).getY()>=p.getY()
                    ||  getPointList().get(j).getY()<=p.getY() && getPointList().get(i).getY()>=p.getY()) {
                if (getPointList().get(i).getX()+(p.getY()-getPointList().get(i).getY())/(getPointList().get(j).getY()-getPointList().get(i).getY())*(getPointList().get(j).getX()-getPointList().get(i).getX())<=p.getX()) {
                    valid=!valid;
                }
            }
            j=i;
        }
        return valid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Integer> getX() {
        return x;
    }

    public void setX(List<Integer> x) {
        this.x = x;
    }

    public List<Integer> getY() {
        return y;
    }

    public void setY(List<Integer> y) {
        this.y = y;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Point> getPointList() {
        return pointList;
    }

    public void setPointList(List<Point> pointList) {
        this.pointList = pointList;
    }

    public List<DesignMText> getmTexts() {
        return mTexts;
    }

    public void setmTexts(List<DesignMText> mTexts) {
        this.mTexts = mTexts;
    }

    public void addMText(DesignMText mText){
        if(null == getmTexts())
            setmTexts(new ArrayList<DesignMText>());
        getmTexts().add(mText);
    }

    public boolean isHasPolyLine() {
        return hasPolyLine;
    }

    public void setHasPolyLine(boolean hasPolyLine) {
        this.hasPolyLine = hasPolyLine;
    }

    public String toString() {
        return "DesignPolyLine{" +
                "x=[x1=" + x.get(0)+" x2= "+x.get(x.size()-1)+"]" +
                ",  y=[y1=" + y.get(0)+" y2= "+y.get(y.size()-1)+"]" +
                "Text="+text+" }";
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DesignPolyLine that = (DesignPolyLine) o;
        if (x != null ? !x.equals(that.x) : that.x != null) return false;
        if (y != null ? !y.equals(that.y) : that.y != null) return false;
        return true;
    }

    public int hashCode() {
        int result = x != null ? x.hashCode() : 0;
        result = 31 * result + (y != null ? y.hashCode() : 0);
        return result;
    }

    public int compare(DesignPolyLine o1, DesignPolyLine o2) {
        int xv = o1.getX().get(o1.getX().size()-1).compareTo(o2.getX().get(o2.getX().size()-1));
        int yv = o1.getY().get(o1.getY().size()-1).compareTo(o2.getY().get(o2.getY().size()-1));
        return xv>0?yv>0?1:0:0;
    }
}
