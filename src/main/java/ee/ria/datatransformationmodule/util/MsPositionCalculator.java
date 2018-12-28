package ee.ria.datatransformationmodule.util;

import ee.ria.datatransformationmodule.data.Point;
import ee.ria.datatransformationmodule.data.PositionPoint;

import java.util.ArrayList;

public class  MsPositionCalculator {

    public static ArrayList<Point> findIntersectionPointsOf2Circles(float x1, float y1, float r1, float x2, float y2,
                                                                    float r2) {

        float distanceBetweenCenters = (float) Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
        float delta = (float) (1f/4f * Math.sqrt((distanceBetweenCenters+r1+r2)*(distanceBetweenCenters+r1-r2) *
                (distanceBetweenCenters-r1+r2) * (- distanceBetweenCenters +r1+r2)));

        float a = (x1 + x2) / 2f + (x2 - x1) * (r1 * r1 - r2 * r2) / (2f * distanceBetweenCenters * distanceBetweenCenters);
        float b = (y1 + y2) / 2f + (y2 - y1) * (r1 * r1 - r2 * r2) / (2f * distanceBetweenCenters * distanceBetweenCenters);
        float intersectionPointX1 = a + 2f * (y1 - y2) / (distanceBetweenCenters * distanceBetweenCenters) * delta;
        float intersectionPointX2 = a - 2f * (y1 - y2) / (distanceBetweenCenters * distanceBetweenCenters) * delta;
        float intersectionPointY1 = b - 2f * (x1 - x2) / (distanceBetweenCenters * distanceBetweenCenters) * delta;
        float intersectionPointY2 = b + 2f * (x1 - x2) / (distanceBetweenCenters * distanceBetweenCenters) * delta;

        ArrayList<Point> intersectionPoints = new ArrayList<Point>();
        intersectionPoints.add(new Point(intersectionPointX1, intersectionPointY1));
        intersectionPoints.add(new Point(intersectionPointX2, intersectionPointY2));
        return intersectionPoints;
    }

    public static PositionPoint getPositionBy2Bs(float bs1X, float bs1Y, float bs1Dist, float bs2X,
                                                 float bs2Y, float bs2dist) {

        ArrayList<Point> intersectionPoints = findIntersectionPointsOf2Circles(bs1X, bs1Y, bs1Dist, bs2X, bs2Y, bs2dist);
        float intersectionPointX1 = intersectionPoints.get(0).getX();
        float intersectionPointY1 = intersectionPoints.get(0).getY();
        float intersectionPointX2 = intersectionPoints.get(1).getX();
        float intersectionPointY2 = intersectionPoints.get(1).getY();

        float deltaX = Math.abs(intersectionPointX2 - intersectionPointX1)/2f;
        float deltaY = Math.abs(intersectionPointY2 - intersectionPointY1)/2f;

        float positionX = 0f;
        float positionY = 0f;

        if (intersectionPointX1 < intersectionPointX2) {

            positionX = intersectionPointX1 + deltaX;
        } else {

            positionX = intersectionPointX2 + deltaX;
        }

        if (intersectionPointY1 < intersectionPointY2) {

            positionY = intersectionPointY1 + deltaY;
        } else {

            positionY = intersectionPointY2 + deltaY;
        }

        float errorRadius = (float) Math.sqrt(Math.abs(intersectionPointX2 - intersectionPointX1)/2f *
                Math.abs(intersectionPointX2 - intersectionPointX1)/2f + Math.abs(intersectionPointY2 -
                intersectionPointY1)/2f * Math.abs(intersectionPointY2 - intersectionPointY1)/2f);

        PositionPoint position = new PositionPoint(positionX, positionY,errorRadius);
        return position;
    }

    public static PositionPoint getPositionBy3Bs(float bs1x, float bs1y, float bs1Dist, float bs2x, float bs2y,
                                                float bs2Dist, float bs3x, float bs3y, float bs3Dist) {

        ArrayList<Point> intersectionPoints = findIntersectionPointsOf2Circles(bs1x, bs1y, bs1Dist, bs2x, bs2y, bs2Dist);
        PositionPoint result = null;
        if (isPointOnCircle(intersectionPoints.get(0), bs3x, bs3y, bs3Dist)) {

            result = new PositionPoint(intersectionPoints.get(0).getX(), intersectionPoints.get(0).getY(), 0);
        } else {

            result = new PositionPoint(intersectionPoints.get(1).getX(), intersectionPoints.get(1).getY(), 0);
        }
        return result;
    }

    private static boolean isPointOnCircle(Point Point, float centerX, float centerY, float radius) {

        float distanceFromCenter = (float) Math.sqrt((centerX - Point.getX()) * (centerX - Point.getX()) +
                (centerY - Point.getY())*(centerY -Point.getY()));
        float threshold = 0.001f;
        return (Math.abs(distanceFromCenter - radius) < threshold );
    }
}
