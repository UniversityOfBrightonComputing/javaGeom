/* file : CirculinearCurve2DUtilsTest.java
 * 
 * Project : geometry
 *
 * ===========================================
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY, without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. if not, write to :
 * The Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 * 
 * Created on 7 mars 2007
 *
 */
package math.geom2d.circulinear;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Assert;
import junit.framework.TestCase;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.conic.CircleArc2D;
import math.geom2d.curve.CurveArray2D;
import math.geom2d.curve.PolyCurve2D;
import math.geom2d.curve.SmoothCurve2D;
import math.geom2d.domain.Boundary2D;
import math.geom2d.line.InvertedRay2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.Ray2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.polygon.LinearRing2D;
import math.geom2d.polygon.Polyline2D;
import math.geom2d.spline.CubicBezierCurve2D;

public class CirculinearCurve2DUtilsTest extends TestCase {

	public void testConvert_Element() {
		CirculinearCurve2D conv;
		LineSegment2D seg = new LineSegment2D(new Point2D(0, 10), new Point2D(10, 20));
		conv = CirculinearCurve2DUtils.convert(seg);
		assertTrue(seg.equals(conv));
	}
	
	@SuppressWarnings("unchecked")
	public void testConvert_PolyCurve() {
		CirculinearCurve2D conv;
		Point2D p1 = new Point2D(0, 10);
		Point2D p2 = new Point2D(10, 20);
		Point2D p3 = new Point2D(0, 30);
		LineSegment2D seg1 = new LineSegment2D(p1, p2);
		LineSegment2D seg2 = new LineSegment2D(p2, p3);
		PolyCurve2D<LineSegment2D> curve = 
			new PolyCurve2D<LineSegment2D>(new LineSegment2D[]{seg1, seg2});
		
		conv = CirculinearCurve2DUtils.convert(curve);
		assertTrue(conv instanceof PolyCirculinearCurve2D);
		// unchecked class cast
		PolyCirculinearCurve2D<? extends ContinuousCirculinearCurve2D> poly = 
			(PolyCirculinearCurve2D<? extends ContinuousCirculinearCurve2D>) conv;
		assertEquals(2, poly.getCurveNumber());
		assertTrue(poly.containsCurve(seg1));
		assertTrue(poly.containsCurve(seg2));
	}
	
	@SuppressWarnings("unchecked")
	public void testConvert_PolyCurveWithSpline() {
		Point2D p1 = new Point2D(0, 10);
		Point2D p2 = new Point2D(10, 20);
		Point2D p3 = new Point2D(0, 30);
		LineSegment2D seg1 = new LineSegment2D(p1, p2);
		LineSegment2D seg2 = new LineSegment2D(p2, p3);
		CubicBezierCurve2D bezier = new CubicBezierCurve2D(
				p3, new Point2D(0, 10), new Point2D(10, 50), new Point2D(20, 50));
		PolyCurve2D<SmoothCurve2D> curve = 
			new PolyCurve2D<SmoothCurve2D>(new SmoothCurve2D[]{seg1, seg2, bezier});
		
		try {
			CirculinearCurve2DUtils.convert(curve);
			Assert.fail("should have thrown an exception");
		} catch (NonCirculinearClassException ex) {
			// should go here
		}
	}

	@SuppressWarnings("unchecked")
	public void testConvert_CurveSet() {
		CirculinearCurve2D conv;
		Point2D p1 = new Point2D(0, 10);
		Point2D p2 = new Point2D(10, 20);
		Point2D p3 = new Point2D(0, 30);
		
		LineSegment2D seg1 = new LineSegment2D(p1, p2);
		LineSegment2D seg2 = new LineSegment2D(p2, p3);
		
		CurveArray2D<LineSegment2D> curve = 
			new CurveArray2D<LineSegment2D>(new LineSegment2D[]{seg1, seg2});
		
		conv = CirculinearCurve2DUtils.convert(curve);
		assertTrue(conv instanceof CirculinearCurveSet2D);
		// unchecked class cast
		CirculinearCurveSet2D<? extends ContinuousCirculinearCurve2D> set = 
			(CirculinearCurveSet2D<? extends ContinuousCirculinearCurve2D>) conv;
		assertEquals(2, set.getCurveNumber());
		assertTrue(set.containsCurve(seg1));
		assertTrue(set.containsCurve(seg2));
	}

