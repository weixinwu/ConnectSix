import java.awt.*;

/**
 * Created by wuwei on 8/22/2016.
 */
public class Points {

    private int color;
    private Point p;
    public Points(int c ,Point point){
        this.color = c;
        this.p = point;
    }

    public int getColor() {
        return color;
    }

    public Point getP() {
        return p;
    }
}
