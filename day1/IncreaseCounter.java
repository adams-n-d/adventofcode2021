import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import java.util.List;
//import java.util.ArrayList;
//import java.util.Iterator;


public class IncreaseCounter
{
	public static void main(String p_args[])
    {

		String fileName = "input.txt";

		try (Stream<String> stream = Files.lines(Paths.get(fileName)))
        {
            count(stream);

		}
        catch (IOException e)
        {
			e.printStackTrace();
            return;
		}
    }

    private static void count(Stream<String> p_stream)
    {
        List<String> list = p_stream.collect(Collectors.toList());
        count(list);
    }

    private static void count(List<String> p_list)
    {
        // Counters
        int increases = 0;
        int decreases = 0;
        int equals = 0;
        String last = null;
        for (String s : p_list)
        {
            if (last == null)
            {
                last = s;
                continue;
            }
            int thisI = Integer.valueOf(s);
            int lastI = Integer.valueOf(last);
            if (thisI > lastI)
            {
                increases++;
            }
            else if (thisI < lastI)
            {
                decreases++;
            }
            else
            {
                equals++;
            }

            last = s;
        }
        System.out.println("Increases: " + increases);
        System.out.println("Decreases: " + decreases);
        System.out.println("Equals: " + equals);
	}
}
