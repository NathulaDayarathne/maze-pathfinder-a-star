public class Point {
    private int x, y;
   private int fCost;
    private Point parent; // Parent node for backtracking


    //        ******* POINT CLASS ********
    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }


//    Getters
    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;

    }

    public int getFCost(){
        return this.fCost;
    }

    public Point getParent(){return this.parent;}



//    Setters

    public void calculateFCost( Point start, Point finish ){

        int gCost = Math.abs(start.getX() - this.x) + Math.abs(start.getY() - this.y);
        int hCost = Math.abs(finish.getX() - this.x) + Math.abs(finish.getY() - this.y);

        fCost = gCost + hCost;
    }

    public void setParent(Point parent){
        this.parent = parent;
    }

}