	@SuppressWarnings("unchecked")
	public void testConvert_CurveSetWithSpline() {
		Point2D p1 = new Point2D(0, 10);
		Point2D p2 = new Point2D(10, 20);
		Point2D p3 = new Point2D(0, 30);
		
		LineSegment2D seg1 = new LineSegment2D(p1, p2);
		LineSegment2D seg2 = new LineSegment2D(p2, p3);
		CubicBezierCurve2D bezier = new CubicBezierCurve2D(
				p3, new Point2D(0, 10), new Point2D(10, 50), new Point2D(20, 50));
		
		CurveArray2D<SmoothCurve2D> curve = 
			new CurveArray2D<SmoothCurve2D>(new SmoothCurve2D[]{seg1, seg2, bezier});
		
		try {
			CirculinearCurve2DUtils.convert(curve);
			Assert.fail("should have thrown an exception");
		} catch (NonCirculinearClassException ex) {
			// should go here
		}
	}

	public void testSplitContinuousCurve() {
		// elements
		LineSegment2D edge1 = new LineSegment2D(
				new Point2D(50, 100), new Point2D(150, 100));
		CircleArc2D arc1 = new CircleArc2D(new Point2D(100, 100), 50, 0, Math.PI/2);
		LineSegment2D edge2 = new LineSegment2D(
				new Point2D(100, 150), new Point2D(100, 50));
		
		// create the curve
		PolyCirculinearCurve2D<CirculinearElement2D> curve  = 
			new PolyCirculinearCurve2D<CirculinearElement2D>(
					new CirculinearElement2D[]{edge1, arc1, edge2} );
		
		// split the curve
		Collection<ContinuousCirculinearCurve2D> set = 
			CirculinearCurve2DUtils.splitContinuousCurve(curve);
		
		// should be two parts
		assertEquals(2, set.size());
	}
	
	public void testSplitIntersectingContours_Circles() {
		Circle2D circle1 = new Circle2D(0, 0, 10);
		Circle2D circle2 = new Circle2D(10, 0, 10);
		Collection<? extends CirculinearContour2D> contours = 
			CirculinearCurve2DUtils.splitIntersectingContours(circle1, circle2);
		assertEquals(contours.size(), 2);
	}
	
	public void testSplitIntersectingContours_Rectangles() {
		LinearRing2D poly1 = new LinearRing2D(new Point2D[]{
				new Point2D(10, 20), new Point2D(40, 20), 
				new Point2D(40, 30), new Point2D(10, 30) });
		LinearRing2D poly2 = new LinearRing2D(new Point2D[]{
				new Point2D(10, 20), new Point2D(40, 20), 
				new Point2D(40, 30), new Point2D(10, 30) });
		
		Collection<CirculinearContour2D> contours = 
			CirculinearCurve2DUtils.splitIntersectingContours(poly1, poly2);
		assertEquals(contours.size(), 2);
	}
	
	public void testSplitIntersectingContours_3Circles() {
		Circle2D ring1 = new Circle2D(150, 150, 100);
		Circle2D ring2 = new Circle2D(250, 150, 100);
		Circle2D ring3 = new Circle2D(200, 220, 100);
		
		ArrayList<CirculinearContour2D> rings = 
			new ArrayList<CirculinearContour2D>(3);
		rings.add(ring1);
		rings.add(ring2);
		rings.add(ring3);

		Collection<CirculinearContour2D> result = 
			CirculinearCurve2DUtils.splitIntersectingContours(rings);
		assertTrue(result.size()==3);
	}
	
	public void testFindSelfIntersections () {
		// create polyline
		LinearRing2D line = new LinearRing2D(new Point2D[]{
				new Point2D(100, 0), 
				new Point2D(0, 0),
				new Point2D(0, 100),
				new Point2D(200, 100),
				new Point2D(200, 200),
				new Point2D(100, 200) });
		
		// detection of intersections
		Collection<Point2D> points = 
			CirculinearCurve2DUtils.findSelfIntersections(line);

		assertEquals(points.size(), 1);
		
		assertTrue(points.contains(new Point2D(100, 100)));
	}
	
