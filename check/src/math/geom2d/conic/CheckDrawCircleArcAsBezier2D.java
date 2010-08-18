package math.geom2d.conic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.spline.CubicBezierCurve2D;

public class CheckDrawCircleArcAsBezier2D  extends JPanel{

	/** */
	private static final long serialVersionUID = 1L;
	
	double x0 = 100;
	double y0 = 100;
	double r = 200;
	double extent = Math.PI/2;
	
	public void paintComponent(Graphics g){
		
		Graphics2D g2 = (Graphics2D) g;

		Circle2D circle = new Circle2D(x0, y0, r);
		CircleArc2D arc = new CircleArc2D(circle, 0, extent);
		
		g2.setColor(Color.BLUE);
		arc.draw(g2);
		
		Point2D p1 = arc.getFirstPoint();
		Point2D p2 = arc.getLastPoint();
		Vector2D v1 = arc.getTangent(arc.getT0());
		Vector2D v2 = arc.getTangent(arc.getT1());
		
		double k = 0.5522847498307933;
		Point2D c1 = new Point2D(p1.getX()+k*v1.getX(), p1.getY()+k*v1.getY());
		Point2D c2 = new Point2D(p2.getX()-k*v2.getX(), p2.getY()-k*v2.getY());
		
		g2.setColor(Color.RED);
		p1.draw(g2, 2);
		p2.draw(g2, 2);
		c1.draw(g2, 2);
		c2.draw(g2, 3);
		System.out.println(Math.PI/6);
		
		CubicBezierCurve2D bezier = new CubicBezierCurve2D(p1, c1, c2, p2);
		bezier.draw(g2);
		
		int N = 200;
		double[] dists = new double[N];
		double maxDist = 0;
		for(int i=0; i<N; i++){
			dists[i] = arc.getDistance(bezier.getPoint(1.0/N));
			maxDist = Math.max(dists[i], maxDist);
		}
		System.out.println(maxDist/2 + "%");
	}

	public final static void main(String[] args){
		System.out.println("draw a circle arc with a bezier");
		
		JPanel panel = new CheckDrawCircleArcAsBezier2D();
		JFrame frame = new JFrame("Check Circle arc");
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		panel.setPreferredSize(new Dimension(400, 400));
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);
	}}