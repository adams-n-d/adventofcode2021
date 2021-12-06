import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import java.util.List;
import java.util.ArrayList;
//import java.util.Iterator;


public class RunningIncreaseCounter
{
	public static void main(String p_args[])
    {

		String fileName = "input.txt";

		try (Stream<String> stream = Files.lines(Paths.get(fileName)))
        {
            runningCount(stream);

		}
        catch (IOException e)
        {
			e.printStackTrace();
            return;
		}
    }
    private static void runningCount(Stream<String> p_stream)
    {
        Integer[] list = p_stream
            .map(Integer::valueOf)
            .toArray(Integer[]::new);
        Integer[] runningList = new Integer[list.length - 2];
        for (int i=0; i<list.length-2; ++i)
        {
            runningList[i] = list[i] + list[i+1] + list[i+2];
        }
        count(runningList);
    }

    //private static void count(Stream<String> p_stream)
    //{
        //String[] list = p_stream.toArray(String[]::new);
        //count(list);
    //}

    private static void count(Integer[] p_list)
    {
        // Counters
        int increases = 0;
        int decreases = 0;
        int equals = 0;
        Integer last = null;
        for (Integer s : p_list)
        {
            if (last == null)
            {
                last = s;
                continue;
            }
            if (s > last)
            {
                increases++;
            }
            else if (s < last)
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