	public void testGetParallelInfiniteCurve () {
		// create an infinite curve, here a straight line
		Point2D p0 = new Point2D(10, 20);
		Vector2D v0 = new Vector2D(10, 20);
		StraightLine2D line = new StraightLine2D(p0, v0);
		
		// computes its parallel
		ContinuousCirculinearCurve2D parallel =
			CirculinearCurve2DUtils.createContinuousParallel(line, 10);
		
		// check some assertions
		assertFalse(parallel==null);
		assertTrue(parallel.getContinuousCurves().size()==1);
		assertFalse(parallel.isEmpty());
		assertEquals(1, parallel.getSmoothPieces().size());
	}
	
	public void testGetParallelBiRay () {
		// first defines some constants
		Point2D origin = new Point2D(10, 20);
		Vector2D v1 = new Vector2D(2, 3);
		Vector2D v2 = new Vector2D(3, 2);
		
		// create elements of the curve
		InvertedRay2D ray1 = new InvertedRay2D(origin, v1);
		Ray2D ray2 = new Ray2D(origin, v2);
		
		// create the curve
		PolyCirculinearCurve2D<CirculinearElement2D> curve =
			new PolyCirculinearCurve2D<CirculinearElement2D>();
		curve.addCurve(ray1);
		curve.addCurve(ray2);
		
		// computes the parallel
		ContinuousCirculinearCurve2D parallel =
			CirculinearCurve2DUtils.createContinuousParallel(curve, 10);
		assertFalse(parallel==null);
		assertFalse(parallel.isEmpty());
		assertEquals(3, parallel.getSmoothPieces().size());

		// the same in opposite direction
		ContinuousCirculinearCurve2D parallel2 =
			CirculinearCurve2DUtils.createContinuousParallel(curve, 10);
		assertFalse(parallel2==null);
		assertFalse(parallel2.isEmpty());
		assertEquals(3, parallel2.getSmoothPieces().size());
		
		Collection<ContinuousCirculinearCurve2D> splittedCurves =
			CirculinearCurve2DUtils.splitContinuousCurve(parallel2);
		assertEquals(2, splittedCurves.size());
	}

	
	
	public void testGetParallel_LinearRing () {
		// polyline with two edges, that are colinear
		LinearRing2D curve = new LinearRing2D(new Point2D[]{
				new Point2D(100, 100), 
				new Point2D(200, 100), 
				new Point2D(200, 200) });
		
		ContinuousCirculinearCurve2D parallel = 
			CirculinearCurve2DUtils.createContinuousParallel(curve, 20);
		
		assertFalse(parallel==null);
		assertFalse(parallel.isEmpty());
		assertEquals(6, parallel.getSmoothPieces().size());
		assertTrue(parallel.isClosed());
		
		ContinuousCirculinearCurve2D parallel2 = 
			CirculinearCurve2DUtils.createContinuousParallel(curve, -20);
		
		assertFalse(parallel2==null);
		assertFalse(parallel2.isEmpty());
		assertEquals(6, parallel2.getSmoothPieces().size());
		assertTrue(parallel.isClosed());
	}

	public void testGetParallelColinearPolyline () {
		// polyline with two edges, that are colinear
		Polyline2D curve = new Polyline2D(new Point2D[]{
				new Point2D(100, 100), 
				new Point2D(150, 100), 
				new Point2D(200, 100) });
		
		ContinuousCirculinearCurve2D parallel = 
			CirculinearCurve2DUtils.createContinuousParallel(curve, 20);
		
		assertFalse(parallel==null);
		assertFalse(parallel.isEmpty());
		assertEquals(2, parallel.getSmoothPieces().size());
		
		ContinuousCirculinearCurve2D parallel2 = 
			CirculinearCurve2DUtils.createContinuousParallel(curve, -20);
		
		assertFalse(parallel2==null);
		assertFalse(parallel2.isEmpty());
		assertEquals(2, parallel2.getSmoothPieces().size());
	}

