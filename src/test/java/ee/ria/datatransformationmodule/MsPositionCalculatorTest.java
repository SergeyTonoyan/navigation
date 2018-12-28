package ee.ria.datatransformationmodule;

import ee.ria.datatransformationmodule.util.MsPositionCalculator;
import ee.ria.datatransformationmodule.data.Point;
import ee.ria.datatransformationmodule.data.PositionPoint;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class MsPositionCalculatorTest {

    @Test
    public void getPositionBy2Bs() {

        float x1 = 6f;
        float y1 = 9f;
        float r1 = 2f;

        float x2 = 8f;
        float y2 = 7f;
        float r2 = 2f;

        PositionPoint actualPosition = MsPositionCalculator.getPositionBy2Bs(x1,y1,r1,x2,y2,r2);
        PositionPoint expectedPosition = new PositionPoint(7, 8, 1.41421356f);

        Assert.assertEquals(expectedPosition.getX(), actualPosition.getX(),0);
        Assert.assertEquals(expectedPosition.getY(), actualPosition.getY(),0);
        Assert.assertEquals(expectedPosition.getErrorRadius(), actualPosition.getErrorRadius(),0);
    }

    @Test
    public void findIntersectionPointsOf2Circles() {

        float x1 = 4f;
        float y1 = 5f;
        float r1 = 2f;

        float x2 = 9f;
        float y2 = 2f;
        float r2 = 4.2426f;

        Point expectedIntersectionPoint1 = new Point(6f, 5f);
        Point expectedIntersectionPoint2 = new Point(4.941f, 3.235f);

        ArrayList<Point> actualIntersectionPoints = MsPositionCalculator.findIntersectionPointsOf2Circles(x1, y1, r1,
                x2, y2, r2);

        Assert.assertEquals(expectedIntersectionPoint1.getX(), actualIntersectionPoints.get(0).getX(),0.01f);
        Assert.assertEquals(expectedIntersectionPoint1.getY(), actualIntersectionPoints.get(0).getY(),0.01f);
        Assert.assertEquals(expectedIntersectionPoint2.getX(), actualIntersectionPoints.get(1).getX(),0.01f);
        Assert.assertEquals(expectedIntersectionPoint2.getY(), actualIntersectionPoints.get(1).getY(),0.01f);
    }
}