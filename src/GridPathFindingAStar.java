import java.util.*;
import java.io.*;

public class GridPathFindingAStar {


    static Point start;
    static Point finish;
    static boolean[][] visited;

    static boolean isWithinGrid(int x, int y, int rows, int cols) {
        return x >= 0 && x < rows && y >= 0 && y < cols;
    }

    //     Get the manhattan distance between two given points
    static int manhattanDistance(Point one, Point two) {
        return Math.abs(one.getX() - two.getX()) + Math.abs(one.getY() - two.getY());
    }


    //    Get the next neighboring points of the current point - Rules are applied
    static List<Point> getNeighboringPoints(Point current, Point start, Point finish, char[][] grid) {

//    List to store the neighbors
        List<Point> stops = new ArrayList<>();

        int yLimit = grid.length -1 ; // Y axis
        int xLimit = grid[0].length -1; // X axis

        int x = current.getX();
        int y = current.getY();



        //UP
        while (y >= 0) {
            if (y == 0 || grid[y - 1][x] == '0' || grid[y][x] == 'F') {
                stops.add(new Point(x, y));
                break;
            }

            y--;
        }



        x = current.getX();
        y = current.getY();

        //DOWN
        while (y <= yLimit) {

            if (y == yLimit || grid[y + 1][x] == '0' || grid[y][x] == 'F') {
                stops.add(new Point(x, y));
                break;

            }

            y++;
        }


        x = current.getX();
        y = current.getY();
        //LEFT
        while (x >= 0) {

            if (x == 0 || grid[y][x-1] == '0' || grid[y][x] == 'F') {
                stops.add(new Point(x, y));
                break;

            }

            x--;
        }

        x = current.getX();
        y = current.getY();
        //RIGHT
        while (x <= xLimit) {

            if (x == xLimit || grid[y][x+1] == '0' || grid[y][x] == 'F') {
                stops.add(new Point(x, y));
                break;

            }

            x++;
        }


//


        return stops;
    }


    // Construct the path
    static List<Point> reconstructPath(Point dest) {
        List<Point> path = new ArrayList<>();
        Point current = dest;
        while (current != null) {
            path.add(current);
            current = current.getParent();
        }
        Collections.reverse(path);
        return path;
    }


    //    Get the shortest path with A* algorithm
    static List<Point> aStar(char[][] grid) {

        if (start == null || finish == null) {
            throw new IllegalArgumentException("Grid does not contain start or end node");
        }

        int rows = grid.length; // Y axis
        int cols = grid[0].length; // X axis
        boolean[][] visited = new boolean[rows][cols];
        int[][] dist = new int[rows][cols];


//        Queue to add the Points and sort them within it
        PriorityQueue<Point> pq = new PriorityQueue<>(Comparator.comparingInt(p -> p.getFCost()));


//        set the fCost of the starting Point and add it to the priority queue
        start.calculateFCost(start, finish);
        pq.add(start);


//      Loop until the priority queue get empty
        while (!pq.isEmpty()) {

//             Get the head of the queue and remove it from the queue
            Point current = pq.poll();

//            todo: remove the test code


//            Stop the loop when the current selected node is the finish node
            if (grid[current.getY()][current.getX()] == 'F') {

                System.out.println("\n\nFound the finishing POINT!");

                List<Point> path = reconstructPath(current);

                return path;
            }


//            Check whether the Point has been visited and add it to the visited array if it is not
            if (visited[current.getY()][current.getX()]) continue;
            visited[current.getY()][current.getX()] = true;


//            Get the neighboring points
            List<Point> neighboringPoints = getNeighboringPoints(current, start, finish, grid);

            for (Point neighbor : neighboringPoints) {



                if(!visited[neighbor.getY()][neighbor.getX()] ){

                    if(!pq.contains(neighbor)){

//                    Set the point attributes
                    neighbor.setParent(current);
                    neighbor.calculateFCost(start,finish);

//                    Add to the priority queue
                    pq.add(neighbor);

                    System.out.println("Result Node added to the open node queue");
                    System.out.println("Size: " + pq.size());
                    System.out.println("\n");

                }}else{
//                    todo: remove the test code
                    System.out.println("Already in the queue");
                }


            }
        }

        System.out.println("❌ Returning a null List");
        return null; // No path found
    }


    public static char[][] readGridFromFile(String filename) throws IOException {
        // Open the file for reading
        BufferedReader reader = new BufferedReader(new FileReader(filename));

        try {
            // Read the first line to determine columns
            String firstLine = reader.readLine();
            int cols = firstLine.length();
            int rows = 1; // Start with 1 for the first line
            boolean inWord = false;

            // Calculate the number of rows
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) { // Only increment rows if the line is not empty
                    rows++;
                }
            }

            // Reset the reader back to the beginning of the file
            reader.close();
            reader = new BufferedReader(new FileReader(filename));

            // Create the grid
            char[][] grid = new char[rows][cols];

            // Read each line and populate the grid
            int rowIndex = 0;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    for (int colIndex = 0; colIndex < cols; colIndex++) {
                        grid[rowIndex][colIndex] = line.charAt(colIndex);
                    }
                    rowIndex++;
                }
            }

            return grid;
        } finally {
            reader.close();  // Ensure closing the reader even if exceptions occur
        }
    }






    public static void main(String[] args) {

        String filename = "puzzle_80.txt";

        try {
            // Read the grid from the file
            char[][] grid = readGridFromFile(filename);

            // Print the road map
            for (int y = 0; y < grid.length; y++) {
                for (int x = 0; x < grid[0].length; x++) {
                    char l = grid[y][x];
                    System.out.print(l + " ");

                    // Set the Starting point and the Finish point
                    if (l == 'S') {
                        start = new Point(x, y);
                    } else if (l == 'F') {
                        finish = new Point(x, y);
                    }
                }
                System.out.println();
            }

            System.out.println("x-> " + grid.length + " y-> " + grid[0].length);

            // Find the shortest path
            List<Point> shortestPath = aStar(grid);

            // Print the shortest path
            if (shortestPath != null) {
                System.out.println("\nShortest path : \n");
                for (Point cell : shortestPath) {
                    System.out.println("(" + cell.getX() + "," + cell.getY() + ")");
                }
            } else {
                System.out.println("\uD83D\uDC80☠\uFE0F No path Found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return; // Exit if there is an error reading the file
        }
}






}