	public void testGetBufferInfiniteCurve () {
		// create an infinite curve, here a straight line
		Point2D p0 = new Point2D(10, 20);
		Vector2D v0 = new Vector2D(10, 20);
		StraightLine2D line = new StraightLine2D(p0, v0);
		
		// compute parallel
		ContinuousCirculinearCurve2D parallel =
			CirculinearCurve2DUtils.createContinuousParallel(line, 10);
		assertFalse(parallel==null);
		assertTrue(parallel.getContinuousCurves().size()==1);
		assertFalse(parallel.isEmpty());
		assertEquals(1, parallel.getSmoothPieces().size());
		
		// same in other direction
		ContinuousCirculinearCurve2D parallel2 =
			CirculinearCurve2DUtils.createContinuousParallel(line, -10);
		assertFalse(parallel2==null);
		assertTrue(parallel2.getContinuousCurves().size()==1);
		assertFalse(parallel2.isEmpty());
		assertEquals(1, parallel2.getSmoothPieces().size());		
	}

	public void testSplitContinuousCurveParallelBiRay () {
		// first defines some constants
		Point2D origin = new Point2D(10, 10);
		Vector2D v1 = new Vector2D(3, 4);
		Vector2D v2 = new Vector2D(4, 3);
		
		// create elements of the curve
		InvertedRay2D ray1 = new InvertedRay2D(origin, v1);
		Ray2D ray2 = new Ray2D(origin, v2);
		
		// create the curve
		PolyCirculinearCurve2D<CirculinearElement2D> curve =
			new PolyCirculinearCurve2D<CirculinearElement2D>();
		curve.addCurve(ray1);
		curve.addCurve(ray2);
		assertEquals(2, curve.getSmoothPieces().size());

		// computes the parallel
		ContinuousCirculinearCurve2D parallel = curve.getParallel(10);
		Collection<ContinuousCirculinearCurve2D> splittedCurves =
			CirculinearCurve2DUtils.splitContinuousCurve(parallel);
		
		assertFalse(splittedCurves.isEmpty());
		assertEquals(2, splittedCurves.size());
		
		// computes the other parallel
		ContinuousCirculinearCurve2D parallel2 = curve.getParallel(-10);
		Collection<Point2D> points = parallel2.getSingularPoints();
		assertEquals(2, points.size());
		assertTrue(points.contains(new Point2D(2, 16)));
		assertTrue(points.contains(new Point2D(4, 18)));
	}
	
	public void testGetBufferBiRay () {
		// first defines some constants
		Point2D origin = new Point2D(10, 20);
		Vector2D v1 = new Vector2D(3, 4);
		Vector2D v2 = new Vector2D(4, 3);
		
		// create elements of the curve
		InvertedRay2D ray1 = new InvertedRay2D(origin, v1);
		Ray2D ray2 = new Ray2D(origin, v2);
		
		// create the curve
		PolyCirculinearCurve2D<CirculinearElement2D> curve =
			new PolyCirculinearCurve2D<CirculinearElement2D>();
		curve.addCurve(ray1);
		curve.addCurve(ray2);
		assertEquals(2, curve.getSmoothPieces().size());

		// computes the buffer
		CirculinearDomain2D buffer =
			CirculinearCurve2DUtils.computeBuffer(curve, 10);
		assertFalse(buffer==null);
		assert buffer!=null;
		assertFalse(buffer.isEmpty());
		
		// Extract boundary of buffer
		Boundary2D boundary = buffer.getBoundary();
		assertEquals(2, boundary.getContinuousCurves().size());
	}
	
	public void testGetBufferColinearPolyline () {
		// polyline with two edges, that are colinear
		Polyline2D curve = new Polyline2D(new Point2D[]{
				new Point2D(100, 100), 
				new Point2D(150, 100), 
				new Point2D(200, 100) });
		
		CirculinearDomain2D buffer = 
			CirculinearCurve2DUtils.computeBuffer(curve, 20);
		
		assertFalse(buffer==null);
		assertFalse(buffer.isEmpty());
	}
		
	public void testGetBufferTwoLines() {
		StraightLine2D line1 = new StraightLine2D(new Point2D(0, 0), new Vector2D(10, 0));
		StraightLine2D line2 = new StraightLine2D(new Point2D(0, 0), new Vector2D(0, 10));
		CirculinearCurveSet2D<StraightLine2D> set = 
			new CirculinearCurveSet2D<StraightLine2D>();
		set.addCurve(line1);
		set.addCurve(line2);
		
		CirculinearDomain2D buffer = set.getBuffer(10);
		assertEquals(4, buffer.getBoundary().getBoundaryCurves().size());
	}
}
