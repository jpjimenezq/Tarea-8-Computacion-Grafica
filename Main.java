import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

public class Main extends JPanel {
    private static Punto[] controlPoints = {
        new Punto(0, 100, 0),
        new Punto(200, 33, 0),
        new Punto(-200, -33, 0),
        new Punto(0, -100, 0)
    };

    private static final int N = 3;
    private List<Punto> curvePoints;

    public Main() {
        generateCurvePoints();
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
    }

    private void generateCurvePoints() {
        curvePoints = new ArrayList<>();
        int numPuntos = 100;

        for (int i = 0; i <= numPuntos; i++) {
            double t = (double) i / numPuntos;
            Punto punto = evaluateBezier(t);
            curvePoints.add(punto);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawLine(0, centerY, getWidth(), centerY);
        g2d.drawLine(centerX, 0, centerX, getHeight());

        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(3));

        Path2D path = new Path2D.Double();
        boolean first = true;

        for (Punto punto : curvePoints) {
            int x = centerX + (int) punto.x;
            int y = centerY - (int) punto.y;

            if (first) {
                path.moveTo(x, y);
                first = false;
            } else {
                path.lineTo(x, y);
            }
        }

        g2d.draw(path);
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(2));

        for (int i = 0; i < controlPoints.length; i++) {
            int x = centerX + (int) controlPoints[i].x;
            int y = centerY - (int) controlPoints[i].y;

            Ellipse2D circle = new Ellipse2D.Double(x - 5, y - 5, 10, 10);
            g2d.fill(circle);

            g2d.setColor(Color.BLACK);
            g2d.drawString("P" + i, x + 8, y + 5);
            g2d.setColor(Color.RED);
        }

        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));

        for (int i = 0; i < controlPoints.length - 1; i++) {
            int x1 = centerX + (int) controlPoints[i].x;
            int y1 = centerY - (int) controlPoints[i].y;
            int x2 = centerX + (int) controlPoints[i + 1].x;
            int y2 = centerY - (int) controlPoints[i + 1].y;

            g2d.drawLine(x1, y1, x2, y2);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Curva de Bézier");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new Main());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            System.out.println("Curva de Bézier generada y mostrada en pantalla");
            System.out.println("\nPuntos de control:");
            for (int i = 0; i < controlPoints.length; i++) {
                System.out.println("P" + i + ": " + controlPoints[i]);
            }
        });
    }

    public static Punto evaluateBezier(double t) {
        Punto acum = new Punto();

        for (int i = 0; i < controlPoints.length; i++) {
            double bernstein = bernsteinPolynomial(N, i, t);
            acum.x += controlPoints[i].x * bernstein;
            acum.y += controlPoints[i].y * bernstein;
            acum.z += controlPoints[i].z * bernstein;
        }

        return acum;
    }

    public static double bernsteinPolynomial(int n, int i, double t) {
        return binomialCoefficient(n, i) * Math.pow(t, i) * Math.pow(1 - t, n - i);
    }

    public static double binomialCoefficient(int n, int k) {
        if (k > n || k < 0) return 0;
        if (k == 0 || k == n) return 1;

        double result = 1;
        for (int i = 0; i < k; i++) {
            result = result * (n - i) / (i + 1);
        }
        return result;
    }
}
