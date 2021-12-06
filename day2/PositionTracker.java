import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;


public class PositionTracker
{
    public static void main(String[] p_args)
    {
		String fileName = "input.txt";

		try (Stream<String> stream = Files.lines(Paths.get(fileName)))
        {
            track(stream);

		}
        catch (IOException e)
        {
			e.printStackTrace();
            return;
		}
    }

    private static void track(Stream<String> p_stream)
    {
        String[] array = p_stream.toArray(String[]::new);
        track(array);
    }
    private static void track(String[] p_array)
    {
        int x = 0;
        int y = 0;
        for (String s : p_array)
        {
            String[] parts = s.split(" ");
            int magnitude = 0;
            if (parts.length != 2)
            {
                continue;
            }
            String dir = parts[0];
            magnitude = Integer.valueOf(parts[1]);
            if ("forward".equals(dir))
            {
                x += magnitude;
            }
            else if ("up".equals(dir))
            {
                y -= magnitude;
            }
            else if ("down".equals(dir))
            {
                y += magnitude;
            }
        }
        System.out.println("X: " + x);
        System.out.println("Y: " + y);
        System.out.println("X*Y=" + (x*y));
    }
}
