import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;


public class VentMapper
{
    private static int N = 1000;
    private HashMap<Coordinate,Integer> m_map;

    public VentMapper(String p_filename)
    {
        m_map = new HashMap<Coordinate,Integer>();

		try (Stream<String> stream = Files.lines(Paths.get(p_filename)))
        {
            stream.forEach(this::process);
            int overlaps = findNumberOfOverlapsGreaterThan(1);
            log("Count of points with 2 or more overlaps: " + overlaps);
		}
        catch (Exception e)
        {
			e.printStackTrace();
            return;
		}
    }

    private void process(String p_line)
    {
        try
        {
            String[] parts = p_line.split("->");
            if (parts.length != 2)
            {
                throw new Exception("Line missing string '->':" + p_line);
            }

            String[] aParts = parts[0].split(",");
            if (aParts.length != 2)
            {
                throw new Exception("Point A missing comma: " + p_line);
            }
            Coordinate a = new Coordinate(aParts[0].trim(), aParts[1].trim());


            String[] bParts = parts[1].split(",");
            if (bParts.length != 2)
            {
                throw new Exception("Point B missing comma: " + p_line);
            }
            Coordinate b = new Coordinate(bParts[0].trim(), bParts[1].trim());

            markAllLines(a, b);
            //markLine(a, b);
        }
        catch (Exception e)
        {
            log("Error: ");
            e.printStackTrace();
        }
    }

    private int findNumberOfOverlapsGreaterThan(int p_limit)
    {
        Iterator<Map.Entry<Coordinate,Integer>> it = m_map.entrySet().iterator();
        int retVal = 0;
        while (it.hasNext())
        {
            Map.Entry<Coordinate,Integer> entry = it.next();
            Integer count = entry.getValue();
            log("Coordinate(" + entry.getKey().x + "," + entry.getKey().y + "), count: " + count);
            if (count > p_limit)
            {
                retVal++;
            }
        }
        return retVal;
    }

    private void markAllLines(Coordinate p_a, Coordinate p_b)
    {
        //if (p_a.x != p_b.x && p_a.y != p_b.y)
        //{
            //return;
        //}
        // The delta between points on a line. Can be 1, 0, or -1.
        Integer xDelta = 1;
        if (p_a.x == p_b.x)
        {
            xDelta = 0;
        }
        else if (p_a.x > p_b.x)
        {
            xDelta = -1;
        }

        Integer yDelta = 1;
        if (p_a.y == p_b.y)
        {
            yDelta = 0;
        }
        else if (p_a.y > p_b.y)
        {
            yDelta = -1;
        }

        Coordinate coord = p_a;
        while (!coord.equals(p_b))
        {
            increment(coord);
            coord = new Coordinate(coord.x + xDelta, coord.y + yDelta);
        }
        increment(coord);

    }

    private void markLine(Coordinate p_a, Coordinate p_b)
    {
        Integer low = null;
        Integer high = null;

        if (p_a.x == p_b.x)
        {
            if (p_a.y <= p_b.y)
            {
                low = p_a.y;
                high = p_b.y;
            }
            else
            {
                low = p_b.y;
                high = p_a.y;
            }
            for (int i=low; i<=high; ++i)
            {
                increment(new Coordinate(p_a.x, i));
            }
        }
        else if (p_a.y == p_b.y)
        {
            if (p_a.x <= p_b.x)
            {
                low = p_a.x;
                high = p_b.x;
            }
            else
            {
                low = p_b.x;
                high = p_a.x;
            }
            for (int i=low; i<=high; ++i)
            {
                increment(new Coordinate(i, p_a.y));
            }
        }
        else
        {
        }
    }

    private void increment(Coordinate p_coord)
    {
        Integer val = m_map.get(p_coord);
        if (val == null)
        {
            val = new Integer(0);
        }
        m_map.put(p_coord, val + 1);
    }
    public void log(String msg)
    {
        System.out.println(msg);
    }

    // Represents an X,Y coordinate
    public static class Coordinate
    {
        public int x;
        public int y;
        public Coordinate(String p_x, String p_y)
        {
            x = Integer.valueOf(p_x);
            y = Integer.valueOf(p_y);
        }
        public Coordinate(int p_x, int p_y)
        {
            x = p_x;
            y = p_y;
        }

        @Override
        public int hashCode()
        {
            return x*N + y;
        }
        @Override
        public boolean equals(Object obj)
        {
            if (obj instanceof Coordinate)
            {
                Coordinate coobj = (Coordinate) obj;
                return this.x == coobj.x && this.y == coobj.y;
            }
            return false;
        }
    }

    public static void main(String[] p_args)
    {
        String filename = "input.txt";
        VentMapper vm = new VentMapper(filename);
        //vm.countOverlaps();
    }
}
